package com.nines.sys.shiro.realm;

import com.nines.sys.entity.SysUser;
import com.nines.sys.service.ISysUserService;
import com.nines.sys.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;

/**
 * @author TYJ
 * @date 2020/10/21 15:05
 */
@Slf4j
@Component
public class UPRealm extends AuthorizingRealm {

    @Resource
    private ISysUserService userService;

    /**
     * 账号密码登录
     * @param token 密钥
     * @return boolean
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("————账号密码 认证方法————");
        UsernamePasswordToken usernamepasswordToken = (UsernamePasswordToken) authenticationToken;
        String username = usernamepasswordToken.getUsername();
        if (username == null){
            throw new AuthenticationException("用户名为空");
        }
        SysUser user = userService.getUserByUsername(username);
        if (user == null){
            throw new AuthenticationException("用户不存在");
        }
        if (user.getStatus() == 1){
            throw new AuthenticationException("账号已冻结");
        }
        return new SimpleAuthenticationInfo(user, user.getPassWord(), ByteSource.Util.bytes(user.getSalt()), "upRealm");
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
