package com.matrix.mybatis.interceptor;

import cn.hutool.core.util.ReflectUtil;
import com.matrix.crypto.annotation.EncryptField;
import com.matrix.crypto.service.CryptoService;
import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * MyBatis 读取解密拦截器。
 *
 * <p>在 SELECT 结果集处理后，自动对标注了 {@code @EncryptField} 的实体字段进行解密。</p>
 *
 * @author matrix
 */
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})
public class MybatisDecryptInterceptor implements Interceptor, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(MybatisDecryptInterceptor.class);

    private final Map<Class<?>, List<Field>> encryptFieldCache = new ConcurrentHashMap<>();
    private static volatile CryptoService cryptoService;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        if (result != null && cryptoService != null) {
            if (result instanceof List<?> list) {
                for (Object item : list) {
                    decryptFields(item);
                }
            } else {
                decryptFields(result);
            }
        }
        return result;
    }

    private void decryptFields(Object entity) {
        if (entity == null) {
            return;
        }
        List<Field> fields = encryptFieldCache.computeIfAbsent(entity.getClass(), clazz -> {
            List<Field> result = new java.util.ArrayList<>();
            for (Field field : ReflectUtil.getFields(clazz)) {
                if (field.isAnnotationPresent(EncryptField.class) && field.getType() == String.class) {
                    field.setAccessible(true);
                    result.add(field);
                }
            }
            return result;
        });
        for (Field field : fields) {
            try {
                String value = (String) field.get(entity);
                if (value != null && !value.isEmpty()) {
                    field.set(entity, cryptoService.decrypt(value));
                }
            } catch (Exception e) {
                log.debug("Failed to decrypt field: {}.{}", entity.getClass().getSimpleName(), field.getName(), e);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        try {
            cryptoService = applicationContext.getBean(CryptoService.class);
            log.info("MybatisDecryptInterceptor initialized with CryptoService");
        } catch (BeansException e) {
            log.debug("CryptoService not available, MybatisDecryptInterceptor disabled");
        }
    }
}
