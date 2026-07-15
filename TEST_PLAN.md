# 🧪 本地测试联调方案

## 📋 测试目标

1. 验证后端服务能正常启动（8081端口）
2. 验证网关服务能正常启动（8080端口）
3. 验证前端项目能正常启动
4. 验证所有API接口响应正常
5. 验证前后端数据交互正确
6. 发现并记录问题

---

## 🔧 测试环境准备

### 1. 环境要求
- Java 17+
- Maven 3.8+
- Node.js 18+
- npm 9+

### 2. 端口检查
- 8080端口（网关）
- 8081端口（后端）
- 5173端口（前端，uni-app默认）

### 3. 项目结构确认
```
live-fullstack-demo/
├── frontend/          # 前端项目
├── gateway/           # 网关项目
├── backend/LiveFullstack/  # 后端项目
└── ...
```

---

## 📝 测试步骤

### 阶段1：启动后端服务

#### 1.1 检查Maven配置
```bash
cd D:/Java_study/live-fullstack-demo/backend/LiveFullstack
mvn --version
```

#### 1.2 编译项目
```bash
mvn clean compile
```

#### 1.3 启动后端服务
```bash
mvn spring-boot:run
```

#### 1.4 验证后端启动
```bash
curl http://localhost:8081/api/admin/dashboard
```

**预期响应**：
```json
{
    "code": 0,
    "message": "success",
    "data": {
        "liveStatus": {...},
        "totalUsers": 5000,
        "activeUsers": 1250,
        "totalVotes": 3000,
        "streams": [...]
    }
}
```

---

### 阶段2：启动网关服务

#### 2.1 安装依赖
```bash
cd D:/Java_study/live-fullstack-demo/gateway
npm install
```

#### 2.2 启动网关服务
```bash
npm start
```

#### 2.3 验证网关启动
```bash
curl http://localhost:8080/api/admin/dashboard
```

**预期响应**：与后端响应相同（网关转发）

---

### 阶段3：启动前端项目

#### 3.1 安装依赖
```bash
cd D:/Java_study/live-fullstack-demo/frontend
npm install
```

#### 3.2 启动前端项目
```bash
npm run dev:h5
```

#### 3.3 验证前端启动
- 浏览器访问：http://localhost:5173
- 检查页面是否正常加载

---

### 阶段4：接口测试

#### 4.1 投票系统接口测试

**测试4.1.1：获取票数统计**
```bash
curl "http://localhost:8081/api/v1/votes?stream_id=stream-001"
```
**预期**：返回投票数据，leftVotes + rightVotes = totalVotes

**测试4.1.2：获取票数（兼容版本）**
```bash
curl "http://localhost:8081/api/votes?stream_id=stream-001"
```
**预期**：与4.1.1响应相同

**测试4.1.3：用户投票**
```bash
curl -X POST http://localhost:8081/api/v1/user-vote \
  -H "Content-Type: application/json" \
  -d '{"streamId":"stream-001","leftVotes":60,"rightVotes":40}'
```
**预期**：返回更新后的投票数据

**测试4.1.4：查询用户投票状态**
```bash
curl "http://localhost:8081/api/v1/user-votes?stream_id=stream-001&user_id=user-001"
```
**预期**：返回用户投票记录

---

#### 4.2 AI内容接口测试

**测试4.2.1：获取AI内容**
```bash
curl "http://localhost:8081/api/v1/ai-content?stream_id=stream-001"
```
**预期**：返回AI内容列表

---

#### 4.3 评论系统接口测试

**测试4.3.1：添加评论**
```bash
curl -X POST http://localhost:8081/api/comment \
  -H "Content-Type: application/json" \
  -d '{"contentId":"content-001","text":"测试评论","user":"测试用户","avatar":"👤"}'
```
**预期**：返回新创建的评论

**测试4.3.2：点赞**
```bash
curl -X POST http://localhost:8081/api/like \
  -H "Content-Type: application/json" \
  -d '{"contentId":"content-001","commentId":"comment-001"}'
```
**预期**：返回点赞成功

**测试4.3.3：删除评论**
```bash
curl -X DELETE http://localhost:8081/api/comment/comment-001 \
  -H "Content-Type: application/json" \
  -d '{"contentId":"content-001"}'
```
**预期**：返回删除成功

---

#### 4.4 辩题管理接口测试

**测试4.4.1：获取辩题信息**
```bash
curl "http://localhost:8081/api/v1/debate-topic?stream_id=stream-001"
```
**预期**：返回辩题数据，包含title、leftSide、rightSide等字段

---

#### 4.5 直播管理接口测试

**测试4.5.1：获取直播状态**
```bash
curl http://localhost:8081/api/admin/live/status
```
**预期**：返回直播状态，包含isLive、streamUrl等

**测试4.5.2：获取数据概览**
```bash
curl http://localhost:8081/api/admin/dashboard
```
**预期**：返回数据概览，包含liveStatus、totalUsers、streams等

**测试4.5.3：控制直播**
```bash
curl -X POST http://localhost:8081/api/live/control \
  -H "Content-Type: application/json" \
  -d '{"action":"start"}'
```
**预期**：返回更新后的直播状态

**测试4.5.4：获取直播流列表**
```bash
curl http://localhost:8081/api/v1/admin/streams
```
**预期**：返回直播流列表

**测试4.5.5：获取投票统计**
```bash
curl "http://localhost:8081/api/v1/admin/votes/statistics?stream_id=stream-001"
```
**预期**：返回投票统计数据，包含leftVotes、rightVotes、leftPercentage等

---

### 阶段5：网关转发测试

#### 5.1 通过网关访问后端接口
```bash
curl "http://localhost:8080/api/v1/votes?stream_id=stream-001"
curl "http://localhost:8080/api/v1/debate-topic?stream_id=stream-001"
curl http://localhost:8080/api/admin/dashboard
```

**预期**：与直接访问后端响应相同

---

### 阶段6：前端页面测试

#### 6.1 访问前端页面
- 浏览器访问：http://localhost:5173
- 检查页面是否正常加载
- 检查是否有控制台错误

#### 6.2 功能测试
- 测试投票功能是否正常
- 测试评论功能是否正常
- 测试辩题显示是否正常
- 测试直播状态显示是否正常

---

## 📊 测试检查清单

### 后端服务
- [ ] Maven编译成功
- [ ] Spring Boot启动成功
- [ ] 端口8081正常监听
- [ ] 所有接口响应正常

### 网关服务
- [ ] npm install成功
- [ ] 网关启动成功
- [ ] 端口8080正常监听
- [ ] 接口转发正常

### 前端项目
- [ ] npm install成功
- [ ] 前端启动成功
- [ ] 页面正常加载
- [ ] 无控制台错误

### 接口测试
- [ ] 投票系统接口正常
- [ ] AI内容接口正常
- [ ] 评论系统接口正常
- [ ] 辩题管理接口正常
- [ ] 直播管理接口正常

---

## 🐛 问题记录模板

### 问题1
- **问题描述**：
- **重现步骤**：
- **预期结果**：
- **实际结果**：
- **错误日志**：
- **解决方案**：

### 问题2
- **问题描述**：
- **重现步骤**：
- **预期结果**：
- **实际结果**：
- **错误日志**：
- **解决方案**：

---

## 📋 测试报告模板

### 测试概览
- 测试时间：
- 测试环境：
- 测试人员：

### 测试结果
- 总测试用例数：
- 通过用例数：
- 失败用例数：
- 跳过用例数：

### 问题汇总
- 严重问题：0个
- 一般问题：0个
- 轻微问题：0个

### 结论
- [ ] 测试通过，可以部署
- [ ] 测试不通过，需要修复问题

---

**创建时间**: 2026-07-15
**维护者**: 开发者
