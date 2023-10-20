package com.matrix.strategy.service;

/**
 * 模板方法
 *
 * @author ZhaoWeiLong
 * @since 2023/7/24
 **/
public abstract class AbstractBusinessHandler<R, T> implements BusinessHandler<R, T> {

    @Override
    public R businessHandler(T t) {
        beforeHandler(t);
        final R r = handler(t);
        afterHandler(r, t);
        return r;
    }


    /**
     * 前处理程序
     *
     * @param t t
     */
    public void beforeHandler(T t) {
    }

    /**
     * 处理程序
     *
     * @param t t
     * @return {@link R}
     */
    protected abstract R handler(T t);

    /**
     * 后处理程序
     *
     * @param r r
     * @param t t
     */
    public void afterHandler(R r, T t) {
    }

}
