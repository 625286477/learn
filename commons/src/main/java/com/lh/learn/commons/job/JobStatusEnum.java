package com.lh.learn.commons.job;

/**
 * Job 执行状态
 *
 * @author liuhai
 */
public enum JobStatusEnum {
    CREATED,   // 点状态，已创建
    PRE_RUNNING,// 过程状态，准备运行中
    ON_RUNNING,// 过程状态，正在运行中
    STOPPING,// 过程状态，正在停止中
    STOPPED,// 点状态，已停止
    ENDING,// 过程状态，正在结束中
    ENDED;// 点状态，已结束

    /**
     * 是否能够进行 Start Action
     */
    public boolean canStart() {
        return this == CREATED || this == STOPPED;
    }

    /**
     * 是否能够进行 Stop Action
     */
    public boolean canStop() {
        return this == PRE_RUNNING || this == ON_RUNNING;
    }

    /**
     * 是否能够进行 End Action
     */
    public boolean canEnd() {
        return this == CREATED || this == STOPPED;
    }

}
