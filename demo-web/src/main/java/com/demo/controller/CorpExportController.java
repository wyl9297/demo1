package com.demo.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.demo.model.CorpCatalogs;
import com.demo.model.CorpDirectorys;
import com.demo.service.CorpExportService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    @Qualifier("CorpExportService")
    private CorpExportService corpExportService;

    @Autowired
    @Qualifier("purchaseJdbcTemplate")
    private JdbcTemplate corpJdbcTemplate;

    // 根据companyId进行数据导出
    @RequestMapping("/corpExport")
    @ResponseBody
    public String corpExport(HttpServletResponse response,
                             @RequestParam("originCompanyId") Long originCompanyId,
                             @RequestParam("destCompanyId") Long destCompanyId) {
        Map<String, Object> catalogsMap = corpExportService.exportCorpCatalogs(originCompanyId, destCompanyId);
        Map<String, Object> directorysMap = corpExportService.exportDirectorys(originCompanyId, destCompanyId);

        if (CollectionUtils.isEmpty(catalogsMap) && CollectionUtils.isEmpty(directorysMap)) {
            log.info("----------导出采购品信息和采购品目录信息正常，无不符合条件的数据-----------");
            return "success";
        } else {
            log.info("----------准备导出不符合条件的数据-----------");
            Map<String, Object> failCorpCatalog = new HashMap<>();
            Map<String, Object> failDirectory = new HashMap<>();
            List<Map<String, Object>> list = new ArrayList<>();
            if (!CollectionUtils.isEmpty(catalogsMap)) {
                // 导出不符合条件的采购品信息
                ExportParams exportParams = new ExportParams();
                exportParams.setSheetName("eroor_catalog");
                failCorpCatalog.put("title", exportParams);
                failCorpCatalog.put("entity", CorpCatalogs.class);
                failCorpCatalog.put("data", catalogsMap.get("failCatalog"));
                list.add(failCorpCatalog);
            }

            if (!CollectionUtils.isEmpty(directorysMap)) {
                // 导出不符合条件的采购品信息
                ExportParams exportParams3 = new ExportParams();
                exportParams3.setSheetName("eroor_directorys");
                failDirectory.put("title", exportParams3);
                failDirectory.put("entity", CorpDirectorys.class);
                failDirectory.put("data", directorysMap.get("failDirectory"));
                list.add(failDirectory);
            }

            Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);

            // 设置响应输出的头类型
            response.setHeader("content-Type", "application/vnd.ms-excel");
            // 下载文件的默认名称
            response.setHeader("Content-Disposition", "attachment;filename=Error_Info.xls");
            try {
                workbook.write(response.getOutputStream());
            } catch (IOException e) {
                System.out.println("导出Excel出错啦！！！");
                e.printStackTrace();
            }
        }
        return "success";
    }
}
