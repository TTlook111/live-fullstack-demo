package com.LiveFullstack.controller;

import com.LiveFullstack.model.UserVoteRequest;
import com.LiveFullstack.model.Vote;
import com.LiveFullstack.util.ApiResponse;
import com.LiveFullstack.util.MockDataGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投票相关接口控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class VoteController {

    private final MockDataGenerator mockDataGenerator;

    /**
     * 获取投票统计（前端期望格式）
     * 返回 leftVotes 和 rightVotes 的统计数量
     */
    @GetMapping("/api/v1/votes")
    public ApiResponse<Map<String, Object>> getVotesV1(@RequestParam(required = false) String stream_id) {
        log.info("GET /api/v1/votes - 获取投票统计: stream_id={}", stream_id);
        return ApiResponse.success(getVoteStatistics(stream_id));
    }

    /**
     * 获取投票统计（前端期望格式）
     */
    @GetMapping("/api/votes")
    public ApiResponse<Map<String, Object>> getVotes(@RequestParam(required = false) String stream_id) {
        log.info("GET /api/votes - 获取投票统计: stream_id={}", stream_id);
        return ApiResponse.success(getVoteStatistics(stream_id));
    }

    /**
     * 计算投票统计数据
     * 前端期望: { leftVotes: number, rightVotes: number, totalVotes: number, leftPercentage: number, rightPercentage: number }
     */
    private Map<String, Object> getVoteStatistics(String streamId) {
        List<Vote> allVotes = mockDataGenerator.getAllVotes();

        // 统计各选项的票数
        long leftVotes = allVotes.stream()
                .filter(v -> "Java".equals(v.getOption()) || "left".equals(v.getOption()) || "REST".equals(v.getOption()))
                .count();
        long rightVotes = allVotes.stream()
                .filter(v -> "Go".equals(v.getOption()) || "right".equals(v.getOption()) || "GraphQL".equals(v.getOption()))
                .count();

        // 如果没有匹配的数据，使用默认值（模拟数据）
        if (leftVotes == 0 && rightVotes == 0) {
            leftVotes = 65;
            rightVotes = 45;
        }

        long totalVotes = leftVotes + rightVotes;
        int leftPercentage = totalVotes > 0 ? (int) Math.round((leftVotes * 100.0) / totalVotes) : 50;
        int rightPercentage = totalVotes > 0 ? 100 - leftPercentage : 50;

        Map<String, Object> result = new HashMap<>();
        result.put("leftVotes", leftVotes);
        result.put("rightVotes", rightVotes);
        result.put("totalVotes", totalVotes);
        result.put("leftPercentage", leftPercentage);
        result.put("rightPercentage", rightPercentage);

        return result;
    }

    /**
     * 用户投票
     */
    @PostMapping("/api/v1/user-vote")
    public ApiResponse<Map<String, Object>> createUserVote(@RequestBody UserVoteRequest request) {
        log.info("POST /api/v1/user-vote - 用户投票: topicId={}, userId={}, option={}",
                request.getTopicId(), request.getUserId(), request.getOption());

        // 参数校验
        if (request.getTopicId() == null) {
            throw new IllegalArgumentException("topicId不能为空");
        }
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("userId不能为空");
        }
        if (request.getOption() == null || request.getOption().trim().isEmpty()) {
            throw new IllegalArgumentException("option不能为空");
        }

        Vote vote = new Vote();
        vote.setTopicId(request.getTopicId());
        vote.setUserId(request.getUserId());
        vote.setOption(request.getOption());

        mockDataGenerator.createVote(vote);

        // 返回更新后的投票统计
        return ApiResponse.success("投票成功", getVoteStatistics(null));
    }

    /**
     * 获取用户投票记录
     */
    @GetMapping("/api/v1/user-votes")
    public ApiResponse<?> getUserVotes(@RequestParam(required = false) Long topicId,
                                       @RequestParam(required = false) Long userId) {
        log.info("GET /api/v1/user-votes - 查询投票记录: topicId={}, userId={}", topicId, userId);

        // 返回投票统计
        return ApiResponse.success(getVoteStatistics(null));
    }
}
