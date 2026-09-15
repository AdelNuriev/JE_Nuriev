package org.example.discoverystarter.dto;

public class RegistrationRequest {

    private String serviceName;
    private int port;

    public RegistrationRequest(String serviceName, int port) {
        this.serviceName = serviceName;
        this.port = port;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}