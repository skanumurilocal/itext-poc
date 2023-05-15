package com.example.reports.controller;

import com.example.reports.model.AppRelation;
import com.example.reports.pdf.PdfGenerator;
import com.example.reports.model.AppInfo;
import com.example.reports.service.AppRelationService;
import com.example.reports.service.ReportsService;
import com.example.reports.util.JsonFlattenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@Api(value = "Reports Space", tags={"reports-service"})
public class ReportsController {

    @Autowired
    private ReportsService reportsService;
    @Autowired
    private AppRelationService appRelationService;
    @Autowired
    private PdfGenerator pdfGenerator;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JsonFlattenUtil jsonFlattenUtil;
    @GetMapping(value = {"/download"})
    @ApiOperation("Get Report from Apps")
    public void downloadReport(@RequestParam String id, HttpServletResponse response) throws IOException {
        List<AppRelation> appRelations = appRelationService.getAllRelationsByRequestId(Long.parseLong(id));
        Map<String,String> resultMap = new HashMap<>();
        for (AppRelation appRelation :
                appRelations) {
            // convert JSON file to map
            Map<String, Object> map = objectMapper.readValue(appRelation.getRequestJson(),HashMap.class);
            jsonFlattenUtil.jsonFlattenProcessor(map,resultMap);
        }
        Map<Integer,Map<String, List<AppInfo>>> responseData = reportsService.fetchAllAppsInfo();
        pdfGenerator.generatePdf(responseData,resultMap,response);
    }




}
