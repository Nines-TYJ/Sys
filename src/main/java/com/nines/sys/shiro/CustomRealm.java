package com.nines.sys.shiro;

import com.nines.sys.entity.SysUser;
import com.nines.sys.service.ISysUserService;
import com.nines.sys.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author TYJ
 * @date 2020/10/21 15:05
 */
@Slf4j
@Component
public class CustomRealm extends AuthorizingRealm {

    @Resource
    private ISysUserService userService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("————身份认证方法————");
        String token = (String) authenticationToken.getCredentials();
        String username = JWTUtil.getUsername(token);
        if (username == null){
            throw new AuthenticationException("无效的Token");
        }
        SysUser user = userService.getUsernameAndStatusByUsername(username);
        if (user == null){
            throw new AuthenticationException("用户不存在");
        }
        if (!JWTUtil.verify(token, username, user.getPassWord())){
            throw new AuthenticationException("认证失败");
        }
        if (user.getStatus() == 1){
            throw new AuthenticationException("账号已冻结");
        }
        return new SimpleAuthenticationInfo(token, token, "MyRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("————权限认证方法————");
        String username = JWTUtil.getUsername(principalCollection.toString());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 获取用户角色
        List<String> roles = userService.getRoleByUsername(username);
        // 获取用户权限
        List<String> permCodes = userService.getPermCodeByUsername(username);
        // 设置用户拥有的角色和权限
        info.setRoles(new HashSet<>(roles));
        info.setStringPermissions(new HashSet<>(permCodes));
        return info;
    }
}
