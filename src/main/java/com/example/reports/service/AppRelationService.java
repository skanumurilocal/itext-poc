package com.example.reports.service;

import com.example.reports.model.AppRelation;

import java.util.List;

public interface AppRelationService {
    List<AppRelation> getAllRelationsByRequestId(Long requestId);
}
