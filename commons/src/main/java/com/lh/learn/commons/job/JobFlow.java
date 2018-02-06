package com.lh.learn.commons.job;

/**
 * Job 执行流程
 *
 * @author liuhai
 */
public class JobFlow implements Job {


    @Override
    public synchronized boolean submit(JobConfig jobConfig) {
        if (JobManagePool.addJob(jobConfig)) {
            jobConfig.setJobStatus(JobStatusEnum.CREATED);
            return true;
        }

        return false;
    }

    @Override
    public synchronized void submitAndStart(JobConfig jobConfig) {
    }

    @Override
    public void start(JobConfig jobConfig) {
        if(jobConfig.getJobStatus().canStart()){
            //jobConfig.setJobStatus();
        }
    }

    @Override
    public void stop(JobConfig jobConfig) {

    }

    @Override
    public void end(JobConfig jobConfig) {

    }
}
