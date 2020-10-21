package com.nines.sys.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nines.sys.exception.BizException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 数据返回类
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel
public class ResponseVo<T> implements Serializable {

    @ApiModelProperty("响应码，参考HttpStatus")
    private Integer code;
    @ApiModelProperty("响应数据")
    private T data;
    @ApiModelProperty("响应消息")
    private String message;

    /**
     * bad request 用于响应用户参数错误
     */
    public static ResponseVo badRequest(String message) {
        ResponseVo response = new ResponseVo();
        response.code = HttpStatus.OK.value();
        response.message = message;
        return response;
    }

    /**
     * 用于返回服务错误信息
     */
    public static ResponseVo error(String message) {
        ResponseVo response = new ResponseVo();
        response.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        response.message = message;
        return response;
    }

    /**
     * 执行成功
     */
    public static ResponseVo ok() {
        ResponseVo response = new ResponseVo();
        response.code = HttpStatus.OK.value();
        return response;
    }

    /**
     * 执行成功
     */
    public static <T> ResponseVo<T> ok(T data) {
        ResponseVo<T> response = new ResponseVo<>();
        response.code = HttpStatus.OK.value();
        response.data = data;
        return response;
    }

    /**
     * 资源没有找到
     */
    public static ResponseVo notFound() {
        ResponseVo response = new ResponseVo();
        response.code = HttpStatus.NOT_FOUND.value();
        return response;
    }

    /**
     * 没有登录
     */
    public static ResponseVo notLogin() {
        ResponseVo response = new ResponseVo();
        response.code = HttpStatus.UNAUTHORIZED.value();
        response.message = "未登陆";
        return response;
    }

    /**
     * 没有权限
     */
    public static ResponseVo forbidden() {
        ResponseVo response = new ResponseVo();
        response.code = HttpStatus.FORBIDDEN.value();
        response.message = "未授权";
        return response;
    }

    /**
     * 不支持的请求方法
     */
    public static ResponseVo methodNotAllow() {
        ResponseVo response = new ResponseVo();
        response.code = HttpStatus.METHOD_NOT_ALLOWED.value();
        response.message = "不支持的请求方法";
        return response;
    }

    /**
     * 从BizException构建返回结果
     */
    public static ResponseVo buildFromBizException(BizException bizException) {
        ResponseVo response = new ResponseVo();
        response.code = bizException.getErrorCode();
        response.message = bizException.getMessage();
        return response;
    }

}
