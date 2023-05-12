package com.example.reports.service.impl;

import com.example.reports.dao.AppInfoRepository;
import com.example.reports.model.AppInfo;
import com.example.reports.service.ReportsService;
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
        appInfoList.stream().forEach(appInfo -> {
            appInfo.setSortOrder(appInfo.getSortOrder()==null ?0: appInfo.getSortOrder());

        });
        groupSelection = appInfoList.stream().collect(
                Collectors.groupingBy(AppInfo::getSortOrder,Collectors.groupingBy(AppInfo::getCategoryName)));
        TreeMap<Integer,Map<String,List<AppInfo>>> groupSelectionSorted = new TreeMap<>(Collections.reverseOrder());
        groupSelectionSorted.putAll(groupSelection);
        return groupSelectionSorted;
    }
}
