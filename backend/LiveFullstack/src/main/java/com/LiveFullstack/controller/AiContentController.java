package com.LiveFullstack.controller;

import com.LiveFullstack.model.AiContent;
import com.LiveFullstack.util.ApiResponse;
import com.LiveFullstack.util.MockDataGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AI内容相关接口控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AiContentController {

    private final MockDataGenerator mockDataGenerator;

    /**
     * 获取所有AI内容 (v1版本)
     */
    @GetMapping("/api/v1/ai-content")
    public ApiResponse<List<AiContent>> getAiContentsV1() {
        log.info("GET /api/v1/ai-content - 获取所有AI内容");
        List<AiContent> aiContents = mockDataGenerator.getAllAiContents();
        return ApiResponse.success(aiContents);
    }

    /**
     * 获取指定直播间的AI内容
     */
    @GetMapping("/api/v1/ai-content/stream/{streamId}")
    public ApiResponse<List<AiContent>> getAiContentsByStreamId(@PathVariable Long streamId) {
        log.info("GET /api/v1/ai-content/stream/{} - 获取直播间AI内容", streamId);
        List<AiContent> aiContents = mockDataGenerator.getAiContentsByStreamId(streamId);
        return ApiResponse.success(aiContents);
    }

    /**
     * 根据ID获取单个AI内容
     */
    @GetMapping("/api/v1/ai-content/{id}")
    public ApiResponse<AiContent> getAiContentById(@PathVariable Long id) {
        log.info("GET /api/v1/ai-content/{} - 获取AI内容详情", id);
        return mockDataGenerator.getAiContentById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("AI内容不存在"));
    }
}
