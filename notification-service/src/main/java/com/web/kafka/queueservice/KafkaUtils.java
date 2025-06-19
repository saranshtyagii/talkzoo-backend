package com.web.kafka.queueservice;

import com.web.kafka.elaslticsearch.ElasticSearchUtils;
import com.web.kafka.helper.ActiveMembersDetails;
import com.web.kafka.helper.QueueMetaData;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Component
public class KafkaUtils {

    private final ElasticSearchUtils elasticSearchUtils;
    private Map<String, Object> matchQueue;
    private List<QueueMetaData> queueMetaDataList;

    public KafkaUtils(ElasticSearchUtils elasticSearchUtils) {
        this.elasticSearchUtils = elasticSearchUtils;
    }

    private Map<String, Object> getMatchQueue() {
        if (CollectionUtils.isEmpty(matchQueue)) {
            matchQueue = new HashMap<>();
        }
        return matchQueue;
    }

    private List<QueueMetaData> getQueueMetaDataList() {
        if (queueMetaDataList == null) {
            queueMetaDataList = new ArrayList<>();
        }
        return queueMetaDataList;
    }

    public void pushToQueue(QueueMetaData queueMetaData) {
        try {
            getQueueMetaDataList().add(queueMetaData);
        } catch (Exception e) {
            elasticSearchUtils.pushException("PUSH_QUEUE_EXCEPTION", e.getMessage());
        }
    }

    public void pushToQueue(Map<String, Object> queueData) {
        try {
            getMatchQueue().putAll(queueData);
        } catch (Exception e) {
            elasticSearchUtils.pushException("PUSH__MATCH_QUEUE_EXCEPTION", e.getMessage());
        }
    }


    public Map<String, Object> getMatch(List<String> topics, int matchCount) {

        if (matchCount > 10) {
            return null;
        }

        Map<String, Object> matchFoundData = new HashMap<>();

        for (Map.Entry<String, Object> entry : getMatchQueue().entrySet()) {
            Object value = entry.getValue();
            ActiveMembersDetails activeMember = (ActiveMembersDetails) value;

            if (activeMember.isActive() && !activeMember.isEngaged()) {
                List<String> interests = activeMember.getInterests();

                if (interests != null && topics.stream().anyMatch(interests::contains)) {
                    matchFoundData.put(entry.getKey(), value);
                    break;
                }
            }

        }
        if (matchFoundData.isEmpty()) {
            getMatch(topics, matchCount++);
        }
        return matchFoundData;
    }


}
