# CLAUDE.md - 项目开发指南

## 📌 项目概述

**项目名称**: live-fullstack-demo
**项目类型**: 后端岗位线上测试题 - 全栈直播应用后端服务
**GitHub仓库**: https://github.com/TTlook111/live-fullstack-demo.git

## 🛠️ 技术栈

### 后端（已部署）
- **语言**: Java 17
- **框架**: Spring Boot 4.1.0
- **构建工具**: Maven 3.9.16
- **数据方案**: Mock数据（假数据，不使用数据库）
- **项目位置**: `/backend/LiveFullstack/`
- **部署地址**: https://live-fullstack-backend.onrender.com

### 前端（已部署）
- **框架**: Vue.js 3 + uni-app (H5版本)
- **位置**: `/frontend/`
- **部署地址**: https://live-fullstack-frontend.onrender.com

### 网关（已部署）
- **框架**: Node.js + Express
- **位置**: `/gateway/`
- **部署地址**: https://live-fullstack-gateway.onrender.com

## 📁 项目结构

```
live-fullstack-demo/
├── frontend/              # 前端项目（Vue3 + uni-app H5）
│   ├── dist/build/h5/     # 预构建的H5产物
│   ├── pages/             # 页面组件
│   ├── utils/             # 工具函数
│   └── config/            # 配置文件
├── gateway/               # 网关项目（Node.js + Express）
│   ├── gateway.js         # 主入口
│   └── admin/             # 管理后台数据
├── backend/               # 后端项目（Spring Boot）
│   └── LiveFullstack/
│       └── src/main/java/com/LiveFullstack/
│           ├── controller/  # 控制器
│           ├── model/       # 数据模型
│           ├── service/     # 服务层
│           └── util/        # 工具类
├── .gitignore
├── CLAUDE.md              # 本文件
├── README.md              # 项目说明文档
└── 测试题目.md            # 测试题目要求
```

## 🚀 部署信息

### 服务地址
| 服务 | 地址 | 说明 |
|------|------|------|
| 前端 | https://live-fullstack-frontend.onrender.com | H5版本，可直接访问 |
| 网关 | https://live-fullstack-gateway.onrender.com | API网关 |
| 后端 | https://live-fullstack-backend.onrender.com | Java后端服务 |

### 环境变量配置
- **USE_MOCK=true**: 使用Mock数据模式
- **BACKEND_SERVER_URL**: 后端服务地址（网关配置）

## 🔧 开发环境配置

### Java环境
- **Java版本**: Java 17 (LTS)
- **Java路径**: `D:\tool\Java17`
- **JAVA_HOME**: `D:\tool\Java17`

### Maven环境
- **Maven版本**: 3.9.16
- **Maven路径**: `D:\tool\apache-maven-3.9.16`

### Node.js环境
- **Node.js版本**: 22.22.2
- **npm版本**: 10.9.7

### 端口配置
- **网关**: 8080端口（本地开发）
- **后端**: 8081端口（本地开发）

## 🎯 核心需求

### 1. Mock数据（假数据）
- **不使用数据库**
- **所有数据都是内存中的假数据**
- **使用Java代码模拟数据生成**
- **可以使用Faker库或手动创建测试数据**

### 2. 需要实现的接口

#### 投票系统（核心）
```
GET  /api/v1/votes?stream_id={streamId}          # 获取票数统计
POST /api/v1/user-vote                            # 用户投票
GET  /api/v1/user-votes?stream_id={streamId}&user_id={userId}  # 查询用户投票状态
```

#### AI内容
```
GET  /api/v1/ai-content?stream_id={streamId}      # 获取AI识别内容
```

#### 评论系统
```
POST /api/comment                                 # 添加评论
POST /api/like                                    # 点赞功能
DELETE /api/comment/{commentId}                   # 删除评论
```

#### 辩题管理
```
GET  /api/v1/debate-topic?stream_id={streamId}    # 获取辩题信息
```

#### 直播管理
```
GET  /api/admin/live/status                       # 获取当前直播状态
GET  /api/admin/dashboard                         # 获取数据概览
POST /api/live/control                            # 控制直播（开始/停止）
GET  /api/v1/admin/streams                        # 获取直播流列表
GET  /api/v1/admin/votes/statistics               # 获取投票统计
```

### 3. 响应格式
所有接口必须返回统一格式：
```json
{
  "code": 0,
  "message": "success",
  "data": { ... }
}
```

## 🚀 启动命令

### 启动后端服务（Spring Boot）
```bash
cd backend
mvn spring-boot:run
# 或者
mvn clean package
java -jar target/xxx.jar
```

### 启动网关服务（Node.js）
```bash
cd gateway
npm install
npm start
# 或者开发模式（自动重启）
npm run dev
```

### 启动前端（uni-app）
```bash
cd frontend
npm install
npm run dev:h5          # H5开发模式
npm run dev:mp-weixin   # 微信小程序
```

## ⚠️ 注意事项

### 1. 跨域配置
- 网关已处理CORS
- 后端需要配置允许来自网关的请求

### 2. 端口冲突
- 确保8080和8081端口未被占用
- 可以使用 `netstat -ano | findstr :8080` 检查

### 3. 敏感信息
- 已在.gitignore中配置忽略敏感配置文件
- 包含微信密钥的配置文件不会被提交

### 4. Mock数据要点
- 数据要真实合理
- 结构要与前端期望一致
- 需要考虑边界情况

## 📋 开发步骤

1. ✅ 下载前端和网关项目
2. ✅ 分析前端需要的接口
3. ⏳ 创建Spring Boot项目
4. ⏳ 实现Mock数据接口
5. ⏳ 本地测试联调
6. ⏳ 部署到云端
7. ⏳ 编写README文档

## 🔍 调试技巧

### 查看端口占用
```bash
netstat -ano | findstr :8080
netstat -ano | findstr :8081
```

### 测试API接口
```bash
# 测试后端
curl http://localhost:8081/api/v1/votes?stream_id=test

# 测试网关
curl http://localhost:8080/api/v1/votes?stream_id=test
```

### Maven常用命令
```bash
mvn clean               # 清理
mvn compile             # 编译
mvn test                # 测试
mvn package             # 打包
mvn spring-boot:run     # 启动Spring Boot
```

## 📚 参考资源

- [Spring Boot官方文档](https://spring.io/projects/spring-boot)
- [Maven官方文档](https://maven.apache.org/)
- [测试题目要求](测试题目.md)

---

**最后更新**: 2026-07-15
**维护者**: 开发者
