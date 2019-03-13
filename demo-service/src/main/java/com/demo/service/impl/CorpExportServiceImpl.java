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
import java.util.*;

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
/*
    @Autowired
    @Qualifier("purchaseJdbcTemplate")
    private JdbcTemplate corpJdbcTemplate;*/

    @Autowired
    @Qualifier("uniregJdbcTemplate")
    protected JdbcTemplate uniregJdbcTemplate;

    // 定义批处理的数值
    private static final double NUM = 500.0;


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
    public Map<String, Object> exportCorpCatalogs(Long originCompanyId, Long destCompanyId) throws DataAccessException {

        log.info("进入方法：{}", "exportCorpCatalogs()");

        // 根据companyId 查询 悦采吃啊够拼目录总记录数
        Long count = corpCatalogsMapper.selCataLogsByCompanyIdWtihCount(originCompanyId);

        // 不符合条件的 采购品目录信息集合
        List<CorpCatalogs> failList = new ArrayList<>();

        // 新采购品目录集合
        List<Object[]> newCatalogs = new ArrayList<>();
        // 存储 中间表信息    包含：oldId  newId   companyId
        List<Object[]> middle = new ArrayList<>();

        // 根据companyID查询用户中心信息
        TRegUser tRegUser = findByCondition(originCompanyId);

        // 分页查询数量
        List<CorpCatalogs> corpCatalogs = null;
        // 判断数据量的大小是否需要做批量处理
        if (count > NUM) {
            // 计算最后页的条数
            int remainder = (int) (count % NUM);
            // 计算 分页数 向上取整( 10.1 --> 11)
            int ceil = (int) Math.ceil(count / NUM);
            // 批量查询 采购品目录信息
            for (int i = 0; i < ceil; i++) {

                if (i == ceil - 1) {
                    corpCatalogs = corpCatalogsMapper.selCataLogsByCompanyIdWithPageing(originCompanyId, i, remainder);
                } else {
                    corpCatalogs = corpCatalogsMapper.selCataLogsByCompanyIdWithPageing(originCompanyId, i, (int) NUM);
                }
                // 批量插入采购品目录信息
                dealWithCatalogs(destCompanyId, failList, newCatalogs, middle, corpCatalogs , tRegUser);
                updateCatalogTreePath();
            }
        } else {
            // 批量插入采购品目录信息
            corpCatalogs = corpCatalogsMapper.selCataLogsByCompanyIdWithPageing(originCompanyId, 0, count.intValue());
            dealWithCatalogs(destCompanyId, failList, newCatalogs, middle, corpCatalogs , tRegUser);
            updateCatalogTreePath();
        }
        // 如果  存在存储失败的数据，放入map集合
        if (failList.size() != 0 && failList != null) {
            Map<String, Object> map = Maps.newHashMap();
            // 插入失败的
            map.put("failCatalog", failList);
            return map;
        } else {
            return null;
        }
    }

    /**
     * 更新采购品目录  树状结构
     */
    private void updateCatalogTreePath() {

        String updateCatalogSql = "UPDATE corp_catalog c1 JOIN\n" +
                "  corp_catalog c2\n" +
                "  ON c1.PARENT_ID=c2.ID\n" +
                "  SET c1.ID_PATH=concat(c2.ID_PATH,concat('',c1.id,''),'');";

        // 通过遍历的方式更新 id_path
        for (int j = 0; j < 10; j++) {
            int update = uniregJdbcTemplate.update(updateCatalogSql);
            // 当update条数为0时，即id_path更新完成，结束循环
            if (update == 0) {
                break;
            }
        }
    }

    private void dealWithCatalogs(Long destCompanyId, List<CorpCatalogs> failList, List<Object[]> newCatalogs, List<Object[]> middle, List<CorpCatalogs> corpCatalogs , TRegUser tRegUser) {

        // 获取新旧Id对照Map
        Map<Long, Long> idsMap = idMap(corpCatalogs);

        String insertCatalogsSQL = "INSERT INTO `corp_catalog`(`ID`, `NAME`, `CODE`, `PARENT_ID`, `IS_ROOT`, `ID_PATH`, `NAME_PATH`, `company_id`, `create_user_id`, `create_user_name`, `create_time`, `update_user_id`, `update_user_name`, `update_time`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        String insertMiddleSQL = "INSERT INTO `middle_corp_catalog`(`catalog_id`, `old_id`, `company_id`) VALUES (?, ?, ?);";

        for (int j = 0; j < corpCatalogs.size(); j++) {
            CorpCatalogs catalog = corpCatalogs.get(j);
            CorpCatalogNew catalogNew = new CorpCatalogNew();
            try {
                if (catalog.getName().length() > 50 || catalog.getCode().length() > 20) {
                    log.error("请检查name code 字段，存储失败。原因：长度过长 --> id:{},name:{},companyId:{}", catalog.getId(), catalog.getName(), catalog.getCompanyId());
                    failList.add(catalog);
                    continue;
                } else {
                    // 给CorpCatalogNew对象赋值
                    BeanUtils.copyProperties(catalogNew, catalog);

                    if (catalog.getCreator() == null || catalog.getModifier() == null) { // 如果creator为null或者modifier为null，使用主账号的信息
                        log.warn("creator 为null:{}", catalog.getCreator());
                        catalogNew.setCreateUserId(tRegUser.getId());
                        catalogNew.setCreateUserName(tRegUser.getName());
                        catalogNew.setUpdateUserId(tRegUser.getId());
                        catalogNew.setUpdateUserName(tRegUser.getName());
                    } else {
                        catalogNew.setCreateUserId(catalog.getCreator());
                        catalogNew.setCreateUserName(tRegUser.getName());
                        catalogNew.setUpdateUserId(catalog.getModifier());
                        catalogNew.setUpdateUserName(tRegUser.getName());
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

                    if (null == catalogNew.getParentId() && isRoot == 1) {

                        idPath = "#" + newId + "#";
                        newCatalogs.add(new Object[]{newId, catalogNew.getName(), catalogNew.getCode(), idsMap.get(catalog.getParentId()), catalogNew.getIsRoot(), idPath, catalogNew.getCatalogNamePath(),
                                catalogNew.getCompanyId(), catalogNew.getCreateUserId(), catalogNew.getCreateUserName(), catalogNew.getCreateTime(), catalogNew.getUpdateUserId(), catalogNew.getUpdateUserName(), catalogNew.getUpdateTime()});
                    } else {

                        newCatalogs.add(new Object[]{newId, catalogNew.getName(), catalogNew.getCode(), idsMap.get(catalog.getParentId()), catalogNew.getIsRoot(), catalogNew.getTreepath(), catalogNew.getCatalogNamePath(),
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
        int[] corp_catalogs = uniregJdbcTemplate.batchUpdate(insertCatalogsSQL, newCatalogs);
        int[] middles = uniregJdbcTemplate.batchUpdate(insertMiddleSQL, middle);
    }

    /**
     * 采购品信息处理 及 失败数据导出
     *
     * @param originCompanyId 悦采companyId  隆道云companyId
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> exportDirectorys(Long originCompanyId, Long destCompanyId) throws DataAccessException {

        // 查询需要导出的采购品信息总记录数
        Long count = corpDirectorysMapper.selCountByCompanyId(originCompanyId);

        List<Object[]> params = new ArrayList<>();

        // 不符合条件的 采购品目录信息集合
        List<CorpDirectorys> failList = new ArrayList<>();

        // 分页查询数量
        List<CorpDirectorys> byCompanyIdWithPageing = null;
        if (count > NUM) {
            // 计算最后页的条数
            int remainder = (int) (count % NUM);
            // 计算 分页数 向上取整( 10.1 --> 11)
            int ceil = (int) Math.ceil(count / NUM);
            for (int i = 0; i < ceil; i++) {
                if (i == ceil - 1) {
                    byCompanyIdWithPageing = corpDirectorysMapper.findByCompanyIdWithPageing(originCompanyId, i, remainder);
                } else {
                    byCompanyIdWithPageing = corpDirectorysMapper.findByCompanyIdWithPageing(originCompanyId, i, (int) NUM);
                }
                dealWithDirectory(originCompanyId, destCompanyId, params, failList, byCompanyIdWithPageing);
            }
        } else {
            byCompanyIdWithPageing = corpDirectorysMapper.findByCompanyIdWithPageing(originCompanyId, 0, count.intValue());
            dealWithDirectory(originCompanyId, destCompanyId, params, failList, byCompanyIdWithPageing);
        }
        // 如果  存在存储失败的数据，放入map集合
        if (failList.size() != 0 && failList != null) {
            Map<String, Object> map = Maps.newHashMap();
            // 插入失败的
            map.put("failDirectory", failList);
            return map;
        } else {
            return null;
        }
    }

    private void dealWithDirectory(Long originCompanyId, Long destCompanyId, List<Object[]> params, List<CorpDirectorys> failList, List<CorpDirectorys> byCompanyIdWithPageing) {

        // 根据companyID查询用户中心信息
        TRegUser tRegUser = findByCondition(originCompanyId);

        // 插入采购品信息 SQL
        String insertSql = "INSERT INTO `corp_directory`\n" +
                "(`ID`, `CATALOG_ID`, `CATALOG_NAME_PATH`, `CATALOG_ID_PATH`, `CODE`, `NAME`, `SPEC`, `ABANDON`, `PCODE`, `PRODUCTOR`, `UNITNAME`, `PRODUCING_ADDRESS`, `BRAND`, `PURPOSE`," +
                " `MARKET_PRICE`, `SPECIALITY`, `SOURCE`, `tech_parameters`, `DEMO`, `unit_precision`, `price_precision`, `company_id`, `create_user_id`, `create_user_name`, " +
                "`create_time`, `update_user_id`, `update_user_name`, `update_time`) \n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?);";
        // 更新采购品信息[catalog_id  和   catalog_id_path ] SQL
        String updateSql = "UPDATE corp_directory cd \n" +
                "join corp_catalog cc on cd.catalog_name_path = cc.name_path and cd.company_id = cc.company_id\n" +
                "set cd.catalog_id = cc.id , cd.catalog_id_path = cc.id_path";

        for (int j = 0; j < byCompanyIdWithPageing.size(); j++) {
            CorpDirectorys directorys = byCompanyIdWithPageing.get(j);
            CorpDirectorysNew corpDirectorysNew = new CorpDirectorysNew();
            try {
                BeanUtils.copyProperties(corpDirectorysNew, directorys);

                // 赋值规则：判断悦采的creator 和 modifier 字段中是否有空值，如果有，使用中心库中的id,name为 createUserId、createUserName、updateUserId、updateUserName
                if (directorys.getCreator() == null || directorys.getModifier() == null) {
                    corpDirectorysNew.setCreateUserId(tRegUser.getId());
                    corpDirectorysNew.setCreateUserName(tRegUser.getName());
                    corpDirectorysNew.setUpdateUserId(tRegUser.getId().intValue());
                    corpDirectorysNew.setUpdateUserName(tRegUser.getName());
                } else {
                    corpDirectorysNew.setCreateUserId(directorys.getCreator());
                    corpDirectorysNew.setCreateUserName(tRegUser.getName());
                    corpDirectorysNew.setUpdateUserId(directorys.getModifier().intValue());
                    corpDirectorysNew.setUpdateUserName(tRegUser.getName());
                }
                if (directorys.getName().length() > 200 || (directorys.getCode() != null && directorys.getCode().length() > 20) || (directorys.getSpec() != null && directorys.getSpec().length() > 500)) {
                    log.error("字符长度过长，存储失败,name 长度为：{},code 长度为：{},spec 长度为：{}", directorys.getName().length(), directorys.getCode().length(), directorys.getSpec().length());
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
                        IdWork.nextId(), corpDirectorysNew.getCatalogId(), corpDirectorysNew.getCatalogName(), corpDirectorysNew.getTreepath(), corpDirectorysNew.getCode(),
                        corpDirectorysNew.getName(), corpDirectorysNew.getSpec(), corpDirectorysNew.getAbandon(), corpDirectorysNew.getPcode(), corpDirectorysNew.getProductor(),
                        corpDirectorysNew.getUnitname(), corpDirectorysNew.getProducingAddress(), corpDirectorysNew.getBrand(), corpDirectorysNew.getPurpose(), corpDirectorysNew.getMarketPrice(),
                        corpDirectorysNew.getSpeciality(), corpDirectorysNew.getSource(), corpDirectorysNew.getTechParameters(), corpDirectorysNew.getDemo(), corpDirectorysNew.getUnitPrecision(),
                        corpDirectorysNew.getPricePrecision(), corpDirectorysNew.getCompanyId(), corpDirectorysNew.getCreateUserId(), corpDirectorysNew.getCreateUserName(), corpDirectorysNew.getCreateTime(),
                        corpDirectorysNew.getUpdateUserId(), corpDirectorysNew.getUpdateUserName(), corpDirectorysNew.getUpdateTime()
                });
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        int[] insertDirectorys = uniregJdbcTemplate.batchUpdate(insertSql, params);

        int[] directoryResult = uniregJdbcTemplate.batchUpdate(updateSql);
    }

    /**
     * 查询新中心库信息
     *
     * @return
     */
    public TRegUser findByCondition(Long companyId) {

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
            return null;
        } else {
            TRegUser user = result.get(0);
            return user;
        }
    }
}
