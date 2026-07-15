package com.LiveFullstack.controller;

import com.LiveFullstack.model.DebateTopic;
import com.LiveFullstack.util.ApiResponse;
import com.LiveFullstack.util.MockDataGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 辩论话题相关接口控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class DebateTopicController {

    private final MockDataGenerator mockDataGenerator;

    /**
     * 获取所有辩论话题 (v1版本)
     */
    @GetMapping("/api/v1/debate-topic")
    public ApiResponse<List<DebateTopic>> getDebateTopicsV1() {
        log.info("GET /api/v1/debate-topic - 获取所有辩论话题");
        List<DebateTopic> topics = mockDataGenerator.getAllDebateTopics();
        return ApiResponse.success(topics);
    }
}