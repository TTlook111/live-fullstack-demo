package com.LiveFullstack.util;

import com.LiveFullstack.model.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MockDataGenerator {

    private final ConcurrentHashMap<Long, Stream> streams = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, DebateTopic> debateTopics = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Comment> comments = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Vote> votes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, AiContent> aiContents = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Dashboard> dashboards = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Like> likes = new ConcurrentHashMap<>();

    private final AtomicLong streamIdGenerator = new AtomicLong(1);
    private final AtomicLong topicIdGenerator = new AtomicLong(1);
    private final AtomicLong commentIdGenerator = new AtomicLong(1);
    private final AtomicLong voteIdGenerator = new AtomicLong(1);
    private final AtomicLong aiContentIdGenerator = new AtomicLong(1);
    private final AtomicLong dashboardIdGenerator = new AtomicLong(1);
    private final AtomicLong likeIdGenerator = new AtomicLong(1);

    @PostConstruct
    public void init() {
        log.info("Initializing MockDataGenerator with sample data...");
        initSampleStreams();
        initSampleDebateTopics();
        initSampleComments();
        initSampleVotes();
        initSampleAiContents();
        initSampleDashboards();
        log.info("Mock data initialization completed.");
    }

    // ==================== Stream CRUD ====================

    public List<Stream> getAllStreams() {
        return new ArrayList<>(streams.values());
    }

    public Optional<Stream> getStreamById(Long id) {
        return Optional.ofNullable(streams.get(id));
    }

    public Stream createStream(Stream stream) {
        Long id = streamIdGenerator.getAndIncrement();
        stream.setId(id);
        stream.setCreatedAt(LocalDateTime.now());
        if (stream.getStatus() == null) {
            stream.setStatus("offline");
        }
        if (stream.getViewerCount() == null) {
            stream.setViewerCount(0);
        }
        streams.put(id, stream);
        return stream;
    }

    public Optional<Stream> updateStream(Long id, Stream stream) {
        if (!streams.containsKey(id)) {
            return Optional.empty();
        }
        stream.setId(id);
        streams.put(id, stream);
        return Optional.of(stream);
    }

    public boolean deleteStream(Long id) {
        return streams.remove(id) != null;
    }

    public List<Stream> getStreamsByStatus(String status) {
        return streams.values().stream()
                .filter(s -> status.equals(s.getStatus()))
                .collect(Collectors.toList());
    }

    // ==================== DebateTopic CRUD ====================

    public List<DebateTopic> getAllDebateTopics() {
        return new ArrayList<>(debateTopics.values());
    }

    public Optional<DebateTopic> getDebateTopicById(Long id) {
        return Optional.ofNullable(debateTopics.get(id));
    }

    public DebateTopic createDebateTopic(DebateTopic topic) {
        Long id = topicIdGenerator.getAndIncrement();
        topic.setId(id);
        topic.setCreatedAt(LocalDateTime.now());
        if (topic.getActive() == null) {
            topic.setActive(true);
        }
        debateTopics.put(id, topic);
        return topic;
    }

    public Optional<DebateTopic> updateDebateTopic(Long id, DebateTopic topic) {
        if (!debateTopics.containsKey(id)) {
            return Optional.empty();
        }
        topic.setId(id);
        debateTopics.put(id, topic);
        return Optional.of(topic);
    }

    public boolean deleteDebateTopic(Long id) {
        return debateTopics.remove(id) != null;
    }

    public List<DebateTopic> getActiveDebateTopics() {
        return debateTopics.values().stream()
                .filter(DebateTopic::getActive)
                .collect(Collectors.toList());
    }

    // ==================== Comment CRUD ====================

    public List<Comment> getAllComments() {
        return new ArrayList<>(comments.values());
    }

    public Optional<Comment> getCommentById(Long id) {
        return Optional.ofNullable(comments.get(id));
    }

    public Comment createComment(Comment comment) {
        Long id = commentIdGenerator.getAndIncrement();
        comment.setId(id);
        comment.setCreatedAt(LocalDateTime.now());
        comments.put(id, comment);
        return comment;
    }

    public Optional<Comment> updateComment(Long id, Comment comment) {
        if (!comments.containsKey(id)) {
            return Optional.empty();
        }
        comment.setId(id);
        comments.put(id, comment);
        return Optional.of(comment);
    }

    public boolean deleteComment(Long id) {
        return comments.remove(id) != null;
    }

    public List<Comment> getCommentsByStreamId(Long streamId) {
        return comments.values().stream()
                .filter(c -> streamId.equals(c.getStreamId()))
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    // ==================== Like CRUD ====================

    public List<Like> getAllLikes() {
        return new ArrayList<>(likes.values());
    }

    public Optional<Like> getLikeById(Long id) {
        return Optional.ofNullable(likes.get(id));
    }

    public Like createLike(Like like) {
        Long id = likeIdGenerator.getAndIncrement();
        like.setId(id);
        like.setCreatedAt(LocalDateTime.now());
        likes.put(id, like);
        return like;
    }

    public boolean deleteLike(Long id) {
        return likes.remove(id) != null;
    }

    public List<Like> getLikesByCommentId(Long commentId) {
        return likes.values().stream()
                .filter(l -> commentId.equals(l.getCommentId()))
                .collect(Collectors.toList());
    }

    public long countLikesByCommentId(Long commentId) {
        return likes.values().stream()
                .filter(l -> commentId.equals(l.getCommentId()))
                .count();
    }

    public Optional<Like> getLikeByCommentIdAndUserId(Long commentId, Long userId) {
        return likes.values().stream()
                .filter(l -> commentId.equals(l.getCommentId()) && userId.equals(l.getUserId()))
                .findFirst();
    }

    // ==================== Vote CRUD ====================

    public List<Vote> getAllVotes() {
        return new ArrayList<>(votes.values());
    }

    public Optional<Vote> getVoteById(Long id) {
        return Optional.ofNullable(votes.get(id));
    }

    public Vote createVote(Vote vote) {
        Long id = voteIdGenerator.getAndIncrement();
        vote.setId(id);
        vote.setCreatedAt(LocalDateTime.now());
        votes.put(id, vote);
        return vote;
    }

    public Optional<Vote> updateVote(Long id, Vote vote) {
        if (!votes.containsKey(id)) {
            return Optional.empty();
        }
        vote.setId(id);
        votes.put(id, vote);
        return Optional.of(vote);
    }

    public boolean deleteVote(Long id) {
        return votes.remove(id) != null;
    }

    public List<Vote> getVotesByTopicId(Long topicId) {
        return votes.values().stream()
                .filter(v -> topicId.equals(v.getTopicId()))
                .collect(Collectors.toList());
    }

    public Map<String, Long> getVoteStatsByTopicId(Long topicId) {
        return votes.values().stream()
                .filter(v -> topicId.equals(v.getTopicId()))
                .collect(Collectors.groupingBy(Vote::getOption, Collectors.counting()));
    }

    // ==================== AiContent CRUD ====================

    public List<AiContent> getAllAiContents() {
        return new ArrayList<>(aiContents.values());
    }

    public Optional<AiContent> getAiContentById(Long id) {
        return Optional.ofNullable(aiContents.get(id));
    }

    public AiContent createAiContent(AiContent aiContent) {
        Long id = aiContentIdGenerator.getAndIncrement();
        aiContent.setId(id);
        aiContent.setCreatedAt(LocalDateTime.now());
        aiContents.put(id, aiContent);
        return aiContent;
    }

    public Optional<AiContent> updateAiContent(Long id, AiContent aiContent) {
        if (!aiContents.containsKey(id)) {
            return Optional.empty();
        }
        aiContent.setId(id);
        aiContents.put(id, aiContent);
        return Optional.of(aiContent);
    }

    public boolean deleteAiContent(Long id) {
        return aiContents.remove(id) != null;
    }

    public List<AiContent> getAiContentsByStreamId(Long streamId) {
        return aiContents.values().stream()
                .filter(a -> streamId.equals(a.getStreamId()))
                .collect(Collectors.toList());
    }

    // ==================== Dashboard CRUD ====================

    public List<Dashboard> getAllDashboards() {
        return new ArrayList<>(dashboards.values());
    }

    public Optional<Dashboard> getDashboardById(Long id) {
        return Optional.ofNullable(dashboards.get(id));
    }

    public Dashboard createDashboard(Dashboard dashboard) {
        Long id = dashboardIdGenerator.getAndIncrement();
        dashboard.setId(id);
        dashboard.setGeneratedAt(LocalDateTime.now());
        dashboards.put(id, dashboard);
        return dashboard;
    }

    public Optional<Dashboard> updateDashboard(Long id, Dashboard dashboard) {
        if (!dashboards.containsKey(id)) {
            return Optional.empty();
        }
        dashboard.setId(id);
        dashboards.put(id, dashboard);
        return Optional.of(dashboard);
    }

    public boolean deleteDashboard(Long id) {
        return dashboards.remove(id) != null;
    }

    public Optional<Dashboard> getDashboardByStreamId(Long streamId) {
        return dashboards.values().stream()
                .filter(d -> streamId.equals(d.getStreamId()))
                .findFirst();
    }

    // ==================== Sample Data Initialization ====================

    private void initSampleStreams() {
        createStream(buildStream("Java Spring Boot 实战开发", "从零开始学习Spring Boot框架", "stream_key_001", 1L, "live", 1250));
        createStream(buildStream("Vue3 前端入门教程", "Vue3组合式API详解", "stream_key_002", 2L, "live", 890));
        createStream(buildStream("MySQL数据库优化", "SQL性能调优实战", "stream_key_003", 3L, "offline", 0));
        createStream(buildStream("Redis缓存实战", "Redis高可用架构设计", "stream_key_004", 1L, "live", 2100));
        createStream(buildStream("Docker容器化部署", "从开发到生产的容器化方案", "stream_key_005", 4L, "offline", 0));
    }

    private void initSampleDebateTopics() {
        createDebateTopic(buildDebateTopic("Java vs Go 哪个更适合微服务?", "讨论两种语言在微服务架构中的优劣", "Java", "Go", true));
        createDebateTopic(buildDebateTopic("REST vs GraphQL API设计选择", "API设计风格的技术选型", "REST", "GraphQL", true));
        createDebateTopic(buildDebateTopic("单体架构 vs 微服务架构", "项目初期应该如何选择架构", "单体架构", "微服务架构", false));
    }

    private void initSampleComments() {
        createComment(buildComment(1L, 1L, "张三", "老师讲得太好了!"));
        createComment(buildComment(1L, 2L, "李四", "能详细讲讲依赖注入吗?"));
        createComment(buildComment(1L, 3L, "王五", "这个Demo很实用"));
        createComment(buildComment(2L, 1L, "张三", "Vue3的Composition API真香"));
        createComment(buildComment(2L, 4L, "赵六", "什么时候讲Pinia?"));
        createComment(buildComment(4L, 2L, "李四", "Redis集群怎么搭建?"));
    }

    private void initSampleVotes() {
        createVote(buildVote(1L, 1L, "Java"));
        createVote(buildVote(1L, 2L, "Go"));
        createVote(buildVote(1L, 3L, "Java"));
        createVote(buildVote(1L, 4L, "Go"));
        createVote(buildVote(1L, 5L, "Java"));
        createVote(buildVote(2L, 1L, "REST"));
        createVote(buildVote(2L, 2L, "GraphQL"));
        createVote(buildVote(2L, 3L, "REST"));
    }

    private void initSampleAiContents() {
        createAiContent(buildAiContent(1L, "summary", "本场直播介绍了Spring Boot的核心概念和实战技巧"));
        createAiContent(buildAiContent(1L, "highlights", "精彩瞬间: 依赖注入演示、自动配置原理讲解"));
        createAiContent(buildAiContent(2L, "summary", "Vue3入门教程，讲解了组合式API的基本用法"));
    }

    private void initSampleDashboards() {
        createDashboard(buildDashboard(1L, 1250, 2800, 156, 89));
        createDashboard(buildDashboard(2L, 890, 1500, 98, 45));
        createDashboard(buildDashboard(4L, 2100, 3500, 234, 120));
    }

    // ==================== Builder Helpers ====================

    private Stream buildStream(String title, String description, String streamKey,
                               Long userId, String status, Integer viewerCount) {
        Stream stream = new Stream();
        stream.setTitle(title);
        stream.setDescription(description);
        stream.setStreamKey(streamKey);
        stream.setUserId(userId);
        stream.setStatus(status);
        stream.setViewerCount(viewerCount);
        stream.setStartedAt(LocalDateTime.now().minusHours(2));
        return stream;
    }

    private DebateTopic buildDebateTopic(String title, String description,
                                         String optionA, String optionB, Boolean active) {
        DebateTopic topic = new DebateTopic();
        topic.setTitle(title);
        topic.setDescription(description);
        topic.setOptionA(optionA);
        topic.setOptionB(optionB);
        topic.setActive(active);
        return topic;
    }

    private Comment buildComment(Long streamId, Long userId, String nickname, String content) {
        Comment comment = new Comment();
        comment.setStreamId(streamId);
        comment.setUserId(userId);
        comment.setNickname(nickname);
        comment.setContent(content);
        return comment;
    }

    private Vote buildVote(Long topicId, Long userId, String option) {
        Vote vote = new Vote();
        vote.setTopicId(topicId);
        vote.setUserId(userId);
        vote.setOption(option);
        return vote;
    }

    private AiContent buildAiContent(Long streamId, String contentType, String content) {
        AiContent aiContent = new AiContent();
        aiContent.setStreamId(streamId);
        aiContent.setContentType(contentType);
        aiContent.setContent(content);
        return aiContent;
    }

    private Dashboard buildDashboard(Long streamId, Integer totalViewers,
                                     Integer peakViewers, Integer totalComments, Integer totalVotes) {
        Dashboard dashboard = new Dashboard();
        dashboard.setStreamId(streamId);
        dashboard.setTotalViewers(totalViewers);
        dashboard.setPeakViewers(peakViewers);
        dashboard.setTotalComments(totalComments);
        dashboard.setTotalVotes(totalVotes);
        return dashboard;
    }
}
