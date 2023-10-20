package com.matrix.translation.api.client;

import com.matrix.common.constant.ServerNameConstants;
import com.matrix.common.result.R;
import com.matrix.common.vo.UserInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务
 */
@FeignClient(contextId = "IUserNameService", value = ServerNameConstants.SYSTEM)
public interface IUserNameService {

    /**
     * 通过userId查询用户账户
     *
     * @param userId 用户id
     * @return 结果
     */
    @GetMapping("/sys/user/selectUserNameById")
    R<String> selectUserNameById(@RequestParam("userId") Long userId);


    /**
     * 选择用户信息通过id
     *
     * @param userId 用户id
     * @return {@link R}<{@link String}>
     */
    @GetMapping("/sys/user/selectUserInfoById")
    R<UserInfoVo> selectUserInfoById(@RequestParam("userId") Long userId);
}
