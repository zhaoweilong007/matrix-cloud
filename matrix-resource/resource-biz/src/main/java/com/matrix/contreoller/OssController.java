package com.matrix.contreoller;

import com.matrix.api.resource.OssAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：oss控制器
 *
 * @author zwl
 * @since 2022/11/23 16:43
 **/
@RestController
@Slf4j
@RequestMapping(OssAPI.PREFIX)
public class OssController implements OssAPI {

}
