package com.matrix.translation.impl;

import cn.hutool.core.util.ObjectUtil;
import com.matrix.common.constant.CacheConstants;
import com.matrix.common.model.SysDictDataVo;
import com.matrix.common.result.R;
import com.matrix.common.service.IDictService;
import com.matrix.common.util.StreamUtils;
import com.matrix.common.util.StringUtils;
import com.matrix.common.util.VUtils;
import com.matrix.redis.utils.RedisUtils;
import com.matrix.translation.api.client.IRemoteDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典服务服务
 */
@Service
public class DictServiceImpl implements IDictService {

    @Autowired
    private IRemoteDictService remoteDictService;

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @param separator 分隔符
     * @return 字典标签
     */
    @Override
    public String getDictLabel(String dictType, String dictValue, String separator) {
        // 优先从本地缓存获取
        List<SysDictDataVo> datas = RedisUtils.getCacheMapValue(CacheConstants.SYS_DICT_KEY, dictType);
        if (ObjectUtil.isNull(datas)) {
            final R<List<SysDictDataVo>> r = remoteDictService.selectDictDataByTypeForTranslate(dictType);
            if (VUtils.checkRes(r)) {
                datas = r.getData();
            }
        }

        Map<String, String> map = StreamUtils.toMap(datas, SysDictDataVo::getDictValue, SysDictDataVo::getDictLabel);
        if (StringUtils.containsAny(dictValue, separator)) {
            return Arrays.stream(dictValue.split(separator))
                    .map(v -> map.getOrDefault(v, StringUtils.EMPTY))
                    .collect(Collectors.joining(separator));
        } else {
            return map.getOrDefault(dictValue, StringUtils.EMPTY);
        }
    }

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @param separator 分隔符
     * @return 字典值
     */
    @Override
    public String getDictValue(String dictType, String dictLabel, String separator) {
        // 优先从本地缓存获取
        List<SysDictDataVo> datas = RedisUtils.getCacheMapValue(CacheConstants.SYS_DICT_KEY, dictLabel);
        if (ObjectUtil.isNull(datas)) {
            final R<List<SysDictDataVo>> r = remoteDictService.selectDictDataByTypeForTranslate(dictType);
            if (VUtils.checkRes(r)) {
                datas = r.getData();
            }
        }

        Map<String, String> map = StreamUtils.toMap(datas, SysDictDataVo::getDictLabel, SysDictDataVo::getDictValue);
        if (StringUtils.containsAny(dictLabel, separator)) {
            return Arrays.stream(dictLabel.split(separator))
                    .map(l -> map.getOrDefault(l, StringUtils.EMPTY))
                    .collect(Collectors.joining(separator));
        } else {
            return map.getOrDefault(dictLabel, StringUtils.EMPTY);
        }
    }

}
