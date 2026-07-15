# CLAUDE.md - 项目开发指南

## 📌 项目概述

**项目名称**: live-fullstack-demo
**项目类型**: 后端岗位线上测试题 - 全栈直播应用后端服务
**GitHub仓库**: https://github.com/TTlook111/live-fullstack-demo.git

## 🛠️ 技术栈

### 后端（开发中）
- **语言**: Java 17
- **框架**: Spring Boot 3.x
- **构建工具**: Maven 3.9.16
- **数据方案**: Mock数据（假数据，不使用数据库）

### 前端（已下载）
- **框架**: Vue.js 3 + uni-app
- **位置**: `/frontend/`

### 网关（已下载）
- **框架**: Node.js + Express
- **位置**: `/gateway/`
- **端口**: 8080

## 📁 项目结构

```
live-fullstack-demo/
├── frontend/              # 前端项目（Vue3 + uni-app）
├── gateway/               # 网关项目（Node.js + Express，端口8080）
├── backend/               # 后端项目（Spring Boot，端口8081）
├── .gitignore             # Git忽略配置
├── CLAUDE.md              # 本文件
├── README.md              # 项目说明（待创建）
└── 测试题目.md            # 测试题目要求
```

## 🔧 开发环境配置

### Java环境
- **Java版本**: Java 17 (LTS)
- **Java路径**: `D:\tool\Java17`
- **JAVA_HOME**: `D:\tool\Java17`

### Maven环境
- **Maven版本**: 3.9.16
- **Maven路径**: `D:\tool\apache-maven-3.9.16`
- **Maven命令**: 使用 `mvn` 或 `mvn.cmd`
- **本地仓库**: `~/.m2/repository/`
- **MAVEN_HOME**: `D:\tool\apache-maven-3.9.16`

### Node.js环境（用于网关和前端）
- **Node.js版本**: 22.22.2
- **npm版本**: 10.9.7

### Git配置
- **用户名**: muyou
- **邮箱**: 2725077832@qq.com

### IDE环境
- **IntelliJ IDEA**: 2023.2 (位于 `D:/tool/IntelliJIdea2023.2/`)
- **DataGrip**: 2024.1 (位于 `D:/tool/DataGrip2024.1/`)

### 常用工具
- **Git**: `D:\git\Git\cmd\git.exe`
- **SSH**: `D:\git\Git\usr\bin\ssh.exe`
- **curl**: 系统自带
- **Docker**: `C:\Program Files\Docker\Docker\resources\bin\docker.exe`
- **kubectl**: `C:\Program Files\Docker\Docker\resources\bin\kubectl.exe`

### 端口配置
- **前端**: 由uni-app开发工具管理
- **网关**: 8080端口
- **后端**: 8081端口

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
