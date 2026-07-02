package com.matrix.crypto.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记实体类字段需要自动加解密。
 *
 * <p>配合 {@code MybatisEncryptInterceptor} 和 {@code MybatisDecryptInterceptor} 使用：
 * 写入数据库时自动加密，读取时自动解密。</p>
 *
 * <p>示例：</p>
 * <pre>
 * public class User extends BaseEntity {
 *     {@literal @}EncryptField
 *     private String phone;  // 入库加密，出库解密
 * }
 * </pre>
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EncryptField {
}
