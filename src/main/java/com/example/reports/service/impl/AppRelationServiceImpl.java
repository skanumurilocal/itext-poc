package com.example.reports.service.impl;

import com.example.reports.dao.AppRelationRepository;
import com.example.reports.model.AppRelation;
import com.example.reports.service.AppRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppRelationServiceImpl implements AppRelationService {
    @Autowired
    private AppRelationRepository appRelationRepository;
    @Override
    public List<AppRelation> getAllRelationsByRequestId(Long requestId) {
        return appRelationRepository.findByRequestId(requestId);
    }
}
