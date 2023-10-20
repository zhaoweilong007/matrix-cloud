package com.matrix.common.util;


import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.common.result.R;
import com.matrix.common.vo.PageResult;
import lombok.experimental.UtilityClass;
import net.dreamlu.mica.core.result.IResultCode;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 描述：<p></p>
 *
 * @author ZhaoWeiLong
 * @since 2023/3/13
 **/
@UtilityClass
public class VUtils {

    Predicate<R<?>> checkRes = result -> result != null && result.getCode() == SystemErrorTypeEnum.SUCCESS.getCode() && result.getData() != null;
    Predicate<R<PageResult<?>>> checkResPage = result -> result != null && result.getCode() == SystemErrorTypeEnum.SUCCESS.getCode() && result.getData() != null;


    public static <T> Boolean checkRes(R<T> result) {
        return checkRes.test(result);
    }

    public static <T> Boolean checkPageRes(R<PageResult<?>> result) {
        return checkResPage.test(result);
    }


    public static <T> void checkRes(R<T> result, IResultCode errorType) {
        if (checkRes.test(result)) {
            throw new ServiceException(errorType);
        }
    }

    public static <T> void checkAndThen(R<T> result, Consumer<T> consumer) {
        if (checkRes(result)) {
            consumer.accept(result.getData());
        }
    }

    public static <T, D> D checkAndThen(R<T> result, Function<T, D> function) {
        if (checkRes(result)) {
            return function.apply(result.getData());
        }
        return null;
    }
}
