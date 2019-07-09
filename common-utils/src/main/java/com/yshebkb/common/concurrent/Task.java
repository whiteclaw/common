package com.yshebkb.common.concurrent;

/**
 * ObservableThread接收的任务接口
 *
 * @author whiteclaw
 * created on 2019-01-04
 */
@FunctionalInterface
public interface Task<T> {
    /**
     * 任务接口, 该接口允许有返回值
     *
     * @return 任务返回值
     */
    T call();
}
