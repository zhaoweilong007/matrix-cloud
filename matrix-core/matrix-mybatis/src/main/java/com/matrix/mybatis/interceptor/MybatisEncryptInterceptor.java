package com.matrix.mybatis.interceptor;

import cn.hutool.core.util.ReflectUtil;
import com.matrix.crypto.annotation.EncryptField;
import com.matrix.crypto.service.CryptoService;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
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
 * MyBatis 写入加密拦截器。
 *
 * <p>在 INSERT/UPDATE 执行前，自动对标注了 {@code @EncryptField} 的实体字段进行加密。</p>
 *
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class MybatisEncryptInterceptor implements Interceptor, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(MybatisEncryptInterceptor.class);

    private final Map<Class<?>, List<Field>> encryptFieldCache = new ConcurrentHashMap<>();
    private static volatile CryptoService cryptoService;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object parameter = invocation.getArgs()[1];
        boolean hasEncrypted = false;
        if (parameter != null && cryptoService != null) {
            MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
            SqlCommandType commandType = ms.getSqlCommandType();
            if (commandType == SqlCommandType.INSERT || commandType == SqlCommandType.UPDATE) {
                encryptFields(parameter);
                hasEncrypted = true;
            }
        }
        try {
            return invocation.proceed();
        } finally {
            if (hasEncrypted) {
                // SQL执行完成后，还原Java实体属性为明文状态，避免污染逻辑层对象
                decryptFields(parameter);
            }
        }
    }

    private void encryptFields(Object entity) {
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
                    field.set(entity, cryptoService.encrypt(value));
                }
            } catch (Exception e) {
                log.debug("Failed to encrypt field: {}.{}", entity.getClass().getSimpleName(), field.getName(), e);
            }
        }
    }

    private void decryptFields(Object entity) {
        if (entity == null) {
            return;
        }
        List<Field> fields = encryptFieldCache.get(entity.getClass());
        if (fields == null) {
            return;
        }
        for (Field field : fields) {
            try {
                String value = (String) field.get(entity);
                if (value != null && !value.isEmpty()) {
                    field.set(entity, cryptoService.decrypt(value));
                }
            } catch (Exception e) {
                log.debug("Failed to decrypt field on rollback: {}.{}", entity.getClass().getSimpleName(), field.getName(), e);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        try {
            cryptoService = applicationContext.getBean(CryptoService.class);
            log.info("MybatisEncryptInterceptor initialized with CryptoService");
        } catch (BeansException e) {
            log.debug("CryptoService not available, MybatisEncryptInterceptor disabled");
        }
    }
}
