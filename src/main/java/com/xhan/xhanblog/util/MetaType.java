package com.xhan.xhanblog.util;

public enum MetaType {

    CATEGORY("CATEGORY"), TAG("TAG");

    private String type;

    public String getType() {
        return type;
    }

    MetaType(String string) {
        this.type = string;
    }
}
