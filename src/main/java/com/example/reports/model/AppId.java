package com.example.reports.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
public class AppId implements Serializable {
    private Integer appId;
    private String categoryName;
    private String businessTerm;
}
