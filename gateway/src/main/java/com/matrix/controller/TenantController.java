package com.matrix.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.convert.ConvertMapper;
import com.matrix.entity.dto.TenantDto;
import com.matrix.entity.po.Tenant;
import com.matrix.entity.vo.Result;
import com.matrix.service.TenantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * 租户管理
 *
 * @author xxxxx
 */
@RestController
@RequestMapping("/tenant")
public class TenantController {
    /**
     * 服务对象
     */
    @Resource
    private TenantService tenantService;


    @PostMapping
    public Mono<Result<Boolean>> add(TenantDto tenantDto) {
        Tenant tenant = ConvertMapper.INSTALL.convert(tenantDto);
        return Mono.create(sink -> sink.success(Result.success(tenantService.save(tenant))));
    }


    @GetMapping("list")
    public Mono<Result<Page<Tenant>>> list(Page<Tenant> page) {
        return Mono.create(sink -> sink.success(Result.success(tenantService.page(page))));
    }


    @PostMapping("/assignUser")
    public Mono<Result<Boolean>> assignUser(List<Long> userIds, Long tenantId) {
        return Mono.just(Result.success(tenantService.assignUser(userIds, tenantId)));

    }

}
