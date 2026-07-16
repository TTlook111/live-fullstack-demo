# Live Fullstack Demo - 完整部署指南

> 直播辩论小程序全栈部署手册，使用免费方案部署。

---

## 项目架构概览

```
┌──────────────┐     ┌──────────────┐     ┌──────────────────┐
│   Frontend   │────>│    Gateway   │────>│     Backend      │
│  uni-app H5  │     │  Node.js     │     │  Spring Boot     │
│  Vercel      │     │  Render      │     │  Render          │
└──────────────┘     └──────────────┘     └──────────────────┘
       │                    │
       │              WebSocket + 代理
       │              API转发
       └────────────────────┘
            静态资源 + API
```

- **Frontend** (`frontend/`): uni-app H5 前端，部署到 Vercel（免费）
- **Gateway** (`gateway/`): 中间层网关，部署到 Render（免费）
- **Backend** (`backend/LiveFullstack/`): Spring Boot 4.1.0 后端，部署到 Render（免费）

---

## 💰 免费方案说明

| 服务 | 用途 | 免费额度 | 限制 |
|------|------|----------|------|
| **Render** | 后端 + 网关 | 750小时/月 | 15分钟无请求会休眠 |
| **Vercel** | 前端 | 100GB带宽/月 | 个人项目完全免费 |

**优点**：
- ✅ 完全免费，不需要信用卡
- ✅ 自动HTTPS证书
- ✅ 支持自定义域名
- ✅ GitHub代码推送自动部署

**缺点**：
- ⚠️ Render免费tier 15分钟无请求会休眠
- ⚠️ 首次访问需等待30秒唤醒

---

## 一、Render + Vercel 方案（推荐）

> Vercel 部署前端静态站点，Render 部署 Node.js 网关和 Java 后端。完全免费，无需信用卡。

### 1.1 准备工作

1. 注册 [Vercel](https://vercel.com) 账号（用GitHub账号登录）
2. 注册 [Render](https://render.com) 账号（用GitHub账号登录）
3. 将代码推送到 GitHub 仓库：`https://github.com/TTlook111/live-fullstack-demo.git`

---

### 1.2 部署 Backend（Spring Boot）到 Render

> 注意：Render 免费tier 不直接支持 Java，需要使用 Docker 部署。

**第一步：创建 Dockerfile**

在 `backend/LiveFullstack/` 目录下创建 `Dockerfile` 文件：

```dockerfile
# 使用 OpenJDK 17 镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制 jar 文件
COPY target/LiveFullstack-0.0.1-SNAPSHOT.jar app.jar

# 暴露端口
EXPOSE 8000

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**第二步：创建 Render Web Service**

1. 登录 Render（https://render.com）
2. 点击左上角 "New" -> "Web Service"
3. 连接 GitHub 仓库：
   - 点击 "Connect a repository"
   - 选择 `TTlook111/live-fullstack-demo`
   - 点击 "Connect"

**第三步：配置服务**

填写以下配置：

| 配置项 | 值 |
|--------|-----|
| **Name** | `live-fullstack-backend` |
| **Region** | 选择离你最近的区域（如 Singapore） |
| **Branch** | `main` |
| **Root Directory** | `backend/LiveFullstack` |
| **Runtime** | `Docker` |
| **Dockerfile Path** | `./Dockerfile` |
| **Instance Type** | `Free` |

**第四步：添加环境变量**

点击 "Environment" 标签，添加以下环境变量：

```
PORT = 8000
```

**第五步：创建并部署**

1. 点击 "Create Web Service"
2. 等待构建和部署完成（约5-10分钟）
3. 部署成功后，记录分配的 URL：
   ```
   https://live-fullstack-backend.onrender.com
   ```

**第六步：验证后端服务**

访问后端地址，测试接口：
```bash
curl https://live-fullstack-backend.onrender.com/api/admin/dashboard
```

---

### 1.3 部署 Gateway（Node.js）到 Render

**第一步：创建 Render Web Service**

1. 在 Render 点击 "New" -> "Web Service"
2. 选择同一个仓库 `TTlook111/live-fullstack-demo`

**第二步：配置服务**

填写以下配置：

| 配置项 | 值 |
|--------|-----|
| **Name** | `live-fullstack-gateway` |
| **Region** | 选择与后端相同的区域 |
| **Branch** | `main` |
| **Root Directory** | `gateway` |
| **Runtime** | `Node` |
| **Build Command** | `npm install` |
| **Start Command** | `npm start` |
| **Instance Type** | `Free` |

**第三步：添加环境变量**

点击 "Environment" 标签，添加以下环境变量：

```
BACKEND_SERVER_URL = https://live-fullstack-backend.onrender.com
PORT = 8080
```

**第四步：创建并部署**

1. 点击 "Create Web Service"
2. 等待构建和部署完成（约2-3分钟）
3. 部署成功后，记录分配的 URL：
   ```
   https://live-fullstack-gateway.onrender.com
   ```

**第五步：验证网关服务**

访问网关地址，测试转发：
```bash
curl https://live-fullstack-gateway.onrender.com/api/admin/dashboard
```

---

### 1.4 部署 Frontend（uni-app H5）到 Vercel

**第一步：修改前端配置**

在部署前，需要修改前端配置，将 API 地址指向 Render 网关。

编辑 `frontend/config/server-mode.js`，修改以下内容：

```javascript
// 修改前
const BACKEND_SERVER_URL = 'http://localhost:8081';

// 修改后
const BACKEND_SERVER_URL = 'https://live-fullstack-gateway.onrender.com';
```

提交代码：
```bash
git add .
git commit -m "config: 修改前端API地址为Render网关地址"
git push origin main
```

**第二步：创建 Vercel 项目**

1. 登录 Vercel（https://vercel.com）
2. 点击 "Add New" -> "Project"
3. 导入 GitHub 仓库：
   - 选择 `TTlook111/live-fullstack-demo`
   - 点击 "Import"

**第三步：配置项目**

填写以下配置：

| 配置项 | 值 |
|--------|-----|
| **Framework Preset** | `Other` |
| **Root Directory** | `frontend` |
| **Build Command** | `npm run build:h5` |
| **Output Directory** | `dist/build/h5` |

**第四步：部署**

1. 点击 "Deploy"
2. 等待构建和部署完成（约2-3分钟）
3. 部署成功后，记录分配的 URL：
   ```
   https://live-fullstack-demo.vercel.app
   ```

**第五步：验证前端访问**

在浏览器中访问前端地址，检查：
- 页面是否正常加载
- 数据是否正常显示
- 功能是否正常工作

---

## 二、部署后配置

### 2.1 配置自定义域名（可选）

#### Vercel 自定义域名
1. 进入 Vercel 项目 Settings -> Domains
2. 添加你的域名
3. 按照提示配置 DNS 解析

#### Render 自定义域名
1. 进入 Render 服务 Settings -> Custom Domains
2. 添加你的域名
3. 按照提示配置 DNS 解析

### 2.2 防止 Render 休眠（可选）

Render 免费tier 15分钟无请求会休眠。可以使用 UptimeRobot 防止休眠：

1. 注册 [UptimeRobot](https://uptimerobot.com)（免费）
2. 添加 HTTP(s) 监控
3. URL 填写：`https://live-fullstack-backend.onrender.com/api/admin/dashboard`
4. 监控间隔设置为 5 分钟

---

## 三、部署检查清单

### 后端服务（Render）
- [ ] Render 账号已注册
- [ ] GitHub 仓库已连接
- [ ] Web Service 已创建
- [ ] 环境变量已配置
- [ ] 构建成功
- [ ] 服务正常运行
- [ ] 接口可访问

### 网关服务（Render）
- [ ] Web Service 已创建
- [ ] 环境变量已配置（BACKEND_SERVER_URL）
- [ ] 构建成功
- [ ] 服务正常运行
- [ ] 转发功能正常

### 前端服务（Vercel）
- [ ] Vercel 账号已注册
- [ ] GitHub 仓库已导入
- [ ] 项目配置正确
- [ ] 构建成功
- [ ] 页面可访问
- [ ] API 调用正常

---

## 四、常见问题

### 1. Render 服务休眠
**问题**：首次访问需要等待30秒
**解决**：使用 UptimeRobot 定时访问，或接受首次访问稍慢

### 2. 构建失败
**问题**：Maven 或 npm 构建失败
**解决**：
- 检查 Build Command 是否正确
- 检查 Root Directory 是否正确
- 查看构建日志，修复错误

### 3. 接口 404
**问题**：API 接口返回 404
**解决**：
- 检查后端服务是否正常运行
- 检查网关 BACKEND_SERVER_URL 配置
- 检查前端 API 地址配置

### 4. CORS 错误
**问题**：前端跨域请求失败
**解决**：
- 后端已配置 CORS 允许所有来源
- 检查网关是否正确转发

### 5. WebSocket 连接失败
**问题**：WebSocket 无法连接
**解决**：
- WebSocket 由网关处理
- 检查网关服务是否正常运行

---

## 五、部署成本

| 服务 | 用途 | 月费用 | 备注 |
|------|------|--------|------|
| Render | 后端 | $0 | 750小时/月免费 |
| Render | 网关 | $0 | 750小时/月免费 |
| Vercel | 前端 | $0 | 100GB带宽/月免费 |
| **总计** | | **$0** | **完全免费** |

---

## 六、部署后验证

### 1. 访问前端页面
```
https://live-fullstack-demo.vercel.app
```

### 2. 测试后端接口
```bash
# 测试直播状态
curl https://live-fullstack-backend.onrender.com/api/admin/live/status

# 测试数据概览
curl https://live-fullstack-backend.onrender.com/api/admin/dashboard

# 测试投票接口
curl "https://live-fullstack-backend.onrender.com/api/v1/votes?stream_id=stream-001"
```

### 3. 测试网关转发
```bash
# 通过网关访问后端接口
curl https://live-fullstack-gateway.onrender.com/api/admin/dashboard
```

---

## 七、相关资源

- [Render 官方文档](https://render.com/docs)
- [Vercel 官方文档](https://vercel.com/docs)
- [UptimeRobot 官网](https://uptimerobot.com)
- [项目 GitHub 仓库](https://github.com/TTlook111/live-fullstack-demo)

---

**创建时间**: 2026-07-16
**最后更新**: 2026-07-16
**维护者**: 开发者
