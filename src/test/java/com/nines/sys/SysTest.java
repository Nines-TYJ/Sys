package com.nines.sys;

import com.nines.sys.entity.SysUser;
import com.nines.sys.service.ISysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author TYJ
 * @date 2020/10/21 15:26
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SysApplication.class)
public class SysTest {

    @Autowired
    private ISysUserService userService;

    @Test
    public void getPasswordTest(){
        SysUser user = userService.getUserByUsername("root");
        System.out.println(user.toString());
    }

}
