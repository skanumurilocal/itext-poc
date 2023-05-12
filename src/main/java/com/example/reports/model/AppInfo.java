package com.example.reports.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "app_info")
@IdClass(AppId.class)
public class AppInfo {
    @Id
    @Column(name="APP_ID")
    private Integer appId;
    @Id
    @Column(name = "CATEGORY_NAME")
    private String categoryName;
    @Id
    @Column(name="BUSINESS_TERM")
    private String businessTerm;
    @Column(name="SORT_ORDER")
    private Integer sortOrder;
}
