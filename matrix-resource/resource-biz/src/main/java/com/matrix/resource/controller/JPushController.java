package com.matrix.resource.controller;

import com.matrix.api.resource.client.JPushApi;
import com.matrix.common.push.enums.AppEnum;
import com.matrix.common.result.R;
import com.matrix.jpush.JPushTemplate;
import com.matrix.jpush.factory.JPushFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = JPushApi.PREFIX)
@Tag(name = "极光推送服务", description = "极光推送服务")
@Slf4j
public class JPushController implements JPushApi {

    @Override
    public R<String> getPhoneNumberByToken(AppEnum appEnum, String token) {
        final JPushTemplate template = JPushFactory.getTemplate(appEnum);
        Assert.notNull(template, "app类型不支持：" + appEnum);
        return R.success(template.getPhoneNumberByToken(token));
    }

}
