package com.demo.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.bidlink.base.ServiceResult;
import cn.bidlink.framework.util.gen.IdWork;
import cn.bidlink.usercenter.server.entity.TRegUser;
import cn.bidlink.usercenter.server.service.DubboTRegUserService;
import com.demo.model.*;
import com.demo.persistence.dao.CorpCatalogsMapper;
import com.demo.persistence.dao.CorpDirectorysMapper;
import com.demo.service.DataMigrationService;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  16:07 2019-02-20
 * @Description :
 */
@Service("DataMigrationService")
public class DataMigrationServiceImpl implements DataMigrationService {

    private static Logger log = LoggerFactory.getLogger(DataMigrationServiceImpl.class);
    @Autowired
    private CorpCatalogsMapper corpCatalogsMapper;

    @Autowired
    private CorpDirectorysMapper corpDirectorysMapper;

    @Autowired
    private DubboTRegUserService dubboTRegUserService;

    /**
     *  生成 key-value对照
     *  key:旧ID
     *  value：新生成的ID
     * @param corpCatalogs
     * @return
     */
    public Map<Long, Long> idMap(List<CorpCatalog> corpCatalogs) {
        Map<Long, Long> map = Maps.newHashMap();
        log.info("即将进入循环 ------------");
        log.info("参数值：{}",corpCatalogs.size());
        if(corpCatalogs.size()<=0){
            log.error("参数为空，请检验");
        }else{
            log.info("生成id对应关系 key-value");
            for (int i = 0; i < corpCatalogs.size(); i++) {
                Long oldId = corpCatalogs.get(i).getId();
                Long oldParentId = corpCatalogs.get(i).getParentId();
                // 新生成的ID
                long newId = IdWork.nextId();

                map.put(oldId, newId);//代表对照关系
                log.info("oldId:{},oldParentId:{},newId:{}", oldId, oldParentId, newId);
            }
        }
        return map;
    }

    /**
     * 采购品目录信息  及  对照关系表数据导出
     * @param companyId
     * @return
     */
    @Override
    public Map<String, Object> exportCorpCatalogs(Long companyId) {
        log.info("进入方法：{}", "exportCorpCatalogs()");

        // 根据companyID查询悦采 采购品目录信息
        List<CorpCatalog> corpCatalogs = corpCatalogsMapper.selCataLogs(companyId);

        List<CorpCatalogNew> listCatalog = new ArrayList<>(); // 新采购品目录集合
        // 不符合条件的 采购品目录信息集合
        List<CorpCatalog> failList = new ArrayList<>();
        // 存储 中间表信息    包含：oldId  newId   companyId
        List<MiddleTable> middleTables = new ArrayList<>();
        // 获取新旧Id对照Map
        Map<Long, Long> idsMap = idMap(corpCatalogs);

        for (CorpCatalog catalog : corpCatalogs){

            CorpCatalogNew catalogNew = new CorpCatalogNew();

            try {
                if(catalog.getName().length()>50 || catalog.getCode().length()>20 ){
                    log.error("请检查name code 字段，存储失败。原因：长度过长 --> id:{},name:{},companyId:{}",catalog.getId(),catalog.getName(),catalog.getCompanyId());
                    failList.add(catalog);
                    break;
                }
                // 给CorpCatalogNew对象赋值
                BeanUtils.copyProperties(catalogNew,catalog);

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

                // 设置最新时间
                catalogNew.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

                // 获取oldId
                Long oldId = catalog.getId();
                // 获取新Id
                Long newId = idsMap.get(oldId);

                catalogNew.setId(newId);

                // 将新采购品目录信息添加进集合
                listCatalog.add(catalogNew);

                // 将采购品目录信息添加进集合
                middleTables.add(new MiddleTable(oldId,newId,catalog.getCompanyId()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        Map<String,Object> map = Maps.newHashMap();
        map.put("success",listCatalog);
        map.put("fail",failList);
        map.put("middleTable",middleTables);
        map.put("idsMap",idsMap);
        return map;
    }

    /**
     * 导出采购品信息
     * @param companyId 公司ID
     * @return
     */
    public Map<String,Object> exportDirectorys(Long companyId) {
        CorpDirectorys corpDirectorys = new CorpDirectorys();
        corpDirectorys.setCompanyId(companyId);

        List<CorpDirectorys> directorysList = corpDirectorysMapper.findByCondition(corpDirectorys);

        List<CorpDirectorysNew> copyList = new ArrayList<>();
        for (CorpDirectorys directorys : directorysList) {

            // 新corp_directory表的id字段
            directorys.setId(IdWork.nextId());
            CorpDirectorysNew corpDirectorysNew = new CorpDirectorysNew();
            try {
                BeanUtils.copyProperties(corpDirectorysNew, directorys);
                // 根据companyID查询用户中心信息
                List<TRegUser> tRegUser = findByCondition(companyId);

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

                corpDirectorysNew.setPricePrecision(2l);
                corpDirectorysNew.setUnitPrecision(2l);
                corpDirectorysNew.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

                copyList.add(corpDirectorysNew);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        Map<String,Object> map = Maps.newHashMap();
        map.put("directorys",copyList);
        return map;
    }

    public Map<String, Object> importExcel() {
        String path = "C:/Users/从尧/Desktop/8.xls";
        Map<String, Object> map = Maps.newHashMap();
        try {
            ImportParams params = new ImportParams();
            params.setTitleRows(0);
            params.setHeadRows(1);
            params.setStartRows(0);
            params.setNeedVerify(true);// 配置后，默认保存路径
            for (int i = 0; i < 2; i++) {
                if (i == 0) {
                    ExcelImportResult<MiddleTable> result = ExcelImportUtil.importExcelMore(new FileInputStream(path), MiddleTable.class, params);
                    List<MiddleTable> successList = result.getList();
                    List<MiddleTable> failList = result.getFailList();
                    log.info("是否存在验证未通过的数据:" + result.isVerfiyFail());
                    log.info("验证通过的数量:" + successList.size());
                    log.info("验证未通过的数量:" + failList.size());
                    for (MiddleTable success : successList) {
                        log.info("成功列表信息:" + success.toString());
                    }
                    for (MiddleTable fail : failList) {
                        log.info("失败列表信息:" + fail.toString());
                    }
                    map.put("middleSuccess", successList);
                    map.put("middleFail", failList);
                } else if (i == 1) {
                    ExcelImportResult<CorpCatalogNew> result = ExcelImportUtil.importExcelMore(new FileInputStream(path), CorpCatalogNew.class, params);
                    List<CorpCatalogNew> successList = result.getList();
                    List<CorpCatalogNew> failList = result.getFailList();
                    log.info("是否存在验证未通过的数据:" + result.isVerfiyFail());
                    log.info("验证通过的数量:" + successList.size());
                    log.info("验证未通过的数量:" + failList.size());
                    log.info("验证未通过的数量:" + failList.size());
                    for (CorpCatalogNew success : successList) {
                        log.info("成功列表信息:" + success.toString());
                    }
                    for (CorpCatalogNew fail : failList) {
                        log.info("失败列表信息:" + fail.toString());
                    }
                    map.put("catalogSuccess", successList);
                    map.put("catalogFail", failList);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return map;
    }

    /**
     * 查询新中心库信息
     * @return
     */
    public List<TRegUser> findByCondition(Long companyId){
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
