package com.example.httphelplib.host;

import java.io.Serializable;

public class UrlEntity implements Serializable {

    private static final long serialVersionUID = 8985632071136009188L;

    private String name;
    private String scheme;
    private String host;
    private int port;
    private boolean isSelected;

    public UrlEntity() {
    }

    public UrlEntity(String name, String scheme, String host, int port, boolean isSelected) {
        this.name = name;
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.isSelected = isSelected;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "{\"name\": " + name + "" +
                ",\"scheme\": " + scheme + "" +
                ",\"host\": " + host + "" +
                ",\"port\": " + port + "" +
                ",\"isSelected\": " + isSelected + "}";
    }
}
