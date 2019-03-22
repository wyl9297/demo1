package com.demo.service.impl;

import cn.bidlink.framework.util.gen.IdWork;
import com.demo.model.CorpCatalogNew;
import com.demo.model.CorpCatalogs;
import com.demo.model.CorpDirectorys;
import com.demo.model.CorpDirectorysNew;
import com.demo.persistence.dao.CorpCatalogsMapper;
import com.demo.persistence.dao.CorpDirectorysMapper;
import com.demo.service.CorpExportService;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.util.CollectionUtils;
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

    /* 本地数据源，仅供测试
    @Autowired
    @Qualifier("purchaseJdbcTemplate")
    private JdbcTemplate corpJdbcTemplate;*/

    @Autowired
    @Qualifier("uniregJdbcTemplate")
    protected JdbcTemplate uniregJdbcTemplate;

    // 定义批处理的数值
    private static final double PAGESIZE = 1500.0;


    /**
     * 生成 key-value对照
     * key:旧ID
     * value：新生成的ID
     *
     * @param corpCatalogs
     * @return
     */
    public Map<Long, Long> idMap(List<CorpCatalogs> corpCatalogs, Long destCompanyId) {
        Long newRootId = uniregJdbcTemplate.queryForObject("select  id from `corp_catalog` where is_root = 1 and parent_id is null and company_id = ?", new Object[]{destCompanyId}, Long.class);
        Map<Long, Long> map = Maps.newHashMap();
        log.info("进入方法：{}", "CorpExportServiceImpl.idMap");
        log.info("------------ 即将生成 catalog 新旧id对照关系 ------------");
        log.info("需要生成 {} 个对照关系", corpCatalogs.size());
        if (corpCatalogs.size() <= 0) {
            log.error("参数为空，请检验");
        } else {
            log.info("生成id对应关系 key-value");
            for (int i = 0; i < corpCatalogs.size(); i++) {
                Long oldId = corpCatalogs.get(i).getId();
                if (null == corpCatalogs.get(i).getParentId() && corpCatalogs.get(i).getIsRoot() == 1) {
                    // 新生成的ID
                    map.put(oldId, newRootId);//代表对照关系
                } else {
                    // 新生成的ID
                    map.put(oldId, IdWork.nextId());//代表对照关系
                }
            }
        }
        return map;
    }

    /**
     * 采购品目录信息  及  采购品目录对照关系  处理
     *
     * @param originCompanyId destCompanyId
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> exportCorpCatalogs(Long originCompanyId, Long destCompanyId) throws DataAccessException {

        log.info("进入方法：{}", "com.demo.service.impl.CorpExportServiceImpl.exportCorpCatalogs");

        // 根据companyId 查询 悦采采购品目录总记录数
        Long count = corpCatalogsMapper.selCataLogsByCompanyIdWtihCount(originCompanyId);

        // 不符合条件的 采购品目录信息存储
        List<CorpCatalogs> failList = new ArrayList<>();

        // 新采购品目录信息存储集合
        List<Object[]> newCatalogs = new ArrayList<>();

        // 存储 采购品目录中间表信息    包含：catalogId oldId  companyId
        List<Object[]> middle = new ArrayList<>();

        // 根据companyID查询用户中心信息
        Map<String, Object> userMap = this.findByCondition(originCompanyId);


        // 存储分页信息
        List<CorpCatalogs> corpCatalogs = null;
        // 判断数据量的大小是否需要做批量处理
        if (count > PAGESIZE) {
            log.info("---------- 批量读取数据 ---------");
            // 计算最后页的数量
            int remainder = (int) (count % PAGESIZE);
            // 计算分页数  向上取整( 10.1 --> 11)
            int ceil = (int) Math.ceil(count / PAGESIZE);
            int pageNum = 0;
            for (int i = 0; i < ceil; i++) {

                if (i == ceil - 1) {
                    corpCatalogs = corpCatalogsMapper.selCataLogsByCompanyIdWithPageing(originCompanyId, pageNum, remainder);
                } else {
                    corpCatalogs = corpCatalogsMapper.selCataLogsByCompanyIdWithPageing(originCompanyId, pageNum, (int) PAGESIZE);
                }

                pageNum += (int) PAGESIZE;

                // 批量插入采购品目录信息
                this.dealWithCatalogs(destCompanyId, failList, newCatalogs, middle, corpCatalogs, userMap);
                // 更新采购品目录树形结构
                this.updateCatalogTreePath(originCompanyId);
            }
        } else {
            log.info("---------- 读取数据 ---------");
            // 批量插入采购品目录信息
            corpCatalogs = corpCatalogsMapper.selCataLogsByCompanyIdWithPageing(originCompanyId, 0, count.intValue());
            // 批量插入采购品目录信息
            this.dealWithCatalogs(destCompanyId, failList, newCatalogs, middle, corpCatalogs, userMap);
            // 更新采购品目录树形结构
            this.updateCatalogTreePath(originCompanyId);
        }
        //不符合存储条件的数据，放入map集合
        if (failList.size() != 0 && failList != null) {
            Map<String, Object> map = Maps.newHashMap();
            log.info("不符合条件的数量: {} ", failList.size());
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
    private void updateCatalogTreePath(Long originCompanyId) {

        String updateCatalogSql = "UPDATE `corp_catalog` c1 JOIN\n" +
                " `corp_catalog` c2\n" +
                "  ON c1.PARENT_ID=c2.ID and c1.company_id = c2.company_id \n" +
                "  SET c1.ID_PATH=concat(c2.ID_PATH,concat('',c1.id,'#'),'') where c1.company_id = ?;";

        // 通过遍历的方式更新 id_path
        for (int j = 0; j < 10; j++) {
            int update = uniregJdbcTemplate.update(updateCatalogSql, originCompanyId);
            // 当update条数为0时，即id_path更新完成，结束循环
            if (update == 0) {
                break;
            }
        }
    }

    /**
     * 采购品目录信息 处理
     *
     * @param destCompanyId 新companyId
     * @param failList      不符合存储条件的信息
     * @param newCatalogs   新采购品目录信息存储
     * @param middle        采购品目录 新旧id对照表
     * @param corpCatalogs  采购品目录集合
     * @param map           用户信息
     */
    private void dealWithCatalogs(Long destCompanyId, List<CorpCatalogs> failList, List<Object[]> newCatalogs, List<Object[]> middle, List<CorpCatalogs> corpCatalogs, Map<String, Object> map) {

        log.info("进入方法 {} , 处理采购品目录信息", "com.demo.service.impl.CorpExportServiceImpl.dealWithCatalogs");
        // 获取新旧Id对照Map
        Map<Long, Long> idsMap = idMap(corpCatalogs, destCompanyId);;
        long main_user_id = (long) map.get("id");
        String main_user_name = (String) map.get("name");
        String insertCatalogsSQL = "INSERT INTO `corp_catalog`(`ID`, `NAME`, `CODE`, `PARENT_ID`, `IS_ROOT`, `ID_PATH`, `NAME_PATH`, `company_id`, `create_user_id`, `create_user_name`, " +
                "`create_time`, `update_user_id`, `update_user_name`, `update_time`) " +
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
                        catalogNew.setCreateUserId(main_user_id);
                        catalogNew.setCreateUserName(main_user_name);
                        catalogNew.setUpdateUserId(main_user_id);
                        catalogNew.setUpdateUserName(main_user_name);
                    } else {
                        catalogNew.setCreateUserId(catalog.getCreator());
                        catalogNew.setCreateUserName(main_user_name);
                        catalogNew.setUpdateUserId(catalog.getModifier());
                        catalogNew.setUpdateUserName(main_user_name);
                    }

                    /**
                     * 如果创建时间为null，设置为最新时间
                     */
                    if (catalog.getCreateTime() == null) {
                        catalogNew.setCreateTime(new Date());
                    }
                    // 更新时间为当前时间
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
                        // do nothing
                       /* idPath = "#" + newId + "#";
                        newCatalogs.add(new Object[]{newId, catalogNew.getName(), catalogNew.getCode(), idsMap.get(catalog.getParentId()), catalogNew.getIsRoot(), idPath, catalogNew.getCatalogNamePath(),
                                catalogNew.getCompanyId(), catalogNew.getCreateUserId(), catalogNew.getCreateUserName(), catalogNew.getCreateTime(), catalogNew.getUpdateUserId(), catalogNew.getUpdateUserName(), catalogNew.getUpdateTime()});*/
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
        log.info("批量插入采购品目录信息");
        int[] corp_catalogs = uniregJdbcTemplate.batchUpdate(insertCatalogsSQL, newCatalogs);

        log.info("批量插入采购品对照表信息信息");
        int[] middles = uniregJdbcTemplate.batchUpdate(insertMiddleSQL, middle);

        // 避免插入重复的数据
        middle.clear();
        newCatalogs.clear();
    }

    /**
     * 采购品信息 及 失败数据 处理
     *
     * @param originCompanyId 悦采companyId  隆道云companyId
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> exportDirectorys(Long originCompanyId, Long destCompanyId) throws DataAccessException {

        log.info("进入方法 {} ", "com.demo.service.impl.CorpExportServiceImpl.exportDirectorys");
        // 查询需要导出的采购品信息总记录数
        Long count = corpDirectorysMapper.selCountByCompanyId(originCompanyId);

        List<Object[]> params = new ArrayList<>();

        // 不符合条件的 采购品目录信息集合
        List<CorpDirectorys> failList = new ArrayList<>();

        // 根据companyID查询用户中心信息
        Map<String, Object> userMap = this.findByCondition(originCompanyId);

        // 存储 中间表信息    包含：directory_id  oldId   companyId
        List<Object[]> middle = new ArrayList<>();
        // 分页查询数量
        List<CorpDirectorys> byCompanyIdWithPageing = null;
        if (count > PAGESIZE) {
            log.info("---------- 批量读取数据 ---------");
            // 计算最后页的数量
            int remainder = (int) (count % PAGESIZE);
            // 计算 分页数 向上取整( 10.1 --> 11)
            int ceil = (int) Math.ceil(count / PAGESIZE);
            int pageNum = 0;
            for (int i = 0; i < ceil; i++) {
                if (i == ceil - 1) {
                    byCompanyIdWithPageing = corpDirectorysMapper.findByCompanyIdWithPageing(originCompanyId, pageNum, remainder);
                } else {
                    byCompanyIdWithPageing = corpDirectorysMapper.findByCompanyIdWithPageing(originCompanyId, pageNum, (int) PAGESIZE);
                    pageNum += (int) PAGESIZE;

                }
                this.dealWithDirectory(userMap, originCompanyId, destCompanyId, params, failList, byCompanyIdWithPageing, middle);
            }
        } else {
            byCompanyIdWithPageing = corpDirectorysMapper.findByCompanyIdWithPageing(originCompanyId, 0, count.intValue());
            this.dealWithDirectory(userMap, originCompanyId, destCompanyId, params, failList, byCompanyIdWithPageing, middle);
        }
        // 不符合存储条件的数据，放入map集合
        if (failList.size() != 0 && failList != null) {
            Map<String, Object> map = Maps.newHashMap();
            log.info("不符合条件的数量: {} ", failList.size());
            map.put("failDirectory", failList);
            return map;
        } else {
            return null;
        }
    }

    /**
     * 采购品信息 处理
     *
     * @param userMap                用户中心信息
     * @param originCompanyId        原companyId
     * @param destCompanyId          新companyId
     * @param params                 批处理 参数
     * @param failList               不符合条件的数据
     * @param byCompanyIdWithPageing 分页数据
     * @param middle                 采购品中间表
     */
    private void dealWithDirectory(Map<String, Object> userMap, Long originCompanyId, Long destCompanyId, List<Object[]> params, List<CorpDirectorys> failList, List<CorpDirectorys> byCompanyIdWithPageing, List<Object[]> middle) {

        long main_user_id = (long) userMap.get("id");
        String main_user_name = (String) userMap.get("name");
        log.info("进入方法：{} , 处理采购品", "com.demo.service.impl.CorpExportServiceImpl.dealWithDirectory");
        // 插入采购品信息 SQL
        String insertSql = "INSERT INTO `corp_directory`\n" +
                "(`ID`, `CATALOG_ID`, `CATALOG_NAME_PATH`, `CATALOG_ID_PATH`, `CODE`, `NAME`, `SPEC`, `ABANDON`, `PCODE`, `PRODUCTOR`, `UNITNAME`, `PRODUCING_ADDRESS`, `BRAND`, `PURPOSE`," +
                " `MARKET_PRICE`, `SPECIALITY`, `SOURCE`, `tech_parameters`, `DEMO`, `company_id`, `create_user_id`, `create_user_name`, " +
                "`create_time`, `update_user_id`, `update_user_name`, `update_time`) \n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        // 更新采购品信息[catalog_id  和   catalog_id_path ] SQL
        String updateSql = "UPDATE `corp_directory` cd\n" +
                "JOIN `middle_corp_catalog` mcc ON cd.catalog_id = mcc.old_id\n" +
                "JOIN `corp_catalog` ccl ON ccl.id = mcc.catalog_id \n" +
                "SET cd.catalog_id = ccl.id,cd.catalog_id_path = ccl.id_path \n" +
                "WHERE\n" +
                "\t cd.company_id = ?";

        String insertMiddle = "INSERT INTO `middle_corp_directory`(`directory_id`, `old_id`, `company_id`) VALUES (?, ?, ?);\n";

        for (int j = 0; j < byCompanyIdWithPageing.size(); j++) {
            CorpDirectorys directorys = byCompanyIdWithPageing.get(j);
            CorpDirectorysNew corpDirectorysNew = new CorpDirectorysNew();
            try {
                BeanUtils.copyProperties(corpDirectorysNew, directorys);

                // 赋值规则：判断悦采的creator 和 modifier 字段中是否有空值，如果有，使用中心库中的id,name为 createUserId、createUserName、updateUserId、updateUserName
                if (directorys.getCreator() == null || directorys.getModifier() == null) {
                    corpDirectorysNew.setCreateUserId(main_user_id);
                    corpDirectorysNew.setCreateUserName(main_user_name);
                    corpDirectorysNew.setUpdateUserId((int) main_user_id);
                    corpDirectorysNew.setUpdateUserName(main_user_name);
                } else {
                    corpDirectorysNew.setCreateUserId(directorys.getCreator());
                    corpDirectorysNew.setCreateUserName(main_user_name);
                    corpDirectorysNew.setUpdateUserId(directorys.getModifier().intValue());
                    corpDirectorysNew.setUpdateUserName(main_user_name);
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
                long newId = IdWork.nextId();
                //悦采  catalog_name  对应隆道云  	catalog_name_path
                //悦采  treepath      对应隆道云    catalog_id_path
                params.add(new Object[]{
                        newId, corpDirectorysNew.getCatalogId(), corpDirectorysNew.getCatalogName(), corpDirectorysNew.getTreepath(), corpDirectorysNew.getCode(),
                        corpDirectorysNew.getName(), corpDirectorysNew.getSpec(), corpDirectorysNew.getAbandon(), corpDirectorysNew.getPcode(), corpDirectorysNew.getProductor(),
                        corpDirectorysNew.getUnitName(), corpDirectorysNew.getProducingAddress(), corpDirectorysNew.getBrand(), corpDirectorysNew.getPurpose(), corpDirectorysNew.getMarketPrice(),
                        corpDirectorysNew.getSpeciality(), corpDirectorysNew.getSource(), corpDirectorysNew.getTechParameters(), corpDirectorysNew.getDemo(), corpDirectorysNew.getCompanyId(), corpDirectorysNew.getCreateUserId(), corpDirectorysNew.getCreateUserName(), corpDirectorysNew.getCreateTime(),
                        corpDirectorysNew.getUpdateUserId(), corpDirectorysNew.getUpdateUserName(), corpDirectorysNew.getUpdateTime()
                });
                middle.add(new Object[]{newId, corpDirectorysNew.getId(), originCompanyId});
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        log.info("---------- 批量插入采购品信息 ------------");
        int[] insertDirectorys = uniregJdbcTemplate.batchUpdate(insertSql, params);
        log.info("---------- 批量插入采购品中间表信息(directorys_id , oldId , companyId) ----------");
        int[] middleTable = uniregJdbcTemplate.batchUpdate(insertMiddle, middle);
        log.info("---------- 更新采购品的 catalog_id 和 catalog_id_path------------");
        int result = uniregJdbcTemplate.update(updateSql, originCompanyId);

        // 避免多次调用list值追加问题
        params.clear();
        middle.clear();
    }

    /**
     * 查询新中心库信息
     *
     * @return
     */
    public Map<String, Object> findByCondition(Long companyId) {

        Map<String, Object> map = uniregJdbcTemplate.queryForMap(" select id,name from t_reg_user where company_id = ? and is_subuser = 0", companyId);

        if (CollectionUtils.isEmpty(map)) {
            throw new RuntimeException("查询中心库信息失败");
        } else {
            return map;
        }
    }

    @Override
    public Map<String, Object> checkCorp(Long originCompanyId, Long destCompanyId) {
        List<CorpCatalogs> catalogs = corpCatalogsMapper.selCataLogsByCompanyId(originCompanyId);
        List<CorpCatalogs> failCatalogs = new ArrayList<>();
        for (int j = 0; j < catalogs.size(); j++) {
            CorpCatalogs catalog = catalogs.get(j);
            if (catalog.getName().length() > 50 || catalog.getCode().length() > 20) {
                log.error("请检查name code 字段，存储失败。原因：长度过长 --> id:{},name:{},companyId:{}", catalog.getId(), catalog.getName(), catalog.getCompanyId());
                failCatalogs.add(catalog);
                continue;
            }

        }
        List<CorpDirectorys> failDirectorys = new ArrayList<>();
        List<CorpDirectorys> corpDirectorys = corpDirectorysMapper.findByCompanyId(originCompanyId);
        for (int i = 0; i < corpDirectorys.size(); i++) {
            CorpDirectorys directorys = corpDirectorys.get(i);
            if (directorys.getName().length() > 200 || (directorys.getCode() != null && directorys.getCode().length() > 20) || (directorys.getSpec() != null && directorys.getSpec().length() > 500)) {
                log.error("字符长度过长，存储失败,name 长度为：{},code 长度为：{},spec 长度为：{}", directorys.getName().length(), directorys.getCode().length(), directorys.getSpec().length());
                failDirectorys.add(directorys);
                continue;
            }
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("failCatalogs", failCatalogs);
        map.put("failDirectorys", failDirectorys);
        return map;
    }
}
