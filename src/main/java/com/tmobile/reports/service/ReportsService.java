package com.tmobile.reports.service;

import com.tmobile.reports.model.AppInfo;

import java.util.List;
import java.util.Map;

public interface ReportsService {

    public Map<Integer, Map<String,List<AppInfo>>> fetchAllAppsInfo();
}
