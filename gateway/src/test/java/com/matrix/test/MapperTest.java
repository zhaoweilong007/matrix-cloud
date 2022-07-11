package com.matrix.test;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.matrix.entity.po.SysAdmin;
import com.matrix.service.SysAdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/11 17:35
 **/
@SpringBootTest
public class MapperTest {


    @Autowired
    SysAdminService sysAdminService;


    @Test
    public void sysadminTest() {
        SysAdmin sysAdmin = new SysAdmin();
        sysAdmin.setUsername("zhangsan");
        sysAdmin.setPassword(SaSecureUtil.md5BySalt("123456", "123456"));
        sysAdmin.setIcon("qweq");
        sysAdmin.setEmail("ewqe");
        sysAdmin.setNickName("张三");
        sysAdmin.setNote("ewqeq");
        sysAdmin.setLoginTime(new Date());
        sysAdmin.setStatus(0);
        sysAdmin.insert();

    }


}
