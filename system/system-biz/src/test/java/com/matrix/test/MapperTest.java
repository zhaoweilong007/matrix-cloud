package com.matrix.test;

import com.matrix.api.system.entity.po.SysResource;
import com.matrix.service.SysResourceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/8/4 15:38
 **/
@SpringBootTest
public class MapperTest {

    @Autowired
    SysResourceService sysResourceService;

    @Test
    @DisplayName("查询数据")
    public void query() {
        List<SysResource> resourceByAdminId = sysResourceService.getResourceByAdminId(1L);
        System.out.println(resourceByAdminId);
    }
}
