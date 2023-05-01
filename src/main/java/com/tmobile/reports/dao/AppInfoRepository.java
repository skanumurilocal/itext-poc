package com.tmobile.reports.dao;

import com.tmobile.reports.model.AppInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppInfoRepository extends JpaRepository<AppInfo, Long> {
}