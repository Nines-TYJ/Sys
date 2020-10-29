package com.nines.sys.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.nines.sys.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;

/**
 * @author TYJ
 * @date 2020/10/21 10:03
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕捉shiro的异常
     */
    @ExceptionHandler(ShiroException.class)
    public ResponseVo handle401(ShiroException e) {
        log.error("shiro异常！原因是：{}",e.getMessage());
        return ResponseVo.notAccess(e.getMessage());
    }

    /**
     * 处理未登录的异常
     */
    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseVo handle401(AuthenticationException e){
        log.error("未登录异常！原因是：{}",e.getMessage());
        return ResponseVo.notAccess("未登录");
    }

    /**
     * 捕捉没有相应的权限或者角色的异常
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseVo handle401(UnauthorizedException e) {
        log.error("无权限异常！原因是：{}",e.getMessage());
        return ResponseVo.notAccess("无访问权限");
    }

    /**
     * 处理Assert的异常
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseVo handler(IllegalArgumentException e) {
        log.error("Assert异常！原因是：{}",e.getMessage());
        return ResponseVo.fail(e.getMessage());
    }

    /**
     * token过期
     */
    @ExceptionHandler(value = TokenExpiredException.class)
    public ResponseVo handler(TokenExpiredException e){
        log.error("token过期！原因是：{}",e.getMessage());
        return ResponseVo.fail("token已经过期，请重新登录");
    }

    /**
     * 请求没有请求体
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseVo handler(HttpMessageNotReadableException e){
        log.error("空的请求体");
        return ResponseVo.fail("空的请求体");
    }

    /**
     * 运行时错误处理
     */
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseVo handle(RuntimeException e){
        log.error("发生运行时异常！原因是:",e.getMessage());
        return ResponseVo.error("服务器错误");
    }

    /**
     * 处理自定义的业务异常
     */
    @ExceptionHandler(value = BizException.class)
    public ResponseVo bizExceptionHandler(BizException e){
        log.error("发生业务异常！原因是：{}",e.getErrorMsg());
        return ResponseVo.error(e.getErrorMsg());
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseVo exceptionHandler(Exception e){
        log.error("未知异常！原因是：{}",e.getMessage());
        return ResponseVo.error("服务器错误");
    }

}
