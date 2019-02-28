package com.demo.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.demo.model.CorpCatalogNew;
import com.demo.model.CorpDirectorysNew;
import com.demo.model.MiddleTable;
import com.demo.service.DataMigrationService;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  16:05 2019-02-20
 * @Description : 目前 悦采数据源配置的未测试库
 */
@Controller
public class CorpExportController {

    private static Logger log = LoggerFactory.getLogger(CorpExportController.class);

    @Autowired
    @Qualifier("DataMigrationService")
    private DataMigrationService corpCatalogsService;


    // 根据companyId进行数据导出
    @RequestMapping("/export")
    public void export(HttpServletResponse response,Long companyId) {
        if(companyId ==null){
            log.error("companyId为必填字段");
        }else{
            log.info("进入Controller {}",CorpExportController.class.getName());
            Map<String, Object> maps = corpCatalogsService.exportCorpCatalogs(companyId);
            Map<String, Object> directorysMaps = corpCatalogsService.exportDirectorys(companyId);
            if (maps != null && directorysMaps != null) {
                try {
                    log.info("数据导出准备----------");
                    // =========easypoi部分
                    // 导出中间表数据(oldId,newId,companyId)
                    ExportParams exportParams = new ExportParams();
                    exportParams.setSheetName("middleTable");
                    Map<String,Object> middleMap = new HashMap<>();
                    middleMap.put("title",exportParams);
                    middleMap.put("entity", MiddleTable.class);
                    middleMap.put("data",maps.get("middleTable"));

                    // 导出采购品目录信息
                    ExportParams exportParams2 = new ExportParams();
                    Map<String,Object> catalogMap = new HashMap<>();
                    exportParams2.setSheetName("corp_catalogs");
                    catalogMap.put("title",exportParams2);
                    catalogMap.put("entity", CorpCatalogNew.class);
                    catalogMap.put("data",maps.get("success"));

                    // 导出采购品信息
                    ExportParams exportParams3 = new ExportParams();
                    Map<String,Object> directorysMap = new HashMap<>();
                    exportParams3.setSheetName("corp_directorys");
                    directorysMap.put("title",exportParams3);
                    directorysMap.put("entity", CorpDirectorysNew.class);
                    directorysMap.put("data",directorysMaps.get("directorys"));

                    /***
                     * 导出失败的采购品信息.....
                     */

                    List<Map<String,Object>> list = new ArrayList<>();
                    list.add(middleMap);
                    list.add(catalogMap);
                    list.add(directorysMap);
                    Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);

                    // 设置响应输出的头类型
                    response.setHeader("content-Type", "application/vnd.ms-excel");
                    // 下载文件的默认名称
                    response.setHeader("Content-Disposition", "attachment;filename=CorpInfoExport.xls");
                    workbook.write(response.getOutputStream());
                } catch (Exception e) {
                    System.out.println("有个异常");
                }
            }
        }
    }
}
