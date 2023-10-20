package com.matrix.api.api;


import com.matrix.common.result.R;
import com.matrix.common.vo.PageResult;
import com.matrix.validator.group.AddGroup;
import com.matrix.validator.group.EditGroup;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 描述：<p>feign client基类</p>
 *
 * @author ZhaoWeiLong
 * @since 2023/4/14
 **/
public interface IBaseFeignClient<T, V, Q> {

    /**
     * 通过id查询
     *
     * @param id 主键
     * @return {@link R}<{@link T}>
     */
//    @Parameter(name = "id", description = "主键", in = ParameterIn.QUERY, required = true)
//    @Operation(summary = "通过id查询", description = "通过id查询")
    @GetMapping
    @Operation(hidden = true)
    R<T> queryById(@NotNull(message = "id不能为空") @RequestParam("id") Long id);


    /**
     * 通过id查询vo详情
     *
     * @param id id
     * @return {@link R}<{@link T}>
     */
    @Operation(hidden = true)
    @GetMapping("/queryVoById")
    R<V> queryVoById(@NotNull(message = "id不能为空") @RequestParam("id") Long id);

    /**
     * 新增记录
     *
     * @param entity 新增对象
     * @return {@link R}<{@link Long}>
     */
//    @Operation(summary = "新增记录", description = "新增记录")
    @PostMapping("/save")
    @Operation(hidden = true)
    R<Long> save(@RequestBody @Validated(value = AddGroup.class) T entity);


    /**
     * 通过id更新
     *
     * @param entity 更新对象
     * @return {@link R}<{@link Boolean}>
     */
//    @Operation(summary = "通过id更新", description = "通过id更新")
    @PostMapping("/updateById")
    @Operation(hidden = true)
    R<Boolean> updateById(@RequestBody @Validated(value = EditGroup.class) T entity);


    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return {@link R}<{@link PageResult}<{@link V}>>
     */
//    @Operation(summary = "分页查询", description = "分页查询")
    @PostMapping("/pageQuery")
    @Operation(hidden = true)
    R<PageResult<V>> pageQuery(@Valid @RequestBody Q query);


    /**
     * 通过id删除
     *
     * @param id 主键
     * @return {@link R}<{@link Boolean}>
     */
//    @Parameter(name = "id", description = "主键", in = ParameterIn.QUERY, required = true)
//    @Operation(summary = "通过id删除", description = "通过id删除")
    @DeleteMapping
    @Operation(hidden = true)
    R<Boolean> deleteById(@NotNull(message = "id不能为空") @RequestParam("id") Long id);

}
