package com.LiveFullstack.controller;

import com.LiveFullstack.model.DebateTopic;
import com.LiveFullstack.util.ApiResponse;
import com.LiveFullstack.util.MockDataGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 辩论话题相关接口控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class DebateTopicController {

    private final MockDataGenerator mockDataGenerator;

    /**
     * 获取当前辩论话题（前端期望格式）
     * 返回单个辩题对象，包含 leftSide 和 rightSide
     */
    @GetMapping("/api/v1/debate-topic")
    public ApiResponse<Map<String, Object>> getDebateTopic(@RequestParam(required = false) String stream_id) {
        log.info("GET /api/v1/debate-topic - 获取辩题信息: stream_id={}", stream_id);

        List<DebateTopic> topics = mockDataGenerator.getAllDebateTopics();

        // 获取第一个活跃的辩题
        DebateTopic topic = topics.stream()
                .filter(DebateTopic::getActive)
                .findFirst()
                .orElse(topics.isEmpty() ? null : topics.get(0));

        if (topic == null) {
            return ApiResponse.error(404, "没有辩题数据");
        }

        // 转换为前端期望的格式
        Map<String, Object> result = new HashMap<>();
        result.put("id", topic.getId());
        result.put("title", topic.getTitle());
        result.put("description", topic.getDescription());
        result.put("leftSide", topic.getOptionA());
        result.put("rightSide", topic.getOptionB());
        result.put("leftPosition", topic.getOptionA());
        result.put("rightPosition", topic.getOptionB());

        return ApiResponse.success(result);
    }

    /**
     * 获取所有辩论话题
     */
    @GetMapping("/api/debate-topic")
    public ApiResponse<List<DebateTopic>> getAllDebateTopics() {
        log.info("GET /api/debate-topic - 获取所有辩论话题");
        List<DebateTopic> topics = mockDataGenerator.getAllDebateTopics();
        return ApiResponse.success(topics);
    }
}