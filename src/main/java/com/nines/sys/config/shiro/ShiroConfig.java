package com.nines.sys.config.shiro;

import com.nines.sys.config.shiro.cache.MyCacheManager;
import com.nines.sys.config.shiro.jwt.JWTFilter;
import com.nines.sys.config.shiro.realm.JWTRealm;
import com.nines.sys.config.shiro.realm.UPRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

/**
 * @author TYJ
 * @date 2020/10/21 11:46
 */
@Configuration
public class ShiroConfig {

    // 设置加密算法
    @Bean
    public HashedCredentialsMatcher credentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("md5");
        credentialsMatcher.setHashIterations(2);
        // true 密码加密用hex编码; false 用base64编码
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }

    /**
     * 先走 filter ，然后 filter 如果检测到请求头存在 token，则用 token 去 login，走 Realm 去验证
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter() {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager());
        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<>();
        //设置我们自定义的JWT过滤器
        filterMap.put("jwt", new JWTFilter());
        factoryBean.setFilters(filterMap);
        // 设置无权限时跳转的 url;
//        factoryBean.setUnauthorizedUrl("/unauthorized/无权限");
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 所有请求通过我们自己的JWT Filter
        filterChainDefinitionMap.put("/sys/admin/**", "anon");
        filterChainDefinitionMap.put("/sys/**", "jwt");
        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        // 打印 拦截路径
//        factoryBean.getFilterChainDefinitionMap().forEach((k,v) -> System.out.println(k + " -> " + v));
        return factoryBean;
    }

    /**
     * 注入 securityManager
     */
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(modularRealmAuthenticator());
        // 设置自定义 realms
        List<Realm> realmList = new ArrayList<>();
        realmList.add(upRealm());
        realmList.add(jwtRealm());
        securityManager.setRealms(realmList);
        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        // 设置自定义缓存（cache）管理器
        securityManager.setCacheManager(new MyCacheManager());
        return securityManager;
    }

    /**
     * 添加注解支持
     */
//    @Bean
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
//        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
//        // https://zhuanlan.zhihu.com/p/29161098
//        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
//        return defaultAdvisorAutoProxyCreator;
//    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        return advisor;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public JWTRealm jwtRealm(){
        return new JWTRealm();
    }

    @Bean
    public UPRealm upRealm(){
        UPRealm upRealm = new UPRealm();
        upRealm.setCredentialsMatcher(credentialsMatcher());
        return upRealm;
    }

    @Bean
    public ModularRealmAuthenticator modularRealmAuthenticator(){
        return new MyModularRealmAuthenticator();
    }
}
