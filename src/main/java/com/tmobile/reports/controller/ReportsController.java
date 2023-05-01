package com.tmobile.reports.controller;

import com.tmobile.reports.model.AppInfo;
import com.tmobile.reports.pdf.PdfGenerator;
import com.tmobile.reports.service.ReportsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@Api(value = "Reports Space", tags={"reports-service"})
public class ReportsController {

    @Autowired
    private ReportsService reportsService;
    @Autowired
    private PdfGenerator pdfGenerator;
    @GetMapping(value = {"/download"})
    @ApiOperation("Get Report from Apps")
    public void downloadReport(@RequestParam String id, HttpServletResponse response) throws FileNotFoundException {
        Map<Integer,Map<String, List<AppInfo>>> responseData = reportsService.fetchAllAppsInfo();
        pdfGenerator.generatePdf(responseData);
    }




}
