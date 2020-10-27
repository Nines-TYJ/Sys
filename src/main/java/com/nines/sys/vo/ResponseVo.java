package com.nines.sys.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 数据返回类
 * @author TYJ
 */
@Data
public class ResponseVo<T> implements Serializable {

    private Integer code;

    private T data;

    private String message;

    public static ResponseVo ok(){
        return ok(null);
    }

    public static ResponseVo ok(String message){
        return ok(null, message);
    }

    public static <T> ResponseVo ok(T data){
        return ok(data, null);
    }

    /**
     * 请求成功
     */
    public static <T> ResponseVo ok(T data, String message){
        ResponseVo responseVo = new ResponseVo();
        responseVo.code = HttpStatus.OK.value();
        responseVo.data = data;
        responseVo.message = message;
        return responseVo;
    }

    /**
     * 服务器错误
     */
    public static ResponseVo error(String message){
        ResponseVo responseVo = new ResponseVo();
        responseVo.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        responseVo.message = message;
        return responseVo;
    };

    /**
     * 请求失败
     */
    public static ResponseVo fail(String message){
        ResponseVo responseVo = new ResponseVo();
        responseVo.code = HttpStatus.BAD_REQUEST.value();
        responseVo.message = message;
        return responseVo;
    }

    /**
     * 未登录，没有权限
     */
    public static ResponseVo notAccess(String message){
        ResponseVo responseVo = new ResponseVo();
        responseVo.code = HttpStatus.UNAUTHORIZED.value();
        responseVo.message = message;
        return responseVo;
    }
}
