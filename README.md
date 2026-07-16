# 🎙️ Live Debate - 全栈直播辩论应用

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-green?style=flat-square&logo=springboot)
![Node.js](https://img.shields.io/badge/Node.js-22-brightgreen?style=flat-square&logo=node.js)
![Vue.js](https://img.shields.io/badge/Vue.js-3-blue?style=flat-square&logo=vue.js)
![Render](https://img.shields.io/badge/Deploy-Render-purple?style=flat-square)

**一个支持实时辩论、投票、AI内容识别的全栈直播应用**

[🚀 在线演示](https://live-fullstack-frontend.onrender.com) | [📖 API文档](#-api接口文档) | [🛠️ 本地开发](#-本地开发指南)

</div>

---

## 📋 目录

- [📌 项目简介](#-项目简介)
- [🚀 演示地址](#-演示地址)
- [🧱 技术栈](#-技术栈)
- [📁 项目结构](#-项目结构)
- [🔗 API接口文档](#-api接口文档)
- [🛠️ 本地开发指南](#-本地开发指南)
- [☁️ 部署指南](#-部署指南)
- [🐛 部署问题与解决方案](#-部署问题与解决方案)
- [📝 开发笔记](#-开发笔记)
- [👤 个人介绍](#-个人介绍)

---

## 📌 项目简介

**Live Debate** 是一个全栈直播辩论应用，支持：

- 🎥 **实时直播** - 观看辩论直播流
- 🗳️ **实时投票** - 观众可以为正反方投票
- 🤖 **AI内容识别** - 自动识别和展示辩论要点
- 💬 **互动评论** - 观众可以评论和点赞
- 📊 **数据统计** - 实时显示投票数据和观众统计

### 核心功能

```
┌─────────────────────────────────────────────────────────────┐
│                      前端 (Vue3 + uni-app)                    │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐    │
│  │ 直播播放  │  │ 实时投票  │  │ AI内容   │  │ 互动评论  │    │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘    │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    网关 (Node.js + Express)                   │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐    │
│  │ API路由   │  │ WebSocket│  │ CORS处理  │  │ 请求代理  │    │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘    │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   后端 (Spring Boot)                         │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐    │
│  │ 投票系统  │  │ AI内容   │  │ 辩题管理  │  │ Mock数据  │    │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘    │
└─────────────────────────────────────────────────────────────┘
```

---

## 🚀 演示地址

| 服务 | 地址 | 状态 | 说明 |
|------|------|------|------|
| 🖥️ **前端** | [live-fullstack-frontend.onrender.com](https://live-fullstack-frontend.onrender.com) | ✅ 运行中 | H5版本，可直接在浏览器访问 |
| 🌐 **网关** | [live-fullstack-gateway.onrender.com](https://live-fullstack-gateway.onrender.com) | ✅ 运行中 | API网关服务 |
| ⚙️ **后端** | [live-fullstack-backend.onrender.com](https://live-fullstack-backend.onrender.com) | ✅ 运行中 | Java后端服务 |

### 快速体验

1. 访问 [前端地址](https://live-fullstack-frontend.onrender.com)
2. 点击 **"一键闪电登录"** 按钮
3. 选择直播间进入
4. 体验投票、查看AI内容等功能

---

## 🧱 技术栈

### 后端

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 (LTS) | 编程语言 |
| Spring Boot | 4.1.0 | 应用框架 |
| Maven | 3.9.16 | 构建工具 |
| Lombok | - | 简化代码 |

### 前端

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue.js | 3.x | 前端框架 |
| uni-app | - | 跨平台框架 |
| uni-ui | - | UI组件库 |

### 网关

| 技术 | 版本 | 说明 |
|------|------|------|
| Node.js | 22.x | 运行时 |
| Express | 4.18.x | Web框架 |
| WebSocket (ws) | 8.x | 实时通信 |
| http-proxy-middleware | 2.x | 请求代理 |

### 部署

| 服务 | 平面 | 说明 |
|------|------|------|
| Render | - | 云服务平台 |
| Git | - | 版本控制 |
| GitHub | - | 代码托管 |

---

## 📁 项目结构

```
live-fullstack-demo/
├── 📂 frontend/                    # 前端项目
│   ├── 📂 dist/build/h5/           # 预构建的H5产物
│   │   ├── index.html              # 入口文件
│   │   └── assets/                 # 静态资源(JS/CSS/图片)
│   ├── 📂 pages/                   # 页面组件
│   │   ├── index/                  # 登录页
│   │   ├── live-select/            # 直播选择页
│   │   └── home/                   # 主页面
│   ├── 📂 utils/                   # 工具函数
│   │   ├── api-service.js          # API服务封装
│   │   └── api-interceptor.js      # 请求拦截器
│   ├── 📂 config/                  # 配置文件
│   │   └── server-mode.js          # 服务器配置
│   ├── 📂 static/                  # 静态资源
│   ├── server.js                   # Express服务器
│   └── package.json                # 依赖配置
│
├── 📂 gateway/                     # 网关项目
│   ├── gateway.js                  # 主入口(3600+行)
│   ├── 📂 admin/                   # 管理后台
│   │   └── db.js                   # 内存数据库
│   └── package.json
│
├── 📂 backend/                     # 后端项目
│   └── 📂 LiveFullstack/
│       └── 📂 src/main/java/com/LiveFullstack/
│           ├── 📂 controller/      # 控制器层
│           │   ├── VoteController.java
│           │   ├── AiContentController.java
│           │   ├── DebateTopicController.java
│           │   ├── CommentController.java
│           │   └── AdminController.java
│           ├── 📂 model/           # 数据模型
│           │   ├── Vote.java
│           │   ├── AiContent.java
│           │   ├── DebateTopic.java
│           │   ├── Comment.java
│           │   ├── Stream.java
│           │   └── Dashboard.java
│           ├── 📂 util/            # 工具类
│           │   ├── ApiResponse.java
│           │   └── MockDataGenerator.java
│           └── 📂 config/          # 配置类
│               ├── CorsConfig.java
│               └── GlobalExceptionHandler.java
│
├── CLAUDE.md                       # Claude配置文件
├── README.md                       # 项目说明文档
└── 测试题目.md                     # 测试题目要求
```

---

## 🔗 API接口文档

### 基础信息

- **基础URL**: `https://live-fullstack-gateway.onrender.com`
- **认证方式**: 无需认证（Mock模式）
- **数据格式**: JSON

### 投票系统

#### 获取投票统计
```
GET /api/v1/votes?stream_id={streamId}
```

**响应示例**:
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

#### 用户投票
```
POST /api/v1/user-vote
```

**请求体**:
```json
{
  "leftVotes": 60,
  "rightVotes": 40,
  "userId": "user_123",
  "streamId": "stream-001"
}
```

### AI内容

#### 获取AI识别内容
```
GET /api/v1/ai-content?stream_id={streamId}
```

### 辩题管理

#### 获取当前辩题
```
GET /api/v1/debate-topic?stream_id={streamId}
```

**响应示例**:
```json
{
  "success": true,
  "data": {
    "id": "debate-default-001",
    "title": "如果有一个能一键消除痛苦的按钮，你会按吗？",
    "description": "这是一个关于痛苦、成长与人性选择的深度辩论",
    "leftSide": "按下去，消除痛苦",
    "rightSide": "不按，痛苦是成长的一部分"
  }
}
```

### 评论系统

#### 添加评论
```
POST /api/comment
```

#### 点赞
```
POST /api/like
```

### 直播管理

#### 获取直播状态
```
GET /api/admin/live/status
```

#### 控制直播
```
POST /api/live/control
```

**请求体**:
```json
{
  "action": "start",
  "streamId": "stream-001"
}
```

### 完整接口列表

| 功能 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 投票统计 | GET | /api/v1/votes | 获取正反方投票数 |
| 用户投票 | POST | /api/v1/user-vote | 提交投票 |
| 用户投票记录 | GET | /api/v1/user-votes | 查询投票状态 |
| AI内容 | GET | /api/v1/ai-content | 获取AI识别内容 |
| 辩题信息 | GET | /api/v1/debate-topic | 获取当前辩题 |
| 添加评论 | POST | /api/comment | 添加评论 |
| 点赞 | POST | /api/like | 点赞内容/评论 |
| 删除评论 | DELETE | /api/comment/{id} | 删除评论 |
| 直播状态 | GET | /api/admin/live/status | 获取直播状态 |
| 数据概览 | GET | /api/admin/dashboard | 获取统计数据 |
| 控制直播 | POST | /api/live/control | 开始/停止直播 |
| 直播流列表 | GET | /api/v1/admin/streams | 获取直播流 |
| 投票统计详情 | GET | /api/v1/admin/votes/statistics | 详细投票统计 |

---

## 🛠️ 本地开发指南

### 环境要求

- **Java**: 17+
- **Node.js**: 18+
- **Maven**: 3.8+

### 启动后端

```bash
# 进入后端目录
cd backend/LiveFullstack

# 编译并启动
mvn spring-boot:run

# 后端运行在 http://localhost:8081
```

### 启动网关

```bash
# 进入网关目录
cd gateway

# 安装依赖
npm install

# 启动服务
npm start

# 网关运行在 http://localhost:8080
```

### 启动前端

```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install

# 启动H5开发模式
npm run dev:h5

# 前端运行在 http://localhost:8080
```

### 测试API

```bash
# 测试后端
curl http://localhost:8081/api/v1/votes

# 测试网关
curl http://localhost:8080/api/v1/votes
```

---

## ☁️ 部署指南

### 部署架构

```
┌─────────────────────────────────────────────────────────────┐
│                        Render Cloud                         │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────┐ │
│  │   Frontend      │  │    Gateway      │  │   Backend   │ │
│  │  (Web Service)  │  │  (Web Service)  │  │ (Web Service)│ │
│  │  Port: auto     │  │  Port: auto     │  │ Port: auto  │ │
│  └─────────────────┘  └─────────────────┘  └─────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### 部署步骤

#### 1. 部署后端

1. 在 Render 创建 Web Service
2. 连接 GitHub 仓库
3. 配置:
   - **Root Directory**: `backend/LiveFullstack`
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/*.jar`
   - **Environment**: `USE_MOCK=true`

#### 2. 部署网关

1. 在 Render 创建 Web Service
2. 连接 GitHub 仓库
3. 配置:
   - **Root Directory**: `gateway`
   - **Build Command**: `npm install`
   - **Start Command**: `npm start`
   - **Environment**:
     - `USE_MOCK=true`
     - `BACKEND_SERVER_URL=https://live-fullstack-backend.onrender.com`

#### 3. 部署前端

1. 在 Render 创建 Web Service
2. 连接 GitHub 仓库
3. 配置:
   - **Root Directory**: `frontend`
   - **Build Command**: `npm install`
   - **Start Command**: `npm start`
   - **Environment**: `USE_MOCK=true`

### 环境变量说明

| 变量名 | 说明 | 示例值 |
|--------|------|--------|
| `USE_MOCK` | 是否使用Mock数据 | `true` |
| `PORT` | 服务端口（Render自动设置） | `10000` |
| `BACKEND_SERVER_URL` | 后端服务地址 | `https://xxx.onrender.com` |
| `NODE_ENV` | Node环境 | `production` |

---

## 🐛 部署问题与解决方案

### 问题1: uni-app H5构建失败

**现象**:
```
Error: ENOENT: no such file or directory, open 'src/manifest.json'
```

**原因**: uni-app CLI项目需要 `src` 目录结构，但项目是 HBuilderX 格式

**解决方案**:
- 使用预构建的 `dist` 目录
- Build Command 只需 `npm install`，不需要 `npm run build:h5`

---

### 问题2: npm install 跳过 devDependencies

**现象**:
```
Error: Cannot find module '@dcloudio/vite-plugin-uni'
```

**原因**: `NODE_ENV=production` 导致 `npm install` 跳过 devDependencies

**解决方案**:
```bash
# Build Command 改为
npm install --include=dev
```

---

### 问题3: 前端页面空白

**现象**: 页面加载但显示空白，控制台无错误

**原因**: 构建产物只有1.5KB（空壳），页面组件未打包

**解决方案**:
- 确保 `src` 目录包含所有必要文件（pages, utils, config, static）
- 设置 `UNI_INPUT_DIR=./src` 环境变量
- 或使用预构建的完整 dist（224KB）

---

### 问题4: API返回404

**现象**: 前端请求返回 `404 Not Found`

**原因**: 网关缺少 v1 版本的路由

**解决方案**: 在网关添加 v1 路由别名
```javascript
app.get('/api/v1/admin/streams', (req, res) => {
  req.url = '/api/admin/streams';
  app.handle(req, res);
});
```

---

### 问题5: 后端API格式不匹配

**现象**: 前端无法正确显示数据

**原因**: 后端返回格式与前端期望不一致

**解决方案**: 修改后端Controller返回格式
```java
// 前端期望: { success: true, data: { leftVotes, rightVotes } }
// 而不是: { code: 200, data: [...数组] }
```

---

### 问题6: H5模式登录失败

**现象**: 点击登录按钮提示"微信登录功能仅在微信小程序环境中可用"

**原因**: H5模式不支持微信登录API

**解决方案**: 添加 H5 模式登录绕过逻辑
```javascript
// #ifndef MP-WEIXIN
// H5环境：跳过微信登录，直接进入
const mockUser = { id: 'h5_user_' + Date.now(), ... };
uni.setStorageSync('currentUser', mockUser);
uni.redirectTo({ url: '/pages/live-select/live-select' });
// #endif
```

---

### 问题7: Render部署后静态文件404

**现象**: 页面返回 `404 Not Found`

**原因**: `dist` 目录在 `.gitignore` 中被忽略

**解决方案**:
1. 从 `.gitignore` 移除 `/dist/`
2. 使用 `git add -f frontend/dist/` 强制添加
3. 提交 dist 到 Git

---

## 📝 开发笔记

### 实现思路

1. **需求分析**
   - 阅读前端代码，提取所有 API 调用
   - 整理接口清单和数据格式要求

2. **后端设计**
   - 设计数据模型（Vote, AiContent, DebateTopic 等）
   - 实现 Mock 数据生成器（MockDataGenerator）
   - 实现 Controller 层，确保返回格式匹配

3. **网关配置**
   - 添加 v1 版本路由别名
   - 配置 CORS 跨域支持
   - 配置 WebSocket 实时通信

4. **部署调试**
   - 解决 uni-app 构建兼容性问题
   - 修复 API 路由不匹配问题
   - 优化前端 H5 模式体验

### Mock数据设计

```java
// 内存数据库使用 ConcurrentHashMap
private final ConcurrentHashMap<Long, Vote> votes = new ConcurrentHashMap<>();
private final ConcurrentHashMap<Long, AiContent> aiContents = new ConcurrentHashMap<>();

// 初始化示例数据
@PostConstruct
public void init() {
    initSampleVotes();      // 8条投票记录
    initSampleAiContents(); // 3条AI内容
    initSampleDashboards(); // 3个仪表盘数据
}
```

### API格式统一

```java
// 统一响应格式
@Data
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }
}
```

---

## 👤 个人介绍

### 技术背景简介

#### 主语言与核心栈

- **主力语言**: Java 17，熟练 Spring Boot / Spring Cloud Alibaba 微服务生态
- **辅助语言**: Python（用于 LLM Agent 开发），前端能写 Vue 3 + TypeScript 基础页面

#### 擅长方向

**Java 后端工程化**
- 政企安全运营平台项目经验
- 擅长接口性能优化、通用抽象封装、批量导入链路设计

**Agent / LLM 应用开发**
- DataOcean 项目核心开发
- 基于 LangGraph 编排 NL2SQL 全链路（Query Rewrite → Schema RAG → SQL 生成 → 安全沙箱 → 可视化）
- 熟悉 RAG 检索增强、SSE 流式输出、sqlglot AST 安全校验

**数据与中间件**
- MySQL 索引 / 事务优化
- Redis 缓存策略
- Kafka/Doris 大数据组件
- GaussDB 国产化适配

#### 学习与求职目标

- 深耕 **Java 后端** 与 **AI Agent 开发** 两个方向
- 目标岗位：Java 后端开发 / AI 智能体开发，倾向上海地区
- 持续深入：分布式架构、LangGraph 高级机制、RAG 工程化落地、SQL 安全治理

### 本项目贡献

在 Live Debate 项目中，我负责：

1. ✅ **后端API开发** - 基于 Spring Boot 实现完整的 Mock 数据接口
2. ✅ **格式适配** - 确保 API 返回格式与前端期望一致
3. ✅ **网关配置** - 添加 v1 路由别名，解决前后端通信问题
4. ✅ **部署上线** - 使用 Render 平台完成全栈部署

---

## 📄 许可证

MIT License

---

<div align="center">

**⭐ 如果这个项目对你有帮助，请给个Star！⭐**

Made with ❤️ by [muyou](https://github.com/TTlook111)

</div>
