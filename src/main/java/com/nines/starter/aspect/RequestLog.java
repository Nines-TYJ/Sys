package com.nines.starter.aspect;

import lombok.Data;

@Data
public class RequestLog {

    private String url;
    private String ip;
    private String classMethod;
    private Object[] args;

    public RequestLog(String url, String ip, String classMethod, Object[] args) {
        this.url = url;
        this.ip = ip;
        this.classMethod = classMethod;
        this.args = args;
    }
}
