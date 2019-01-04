package com.whiteclaw.concurrent;

import java.util.Objects;

/**
 * 具有线程生命周期监控功能的线程运行类
 *
 * @author whiteclaw
 * created on 2019-01-04
 */
public class ObservableThread<T> extends Thread implements Observable {
    /**
     * 线程监控周期实例
     */
    private final TaskLifeCycle<T> lifeCycle;

    /**
     * 线程需要运行的任务
     */
    private final Task<T> task;

    /**
     * 当前线程的生命周期状态
     */
    private Cycle cycle;

    public ObservableThread(Task<T> task) {
        this(new EmptyTaskLifeCycle<>(), task);
    }

    public ObservableThread(TaskLifeCycle<T> lifeCycle, Task<T> task) {
        if (Objects.isNull(task)) {
            throw new IllegalArgumentException("task cannot be null");
        }
        this.task = task;
        this.lifeCycle = lifeCycle;
    }

    @Override
    public final void run() {
        // 开始执行任务
        this.update(Cycle.STARTED, null, null);
        try {
            // 执行任务
            T result = this.task.call();
            // 任务执行完成
            this.update(Cycle.DONE, result, null);
        } catch (Exception e) {
            // 处理异常
            this.update(Cycle.ERROR, null, e);
        }
    }

    /**
     * 更新当前线程生命周期
     *
     * @param cycle  状态
     * @param result 任务执行结果
     * @param e      任务抛出的异常
     */
    private void update(Cycle cycle, T result, Exception e) {
        this.cycle = cycle;
        if (Objects.isNull(lifeCycle)) {
            return;
        }
        try {
            switch (cycle) {
                case STARTED:
                    lifeCycle.onStart(currentThread());
                    return;
                case RUNNING:
                    lifeCycle.onRunning(currentThread());
                    return;
                case DONE:
                    lifeCycle.onFinished(currentThread(), result);
                    return;
                case ERROR:
                    lifeCycle.onError(currentThread(), e);
                    return;
                default:
            }
        } catch (Exception ex) {
            if (cycle == Cycle.ERROR) {
                throw ex;
            }
        }
    }

    @Override
    public Cycle getCycle() {
        return this.cycle;
    }
}
