package com.lh.learn.commons.job;

/**
 * must write something to descript this file.
 *
 * @author liuhai
 */
public interface Job {
    boolean submit(JobConfig jobConfig);

    void submitAndStart(JobConfig jobConfig);

    void start(JobConfig jobConfig);

    void stop(JobConfig jobConfig);

    void end(JobConfig jobConfig);
}
