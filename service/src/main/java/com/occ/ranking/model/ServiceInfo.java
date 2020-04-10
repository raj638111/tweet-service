package com.occ.ranking.model;

public class ServiceInfo {
    String serviceName;
    String description;
    public ServiceInfo(String serviceName, String description){
        this.serviceName = serviceName;
        this.description = description;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getDescription() {
        return description;
    }
}
