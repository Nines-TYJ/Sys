package com.nines.sys.shiro.realm;

import com.nines.sys.entity.SysUser;
import com.nines.sys.service.ISysUserService;
import com.nines.sys.shiro.JWTToken;
import com.nines.sys.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TYJ
 * @date 2020/10/21 15:05
 */
@Slf4j
@Component
public class JWTRealm extends AuthorizingRealm {

    @Resource
    private ISysUserService userService;

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
        String username = JWTUtil.getUsername(token);
        if (username == null){
            throw new AuthenticationException("无效的Token,用户名为空");
        }
        SysUser user = userService.getUserByUsername(username);
        if (user == null){
            throw new AuthenticationException("用户不存在");
        }
        if (!JWTUtil.verify(token, user.getUserName(), user.getPassWord())){
            throw new AuthenticationException("token验证失败");
        }
        if (user.getStatus() == 1){
            throw new AuthenticationException("账号已冻结");
        }
        return new SimpleAuthenticationInfo(user, token, "jwtRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return new SimpleAuthorizationInfo();
    }
}
