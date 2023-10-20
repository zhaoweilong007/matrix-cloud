/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.matrix.auto.properties;

import com.matrix.common.constant.ConfigConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Swagger 配置
 */
@Data
@ConfigurationProperties(ConfigConstants.SWAGGER)
public class SwaggerProperties {

    /**
     * 是否开启 swagger，默认：true
     */
    private boolean enabled = true;
    /**
     * 标题，默认：XXX服务
     */
    private String title;
    /**
     * 详情，默认：XXX服务
     */
    private String description;
    /**
     * 版本号，默认：V1.0
     */
    private String version = "V1.0";
    /**
     * 组织名
     */
    private String contactUser;
    /**
     * 组织url
     */
    private String contactUrl;
    /**
     * 组织邮箱
     */
    private String contactEmail;

}
