package com.matrix.client;

import com.matrix.api.system.ResourceAPI;
import com.matrix.api.system.RoleAPI;
import com.matrix.api.system.entity.po.SysResource;
import com.matrix.api.system.entity.po.SysRole;
import com.matrix.entity.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Future;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/8/3 9:14
 **/
@Component
@Slf4j
public class SystemClient {

    @Autowired
    @Lazy
    private RoleAPI roleAPI;
    @Autowired
    @Lazy
    private ResourceAPI resourceAPI;


    @Async
    public Future<Result<List<SysResource>>> getResourceByAdminId(Long userId) {
        return new AsyncResult<>(resourceAPI.getResourceByAdminId(userId));
    }

    public Future<Result<List<SysRole>>> getRoleByAdminId(Long userId) {
        return new AsyncResult<>(roleAPI.getRoleByAdminId(userId));
    }
}
