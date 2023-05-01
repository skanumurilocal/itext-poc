package com.tmobile.reports.model;

import com.tmobile.reports.service.FirstTable;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TableFields {
    private List<DisplayItem> fieldList = new ArrayList<>();

    public void add(String displayName, Object value) {
        fieldList.add(new DisplayItem(displayName, value));
    }

    public void add(DisplayItem field) {
        fieldList.add(field);
    }
}
