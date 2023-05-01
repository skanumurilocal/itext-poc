package com.tmobile.reports.compartor;

import java.util.Comparator;

public class ReverseComparator implements Comparator<Integer> {
    public int compare(Integer num1, Integer num2) {
        return num2.compareTo(num1);
    }
}
