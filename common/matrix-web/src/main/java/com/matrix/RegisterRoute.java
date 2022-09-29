package com.matrix;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * 描述： 注册子服务路由
 *
 * @author zwl
 * @since 2022/9/27 18:11
 **/
@RequiredArgsConstructor
@Slf4j
public class RegisterRoute {

    private final ConfigService configService;

    private final NacosConfigProperties nacosConfigProperties;

    public static String ROUTE_DATA_ID = "gateway-route";


    @Value("${spring.application.name}")
    private String appName;

    @PostConstruct
    public void init() throws NacosException {
        String config = configService.getConfig(ROUTE_DATA_ID, nacosConfigProperties.getGroup(), 3000);
        JSONArray routes = JSON.parseArray(config);
        //是否已经注册
        boolean anyMatch = routes.stream().anyMatch(o -> {
            JSONObject object = (JSONObject) o;
            String id = object.getString("id");
            return Objects.equals(id, appName);
        });

        if (anyMatch) {
            return;
        }

        JSONObject route = new JSONObject();
        route.put("id", appName);
        route.put("uri", "grayLb://" + appName);
        route.put("order", -1);
        route.put("predicates", new JSONArray() {{
            JSONObject predicate = new JSONObject();
            predicate.put("name", "Path");
            predicate.put("args", new JSONObject() {{
                put("pattern", "/" + appName + "/**");
            }});
            add(predicate);
        }});
        routes.add(route);
        String json = routes.toJSONString();
        log.debug("register grayLb route:{}", json);
        configService.publishConfig(ROUTE_DATA_ID, nacosConfigProperties.getGroup(), json, "json");
    }


}
