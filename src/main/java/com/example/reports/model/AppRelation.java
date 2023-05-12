package com.example.reports.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "app_relation")
public class AppRelation {
    @Id
    @Column(name = "appid", nullable = false)
    private Long appId;
    @Column(name="requestid")
    private Long requestId;
    @Column(name = "requestjson")
    private String requestJson;

}
