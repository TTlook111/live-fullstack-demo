# 📋 任务书 - 全栈直播应用后端开发

## 📌 项目概述

**项目名称**: live-fullstack-demo
**项目类型**: 后端岗位线上测试题
**技术栈**: Java 17 + Spring Boot + Maven + Mock数据
**GitHub仓库**: https://github.com/TTlook111/live-fullstack-demo.git

---

## 🎯 总体目标

1. 开发一个完整的后端服务（Spring Boot）
2. 使用Mock数据模拟真实业务逻辑
3. 实现前端所需的所有接口
4. 将前端、网关、后端部署上线
5. 提供在线演示地址

---

## 📅 任务流程（共7个阶段）

### 阶段1：项目初始化与环境配置 ✅ 已完成
**目标**: 搭建项目基础结构，配置开发环境

**任务清单**:
- [x] 1.1 创建GitHub仓库
- [x] 1.2 下载前端项目（ZIP）
- [x] 1.3 下载网关项目（ZIP）
- [x] 1.4 初始化Git仓库
- [x] 1.5 配置.gitignore文件
- [x] 1.6 分析前端接口需求
- [x] 1.7 创建CLAUDE.md开发指南
- [x] 1.8 创建任务书（本文档）

**提交记录**: `feat: 初始化项目结构，添加前端和网关项目`

---

### 阶段2：后端项目搭建 ⏳ 进行中
**目标**: 创建Spring Boot项目框架，配置基础环境

**任务清单**:
- [ ] 2.1 创建Spring Boot项目（已创建基础框架）
- [ ] 2.2 配置application.yml
  - 设置端口为8081
  - 配置跨域（CORS）
  - 配置日志级别
- [ ] 2.3 创建项目包结构
  ```
  com.LiveFullstack
  ├── controller/      # 控制器层
  ├── service/         # 业务逻辑层
  ├── model/           # 数据模型层
  ├── config/          # 配置类
  └── util/            # 工具类
  ```
- [ ] 2.4 创建统一响应格式类 `ApiResponse`
- [ ] 2.5 创建全局异常处理器
- [ ] 2.6 配置CORS跨域
- [ ] 2.7 测试项目启动

**提交记录**: `feat: 搭建Spring Boot项目框架，配置基础环境`

**代码开发详情**:

#### 2.2 配置application.yml
```yaml
server:
  port: 8081

spring:
  application:
    name: live-fullstack-backend

# 日志配置
logging:
  level:
    com.LiveFullstack: DEBUG
```

#### 2.4 统一响应格式类
```java
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

#### 2.6 CORS配置类
```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

---

### 阶段3：Mock数据模型开发
**目标**: 创建数据模型和Mock数据生成器

**任务清单**:
- [ ] 3.1 创建投票数据模型 `Vote`
- [ ] 3.2 创建辩题数据模型 `DebateTopic`
- [ ] 3.3 创建AI内容数据模型 `AiContent`
- [ ] 3.4 创建评论数据模型 `Comment`
- [ ] 3.5 创建直播流数据模型 `Stream`
- [ ] 3.6 创建直播状态数据模型 `LiveStatus`
- [ ] 3.7 创建Mock数据生成器 `MockDataGenerator`
- [ ] 3.8 测试数据生成

**提交记录**: `feat: 创建数据模型和Mock数据生成器`

**代码开发详情**:

#### 3.1 投票数据模型
```java
@Data
public class Vote {
    private String streamId;
    private int leftVotes;
    private int rightVotes;
    private int totalVotes;
    private LocalDateTime updateTime;
}
```

#### 3.2 辩题数据模型
```java
@Data
public class DebateTopic {
    private String id;
    private String title;
    private String description;
    private String leftSide;
    private String rightSide;
    private String leftPosition;
    private String rightPosition;
}
```

#### 3.3 AI内容数据模型
```java
@Data
public class AiContent {
    private String id;
    private String streamId;
    private String content;
    private String type;
    private LocalDateTime createTime;
}
```

#### 3.4 评论数据模型
```java
@Data
public class Comment {
    private String id;
    private String contentId;
    private String text;
    private String user;
    private String avatar;
    private int likes;
    private LocalDateTime createTime;
}
```

#### 3.5 直播流数据模型
```java
@Data
public class Stream {
    private String id;
    private String name;
    private String status;
    private String streamUrl;
    private LocalDateTime startTime;
}
```

#### 3.7 Mock数据生成器
```java
@Component
public class MockDataGenerator {
    // 生成投票数据
    public Vote generateVote(String streamId) { ... }

    // 生成辩题数据
    public DebateTopic generateDebateTopic(String streamId) { ... }

    // 生成AI内容数据
    public List<AiContent> generateAiContent(String streamId) { ... }

    // 生成评论数据
    public List<Comment> generateComments(String contentId) { ... }

    // 生成直播流数据
    public List<Stream> generateStreams() { ... }
}
```

---

### 阶段4：接口开发
**目标**: 实现前端所需的所有接口

**任务清单**:

#### 4.1 投票系统接口（核心）
- [ ] 4.1.1 `GET /api/v1/votes` - 获取票数统计
- [ ] 4.1.2 `POST /api/v1/user-vote` - 用户投票
- [ ] 4.1.3 `GET /api/v1/user-votes` - 查询用户投票状态

#### 4.2 AI内容接口
- [ ] 4.2.1 `GET /api/v1/ai-content` - 获取AI识别内容

#### 4.3 评论系统接口
- [ ] 4.3.1 `POST /api/comment` - 添加评论
- [ ] 4.3.2 `POST /api/like` - 点赞功能
- [ ] 4.3.3 `DELETE /api/comment/{commentId}` - 删除评论

#### 4.4 辩题管理接口
- [ ] 4.4.1 `GET /api/v1/debate-topic` - 获取辩题信息

#### 4.5 直播管理接口
- [ ] 4.5.1 `GET /api/admin/live/status` - 获取当前直播状态
- [ ] 4.5.2 `GET /api/admin/dashboard` - 获取数据概览
- [ ] 4.5.3 `POST /api/live/control` - 控制直播（开始/停止）
- [ ] 4.5.4 `GET /api/v1/admin/streams` - 获取直播流列表
- [ ] 4.5.5 `GET /api/v1/admin/votes/statistics` - 获取投票统计

**提交记录**: `feat: 实现所有后端接口`

**代码开发详情**:

#### 4.1.1 获取票数统计
```java
@RestController
@RequestMapping("/api/v1")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @GetMapping("/votes")
    public ApiResponse<Vote> getVotes(@RequestParam String stream_id) {
        Vote vote = voteService.getVotes(stream_id);
        return ApiResponse.success(vote);
    }
}
```

#### 4.1.2 用户投票
```java
@PostMapping("/user-vote")
public ApiResponse<Vote> userVote(@RequestBody UserVoteRequest request) {
    Vote vote = voteService.userVote(
        request.getStreamId(),
        request.getLeftVotes(),
        request.getRightVotes(),
        request.getUserId()
    );
    return ApiResponse.success(vote);
}
```

#### 4.5.2 获取数据概览
```java
@GetMapping("/admin/dashboard")
public ApiResponse<Dashboard> getDashboard() {
    Dashboard dashboard = dashboardService.getDashboard();
    return ApiResponse.success(dashboard);
}
```

---

### 阶段5：本地测试与联调
**目标**: 确保所有服务正常运行，接口联调成功

**任务清单**:
- [ ] 5.1 启动后端服务（8081端口）
- [ ] 5.2 启动网关服务（8080端口）
- [ ] 5.3 启动前端项目
- [ ] 5.4 测试投票系统接口
- [ ] 5.5 测试AI内容接口
- [ ] 5.6 测试评论系统接口
- [ ] 5.7 测试辩题管理接口
- [ ] 5.8 测试直播管理接口
- [ ] 5.9 测试WebSocket连接
- [ ] 5.10 修复联调问题

**提交记录**: `fix: 修复联调问题，优化接口响应`

**测试命令**:
```bash
# 测试后端接口
curl http://localhost:8081/api/v1/votes?stream_id=test
curl http://localhost:8081/api/v1/debate-topic?stream_id=test
curl http://localhost:8081/api/admin/dashboard

# 测试网关接口
curl http://localhost:8080/api/v1/votes?stream_id=test
curl http://localhost:8080/api/v1/debate-topic?stream_id=test
```

---

### 阶段6：部署上线
**目标**: 将所有服务部署到云端，提供在线演示地址

**任务清单**:
- [ ] 6.1 选择部署平台
  - 前端：Vercel 或 Cloudflare Pages
  - 后端+网关：Railway 或 Render
- [ ] 6.2 部署前端项目
- [ ] 6.3 部署网关项目
- [ ] 6.4 部署后端项目
- [ ] 6.5 配置域名和端口
- [ ] 6.6 测试在线访问
- [ ] 6.7 配置HTTPS证书

**提交记录**: `deploy: 部署项目到云端`

**部署步骤**:

#### 6.2 部署前端到Vercel
1. 登录 Vercel（https://vercel.com）
2. 连接GitHub仓库
3. 选择 `frontend` 目录
4. 配置构建命令：`npm run build`
5. 配置输出目录：`dist`
6. 部署

#### 6.3 部署网关到Railway
1. 登录 Railway（https://railway.app）
2. 连接GitHub仓库
3. 选择 `gateway` 目录
4. 配置启动命令：`npm start`
5. 配置端口：8080
6. 部署

#### 6.4 部署后端到Railway
1. 在Railway创建新项目
2. 连接GitHub仓库
3. 选择 `backend/LiveFullstack` 目录
4. 配置启动命令：`java -jar target/*.jar`
5. 配置端口：8081
6. 部署

---

### 阶段7：文档完善与提交
**目标**: 完善项目文档，准备最终提交

**任务清单**:
- [ ] 7.1 创建README.md
  - 项目名称和简介
  - 演示地址
  - 技术栈说明
  - 项目结构说明
  - 接口文档
  - 开发笔记
  - 个人介绍
- [ ] 7.2 编写接口文档
- [ ] 7.3 编写部署文档
- [ ] 7.4 编写开发笔记
- [ ] 7.5 最终测试
- [ ] 7.6 最终提交

**提交记录**: `docs: 完善项目文档`

**README.md 结构**:
```markdown
# 全栈直播应用后端服务

## 📌 项目简介
简短描述项目背景和目标

## 🚀 演示地址
- 前端访问地址：https://xxx.vercel.app
- 后端API地址：https://xxx.railway.app

## 🛠️ 技术栈
- 后端：Java 17 + Spring Boot
- 前端：Vue.js 3 + uni-app
- 网关：Node.js + Express
- 部署：Vercel + Railway

## 📁 项目结构
描述目录结构

## 📚 接口文档
列出所有接口

## 📝 开发笔记
描述开发过程、遇到的问题和解决方案

## 👤 个人介绍
简短介绍自己
```

---

## 📊 进度跟踪

| 阶段 | 状态 | 预计时间 | 实际时间 | 备注 |
|------|------|----------|----------|------|
| 阶段1：项目初始化 | ✅ 已完成 | 1小时 | - | |
| 阶段2：后端项目搭建 | ⏳ 进行中 | 2小时 | - | |
| 阶段3：Mock数据模型 | ⏳ 待开始 | 2小时 | - | |
| 阶段4：接口开发 | ⏳ 待开始 | 4小时 | - | |
| 阶段5：本地测试 | ⏳ 待开始 | 2小时 | - | |
| 阶段6：部署上线 | ⏳ 待开始 | 3小时 | - | |
| 阶段7：文档完善 | ⏳ 待开始 | 2小时 | - | |

**总预计时间**: 16小时

---

## ⚠️ 注意事项

### 1. 代码提交规范
- 每次功能完成后立即提交
- 提交信息格式：`类型: 简短描述`
- 类型包括：`feat`（新功能）、`fix`（修复）、`docs`（文档）、`refactor`（重构）

### 2. 代码质量要求
- 代码结构清晰
- 命名规范
- 添加必要的注释
- 遵循Java编码规范

### 3. Mock数据要求
- 数据要真实合理
- 结构要与前端期望一致
- 需要考虑边界情况
- 使用内存存储，不使用数据库

### 4. 部署注意事项
- 确保端口配置正确
- 配置跨域（CORS）
- 配置HTTPS证书
- 测试在线访问

---

## 📚 参考资源

- [Spring Boot官方文档](https://spring.io/projects/spring-boot)
- [Maven官方文档](https://maven.apache.org/)
- [Vercel部署文档](https://vercel.com/docs)
- [Railway部署文档](https://docs.railway.app/)
- [测试题目要求](测试题目.md)

---

**创建时间**: 2026-07-15
**最后更新**: 2026-07-15
**维护者**: 开发者
