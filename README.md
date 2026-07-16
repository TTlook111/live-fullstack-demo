# 🎙️ Live Debate - 全栈直播辩论应用

## 📌 基本信息

**项目名称**: Live Debate (直播辩论应用)
**项目类型**: 全栈直播应用后端服务
**GitHub仓库**: https://github.com/TTlook111/live-fullstack-demo.git

## 🚀 演示地址

| 服务 | 地址 | 说明 |
|------|------|------|
| **前端访问地址** | https://live-fullstack-frontend.onrender.com | H5版本，可直接在浏览器访问 |
| **网关API地址** | https://live-fullstack-gateway.onrender.com | API网关服务 |
| **后端API地址** | https://live-fullstack-backend.onrender.com | Java后端服务 |

## 🧱 技术栈说明

### 后端技术栈
- **语言**: Java 17 (LTS)
- **框架**: Spring Boot 4.1.0
- **构建工具**: Maven 3.9.16
- **数据方案**: Mock数据（内存模拟，不使用数据库）

### 前端技术栈
- **框架**: Vue.js 3 + uni-app
- **构建目标**: H5（Web版本）
- **UI组件**: uni-ui

### 网关技术栈
- **运行时**: Node.js 22
- **框架**: Express.js
- **功能**: API路由、WebSocket支持、CORS处理

### Mock数据生成方案
- 使用Java代码手动创建测试数据
- 内存中维护数据状态（ConcurrentHashMap）
- 支持CRUD操作的完整模拟

### 部署平台与方式
- **平台**: Render (https://render.com)
- **前端**: Web Service (Node.js运行时)
- **后端**: Web Service (Node.js运行时，Java通过Maven构建)
- **网关**: Web Service (Node.js运行时)
- **自动部署**: 推送到main分支自动触发部署

## 🔗 项目结构与接口说明

### 后端目录结构
```
backend/LiveFullstack/
└── src/main/java/com/LiveFullstack/
    ├── controller/
    │   ├── VoteController.java        # 投票接口
    │   ├── AiContentController.java   # AI内容接口
    │   ├── DebateTopicController.java # 辩题接口
    │   ├── CommentController.java     # 评论接口
    │   └── AdminController.java       # 管理接口
    ├── model/
    │   ├── Vote.java                  # 投票模型
    │   ├── AiContent.java             # AI内容模型
    │   ├── DebateTopic.java           # 辩题模型
    │   ├── Comment.java               # 评论模型
    │   ├── Stream.java                # 直播流模型
    │   └── Dashboard.java             # 仪表盘模型
    ├── util/
    │   ├── ApiResponse.java           # 统一响应格式
    │   └── MockDataGenerator.java     # Mock数据生成器
    └── config/
        ├── CorsConfig.java            # 跨域配置
        └── GlobalExceptionHandler.java # 全局异常处理
```

### 主要接口列表

| 功能 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 获取投票统计 | GET | /api/v1/votes | 返回正反方投票数和百分比 |
| 用户投票 | POST | /api/v1/user-vote | 提交投票（left/right） |
| 获取用户投票 | GET | /api/v1/user-votes | 查询用户投票记录 |
| 获取AI内容 | GET | /api/v1/ai-content | 获取AI识别的辩论内容 |
| 获取辩题 | GET | /api/v1/debate-topic | 获取当前辩论话题 |
| 添加评论 | POST | /api/comment | 为AI内容添加评论 |
| 点赞 | POST | /api/like | 为内容或评论点赞 |
| 删除评论 | DELETE | /api/comment/{id} | 删除指定评论 |
| 获取直播状态 | GET | /api/admin/live/status | 获取直播是否正在进行 |
| 获取数据概览 | GET | /api/admin/dashboard | 获取统计数据和辩题信息 |
| 控制直播 | POST | /api/live/control | 开始/停止直播 |
| 获取直播流列表 | GET | /api/v1/admin/streams | 获取所有直播流 |
| 获取投票统计 | GET | /api/v1/admin/votes/statistics | 获取详细投票统计 |

### 响应格式示例
```json
{
  "success": true,
  "data": {
    "leftVotes": 65,
    "rightVotes": 45,
    "totalVotes": 110,
    "leftPercentage": 59,
    "rightPercentage": 41
  }
}
```

## 🧠 项目开发过程笔记

### 实现思路
1. **分析前端需求**: 阅读前端代码，提取所有API调用，整理接口清单
2. **设计数据模型**: 根据前端期望的数据格式设计Java模型类
3. **实现Mock数据**: 使用ConcurrentHashMap在内存中模拟数据库
4. **实现Controller**: 按照前端期望的格式实现所有接口
5. **配置网关路由**: 在网关添加v1版本路由，确保前端请求能正确转发
6. **部署上线**: 使用Render平台部署三个服务

### 遇到的问题与解决方案

1. **前端API格式不匹配**
   - 问题: 后端返回 `{ code: 200, data: [...] }` 但前端期望 `{ success: true, data: { leftVotes, rightVotes } }`
   - 解决: 修改Controller返回格式，添加统计数据计算逻辑

2. **网关缺少v1路由**
   - 问题: 前端调用 `/api/v1/admin/streams` 但网关只有 `/api/admin/streams`
   - 解决: 在网关添加所有缺失的v1版本路由

3. **uni-app H5构建问题**
   - 问题: uni-app CLI构建与HBuilderX项目结构不兼容
   - 解决: 使用预构建的dist目录，跳过Render上的构建步骤

4. **H5模式登录问题**
   - 问题: H5模式下微信登录不可用
   - 解决: 添加bypassWechatLogin逻辑，跳过微信登录直接进入

### 本地联调经验
- 后端默认运行在8081端口
- 网关运行在8080端口，代理API请求到后端
- 使用curl测试API: `curl http://localhost:8081/api/votes`

### 部署步骤与踩坑记录
1. **Render部署顺序**: 先部署后端 → 再部署网关 → 最后部署前端
2. **环境变量**: 后端和网关需要设置 `USE_MOCK=true`
3. **构建命令**: 前端不需要构建，直接使用预构建的dist
4. **端口配置**: Render自动设置PORT环境变量

## 🧍 个人介绍

我是一名后端开发工程师，擅长Java Spring Boot框架，有丰富的API设计和开发经验。本次项目中，我负责：
- 设计和实现后端Mock数据接口
- 确保API格式与前端期望一致
- 配置网关路由实现前后端通信
- 完成全栈项目的部署上线

**技术栈**: Java, Spring Boot, Node.js, Express, Vue.js
**学习目标**: 深入学习微服务架构和云原生部署
