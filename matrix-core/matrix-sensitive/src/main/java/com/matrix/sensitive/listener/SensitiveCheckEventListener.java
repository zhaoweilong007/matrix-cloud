package com.matrix.sensitive.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.matrix.auto.properties.OssProperties;
import com.matrix.sensitive.api.client.SensitiveCheckRecordApi;
import com.matrix.sensitive.api.client.SensitiveRelateApi;
import com.matrix.sensitive.entity.SensitiveCheckRecord;
import com.matrix.sensitive.entity.SensitiveRelate;
import com.matrix.sensitive.events.SensitiveCheckEvent;
import com.matrix.sensitive.model.SensitiveCheckDto;
import com.matrix.sensitive.utils.SensitiveUtils;
import com.netease.yidun.sdk.antispam.image.v5.check.ImageV5CheckRequest;
import com.netease.yidun.sdk.antispam.image.v5.check.async.response.ImageV5AsyncCheckResp;
import com.netease.yidun.sdk.antispam.text.v5.check.async.single.TextAsyncCheckResult;
import com.netease.yidun.sdk.antispam.text.v5.check.sync.single.TextCheckRequest;
import com.netease.yidun.sdk.antispam.text.v5.check.sync.single.TextCheckSceneRequest;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 检测监听器
 *
 * @author ZhaoWeiLong
 * @since 2023/4/21
 **/
@Slf4j
public class SensitiveCheckEventListener implements ApplicationListener<SensitiveCheckEvent> {
    @Autowired
    private SensitiveUtils sensitiveUtils;
    @Autowired
    private SensitiveCheckRecordApi sensitiveCheckRecordDao;
    @Autowired
    private SensitiveRelateApi sensitiveRelateDao;
    @Autowired
    private OssProperties ossProperties;

    @Override
    @Async
    public void onApplicationEvent(SensitiveCheckEvent sensitiveCheckEvent) {
        if (sensitiveCheckEvent.getSource() == null) {
            return;
        }
        final SensitiveCheckDto checkDto = (SensitiveCheckDto) sensitiveCheckEvent.getSource();
        if (checkDto.getTexts().isEmpty() && checkDto.getImgs().isEmpty()) {
            return;
        }
        final ArrayList<SensitiveCheckRecord> records = Lists.newArrayList();
        final List<TextCheckSceneRequest> textCheckRequests = handlerText(records, checkDto);
        final List<ImageV5CheckRequest.ImageBeanRequest> imageBeanRequests = handlerImg(records, checkDto);
        if (records.isEmpty()) {
            return;
        }
        sensitiveCheckRecordDao.saveAll(records);
        final SensitiveRelate sensitiveRelate = new SensitiveRelate();
        sensitiveRelate.setGlobalId(checkDto.getGlobalId());
        sensitiveRelate.setCreateTime(LocalDateTime.now());
        sensitiveRelate.setRelateId(checkDto.getRelateId());
        sensitiveRelate.setTotalRecord(records.size());
        sensitiveRelateDao.save(sensitiveRelate);

        final Map<Long, List<SensitiveCheckRecord>> grouping = StreamEx.of(records).groupingBy(SensitiveCheckRecord::getDataId);

        if (!textCheckRequests.isEmpty()) {
            final Tuple2<Boolean, List<TextAsyncCheckResult.CheckText>> textRes = sensitiveUtils.asyncBatchCheckText_v2(textCheckRequests);
            if (!textRes._1()) {
                final List<SensitiveCheckRecord> recordList = textRes._2().stream()
                        .flatMap(checkText -> grouping.get(Long.valueOf(checkText.getDataId())).stream()).collect(Collectors.toList());
                sensitiveCheckRecordDao.deleteAll(recordList);
            }
        }
        if (!imageBeanRequests.isEmpty()) {
            final Tuple2<Boolean, List<ImageV5AsyncCheckResp.ImageRespDetail>> imsRes = sensitiveUtils.asyncBatchCheckImg_v2(imageBeanRequests);
            if (!imsRes._1()) {
                final List<SensitiveCheckRecord> recordList = imsRes._2().stream()
                        .flatMap(checkIms -> grouping.get(Long.valueOf(checkIms.getDataId())).stream()).collect(Collectors.toList());
                sensitiveCheckRecordDao.deleteAll(recordList);
            }
        }
    }

    private List<ImageV5CheckRequest.ImageBeanRequest> handlerImg(ArrayList<SensitiveCheckRecord> records, SensitiveCheckDto checkDto) {
        if (CollUtil.isEmpty(checkDto.getImgs())) {
            return Collections.emptyList();
        }
        final Map<String, List<String>> map = checkDto.getImgs();
        final HashMap<String, String> hashMap = Maps.newHashMap();
        final HashMap<String, String> urlMap = Maps.newHashMap();
        final List<ImageV5CheckRequest.ImageBeanRequest> imageBeanRequests = map.entrySet().parallelStream().flatMap(entry -> {
            final String fieldName = entry.getKey();
            final List<String> urls = entry.getValue();
            return urls.stream().map(url -> {
                if (!StrUtil.startWith(url, ossProperties.getPrefix())) {
                    url = ossProperties.getPrefix().concat(url);
                }
                return url;
            }).map(url -> {
                ImageV5CheckRequest.ImageBeanRequest image = new ImageV5CheckRequest.ImageBeanRequest();
                final String nextId = String.valueOf(IdUtil.getSnowflakeNextId());
                hashMap.put(nextId, fieldName);
                urlMap.put(nextId, url);
                image.setDataId(nextId);
                image.setType(1);
                image.setData(url);
                image.setName(StrUtil.subAfter(url, "/", true));
                return image;
            }).collect(Collectors.toList()).stream();
        }).collect(Collectors.toList());

        final List<SensitiveCheckRecord> checkRecords = imageBeanRequests.stream().map(imageBeanRequest -> {
            final String dataId = imageBeanRequest.getDataId();
            return new SensitiveCheckRecord().setType(2).setGlobalId(checkDto.getGlobalId()).setSuggestion(-1).setDataId(Long.parseLong(dataId))
                    .setRelateId(checkDto.getRelateId()).setClassName(checkDto.getClassName()).setFieldName(hashMap.get(dataId)).setRemark(urlMap.get(dataId))
                    .setCreateTime(LocalDateTime.now());
        }).collect(Collectors.toList());

        records.addAll(checkRecords);
        return imageBeanRequests;
    }


    private List<TextCheckSceneRequest> handlerText(List<SensitiveCheckRecord> records, SensitiveCheckDto checkDto) {
        if (checkDto.getTexts().isEmpty()) {
            return Collections.emptyList();
        }
        final Map<String, String> textMap = checkDto.getTexts();
        final HashMap<String, String> fieldMap = Maps.newHashMap();
        List<TextCheckSceneRequest> textCheckRequests = textMap.entrySet().stream().map(entry -> {
            final TextCheckRequest checkRequest = new TextCheckRequest();
            final String dataId = String.valueOf(IdUtil.getSnowflakeNextId());
            checkRequest.setContent(entry.getValue());
            checkRequest.setDataId(dataId);
            fieldMap.put(dataId, entry.getKey());
            return checkRequest;
        }).collect(Collectors.toList());

        final List<SensitiveCheckRecord> list = textCheckRequests.stream().map(textCheckSceneRequest -> {
            final String dataId = textCheckSceneRequest.getDataId();
            return new SensitiveCheckRecord().setType(1).setGlobalId(checkDto.getGlobalId()).setSuggestion(-1).setDataId(Long.parseLong(dataId))
                    .setRelateId(checkDto.getRelateId()).setClassName(checkDto.getClassName()).setFieldName(fieldMap.get(dataId))
                    .setCreateTime(LocalDateTime.now());
        }).collect(Collectors.toList());
        records.addAll(list);

        return textCheckRequests;
    }
}
