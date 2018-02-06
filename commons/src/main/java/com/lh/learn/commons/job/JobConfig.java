package com.lh.learn.commons.job;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * must write something to descript this file.
 *
 * @author liuhai
 */
@Data
@AllArgsConstructor
public class JobConfig {

    /**
     * 名称,具有全局唯一性
     */
    private String jobName;

    /**
     * 优先级
     */
    private int priority;

    private JobActionEnum jobAction;
    private JobStatusEnum jobStatus;

    /**
     * 参数集合
     */
    private Map<String, Object> jobParams;
}
