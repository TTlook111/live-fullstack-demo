package com.LiveFullstack.controller;

import com.LiveFullstack.model.Dashboard;
import com.LiveFullstack.model.LiveStatus;
import com.LiveFullstack.model.Stream;
import com.LiveFullstack.util.ApiResponse;
import com.LiveFullstack.util.MockDataGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理后台相关接口控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final MockDataGenerator mockDataGenerator;

    /**
     * 获取直播间实时状态
     *
     * @param streamId 直播间ID
     * @return 直播间状态信息
     */
    @GetMapping("/api/admin/live/status")
    public ApiResponse<LiveStatus> getLiveStatus(@RequestParam Long streamId) {
        log.info("GET /api/admin/live/status - 获取直播间状态: streamId={}", streamId);

        return mockDataGenerator.getStreamById(streamId)
                .map(stream -> {
                    // 统计评论数
                    long commentCount = mockDataGenerator.getCommentsByStreamId(streamId).size();
                    // 统计投票数
                    long voteCount = mockDataGenerator.getAllVotes().size();

                    LiveStatus status = new LiveStatus();
                    status.setStreamId(streamId);
                    status.setStatus(stream.getStatus());
                    status.setViewerCount(stream.getViewerCount());
                    status.setCommentCount((int) commentCount);
                    status.setVoteCount((int) voteCount);
                    status.setUpdatedAt(LocalDateTime.now());
                    return ApiResponse.success(status);
                })
                .orElse(ApiResponse.error(404, "直播间不存在"));
    }

    /**
     * 获取管理后台仪表盘数据
     *
     * @param streamId 直播间ID
     * @return 仪表盘数据
     */
    @GetMapping("/api/admin/dashboard")
    public ApiResponse<Dashboard> getDashboard(@RequestParam Long streamId) {
        log.info("GET /api/admin/dashboard - 获取仪表盘数据: streamId={}", streamId);

        return mockDataGenerator.getDashboardByStreamId(streamId)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "仪表盘数据不存在"));
    }

    /**
     * 直播控制接口 (开始/停止直播)
     *
     * @param request 控制请求，包含 streamId 和 action (start/stop)
     * @return 操作结果
     */
    @PostMapping("/api/live/control")
    public ApiResponse<Map<String, Object>> controlLive(@RequestBody Map<String, Object> request) {
        Long streamId = Long.valueOf(request.get("streamId").toString());
        String action = request.get("action").toString();
        log.info("POST /api/live/control - 直播控制: streamId={}, action={}", streamId, action);

        if (!"start".equals(action) && !"stop".equals(action)) {
            return ApiResponse.error(400, "无效的操作类型，仅支持 start/stop");
        }

        return mockDataGenerator.getStreamById(streamId)
                .map(stream -> {
                    if ("start".equals(action)) {
                        stream.setStatus("live");
                        stream.setStartedAt(LocalDateTime.now());
                        mockDataGenerator.updateStream(streamId, stream);
                        log.info("直播间 {} 已开始直播", streamId);
                    } else {
                        stream.setStatus("offline");
                        stream.setEndedAt(LocalDateTime.now());
                        stream.setViewerCount(0);
                        mockDataGenerator.updateStream(streamId, stream);
                        log.info("直播间 {} 已停止直播", streamId);
                    }

                    Map<String, Object> result = new HashMap<>();
                    result.put("streamId", streamId);
                    result.put("action", action);
                    result.put("status", stream.getStatus());
                    return ApiResponse.success("操作成功", result);
                })
                .orElse(ApiResponse.error(404, "直播间不存在"));
    }

    /**
     * 获取所有直播间列表 (管理员视角)
     *
     * @param status 直播间状态过滤 (live/offline，可选)
     * @return 直播间列表
     */
    @GetMapping("/api/v1/admin/streams")
    public ApiResponse<List<Stream>> getAdminStreams(
            @RequestParam(required = false) String status) {
        log.info("GET /api/v1/admin/streams - 获取直播间列表: status={}", status);

        List<Stream> streams;
        if (status != null && !status.trim().isEmpty()) {
            streams = mockDataGenerator.getStreamsByStatus(status);
        } else {
            streams = mockDataGenerator.getAllStreams();
        }
        return ApiResponse.success(streams);
    }

    /**
     * 获取投票统计信息
     *
     * @return 所有话题的投票统计数据
     */
    @GetMapping("/api/v1/admin/votes/statistics")
    public ApiResponse<Map<String, Object>> getVotesStatistics() {
        log.info("GET /api/v1/admin/votes/statistics - 获取投票统计");

        Map<String, Object> statistics = new HashMap<>();

        // 总投票数
        long totalVotes = mockDataGenerator.getAllVotes().size();
        statistics.put("totalVotes", totalVotes);

        // 各话题投票统计
        Map<String, Map<String, Long>> topicVotes = new HashMap<>();
        mockDataGenerator.getAllDebateTopics().forEach(topic -> {
            Map<String, Long> voteStats = mockDataGenerator.getVoteStatsByTopicId(topic.getId());
            topicVotes.put(topic.getTitle(), voteStats);
        });
        statistics.put("topicVotes", topicVotes);

        return ApiResponse.success(statistics);
    }
}
