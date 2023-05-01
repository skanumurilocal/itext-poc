package com.tmobile.reports.service.impl;

import com.tmobile.reports.dao.AppInfoRepository;
import com.tmobile.reports.model.AppInfo;
import com.tmobile.reports.service.ReportsService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportsServiceImpl implements ReportsService {

    @Autowired
    private AppInfoRepository appInfoRepository;

    @Override
    public Map<Integer,Map<String,List<AppInfo>>> fetchAllAppsInfo() {
        List<AppInfo> appInfoList = appInfoRepository.findAll();
        Map<Integer,Map<String,List<AppInfo>>> groupSelection = new HashMap<>();
        groupSelection = appInfoList.stream().collect(
                Collectors.groupingBy(AppInfo::getSortOrder,Collectors.groupingBy(AppInfo::getCategoryName)));
        TreeMap<Integer,Map<String,List<AppInfo>>> groupSelectionSorted = new TreeMap<>(Collections.reverseOrder());
        groupSelectionSorted.putAll(groupSelection);
        return groupSelectionSorted;
    }
}
