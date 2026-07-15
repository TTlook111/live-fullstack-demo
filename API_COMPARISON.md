# 🔍 前后端接口对比分析报告

## 📊 接口对比总览

| 序号 | 前端接口 | 后端实现 | 状态 | 备注 |
|------|----------|----------|------|------|
| 1 | `getVotes(streamId)` | ✅ `GET /api/v1/votes` | ✅ 已实现 | |
| 2 | `getVote(streamId)` | ✅ `GET /api/votes` | ✅ 已实现 | |
| 3 | `userVote(side, votes, streamId)` | ✅ `POST /api/v1/user-vote` | ✅ 已实现 | |
| 4 | `userVoteDistribution(...)` | ✅ `POST /api/v1/user-vote` | ✅ 已实现 | 复用同一接口 |
| 5 | `getAiContent(streamId)` | ✅ `GET /api/v1/ai-content` | ✅ 已实现 | |
| 6 | `addComment(contentId, text, user, avatar)` | ✅ `POST /api/comment` | ✅ 已实现 | |
| 7 | `like(contentId, commentId)` | ✅ `POST /api/like` | ✅ 已实现 | |
| 8 | `deleteComment(contentId, commentId)` | ✅ `DELETE /api/comment/{commentId}` | ✅ 已实现 | |
| 9 | `getDebateTopic(streamId)` | ✅ `GET /api/v1/debate-topic` | ✅ 已实现 | |
| 10 | `getUserVotes(streamId)` | ✅ `GET /api/v1/user-votes` | ✅ 已实现 | |
| 11 | `getLiveStatus()` | ✅ `GET /api/admin/live/status` | ✅ 已实现 | |
| 12 | `getDashboard(streamId)` | ✅ `GET /api/admin/dashboard` | ✅ 已实现 | |
| 13 | `controlLive(action, streamId)` | ✅ `POST /api/live/control` | ✅ 已实现 | |
| 14 | `getStreamsList()` | ✅ `GET /api/v1/admin/streams` | ✅ 已实现 | |
| 15 | `getVotesStatistics(streamId)` | ✅ `GET /api/v1/admin/votes/statistics` | ✅ 已实现 | |
| 16 | `getRtmpToHlsUrls(roomName)` | ❌ 未实现 | ❌ 缺失 | 需要补充 |
| 17 | `testConnection(streamId)` | ✅ 复用其他接口 | ✅ 已实现 | |
| 18 | `getWebSocketUrl()` | ⚠️ 需要网关支持 | ⚠️ 待确认 | 网关负责 |

---

## ✅ 已实现的接口（15个）

### 1. 投票系统接口
```java
// VoteController.java
GET  /api/v1/votes?stream_id={streamId}          // 获取票数统计
GET  /api/votes?stream_id={streamId}              // 获取票数（兼容版本）
POST /api/v1/user-vote                            // 用户投票
GET  /api/v1/user-votes?stream_id={streamId}&user_id={userId}  // 查询用户投票状态
```

### 2. AI内容接口
```java
// AiContentController.java
GET  /api/v1/ai-content?stream_id={streamId}      // 获取AI识别内容
GET  /api/v1/ai-content/stream/{streamId}         // 按流ID获取AI内容
GET  /api/v1/ai-content/{id}                      // 按ID获取AI内容
```

### 3. 评论系统接口
```java
// CommentController.java
POST /api/comment                                 // 添加评论
POST /api/like                                    // 点赞功能
DELETE /api/comment/{commentId}                   // 删除评论
```

### 4. 辩题管理接口
```java
// DebateTopicController.java
GET  /api/v1/debate-topic?stream_id={streamId}    // 获取辩题信息
```

### 5. 直播管理接口
```java
// AdminController.java
GET  /api/admin/live/status                       // 获取当前直播状态
GET  /api/admin/dashboard                         // 获取数据概览
POST /api/live/control                            // 控制直播（开始/停止）
GET  /api/v1/admin/streams                        // 获取直播流列表
GET  /api/v1/admin/votes/statistics               // 获取投票统计
```

---

## ❌ 缺失的接口（1个）

### RTMP转HLS接口
```javascript
// 前端调用（api-service.js 第938-965行）
async getRtmpToHlsUrls(roomName) {
    const response = await this.request({
        url: `/api/admin/rtmp/urls?room_name=${encodeURIComponent(roomName)}`,
        method: 'GET'
    });
    // 返回格式：{ push_url, play_flv, play_hls }
}
```

**需要实现**：
```java
@GetMapping("/api/admin/rtmp/urls")
public ApiResponse<Map<String, String>> getRtmpToHlsUrls(@RequestParam String room_name) {
    // 返回RTMP推流地址和播放地址
    Map<String, String> urls = new HashMap<>();
    urls.put("push_url", "rtmp://server:1935/live/" + room_name);
    urls.put("play_flv", "http://server:8080/live/" + room_name + ".flv");
    urls.put("play_hls", "http://server:8080/live/" + room_name + ".m3u8");
    return ApiResponse.success(urls);
}
```

---

## ⚠️ 需要注意的接口（2个）

### 1. WebSocket接口
```javascript
// 前端调用（api-service.js 第925-931行）
getWebSocketUrl() {
    const baseUrl = this.baseURL || API_BASE_URL;
    const wsProtocol = baseUrl.startsWith('https') ? 'wss' : 'ws';
    const wsHost = baseUrl.replace(/^https?:\/\//, '');
    return `${wsProtocol}://${wsHost}/ws`;
}
```

**说明**：WebSocket由网关（gateway.js）处理，后端无需实现。

### 2. 鉴权逻辑
```javascript
// 前端调用（api-service.js 第104-127行）
let authToken = null;
if (typeof uni !== 'undefined' && uni.getStorageSync) {
    authToken = uni.getStorageSync('authToken');
}
if (authToken) {
    defaultHeaders['Authorization'] = `Bearer ${authToken}`;
}
```

**说明**：前端会发送token，但后端暂未实现鉴权拦截器。对于Mock数据项目，可以不实现。

---

## 📋 接口参数对比

### 1. 投票接口参数

**前端发送**：
```javascript
{
    request: {
        leftVotes: 45,
        rightVotes: 55,
        streamId: "stream-001",
        stream_id: "stream-001",
        userId: "user-001",
        user_id: "user-001"
    }
}
```

**后端接收**：
```java
@Data
public class UserVoteRequest {
    private String streamId;
    private String stream_id;
    private int leftVotes;
    private int rightVotes;
    private String userId;
    private String user_id;
}
```

✅ **匹配**：已处理两种命名风格（驼峰和下划线）

### 2. 评论接口参数

**前端发送**：
```javascript
{
    contentId: "content-001",
    text: "评论内容",
    user: "匿名用户",
    avatar: "👤"
}
```

**后端接收**：
```java
@PostMapping("/api/comment")
public ApiResponse<Comment> addComment(@RequestBody Map<String, String> request) {
    String contentId = request.get("contentId");
    String text = request.get("text");
    String user = request.getOrDefault("user", "匿名用户");
    String avatar = request.getOrDefault("avatar", "👤");
    // ...
}
```

✅ **匹配**：使用Map接收，兼容性好

### 3. 直播控制接口参数

**前端发送**：
```javascript
{
    action: "start",  // 或 "stop"
    streamId: "stream-001"  // 可选
}
```

**后端接收**：
```java
@PostMapping("/api/live/control")
public ApiResponse<LiveStatus> controlLive(@RequestBody Map<String, String> request) {
    String action = request.get("action");
    // ...
}
```

✅ **匹配**：参数格式一致

---

## 📊 响应格式对比

### 前端期望的格式
```javascript
// 成功响应
{
    code: 0,
    message: "success",
    data: { ... }
}

// 错误响应
{
    code: -1,
    message: "错误信息",
    data: null
}
```

### 后端返回的格式
```java
// ApiResponse.java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(0, "success", data);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
```

✅ **完全匹配**：响应格式一致

---

## 🎯 总结

### ✅ 已满足的需求（95%）
- ✅ 15个核心接口已实现
- ✅ 接口路径匹配
- ✅ 参数格式兼容
- ✅ 响应格式统一
- ✅ 跨域配置完成

### ❌ 缺失的需求（5%）
- ❌ RTMP转HLS接口（`/api/admin/rtmp/urls`）

### ⚠️ 可选需求
- ⚠️ 鉴权逻辑（Mock项目可不实现）
- ⚠️ WebSocket（由网关处理）

---

## 🔧 建议补充的接口

### 优先级：中
```java
// 在AdminController.java中添加
@GetMapping("/api/admin/rtmp/urls")
public ApiResponse<Map<String, String>> getRtmpToHlsUrls(@RequestParam String room_name) {
    Map<String, String> urls = new HashMap<>();
    urls.put("room_name", room_name);
    urls.put("push_url", "rtmp://localhost:1935/live/" + room_name);
    urls.put("play_flv", "http://localhost:8080/live/" + room_name + ".flv");
    urls.put("play_hls", "http://localhost:8080/live/" + room_name + ".m3u8");
    return ApiResponse.success(urls);
}
```

---

**分析时间**: 2026-07-15
**结论**: 后端接口基本满足前端需求，仅需补充1个RTMP接口
