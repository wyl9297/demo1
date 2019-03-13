package com.demo.service.impl;

import cn.bidlink.base.ServiceResult;
import cn.bidlink.framework.util.gen.IdWork;
import cn.bidlink.usercenter.server.entity.TRegUser;
import cn.bidlink.usercenter.server.service.DubboTRegUserService;
import com.demo.model.CorpCatalogNew;
import com.demo.model.CorpCatalogs;
import com.demo.model.CorpDirectorys;
import com.demo.model.CorpDirectorysNew;
import com.demo.persistence.dao.CorpCatalogsMapper;
import com.demo.persistence.dao.CorpDirectorysMapper;
import com.demo.service.CorpExportService;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  16:07 2019-02-20
 * @Description :
 */
@Service("CorpExportService")
public class CorpExportServiceImpl implements CorpExportService {

    private static Logger log = LoggerFactory.getLogger(CorpExportServiceImpl.class);
    @Autowired
    private CorpCatalogsMapper corpCatalogsMapper;

    @Autowired
    private CorpDirectorysMapper corpDirectorysMapper;

    @Autowired
    private DubboTRegUserService dubboTRegUserService;

    @Autowired
    @Qualifier("purchaseJdbcTemplate")
    private JdbcTemplate corpJdbcTemplate;

    /**
     * 生成 key-value对照
     * key:旧ID
     * value：新生成的ID
     *
     * @param corpCatalogs
     * @return
     */
    public Map<Long, Long> idMap(List<CorpCatalogs> corpCatalogs) {
        Map<Long, Long> map = Maps.newHashMap();
        log.info("即将进入循环 ------------");
        log.info("参数值：{}", corpCatalogs.size());
        if (corpCatalogs.size() <= 0) {
            log.error("参数为空，请检验");
        } else {
            log.info("生成id对应关系 key-value");
            for (int i = 0; i < corpCatalogs.size(); i++) {
                Long oldId = corpCatalogs.get(i).getId();
                // 新生成的ID
                long newId = IdWork.nextId();

                map.put(oldId, newId);//代表对照关系
                log.info("oldId:{},newId:{}", oldId, newId);
            }
        }
        return map;
    }

    /**
     * 采购品目录信息  及  对照关系表数据导出
     *
     * @param originCompanyId destCompanyId
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> exportCorpCatalogs(Long originCompanyId, Long destCompanyId) throws DataAccessException{
        log.info("进入方法：{}", "exportCorpCatalogs()");

        // 根据companyID查询悦采 采购品目录信息
        List<CorpCatalogs> corpCatalogs = corpCatalogsMapper.selCataLogsByCompanyId(originCompanyId);

        // 不符合条件的 采购品目录信息集合
        List<CorpCatalogs> failList = new ArrayList<>();

        // 获取新旧Id对照Map
        Map<Long, Long> idsMap = idMap(corpCatalogs);

        // 新采购品目录集合
        List<Object[]> catalogs = new ArrayList<>();
        // 存储 中间表信息    包含：oldId  newId   companyId
        List<Object[]> middle = new ArrayList<>();

        for (CorpCatalogs catalog : corpCatalogs) {

            CorpCatalogNew catalogNew = new CorpCatalogNew();

            try {
                if (catalog.getName().length() > 50 || catalog.getCode().length() > 20) {
                    log.error("请检查name code 字段，存储失败。原因：长度过长 --> id:{},name:{},companyId:{}", catalog.getId(), catalog.getName(), catalog.getCompanyId());
                    failList.add(catalog);
                    continue;
                } else {
                    // 给CorpCatalogNew对象赋值
                    BeanUtils.copyProperties(catalogNew, catalog);

                    // 根据companyID查询用户中心信息
                    List<TRegUser> tRegUser = findByCondition(catalog.getCompanyId());

                    if (catalog.getCreator() == null || catalog.getModifier() == null) { // 如果creator为null或者modifier为null，使用主账号的信息
                        log.warn("creator 为null:{}", catalog.getCreator());
                        catalogNew.setCreateUserId(tRegUser.get(0).getId());
                        catalogNew.setCreateUserName(tRegUser.get(0).getName());
                        catalogNew.setUpdateUserId(tRegUser.get(0).getId());
                        catalogNew.setUpdateUserName(tRegUser.get(0).getName());
                    } else {
                        catalogNew.setCreateUserId(catalog.getCreator());
                        catalogNew.setCreateUserName(tRegUser.get(0).getName());
                        catalogNew.setUpdateUserId(catalog.getModifier());
                        catalogNew.setUpdateUserName(tRegUser.get(0).getName());
                    }

                    /**
                     * 如果创建时间为null，设置为最新时间
                     */
                    if (catalog.getCreateTime() == null) {
                        catalogNew.setCreateTime(new Date());
                    }
                    // 设置最新时间
                    catalogNew.setUpdateTime(new Date());

                    // 获取oldId
                    Long oldId = catalog.getId();
                    // 获取新Id
                    Long newId = idsMap.get(oldId);

                    catalogNew.setId(newId);

                    // 设置隆道云新 companyId
                    catalogNew.setCompanyId(destCompanyId);

                    int isRoot = catalogNew.getIsRoot();

                    String idPath = null;

                    if (null == catalogNew.getTreepath() && isRoot == 1) {

                        idPath = "#" + catalogNew.getId();
                        catalogs.add(new Object[]{newId, catalogNew.getName(), catalogNew.getCode(), idsMap.get(catalog.getParentId()), catalogNew.getIsRoot(), idPath, catalogNew.getCatalogNamePath(),
                                catalogNew.getCompanyId(), catalogNew.getCreateUserId(), catalogNew.getCreateUserName(), catalogNew.getCreateTime(), catalogNew.getUpdateUserId(), catalogNew.getUpdateUserName(), catalogNew.getUpdateTime()});

                    } else {

                        catalogs.add(new Object[]{newId, catalogNew.getName(), catalogNew.getCode(), idsMap.get(catalog.getParentId()), catalogNew.getIsRoot(), catalogNew.getTreepath(), catalogNew.getCatalogNamePath(),
                                catalogNew.getCompanyId(), catalogNew.getCreateUserId(), catalogNew.getCreateUserName(), catalogNew.getCreateTime(), catalogNew.getUpdateUserId(), catalogNew.getUpdateUserName(), catalogNew.getUpdateTime()});

                    }

                    middle.add(new Object[]{newId, oldId, catalog.getCompanyId()});
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        int[] corp_catalogs = corpJdbcTemplate.batchUpdate("INSERT INTO `db_boot`.`corp_catalog_ldy`(`ID`, `NAME`, `CODE`, `PARENT_ID`, `IS_ROOT`, `ID_PATH`, `NAME_PATH`, `company_id`, `create_user_id`, `create_user_name`, `create_time`, `update_user_id`, `update_user_name`, `update_time`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", catalogs);
        int[] middles = corpJdbcTemplate.batchUpdate("INSERT INTO `db_boot`.`middle_corp_catalog`(`catalog_id`, `old_id`, `company_id`) VALUES (?, ?, ?);", middle);
        // 通过遍历的方式更新 id_path
        for (int i = 0; i < 10; i++) {
            int update = corpJdbcTemplate.update("UPDATE corp_catalog_ldy c1 JOIN\n" +
                    "  corp_catalog_ldy c2\n" +
                    "  ON c1.PARENT_ID=c2.ID\n" +
                    "  SET c1.id_path=concat(c2.id_path,concat('#',c1.id,''),'');");
            // 当update条数为0时，即id_path更新完成，结束循环
            if (update == 0) {
                break;
            }
        }

        Map<String,Object> map = Maps.newHashMap();
        // 插入失败的
        map.put("failCatalog",failList);
        return map;
    }

    /**
     * 导出采购品信息
     *
     * @param originCompanyId 悦采companyId  隆道云companyId
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> exportDirectorys(Long originCompanyId, Long destCompanyId) throws DataAccessException{

        // 根据companyId 查询 采购品信息
        List<CorpDirectorys> directorysList = corpDirectorysMapper.findByCompanyId(originCompanyId);

        /**
         * 生成 key-value 的 oldId，newId 集合
         */
        Map<Long,Long> idMap = Maps.newHashMap();
        for(CorpDirectorys corp : directorysList){
            long newId = IdWork.nextId();
            Long oldId = corp.getId();
            idMap.put(oldId,newId);
        }

        List<Object[]> params = new ArrayList<>();

        // 不符合条件的 采购品目录信息集合
        List<CorpDirectorys> failList = new ArrayList<>();
        for (CorpDirectorys directorys : directorysList) {

            CorpDirectorysNew corpDirectorysNew = new CorpDirectorysNew();
            try {
                BeanUtils.copyProperties(corpDirectorysNew, directorys);
                // 根据companyID查询用户中心信息
                List<TRegUser> tRegUser = findByCondition(originCompanyId);

                // 赋值规则：判断悦采的creator 和 modifier 字段中是否有空值，如果有，使用中心库中的id,name为 createUserId、createUserName、updateUserId、updateUserName
                if (directorys.getCreator() == null || directorys.getModifier() == null) {
                    corpDirectorysNew.setCreateUserId(tRegUser.get(0).getId());
                    corpDirectorysNew.setCreateUserName(tRegUser.get(0).getName());
                    corpDirectorysNew.setUpdateUserId(tRegUser.get(0).getId().intValue());
                    corpDirectorysNew.setUpdateUserName(tRegUser.get(0).getName());
                } else {
                    corpDirectorysNew.setCreateUserId(directorys.getCreator());
                    corpDirectorysNew.setCreateUserName(tRegUser.get(0).getName());
                    corpDirectorysNew.setUpdateUserId(directorys.getModifier().intValue());
                    corpDirectorysNew.setUpdateUserName(tRegUser.get(0).getName());
                }
                if(directorys.getName().length()>200 || (directorys.getCode()!=null && directorys.getCode().length()>20) || (directorys.getSpec() != null && directorys.getSpec().length()>500)){
                    log.error("字符长度过长，存储失败,name 长度为：{},code 长度为：{},spec 长度为：{}",directorys.getName().length(),directorys.getCode().length(),directorys.getSpec().length());
                    failList.add(directorys);
                    continue;
                }

                corpDirectorysNew.setPricePrecision(2l);
                corpDirectorysNew.setUnitPrecision(2l);
                corpDirectorysNew.setCompanyId(destCompanyId);

                corpDirectorysNew.setUpdateTime(new Date());

                //悦采  catalog_name  对应隆道云  	catalog_name_path
                //悦采  treepath      对应隆道云    catalog_id_path
                params.add(new Object[]{
                        IdWork.nextId(),corpDirectorysNew.getCatalogId(),corpDirectorysNew.getCatalogName(),corpDirectorysNew.getTreepath(),corpDirectorysNew.getCode(),
                        corpDirectorysNew.getName(),corpDirectorysNew.getSpec(),corpDirectorysNew.getAbandon(),corpDirectorysNew.getPcode(),corpDirectorysNew.getProductor(),
                        corpDirectorysNew.getUnitname(),corpDirectorysNew.getProducingAddress(),corpDirectorysNew.getBrand(),corpDirectorysNew.getPurpose(),corpDirectorysNew.getMarketPrice(),
                        corpDirectorysNew.getSpeciality(),corpDirectorysNew.getSource(),corpDirectorysNew.getTechParameters(),corpDirectorysNew.getDemo(),corpDirectorysNew.getUnitPrecision(),
                        corpDirectorysNew.getPricePrecision(),corpDirectorysNew.getCompanyId(),corpDirectorysNew.getCreateUserId(),corpDirectorysNew.getCreateUserName(),corpDirectorysNew.getCreateTime(),
                        corpDirectorysNew.getUpdateUserId(),corpDirectorysNew.getUpdateUserName(),corpDirectorysNew.getUpdateTime()
                });
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        int[] insertDirectorys = corpJdbcTemplate.batchUpdate("INSERT INTO `db_boot`.`corp_directory_ldy`\n" +
                "(`ID`, `CATALOG_ID`, `CATALOG_NAME_PATH`, `CATALOG_ID_PATH`, `CODE`, `NAME`, `SPEC`, `ABANDON`, `PCODE`, `PRODUCTOR`, `UNITNAME`, `PRODUCING_ADDRESS`, `BRAND`, `PURPOSE`, `MARKET_PRICE`, `SPECIALITY`, `SOURCE`, `tech_parameters`, `DEMO`, `unit_precision`, `price_precision`, `company_id`, `create_user_id`, `create_user_name`, `create_time`, `update_user_id`, `update_user_name`, `update_time`) \n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?);", params);

        int[] directoryResult = corpJdbcTemplate.batchUpdate("UPDATE corp_directory_ldy cd \n" +
                "join corp_catalog_ldy cc on cd.catalog_name_path = cc.name_path and cd.company_id = cc.company_id\n" +
                "set cd.catalog_id = cc.id , cd.catalog_id_path = cc.id_path");
        Map<String,Object> map = Maps.newHashMap();
        // 插入失败的
        map.put("failDirectory",failList);
        return map;
    }

    /**
     * 查询新中心库信息
     *
     * @return
     */
    public List<TRegUser> findByCondition(Long companyId) {

        TRegUser tRegUser = new TRegUser();
        tRegUser.setCompanyId(companyId);
        tRegUser.setIsSubuser(0);

        ServiceResult<List<TRegUser>> byCondition = dubboTRegUserService.findByCondition(tRegUser);
        if (!byCondition.getSuccess()) {
            log.error("{}调用{}时发生未知异常,error Message:{}", "com.demo.controller.CorpExportController.test",
                    "byCondition", byCondition.getCode() + "_" + byCondition.getMessage());
            throw new RuntimeException("err_code:" + byCondition.getCode() + ",err_msg:" + byCondition.getMessage());
        }
        List<TRegUser> result = byCondition.getResult();
        if (result == null) {
            log.warn("com.demo.controller.CorpExportController.test时未获取到结果");
        }
        return result;
    }


}
