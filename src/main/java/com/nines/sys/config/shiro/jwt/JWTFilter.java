package com.nines.sys.config.shiro.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nines.sys.service.ISysUserService;
import com.nines.sys.util.Constant;
import com.nines.sys.util.JWTUtil;
import com.nines.sys.util.RedisUtil;
import com.nines.sys.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Objects;

/**
 * preHandle->isAccessAllowed->isLoginAttempt->executeLogin
 * @author TYJ
 * @date 2020/10/21 14:13
 */
@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {

    /**
     * 登录标识
     */
    private static final String LOGIN_SIGN = Constant.LOGIN_SIGN;

    /**
     * shiro token缓存前缀
     */
    private static final String PREFIX_SHIRO_TOKEN = Constant.PREFIX_SHIRO_TOKEN;

    /**
     * token缓存 有效时间（分钟）
     */
    private static final long SHIRO_TOKEN_EXPIRE_TIME = 1000 * 60 * Constant.SHIRO_TOKEN_EXPIRE_TIME;

    /**
     * 执行登陆操作
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(LOGIN_SIGN);
        JWTToken jwtToken = new JWTToken(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 判断用户是否想要登入。
     * 检测 header 里面是否包含 Authorization 字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader(LOGIN_SIGN);
        return token != null;
    }

    /**
     * 如果带有 token，则对 token 进行检查，否则直接通过
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //判断请求的请求头是否带上 "Token"
        if (isLoginAttempt(request, response)) {
            //如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
            try {
                executeLogin(request, response);
                return true;
            } catch (Exception e) {
                /*
                 * 注意这里捕获的异常其实是在Realm抛出的，但是由于executeLogin（）方法抛出的异常是从login（）来的，
                 * login抛出的异常类型是AuthenticationException，所以要去获取它的子类异常才能获取到我们在Realm抛出的异常类型。
                 */
                String msg = e.getMessage();
                Throwable cause = e.getCause();
                // AccessToken过期，尝试去刷新token
                if (cause instanceof TokenExpiredException){
                    String result = refreshToken(request, response);
                    if ("success".equals(result)){
                        return true;
                    }
                    msg = result;
                }
                log.error("Forbidden: {}", msg);
                responseError(response, msg);
            }
        }
        //如果请求头不存在 Token，则可能是执行登陆操作或者是游客状态访问，无需检查 token，直接返回 true
        return false;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) {
        responseError(response, "未登录");
        return false;
    }

    /**
     * 回写没有登陆json
     */
    private void responseError(ServletResponse response, String message) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        try {
            OutputStream out = httpServletResponse.getOutputStream();
            out.write(new ObjectMapper().writeValueAsString(ResponseVo.notAccess(message)).getBytes());
//            httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(ResponseVo.notLogin(message)));
            out.flush();
            out.close();
        }catch (Exception ex){
            log.error("回写数据发生错误: {}", ex);
        }
    }

    private  <T> T getBean(Class<T> clazz, HttpServletRequest request){
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        return applicationContext.getBean(clazz);
    }

    private String refreshToken(ServletRequest request, ServletResponse response){
        HttpServletRequest req = (HttpServletRequest) request;
        // 获取传递过来的accessToken
        String accessToken = req.getHeader(LOGIN_SIGN);
        // 获取token里面的用户名
        String username = JWTUtil.getUsername(accessToken);
        // 判断refreshToken是否过期，过期后redis中的username就不存在
        if (RedisUtil.hasKey(PREFIX_SHIRO_TOKEN + username)){
            //判断refresh的时间节点和传递过来的accessToken的时间节点是否一致，不一致校验失败
            Long tokenCurrent = (Long) RedisUtil.get(PREFIX_SHIRO_TOKEN + username);
            if (Objects.requireNonNull(JWTUtil.getExpire(accessToken)).equals(tokenCurrent)){
               // 获取当前时间戳
                Long current = System.currentTimeMillis();
                // 获取username的password
                ISysUserService userService = getBean(ISysUserService.class, req);
                String password = userService.getUserByUsername(username).getPassWord();
                // 生产刷新的token
                String token = JWTUtil.sign(username, password, current);
                // 刷新redis里面的refreshToken,过期时间是24小时
                RedisUtil.set(PREFIX_SHIRO_TOKEN + username, current, SHIRO_TOKEN_EXPIRE_TIME);
                // 再次交给shiro进行认证
                JWTToken jwtToken = new JWTToken(token);
                try {
                    getSubject(request, response).login(jwtToken);
                    // 最后将刷新的AccessToken存放在Response的Header中的Authorization字段返回
                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                    httpServletResponse.setHeader(LOGIN_SIGN, token);
                    httpServletResponse.setHeader("Access-Control-Expose-Headers", LOGIN_SIGN);
                    return "success";
                } catch (AuthenticationException e) {
                    return e.getMessage();
                }
            }
        }
        return "token认证失效，token过期，重新登陆";
    }
}
