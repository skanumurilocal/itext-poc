package com.example.reports.service;

import com.example.reports.model.AppInfo;

import java.util.List;
import java.util.Map;

public interface ReportsService {

    public Map<Integer, Map<String,List<AppInfo>>> fetchAllAppsInfo();
}
