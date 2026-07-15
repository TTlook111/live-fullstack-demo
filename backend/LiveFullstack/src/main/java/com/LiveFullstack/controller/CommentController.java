package com.LiveFullstack.controller;

import com.LiveFullstack.model.Comment;
import com.LiveFullstack.model.Like;
import com.LiveFullstack.util.ApiResponse;
import com.LiveFullstack.util.MockDataGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 评论和点赞相关接口控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final MockDataGenerator mockDataGenerator;

    /**
     * 发表评论
     * 兼容前端发送的格式：{contentId, text, user, avatar}
     */
    @PostMapping("/api/comment")
    public ApiResponse<Comment> createComment(@RequestBody Map<String, String> request) {
        log.info("POST /api/comment - 发表评论: {}", request);

        // 获取参数，兼容两种格式
        String contentId = request.get("contentId");
        String text = request.get("text");
        String user = request.get("user");
        String avatar = request.get("avatar");

        // 参数校验
        if (contentId == null || contentId.trim().isEmpty()) {
            throw new IllegalArgumentException("contentId不能为空");
        }
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("text不能为空");
        }

        // 构建Comment对象
        Comment comment = new Comment();
        try {
            comment.setStreamId(Long.parseLong(contentId));
        } catch (NumberFormatException e) {
            comment.setStreamId(1L); // 默认值
        }
        comment.setNickname(user != null ? user : "匿名用户");
        comment.setContent(text);
        comment.setCreatedAt(java.time.LocalDateTime.now());

        Comment createdComment = mockDataGenerator.createComment(comment);
        return ApiResponse.success("评论成功", createdComment);
    }

    /**
     * 点赞评论
     *
     * @param like 点赞信息 (commentId, userId)
     * @return 点赞记录
     */
    @PostMapping("/api/like")
    public ApiResponse<Like> likeComment(@RequestBody Like like) {
        log.info("POST /api/like - 点赞评论: commentId={}, userId={}",
                like.getCommentId(), like.getUserId());

        // 参数校验
        if (like.getCommentId() == null) {
            throw new IllegalArgumentException("commentId不能为空");
        }
        if (like.getUserId() == null) {
            throw new IllegalArgumentException("userId不能为空");
        }

        // 检查评论是否存在
        if (mockDataGenerator.getCommentById(like.getCommentId()).isEmpty()) {
            return ApiResponse.error(404, "评论不存在");
        }

        // 检查是否已点赞
        if (mockDataGenerator.getLikeByCommentIdAndUserId(like.getCommentId(), like.getUserId()).isPresent()) {
            return ApiResponse.error(400, "已经点赞过了");
        }

        Like createdLike = mockDataGenerator.createLike(like);
        return ApiResponse.success("点赞成功", createdLike);
    }

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     * @return 删除结果
     */
    @DeleteMapping("/api/comment/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Long commentId) {
        log.info("DELETE /api/comment/{} - 删除评论", commentId);

        // 检查评论是否存在
        if (mockDataGenerator.getCommentById(commentId).isEmpty()) {
            return ApiResponse.error(404, "评论不存在");
        }

        boolean deleted = mockDataGenerator.deleteComment(commentId);
        if (deleted) {
            return ApiResponse.success("删除成功", null);
        } else {
            return ApiResponse.error("删除失败");
        }
    }
}
