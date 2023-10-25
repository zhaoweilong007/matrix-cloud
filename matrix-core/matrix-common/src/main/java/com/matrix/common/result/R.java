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

package com.matrix.common.result;

import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.dreamlu.mica.core.result.IResultCode;
import net.dreamlu.mica.core.result.SystemCode;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * 响应信息主体
 *
 * @param <T> 泛型标记
 * @author L.cm
 */
@Getter
@Setter
@ToString
@Schema(description = "返回信息")
@NoArgsConstructor
public class R<T> implements Serializable {
    private static final long serialVersionUID = -1160662278280275915L;

    @Schema(description = "code值", requiredMode = Schema.RequiredMode.REQUIRED)
    private int code;

    @Schema(description = "是否成功", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean success;

    @Schema(description = "消息", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

    @Schema(description = "返回对象")
    private T data;

    private R(IResultCode resultCode) {
        this(resultCode, resultCode.getMsg(), null);
    }

    private R(IResultCode resultCode, String message) {
        this(resultCode, message, null);
    }

    private R(IResultCode resultCode, T data) {
        this(resultCode, resultCode.getMsg(), data);
    }

    private R(IResultCode resultCode, String message, T data) {
        this.code = resultCode.getCode();
        this.message = message;
        this.data = data;
        this.success = SystemErrorTypeEnum.SUCCESS == resultCode;
    }

    /**
     * 判断返回是否为成功
     *
     * @param result Result
     * @return 是否成功
     */
    public static boolean isSuccess(@Nullable R<?> result) {
        return Optional.ofNullable(result)
                .map(r -> r.code)
                .map(code -> SystemErrorTypeEnum.SUCCESS.getCode() == code)
                .orElse(Boolean.FALSE);
    }

    /**
     * 判断返回是否为成功
     *
     * @param result Result
     * @return 是否成功
     */
    public static boolean isFail(@Nullable R<?> result) {
        return !R.isSuccess(result);
    }

    /**
     * 获取data
     *
     * @param result Result
     * @param <T>    泛型标记
     * @return 泛型对象
     */
    @Nullable
    public static <T> T getData(@Nullable R<T> result) {
        return Optional.ofNullable(result)
                .filter(r -> r.success)
                .map(x -> x.data)
                .orElse(null);
    }

    /**
     * 返回成功
     *
     * @param <T> 泛型标记
     * @return Result
     */
    public static <T> R<T> success() {
        return new R<>(SystemErrorTypeEnum.SUCCESS);
    }

    /**
     * 成功-携带数据
     *
     * @param data 数据
     * @param <T>  泛型标记
     * @return Result
     */
    public static <T> R<T> success(@Nullable T data) {
        return new R<>(SystemErrorTypeEnum.SUCCESS, data);
    }

    /**
     * 根据状态返回成功或者失败
     *
     * @param status  状态
     * @param message 异常message
     * @param <T>     泛型标记
     * @return Result
     */
    public static <T> R<T> status(boolean status, String message) {
        return status ? R.success() : R.fail(message);
    }

    /**
     * 根据状态返回成功或者失败
     *
     * @param status 状态
     * @param sCode  异常code码
     * @param <T>    泛型标记
     * @return Result
     */
    public static <T> R<T> status(boolean status, IResultCode sCode) {
        return status ? R.success() : R.fail(sCode);
    }


    /**
     * 根据状态返回成功或者失败
     *
     * @param status 状态
     * @param data   数据
     * @param sCode  异常code码
     * @param <T>    泛型标记
     * @return Result
     */
    public static <T> R<T> status(boolean status, T data, IResultCode sCode) {
        return status ? R.success(data) : R.fail(sCode);
    }

    /**
     * 根据状态返回成功或者失败
     *
     * @param status 状态
     * @param <T>    泛型标记
     * @return Result
     */
    public static <T> R<T> status(boolean status) {
        return status ? R.success() : R.fail(SystemErrorTypeEnum.OPERATE_FAIL);
    }

    /**
     * 返回失败信息，用于 web
     *
     * @param message 失败信息
     * @param <T>     泛型标记
     * @return {Result}
     */
    public static <T> R<T> fail(String message) {
        return new R<>(SystemCode.FAILURE, message);
    }

    /**
     * 返回失败信息
     *
     * @param rCode 异常枚举
     * @param <T>   泛型标记
     * @return {Result}
     */
    public static <T> R<T> fail(IResultCode rCode) {
        return new R<>(rCode);
    }

    /**
     * 返回失败信息
     *
     * @param rCode   异常枚举
     * @param message 失败信息
     * @param <T>     泛型标记
     * @return {Result}
     */
    public static <T> R<T> fail(IResultCode rCode, String message) {
        return new R<>(rCode, message);
    }

    /**
     * 断言 成功时：直接抛出失败异常，返回传入的 result。
     *
     * @param predicate 断言函数
     * @param object    模型
     * @param result    响应对象
     * @param <T>       泛型
     */
    public static <T> void throwOn(Predicate<T> predicate, T object, R<?> result) {
        if (predicate.test(object)) {
            throw new ServiceException(result);
        }
    }

    /**
     * 断言 成功时：直接抛出失败异常，返回传入的 result。
     *
     * @param predicate 断言函数
     * @param object    模型
     * @param rCode     响应 code
     * @param <T>       泛型
     */
    public static <T> void throwOn(Predicate<T> predicate, T object, IResultCode rCode) {
        if (predicate.test(object)) {
            throw new ServiceException(rCode);
        }
    }

    /**
     * 断言 成功时：直接抛出失败异常，返回传入的 result。
     *
     * @param predicate 断言函数
     * @param object    模型
     * @param rCode     响应 code
     * @param message   自定义消息
     * @param <T>       泛型
     */
    public static <T> void throwOn(Predicate<T> predicate, T object, IResultCode rCode, String message) {
        if (predicate.test(object)) {
            throw new ServiceException(rCode, message);
        }
    }

    /**
     * 当 result 不成功时：直接抛出失败异常，返回传入的 result。
     *
     * @param result R
     */
    public static void throwOnFail(R<?> result) {
        if (R.isFail(result)) {
            throw new ServiceException(result);
        }
    }

    /**
     * 当 result 不成功时：直接抛出失败异常，返回传入的 rCode
     *
     * @param result R
     * @param rCode  异常枚举
     */
    public static void throwOnFail(R<?> result, IResultCode rCode) {
        if (R.isFail(result)) {
            throw new ServiceException(rCode);
        }
    }

    /**
     * 当 result 不成功时：直接抛出失败异常，返回传入的 rCode、message
     *
     * @param result  R
     * @param rCode   异常枚举
     * @param message 失败信息
     */
    public static void throwOnFail(R<?> result, IResultCode rCode, String message) {
        if (R.isFail(result)) {
            throw new ServiceException(rCode, message);
        }
    }

    /**
     * 当 status 不为 true 时：直接抛出失败异常 rCode
     *
     * @param status status
     * @param rCode  异常枚举
     */
    public static void throwOnFalse(boolean status, IResultCode rCode) {
        if (!status) {
            throw new ServiceException(rCode);
        }
    }

    /**
     * 当 status 不为 true 时：直接抛出失败异常 rCode、message
     *
     * @param status  status
     * @param rCode   异常枚举
     * @param message 失败信息
     */
    public static void throwOnFalse(boolean status, IResultCode rCode, String message) {
        if (!status) {
            throw new ServiceException(rCode, message);
        }
    }

    /**
     * 直接抛出失败异常，抛出 code 码
     *
     * @param rCode IResultCode
     */
    public static void throwFail(IResultCode rCode) {
        throw new ServiceException(rCode);
    }

    /**
     * 直接抛出失败异常，抛出 code 码
     *
     * @param rCode   IResultCode
     * @param message 自定义消息
     */
    public static void throwFail(IResultCode rCode, String message) {
        throw new ServiceException(rCode, message);
    }

    /**
     * 直接抛出失败异常，抛出 code 码
     *
     * @param message 自定义消息
     */
    public static void throwFail(String message) {
        throwFail(SystemCode.FAILURE, message);
    }

}