package com.nines.sys.config.shiro.realm;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.nines.sys.entity.SysUser;
import com.nines.sys.service.ISysUserService;
import com.nines.sys.config.shiro.jwt.JWTToken;
import com.nines.sys.util.Constant;
import com.nines.sys.util.JWTUtil;
import com.nines.sys.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author TYJ
 * @date 2020/10/21 15:05
 */
@Slf4j
@Component
public class JWTRealm extends AuthorizingRealm {

    @Resource
    private ISysUserService userService;

    private static final String PREFIX_SHIRO_TOKEN = Constant.PREFIX_SHIRO_TOKEN;

    /**
     * token登录
     * @param token 密钥
     * @return boolean
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("————Token 认证方法————");
        String token = (String) authenticationToken.getPrincipal();
        String username;
        try {
            username= JWTUtil.getUsername(token);
        }catch (Exception e){
            throw new AuthenticationException("token非法，不是规范的token，可能被篡改了，或者过期了");
        }
        if (username == null){
            throw new AuthenticationException("无效的Token,用户名为空");
        }
        SysUser user = userService.getUserByUsername(username);
        if (user == null){
            throw new AuthenticationException("用户不存在");
        }
        //开始认证，只要AccessToken没有过期，或者refreshToken的时间节点和AccessToken一致即可
        if (RedisUtil.hasKey(PREFIX_SHIRO_TOKEN + username)){
            //判断AccessToken有无过期
            if (!JWTUtil.verify(token, user.getPassWord())){
                throw new TokenExpiredException("token认证失效，token过期");
            }
            //判断AccessToken和refreshToken的时间节点是否一致
            Long current = (Long) RedisUtil.get(PREFIX_SHIRO_TOKEN + username);
            if (!Objects.requireNonNull(JWTUtil.getExpire(token)).equals(current)){
                throw new AuthenticationException("token已经失效，请重新登录！");
            }
            if (user.getStatus() == 1){
                throw new AuthenticationException("账号已冻结");
            }
            return new SimpleAuthenticationInfo(user, token, "jwtRealm");
        }
        throw new AuthenticationException("token过期或者Token错误！！");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return new SimpleAuthorizationInfo();
    }
}
