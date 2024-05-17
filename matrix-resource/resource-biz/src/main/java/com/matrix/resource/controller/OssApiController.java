package com.matrix.resource.controller;

import com.matrix.api.resource.client.OssApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = OssApi.PREFIX)
@Tag(name = "对象存储服务", description = "对象存储服务")
public class OssApiController implements OssApi {


}
