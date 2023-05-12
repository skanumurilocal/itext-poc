package com.example.reports.model;

import lombok.Data;

@Data

public class DisplayItem {
    private String displayName;
    private String displayValue;
    public DisplayItem(String name, Object value){
        this.displayName= name;
        this.displayValue=value.toString();

    }
}
