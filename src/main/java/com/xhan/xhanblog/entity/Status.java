package com.xhan.xhanblog.entity;

public enum Status {
    PUBLISHED("published"), BANISHED("banished");

    private String status;

    public String getStatus() {
        return status;
    }

    Status(String status) {
        this.status = status;
    }
}
