package com.matrix.sensitive.api.client;

import com.matrix.common.constant.ServerNameConstants;
import com.matrix.sensitive.entity.SensitiveCheckRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author ZhaoWeiLong
 * @since 2023/8/23
 **/
@FeignClient(contextId = "SensitiveCheckRecordApi", value = ServerNameConstants.RESOURCE, path = "/sensitive/record")
public interface SensitiveCheckRecordApi {

    @PostMapping("/saveAll")
    void saveAll(@RequestBody List<SensitiveCheckRecord> records);

    @PostMapping("/deleteAll")
    void deleteAll(@RequestBody List<SensitiveCheckRecord> recordList);
}
