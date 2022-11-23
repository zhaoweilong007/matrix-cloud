package com.matrix.contreoller;

import com.matrix.api.resource.EmailAPI;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/11/23 16:56
 **/
@RestController
@RequestMapping(EmailAPI.PREFIX)
public class EmailController implements EmailAPI {
}
