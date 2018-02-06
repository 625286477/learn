package com.lh.learn.commons.job;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Job 管理池
 *
 * @author liuhai
 */
public class JobManagePool {
    private final static Map<String, JobConfig> jobPoolMap = new ConcurrentHashMap<>();

    public static boolean addJob(JobConfig jobConfig) {
        JobConfig jobConfig1 = jobPoolMap.putIfAbsent(jobConfig.getJobName(), jobConfig);
        if (jobConfig1==null){
            // 表示添加成功
            return true;
        }

        return false;
    }

    public static void deleteJob(JobConfig jobConfig) {
        deleteJob(jobConfig.getJobName());
    }

    public static void deleteJob(String jobName) {
        jobPoolMap.remove(jobName);
    }
}
