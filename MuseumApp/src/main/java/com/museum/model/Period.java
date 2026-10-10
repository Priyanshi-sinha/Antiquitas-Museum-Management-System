package com.museum.model;

public class Period {
    public String id, name, createdAt, updatedAt;
    public String type = "Period";
    public int startYear, endYear;
    public String getYearRange() {
        String s = startYear < 0 ? Math.abs(startYear) + " BCE" : startYear + " CE";
        String e = endYear   < 0 ? Math.abs(endYear)   + " BCE" : endYear   + " CE";
        return s + " – " + e;
    }
}
