package com.example.reports.dao;

import com.example.reports.model.AppRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppRelationRepository extends JpaRepository<AppRelation,Long> {
    List<AppRelation> findByRequestId(Long requestId);
}
