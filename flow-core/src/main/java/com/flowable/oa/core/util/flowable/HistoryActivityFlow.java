package com.flowable.oa.core.util.flowable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : yangqi
 * @email : lukewei@mockuai.com
 * @description :
 * @since : 2020/2/20 5:47 下午
 */
public class HistoryActivityFlow {

    /**
     * 用以保存高亮的线flowId
     */
    private List<String> highFlows = new ArrayList<>(0);

    /**
     * 用以保存高亮的流程
     */
    private List<String> activitys = new ArrayList<>(0);

    public HistoryActivityFlow() {
    }

    public HistoryActivityFlow(List<String> highFlows, List<String> activitys) {
        this.highFlows = highFlows;
        this.activitys = activitys;
    }

    public List<String> getHighFlows() {
        return highFlows;
    }

    public void setHighFlows(List<String> highFlows) {
        this.highFlows = highFlows;
    }

    public List<String> getActivitys() {
        return activitys;
    }

    public void setActivitys(List<String> activitys) {
        this.activitys = activitys;
    }
}
