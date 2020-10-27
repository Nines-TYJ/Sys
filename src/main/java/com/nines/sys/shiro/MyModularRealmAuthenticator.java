package com.nines.sys.shiro;

import com.nines.sys.shiro.realm.JWTRealm;
import com.nines.sys.shiro.realm.UPRealm;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * @author 刘志远
 * @version 1.00
 */
public class MyModularRealmAuthenticator extends ModularRealmAuthenticator {

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) {
        List<Realm> list = (List) getRealms();
        Map<Class, Realm> realmMap = list.stream().collect(toMap(Realm::getClass, valObj->valObj));
        if(authenticationToken instanceof UsernamePasswordToken){
            return doSingleRealmAuthentication(realmMap.get(UPRealm.class), authenticationToken);
        }else if(authenticationToken instanceof JWTToken){
            return doSingleRealmAuthentication(realmMap.get(JWTRealm.class), authenticationToken);
        }
        return null;
    }
}
