package com.LiveFullstack.controller;

import com.LiveFullstack.model.UserVoteRequest;
import com.LiveFullstack.model.Vote;
import com.LiveFullstack.util.ApiResponse;
import com.LiveFullstack.util.MockDataGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
     * 获取所有投票记录 (v1版本)
     */
    @GetMapping("/api/v1/votes")
    public ApiResponse<List<Vote>> getVotesV1() {
        log.info("GET /api/v1/votes - 获取所有投票记录");
        List<Vote> votes = mockDataGenerator.getAllVotes();
        return ApiResponse.success(votes);
    }

    /**
     * 获取所有投票记录
     */
    @GetMapping("/api/votes")
    public ApiResponse<List<Vote>> getVotes() {
        log.info("GET /api/votes - 获取所有投票记录");
        List<Vote> votes = mockDataGenerator.getAllVotes();
        return ApiResponse.success(votes);
    }

    /**
     * 用户投票
     */
    @PostMapping("/api/v1/user-vote")
    public ApiResponse<Vote> createUserVote(@RequestBody UserVoteRequest request) {
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

        Vote createdVote = mockDataGenerator.createVote(vote);
        return ApiResponse.success("投票成功", createdVote);
    }

    /**
     * 获取用户投票记录
     * 支持按topicId查询投票统计
     */
    @GetMapping("/api/v1/user-votes")
    public ApiResponse<?> getUserVotes(@RequestParam(required = false) Long topicId,
                                       @RequestParam(required = false) Long userId) {
        log.info("GET /api/v1/user-votes - 查询投票记录: topicId={}, userId={}", topicId, userId);

        if (topicId != null) {
            // 返回指定话题的投票统计
            Map<String, Long> voteStats = mockDataGenerator.getVoteStatsByTopicId(topicId);
            return ApiResponse.success(voteStats);
        }

        // 返回所有投票记录
        List<Vote> votes = mockDataGenerator.getAllVotes();
        return ApiResponse.success(votes);
    }
}
