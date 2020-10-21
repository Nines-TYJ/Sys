package com.nines.starter.vo;

import lombok.Data;

@Data
public class Response {

    public final static int OK = 200;
    public final static int ERROR = 500;
    public final static int NOT_FOUND  = 404;
    public final static int NOT_AUTH = 403;
    public final static int NOT_LOGIN = 401;

    private int code;
    private String message;
    private Object data;

    /**
     * 操作成功
     */
    public static Response ok(){
        Response result = new Response();
        result.code = OK;
        return result;
    }

    /**
     * 操作成功带返回数据
     */
    public static Response ok(Object o){
        Response result = new Response();
        result.code = OK;
        result.data = o;
        return result;
    }

    /**
     * 错误
     */
    public static Response error(String message){
        Response result = new Response();
        result.code = ERROR;
        result.message = message;
        return result;
    }

    /**
     * 未授权
     */
    public static Response notAuth(){
        Response result = new Response();
        result.code = NOT_AUTH;
        return result;
    }

    /**
     * 数据为找到
     */
    public static Response notFound(){
        Response result = new Response();
        result.code = NOT_FOUND;
        return result;
    }

    /**
     * 未登录
     */
    public static Response notLogin(){
        Response result = new Response();
        result.code = NOT_LOGIN;
        return result;
    }
}
