package com.matrix.sensitive.service;

import com.netease.yidun.sdk.antispam.image.v5.check.ImageV5CheckRequest.ImageBeanRequest;
import com.netease.yidun.sdk.antispam.image.v5.check.async.response.ImageV5AsyncCheckResp.ImageRespDetail;
import com.netease.yidun.sdk.antispam.text.v5.check.async.single.TextAsyncCheckResult.CheckText;
import com.netease.yidun.sdk.antispam.text.v5.check.sync.single.TextCheckSceneRequest;
import io.vavr.Tuple2;

import java.util.List;
import java.util.Set;

/**
 * 易盾检测服务
 *
 * @author ZhaoWeiLong
 * @since 2023/4/18
 **/
public interface SensitiveService {

    /**
     * 异步批检查文本
     *
     * @param texts 文本集合
     * @return {@link Tuple2}<{@link Boolean}, {@link List}<{@link CheckText}>>
     */
    Tuple2<Boolean, List<CheckText>> asyncBatchCheckText(List<String> texts);

    /**
     * 同步检查文本
     *
     * @param content 内容
     * @return {@link Tuple2}<{@link Boolean}, {@link Set}<{@link String}>>
     */
    Tuple2<Boolean, Set<String>> syncCheckText(String content);


    /**
     * 异步批检查编码器
     *
     * @param imgs 图片集合
     * @return {@link Tuple2}<{@link Boolean}, {@link List}<{@link ImageRespDetail}>>
     */
    Tuple2<Boolean, List<ImageRespDetail>> asyncBatchCheckImg(List<String> imgs);

    /**
     * 异步批检查文本v2
     *
     * @param texts 文本集合
     * @return {@link Tuple2}<{@link Boolean}, {@link List}<{@link CheckText}>>
     */
    Tuple2<Boolean, List<CheckText>> asyncBatchCheckText_v2(List<TextCheckSceneRequest> texts);

    /**
     * 异步批检查图片 v2
     *
     * @param imgs 图片集合
     * @return {@link Tuple2}<{@link Boolean}, {@link List}<{@link ImageRespDetail}>>
     */
    Tuple2<Boolean, List<ImageRespDetail>> asyncBatchCheckImg_v2(List<ImageBeanRequest> imgs);
}
