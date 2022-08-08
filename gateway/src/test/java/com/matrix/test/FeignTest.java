package com.matrix.test;

import com.matrix.api.system.ResourceAPI;
import com.matrix.api.system.entity.po.SysResource;
import com.matrix.entity.vo.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/8/8 10:02
 **/
@SpringBootTest
public class FeignTest {

    @Autowired
    private ResourceAPI resourceAPI;

    @Test
    public void test() {
        Result<List<SysResource>> result = resourceAPI.getResourceByAdminId(1L);
        Assert.notNull(result);
    }

}
