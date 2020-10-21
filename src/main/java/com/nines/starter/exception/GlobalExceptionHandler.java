package com.nines.starter.exception;

import com.nines.starter.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author TYJ
 * @date 2020/10/21 10:03
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常
     */
    @ExceptionHandler(value = BizException.class)
    public ResponseVo bizExceptionHandler(HttpServletRequest req, BizException e){
        log.error("发生业务异常！原因是：{}",e.getErrorMsg());
        return ResponseVo.error(e.getErrorMsg());
    }

    /**
     * 处理空指针的异常
     */
    @ExceptionHandler(value =NullPointerException.class)
    public ResponseVo exceptionHandler(HttpServletRequest req, NullPointerException e){
        log.error("发生空指针异常！原因是:",e);
        return ResponseVo.badRequest(e.getMessage());
    }


    /**
     * 处理其他异常
     */
    @ExceptionHandler(value =Exception.class)
    public ResponseVo exceptionHandler(HttpServletRequest req, Exception e){
        log.error("未知异常！原因是:",e);
        return ResponseVo.error(e.getMessage());
    }

}
