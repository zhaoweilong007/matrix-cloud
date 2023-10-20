package com.matrix.sensitive.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.matrix.auto.properties.YiDunProperties;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.sensitive.events.SensitiveCheckEvent;
import com.matrix.sensitive.model.SensitiveCheckDto;
import com.matrix.sensitive.service.SensitiveService;
import com.netease.yidun.sdk.antispam.AntispamRequester;
import com.netease.yidun.sdk.antispam.image.v5.ImageCheckClient;
import com.netease.yidun.sdk.antispam.image.v5.check.ImageV5CheckRequest.ImageBeanRequest;
import com.netease.yidun.sdk.antispam.image.v5.check.async.request.ImageV5AsyncCheckRequest;
import com.netease.yidun.sdk.antispam.image.v5.check.async.response.ImageV5AsyncCheckResp;
import com.netease.yidun.sdk.antispam.image.v5.check.async.response.ImageV5AsyncCheckResp.ImageRespDetail;
import com.netease.yidun.sdk.antispam.text.TextCheckClient;
import com.netease.yidun.sdk.antispam.text.v5.check.async.batch.TextAsyncBatchCheckRequest;
import com.netease.yidun.sdk.antispam.text.v5.check.async.batch.TextAsyncBatchCheckResponse;
import com.netease.yidun.sdk.antispam.text.v5.check.async.single.TextAsyncCheckResult.CheckText;
import com.netease.yidun.sdk.antispam.text.v5.check.sync.single.TextCheckRequest;
import com.netease.yidun.sdk.antispam.text.v5.check.sync.single.TextCheckResponse;
import com.netease.yidun.sdk.antispam.text.v5.check.sync.single.TextCheckResult;
import com.netease.yidun.sdk.antispam.text.v5.check.sync.single.TextCheckSceneRequest;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 说明：敏感词utils
 *
 * @author jinx
 * @since 2021/1/17 15:14
 */
@Slf4j
public class SensitiveUtils implements SensitiveService {
    private final AntispamRequester antispamRequester;
    private final YiDunProperties yiDunProperties;

    public SensitiveUtils(YiDunProperties yiDunProperties) {
        this.yiDunProperties = yiDunProperties;
        this.antispamRequester = new AntispamRequester(yiDunProperties.getSecretId(), yiDunProperties.getSecretKey());
    }

    public static void publish(Long relateId, Object object) {
        SpringUtil.publishEvent(new SensitiveCheckEvent(SensitiveCheckDto.build(relateId, object)));
    }

    @Override
    public Tuple2<Boolean, Set<String>> syncCheckText(String content) {
        final TextCheckClient textCheckClient = antispamRequester.getTextCheckClient();
        TextCheckRequest checkRequest = new TextCheckRequest();
        checkRequest.setBusinessId(yiDunProperties.getBusinessId());
        checkRequest.setDataId(yiDunProperties.getDataIdPrefix() + "_" + IdUtil.getSnowflakeNextId());
        checkRequest.setContent(content);
        try {
            final TextCheckResponse checkResponse = textCheckClient.syncCheckText(checkRequest);
            if (checkResponse != null && checkResponse.getCode() == 200) {
                TextCheckResult textResult = checkResponse.getResult();
                final Integer suggestion = textResult.getAntispam().getSuggestion();
                final boolean isValid = suggestion == 0;
                if (isValid) {
                    return Tuple.of(true, Collections.emptySet());
                }
                final Stream<String> stream = Optional.ofNullable(textResult.getAntispam().getLabels())
                        .map(antispamLabels -> antispamLabels.stream().flatMap(antispamLabel -> antispamLabel.getSubLabels().stream())
                                .flatMap(antispamSubLabel -> antispamLabels.stream().flatMap(antispamLabel -> antispamLabel.getSubLabels().stream()))
                                .flatMap(antispamSubLabelStream -> Optional.ofNullable(antispamSubLabelStream.getDetails())
                                        .map(TextCheckResult.AntispamSubLabelDetail::getHitInfos).stream().flatMap(Collection::stream)).
                                map(TextCheckResult.AntispamSubLabelDetailHitInfo::getValue)).orElseGet(() -> Stream.<String>builder().build());
                return Tuple.of(false, stream.collect(Collectors.toSet()));
            } else {
                throw new ServiceException(SystemErrorTypeEnum.SENSITIVE_INVOKE_ERROR);
            }
        } catch (Exception e) {
            log.error("check text error", e);
            throw new ServiceException(SystemErrorTypeEnum.SENSITIVE_INVOKE_ERROR);
        }
    }


    @Override
    public Tuple2<Boolean, List<CheckText>> asyncBatchCheckText(List<String> texts) {
        final TextCheckClient textCheckClient = antispamRequester.getTextCheckClient();
        final List<TextCheckSceneRequest> textCheckRequests = texts.stream().map(s -> {
            final TextCheckRequest checkRequest = new TextCheckRequest();
            checkRequest.setContent(s);
            checkRequest.setDataId(yiDunProperties.getDataIdPrefix() + "_" + IdUtil.getSnowflakeNextId());
            checkRequest.setCallbackUrl(yiDunProperties.getTextCallBack());
            return checkRequest;
        }).collect(Collectors.toList());
        TextAsyncBatchCheckRequest checkRequest = new TextAsyncBatchCheckRequest();
        checkRequest.setBusinessId(yiDunProperties.getBusinessId());
        checkRequest.setTexts(textCheckRequests);
        final TextAsyncBatchCheckResponse response = textCheckClient.asyncBatchCheckText(checkRequest);
        if (response != null && response.getCode() == 200) {
            if (response.getResult() != null && response.getResult().getCheckTexts() != null) {
                final List<CheckText> checkTexts = response.getResult().getCheckTexts();
                return Tuple.of(true, checkTexts);
            }
        } else {
            log.warn("response error:{}", response);
        }
        return Tuple.of(false, Collections.emptyList());
    }

    @Override
    public Tuple2<Boolean, List<CheckText>> asyncBatchCheckText_v2(List<TextCheckSceneRequest> texts) {
        final TextCheckClient textCheckClient = antispamRequester.getTextCheckClient();
        texts.forEach(textCheckSceneRequest -> {
            textCheckSceneRequest.setCallbackUrl(yiDunProperties.getTextCallBack());
        });
        TextAsyncBatchCheckRequest checkRequest = new TextAsyncBatchCheckRequest();
        checkRequest.setBusinessId(yiDunProperties.getBusinessId());
        checkRequest.setTexts(texts);
        final TextAsyncBatchCheckResponse response = textCheckClient.asyncBatchCheckText(checkRequest);
        if (response != null && response.getCode() == 200) {
            if (response.getResult() != null && response.getResult().getCheckTexts() != null) {
                final List<CheckText> checkTexts = response.getResult().getCheckTexts();
                return Tuple.of(true, checkTexts);
            }
        } else {
            log.warn("response error:{}", response);
        }
        return Tuple.of(false, Collections.emptyList());
    }

    @Override
    public Tuple2<Boolean, List<ImageRespDetail>> asyncBatchCheckImg_v2(List<ImageBeanRequest> imgs) {
        final ImageCheckClient imageCheckClient = antispamRequester.getImageCheckClient();
        List<List<ImageBeanRequest>> subLists = IntStream.range(0, imgs.size())
                .boxed()
                .collect(Collectors.groupingBy(index -> index / 32))
                .values()
                .stream()
                .map(indices -> indices.stream().map(imgs::get).collect(Collectors.toList()))
                .collect(Collectors.toList());
        final List<ImageRespDetail> collect = subLists.parallelStream().flatMap(reqs -> {
            imgs.forEach(imageBeanRequest -> imageBeanRequest.setCallbackUrl(yiDunProperties.getImgCallBack()));
            ImageV5AsyncCheckRequest checkRequest = new ImageV5AsyncCheckRequest();
            checkRequest.setBusinessId(yiDunProperties.getImgBusinessId());
            checkRequest.setImages(reqs);
            final ImageV5AsyncCheckResp response = imageCheckClient.asyncCheckImage(checkRequest);
            if (response != null && response.getCode() == 200) {
                if (response.getResult() != null && response.getResult().getCheckImages() != null) {
                    return response.getResult().getCheckImages().stream();
                }
            } else {
                log.warn("response error:{}", response);
            }
            return Stream.empty();
        }).collect(Collectors.toList());
        return Tuple.of(true, collect);

    }


    @Override
    public Tuple2<Boolean, List<ImageRespDetail>> asyncBatchCheckImg(List<String> imgs) {
        final ImageCheckClient imageCheckClient = antispamRequester.getImageCheckClient();
        final List<ImageBeanRequest> imageBeanRequests = imgs.stream().map(url -> {
            ImageBeanRequest image = new ImageBeanRequest();
            image.setDataId(yiDunProperties.getDataIdPrefix() + "_" + IdUtil.getSnowflakeNextId());
            image.setCallbackUrl(yiDunProperties.getImgCallBack());
            image.setType(1);
            image.setData(url);
            image.setName(StrUtil.subAfter(url, "/", true));
            return image;
        }).collect(Collectors.toList());
        List<List<String>> subLists = IntStream.range(0, imgs.size())
                .boxed()
                .collect(Collectors.groupingBy(index -> index / 5))
                .values()
                .stream()
                .map(indices -> indices.stream().map(imgs::get).collect(Collectors.toList()))
                .collect(Collectors.toList());
        final List<ImageRespDetail> collect = subLists.parallelStream().flatMap(urls -> {
            ImageV5AsyncCheckRequest checkRequest = new ImageV5AsyncCheckRequest();
            checkRequest.setBusinessId(yiDunProperties.getImgBusinessId());
            checkRequest.setImages(imageBeanRequests);
            final ImageV5AsyncCheckResp response = imageCheckClient.asyncCheckImage(checkRequest);
            if (response != null && response.getCode() == 200) {
                if (response.getResult() != null && response.getResult().getCheckImages() != null) {
                    return response.getResult().getCheckImages().stream();
                }
            } else {
                log.warn("response error:{}", response);
            }
            return Stream.empty();
        }).collect(Collectors.toList());

        return Tuple.of(true, collect);
    }


}
