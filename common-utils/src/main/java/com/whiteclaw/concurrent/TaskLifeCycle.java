package com.whiteclaw.concurrent;

/**
 * 监控线程不同的生命周期, 并在每个生命周期内做相应的动作
 *
 * @author whiteclaw
 * created on 2019-01-04
 */
public interface TaskLifeCycle<T> {
    /**
     * 任务开始动作
     *
     * @param thread 执行任务线程
     */
    void onStart(Thread thread);

    /**
     * 任务运行动作
     *
     * @param thread 执行任务线程
     */
    void onRunning(Thread thread);

    /**
     * 任务完成动作
     *
     * @param thread 执行任务线程
     * @param result 任务执行结果
     */
    void onFinished(Thread thread, T result);

    /**
     * 任务发生异常动作
     *
     * @param thread 执行任务线程
     * @param e      任务抛出的异常
     */
    void onError(Thread thread, Exception e);
}

