package com.yasoka.eazyscreenrecord.model;

public class CountryFilterModel {
    private String countryCode;
    private String countryName;

    public CountryFilterModel(String str, String str2) {
        this.countryName = str;
        this.countryCode = str2;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public void setCountryName(String str) {
        this.countryName = str;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String str) {
        this.countryCode = str;
    }
}
