package com.matrix.websocket.sse;

import com.matrix.common.context.LoginUserContextHolder;
import com.matrix.common.result.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * SSE（Server-Sent Events）控制器。
 *
 * <p>提供 SSE 长连接建立和关闭端点。需在配置中启用：
 * {@code matrix.websocket.sse.enabled=true}</p>
 *
 * @author matrix
 */
@RestController
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "matrix.websocket.sse", name = "enabled", havingValue = "true")
public class SseEmitterController {

    private final SseEmitterSessionManager sessionManager;

    /**
     * 建立 SSE 连接。
     *
     * @param response HTTP 响应
     * @return SseEmitter 实例
     */
    @GetMapping(value = "${matrix.websocket.sse.path:/sse/subscribe}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(HttpServletResponse response) {
        response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("X-Accel-Buffering", "no");

        Long userId = LoginUserContextHolder.getUser().getUserId();
        String tokenValue = getTokenValue();
        return sessionManager.connect(userId, tokenValue);
    }

    /**
     * 关闭 SSE 连接。
     *
     * @return 操作结果
     */
    @GetMapping("${matrix.websocket.sse.path:/sse}/close")
    public R<Void> close() {
        Long userId = LoginUserContextHolder.getUser().getUserId();
        String tokenValue = getTokenValue();
        sessionManager.disconnect(userId, tokenValue);
        return R.success();
    }

    /** 尝试获取当前 Token（反射调用 Sa-Token，避免硬依赖） */
    private String getTokenValue() {
        try {
            Class<?> stpUtilClass = Class.forName("cn.dev33.satoken.stp.StpUtil");
            return (String) stpUtilClass.getMethod("getTokenValue").invoke(null);
        } catch (Exception e) {
            return "unknown";
        }
    }
}
