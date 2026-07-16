# Live Fullstack Demo - 完整部署指南

> 直播辩论小程序全栈部署手册，涵盖三种部署方案及部署后配置。

---

## 项目架构概览

```
┌──────────────┐     ┌──────────────┐     ┌──────────────────┐
│   Frontend   │────>│    Gateway   │────>│     Backend      │
│  uni-app H5  │     │  Node.js     │     │  Spring Boot     │
│  port: 8080  │     │  port: 8081  │     │  port: 8000      │
└──────────────┘     └──────────────┘     └──────────────────┘
       │                    │
       │              WebSocket + 代理
       │              直播流代理(SRS)
       │              后台管理面板
       └────────────────────┘
            静态资源 + API
```

- **Frontend** (`frontend/`): uni-app H5 前端，Express 作为开发/生产服务器，含后台管理面板
- **Gateway** (`gateway/`): 中间层网关，负责 WebSocket 实时通信、API 代理、直播流转发
- **Backend** (`backend/LiveFullstack/`): Spring Boot 4.1.0 后端，Java 17

---

## 一、Vercel + Railway 方案

> Vercel 部署前端静态站点，Railway 部署 Node.js 网关和 Java 后端。适合快速上线，免运维。

### 1.1 准备工作

1. 注册 [Vercel](https://vercel.com) 账号
2. 注册 [Railway](https://railway.app) 账号
3. 将代码推送到 GitHub 仓库

### 1.2 部署 Backend（Spring Boot）到 Railway

**第一步：创建 Railway 项目**

1. 登录 Railway（https://railway.app）
2. 点击右上角 "New Project" 按钮
3. 选择 "Empty Project" 创建空项目
4. 进入项目后，点击 "New" -> "GitHub Repo"
5. 连接你的 GitHub 账号
6. 选择仓库 `TTlook111/live-fullstack-demo`
7. 在 "Root Directory" 中输入 `backend/LiveFullstack`
8. 点击 "Deploy Now"

**第二步：配置构建**

Railway 会自动检测 `pom.xml` 并使用 Maven 构建。

添加环境变量：
1. 点击服务卡片上的 "Variables" 标签
2. 添加以下环境变量：

```env
# Railway 自动设置 PORT，Spring Boot 需要读取
PORT=8000
JAVA_VERSION=17
```

**第三步：配置启动命令**

1. 点击服务卡片上的 "Settings" 标签
2. 找到 "Deploy" 部分
3. 在 "Start Command" 中输入：

```
java -jar target/LiveFullstack-0.0.1-SNAPSHOT.jar --server.port=${PORT}
```

或者在 `backend/LiveFullstack/` 目录下创建 `Procfile` 文件：

```
web: java -jar target/LiveFullstack-0.0.1-SNAPSHOT.jar --server.port=${PORT:-8000}
```

**第四步：记录 Backend 部署 URL**

部署成功后：
1. 点击服务卡片上的 "Settings" 标签
2. 找到 "Networking" 部分
3. 点击 "Generate Domain" 生成公网域名
4. 记录生成的 URL，如：
```
https://live-fullstack-backend.up.railway.app
```

### 1.3 部署 Gateway（Node.js）到 Railway

**第一步：新建 Railway Service**

1. 在同一个 Railway 项目中
2. 点击 "New" -> "GitHub Repo"
3. 选择同一个仓库 `TTlook111/live-fullstack-demo`
4. 在 "Root Directory" 中输入 `gateway`
5. 点击 "Deploy Now"

**第二步：配置环境变量**

在 Railway 的 Variables 面板中添加：

```env
# 后端服务器地址（上一步获得的 URL）
BACKEND_SERVER_URL=https://live-fullstack-backend.up.railway.app

# 服务器 IP（用于直播流地址生成）
SERVER_IP=your-railway-domain.up.railway.app

# 微信配置（生产环境必须使用环境变量，不要硬编码）
WECHAT_APPID=wx94289b0d2ca7a802
WECHAT_SECRET=your_real_secret_here

# 关闭模拟模式
USE_MOCK_SERVER=false
```

**第三步：修改 gateway 配置以支持环境变量**

编辑 `gateway/config/server-mode.node.js`，将关键配置改为读取环境变量：

```javascript
const USE_MOCK_SERVER = process.env.USE_MOCK_SERVER === 'true' ? true : false;
const REAL_SERVER_URL = process.env.BACKEND_SERVER_URL || 'http://localhost:8000';
const REAL_SERVER_PORT = parseInt(process.env.PORT) || 8081;

const REAL_WECHAT_CONFIG = {
    appid: process.env.WECHAT_APPID || 'wx94289b0d2ca7a802',
    secret: process.env.WECHAT_SECRET || ''
};
```

**第四步：记录 Gateway URL**

如：`https://live-fullstack-gateway.up.railway.app`

### 1.4 部署 Frontend（uni-app H5）到 Vercel

**第一步：修改 API 地址**

编辑 `frontend/config/server-mode.js`，将 `API_BASE_URL` 改为 Gateway 的线上地址：

```javascript
export const API_BASE_URL = 'https://live-fullstack-gateway.up.railway.app';
export const MIDDLEWARE_SERVER_URL = 'https://live-fullstack-gateway.up.railway.app';
```

**第二步：部署到 Vercel**

1. 登录 Vercel（https://vercel.com）
2. 点击 "Add New" -> "Project"
3. 导入 GitHub 仓库 `TTlook111/live-fullstack-demo`
4. 配置项目：
   - **Framework Preset**: `Other`
   - **Root Directory**: `frontend`
   - **Build Command**: `npm run build:h5`
   - **Output Directory**: `dist/build/h5`
5. 点击 "Deploy"

**第三步：记录 Frontend URL**

部署成功后，Vercel 会分配一个域名，如：
```
https://live-fullstack-demo.vercel.app
```

**第四步：配置自定义域名（可选）**

在 Vercel 项目 Settings -> Domains 中添加自定义域名。

### 1.5 Vercel 方案的 WebSocket 注意事项

Vercel 的 Serverless Functions **不支持 WebSocket 长连接**。解决方案：

- WebSocket 请求直接指向 Railway 的 Gateway 地址
- 在前端代码中，WebSocket URL 单独配置为 Gateway 地址

在 `frontend/config/server-mode.js` 中添加：

```javascript
export const WS_URL = 'wss://live-fullstack-gateway.up.railway.app/ws';
```

---

## 二、Cloudflare + Render 方案

> Cloudflare Pages 部署前端，Render 部署后端服务。免费额度较充裕。

### 2.1 部署 Backend（Spring Boot）到 Render

**第一步：创建 Web Service**

1. 登录 [Render](https://render.com)，点击 "New" -> "Web Service"
2. 连接 GitHub 仓库
3. 配置：
   - Name: `live-fullstack-backend`
   - Root Directory: `backend/LiveFullstack`
   - Runtime: `Java`
   - Build Command: `./mvnw clean package -DskipTests` 或 `mvn clean package -DskipTests`
   - Start Command: `java -jar target/LiveFullstack-0.0.1-SNAPSHOT.jar --server.port=$PORT`
   - Instance Type: `Free`（测试用）

**第二步：配置环境变量**

```env
JAVA_VERSION=17
```

**第三步：记录 URL**

如：`https://live-fullstack-backend.onrender.com`

### 2.2 部署 Gateway（Node.js）到 Render

**第一步：创建 Web Service**

1. 点击 "New" -> "Web Service"
2. 配置：
   - Name: `live-fullstack-gateway`
   - Root Directory: `gateway`
   - Runtime: `Node`
   - Build Command: `npm install`
   - Start Command: `node gateway.js`
   - Instance Type: `Free`

**第二步：配置环境变量**

```env
BACKEND_SERVER_URL=https://live-fullstack-backend.onrender.com
SERVER_IP=live-fullstack-gateway.onrender.com
WECHAT_APPID=wx94289b0d2ca7a802
WECHAT_SECRET=your_real_secret_here
USE_MOCK_SERVER=false
PORT=10000
```

> Render 的免费实例会在 15 分钟无请求后休眠，首次唤醒需 30-60 秒。

### 2.3 部署 Frontend 到 Cloudflare Pages

**第一步：构建 H5 版本**

```bash
cd frontend
npm install
# 修改 config/server-mode.js 中的 API 地址为 Gateway 的 Render URL
npm run build:h5
```

**第二步：部署**

方式一（Wrangler CLI）：

```bash
npm i -g wrangler
wrangler pages deploy frontend/dist/build/h5 --project-name=live-fullstack-frontend
```

方式二（网页操作）：

1. 登录 [Cloudflare Dashboard](https://dash.cloudflare.com)
2. 进入 Workers & Pages -> Create -> Pages -> Upload assets
3. 上传 `frontend/dist/build/h5` 目录
4. 或连接 GitHub 仓库，配置：
   - Build command: `cd frontend && npm install && npm run build:h5`
   - Build output directory: `frontend/dist/build/h5`

**第三步：配置自定义域名**

在 Cloudflare Pages 项目 -> Custom domains 中添加。

### 2.4 Cloudflare 方案注意事项

- Cloudflare Pages 免费版支持无限请求和带宽
- Render 免费实例有 15 分钟休眠机制，可在 Cloudflare 中配置定时请求保活（每 10 分钟 ping 一次）
- WebSocket 在 Cloudflare Pages 上不直接受支持，需通过 Gateway 的 Render 地址连接

---

## 三、自建服务器方案

> 完全自主控制，适合有云服务器（阿里云/腾讯云/自有机器）的场景。

### 3.1 服务器环境准备

**系统要求：**
- OS: Ubuntu 20.04+ / CentOS 7+ / Debian 10+
- 内存: 2GB+
- 磁盘: 20GB+

**安装基础软件：**

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install -y nginx openjdk-17-jdk nodejs npm curl git

# CentOS
sudo yum install -y nginx java-17-openjdk nodejs npm git

# 安装 Node.js 18+（推荐使用 nvm）
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash
source ~/.bashrc
nvm install 18
nvm use 18

# 验证版本
java -version    # Java 17+
node -v          # v18+
npm -v           # 9+
nginx -v         # nginx/1.18+
```

### 3.2 部署 Backend（Spring Boot）

```bash
# 克隆代码
cd /opt
git clone https://github.com/your-username/live-fullstack-demo.git
cd live-fullstack-demo/backend/LiveFullstack

# 构建
mvn clean package -DskipTests

# 测试运行
java -jar target/LiveFullstack-0.0.1-SNAPSHOT.jar --server.port=8000
# Ctrl+C 停止
```

**创建 systemd 服务：**

```bash
sudo tee /etc/systemd/system/live-backend.service << 'EOF'
[Unit]
Description=Live Fullstack Backend
After=network.target

[Service]
Type=simple
User=www-data
WorkingDirectory=/opt/live-fullstack-demo/backend/LiveFullstack
ExecStart=/usr/bin/java -jar target/LiveFullstack-0.0.1-SNAPSHOT.jar --server.port=8000
Restart=always
RestartSec=5
Environment=SPRING_PROFILES_ACTIVE=production

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable live-backend
sudo systemctl start live-backend
sudo systemctl status live-backend
```

### 3.3 部署 Gateway（Node.js）

```bash
cd /opt/live-fullstack-demo/gateway
npm install --production

# 修改配置（生产环境）
# 编辑 config/server-mode.node.js
# USE_MOCK_SERVER = false
# REAL_SERVER_URL = 'http://localhost:8000'
```

**创建 systemd 服务：**

```bash
sudo tee /etc/systemd/system/live-gateway.service << 'EOF'
[Unit]
Description=Live Fullstack Gateway
After=network.target live-backend.service

[Service]
Type=simple
User=www-data
WorkingDirectory=/opt/live-fullstack-demo/gateway
ExecStart=/usr/local/bin/node gateway.js
Restart=always
RestartSec=5
Environment=NODE_ENV=production
Environment=PORT=8081
Environment=BACKEND_SERVER_URL=http://localhost:8000
Environment=USE_MOCK_SERVER=false
Environment=WECHAT_APPID=wx94289b0d2ca7a802
Environment=WECHAT_SECRET=your_real_secret_here

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable live-gateway
sudo systemctl start live-gateway
```

### 3.4 部署 Frontend（H5 构建 + Nginx）

**构建前端：**

```bash
cd /opt/live-fullstack-demo/frontend
npm install
npm run build:h5
```

**配置 Nginx：**

```bash
sudo tee /etc/nginx/sites-available/live-fullstack << 'EOF'
server {
    listen 80;
    server_name your-domain.com;  # 替换为你的域名

    # 前端静态文件
    root /opt/live-fullstack-demo/frontend/dist/build/h5;
    index index.html;

    # 前端路由（SPA）
    location / {
        try_files $uri $uri/ /index.html;
    }

    # 后台管理面板
    location /admin {
        proxy_pass http://127.0.0.1:8081;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # API 代理到 Gateway
    location /api/ {
        proxy_pass http://127.0.0.1:8081;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_read_timeout 300s;
    }

    # WebSocket 代理
    location /ws {
        proxy_pass http://127.0.0.1:8081;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_read_timeout 86400s;
    }

    # 直播流代理
    location /live/ {
        proxy_pass http://127.0.0.1:8081;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    # 静态资源缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf)$ {
        expires 7d;
        add_header Cache-Control "public, immutable";
    }
}
EOF

sudo ln -s /etc/nginx/sites-available/live-fullstack /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

### 3.5 配置 HTTPS（Let's Encrypt）

```bash
sudo apt install -y certbot python3-certbot-nginx
sudo certbot --nginx -d your-domain.com
# 按提示完成证书申请

# 自动续期测试
sudo certbot renew --dry-run
```

### 3.6 防火墙配置

```bash
# UFW (Ubuntu)
sudo ufw allow 22/tcp     # SSH
sudo ufw allow 80/tcp     # HTTP
sudo ufw allow 443/tcp    # HTTPS
sudo ufw enable

# firewalld (CentOS)
sudo firewall-cmd --permanent --add-service=http
sudo firewall-cmd --permanent --add-service=https
sudo firewall-cmd --reload
```

---

## 四、部署后配置

### 4.1 微信小程序配置

**小程序后台设置：**

1. 登录 [微信公众平台](https://mp.weixin.qq.com)
2. 开发 -> 开发管理 -> 开发设置
3. 服务器域名配置：
   - request 合法域名: `https://your-domain.com`
   - socket 合法域名: `wss://your-domain.com`
   - uploadFile 合法域名: `https://your-domain.com`
   - downloadFile 合法域名: `https://your-domain.com`

**修改前端 API 地址：**

编辑 `frontend/config/server-mode.js`：

```javascript
// 生产环境使用线上地址
export const API_BASE_URL = 'https://your-domain.com';
export const MIDDLEWARE_SERVER_URL = 'https://your-domain.com';
export const REAL_SERVER_URL = 'https://your-domain.com';
```

### 4.2 后台管理面板访问

部署完成后，通过以下地址访问后台管理面板：

```
https://your-domain.com/admin
```

面板功能：
- 直播流管理（添加/编辑/删除/启用/禁用）
- 直播控制（开始/停止/定时）
- 投票管理（查看/设置/重置）
- AI 内容管理（查看/添加/编辑/删除）
- 用户管理
- 统计数据

### 4.3 直播流配置（SRS 服务器）

如果使用自建 SRS 流媒体服务器：

```bash
# 安装 SRS（Docker 方式）
docker run --rm -it -p 1935:1935 -p 8080:8080 ossrs/srs:5

# 推流地址
rtmp://your-server-ip:1935/live/stream-key

# 播放地址
HLS: http://your-server-ip:8080/live/stream-key.m3u8
FLV: http://your-server-ip:8080/live/stream-key.flv
```

在后台管理面板中，添加直播流时选择类型（HLS/RTMP/FLV），填入对应地址。

### 4.4 环境变量汇总

| 变量名 | 说明 | 示例 |
|--------|------|------|
| `PORT` | 服务监听端口 | `8081` |
| `BACKEND_SERVER_URL` | 后端服务器地址 | `http://localhost:8000` |
| `USE_MOCK_SERVER` | 是否使用模拟模式 | `false` |
| `WECHAT_APPID` | 微信小程序 AppID | `wx94289b0d2ca7a802` |
| `WECHAT_SECRET` | 微信小程序 Secret | `your_secret` |
| `SERVER_IP` | 服务器公网 IP | `1.2.3.4` |
| `HLS_SERVER_PORT` | HLS 服务端口 | `8086` |
| `RTMP_SERVER_PORT` | RTMP 服务端口 | `1935` |
| `NODE_ENV` | Node 环境 | `production` |

---

## 五、常见问题

### Q1: 部署后页面空白

**原因：** 前端 API 地址未改为线上地址，或构建产物路径错误。

**解决：**
1. 确认 `frontend/config/server-mode.js` 中 `API_BASE_URL` 已改为线上 Gateway 地址
2. 重新执行 `npm run build:h5`
3. 确认 Nginx 的 `root` 指向 `dist/build/h5` 目录
4. 检查浏览器控制台的网络请求，确认 API 请求是否返回 200

### Q2: WebSocket 连接失败

**原因：** WebSocket 需要特殊的反向代理配置。

**解决：**
1. Nginx 配置中必须包含 WebSocket 升级头：
   ```nginx
   location /ws {
       proxy_http_version 1.1;
       proxy_set_header Upgrade $http_upgrade;
       proxy_set_header Connection "upgrade";
   }
   ```
2. Vercel/Cloudflare Pages 不支持 WebSocket，需直连 Gateway 地址
3. 确认防火墙没有拦截 WebSocket 端口

### Q3: 微信登录报错 "invalid code"

**原因：** AppID 或 AppSecret 不正确，或 code 已过期（有效期 5 分钟）。

**解决：**
1. 检查环境变量 `WECHAT_APPID` 和 `WECHAT_SECRET` 是否正确
2. 生产环境不要硬编码 Secret，使用环境变量
3. 确认小程序后台的 AppSecret 没有被重置
4. 检查服务器时间是否准确（`date` 命令）

### Q4: 微信小程序提示 "不在以下 request 合法域名列表中"

**原因：** 微信小程序后台未配置服务器域名。

**解决：**
1. 登录微信公众平台 -> 开发 -> 开发设置 -> 服务器域名
2. 添加 `https://your-domain.com` 到 request 合法域名
3. 如果使用 WebSocket，还需添加到 socket 合法域名（`wss://your-domain.com`）
4. 开发阶段可在微信开发者工具中勾选 "不校验合法域名"

### Q5: Render 免费实例休眠后首次访问超时

**原因：** Render 免费实例 15 分钟无请求后自动休眠，唤醒需要 30-60 秒。

**解决：**
1. 使用外部定时 ping 服务（如 [UptimeRobot](https://uptimerobot.com)）每 10 分钟请求一次
2. 在 Cloudflare 中使用 Workers 定时触发
3. 升级到 Render 付费实例（无休眠）

### Q6: 构建时 Maven 下载依赖超时

**原因：** Maven 默认使用中央仓库，国内访问较慢。

**解决：**

在 `backend/LiveFullstack/pom.xml` 中添加阿里云镜像：

```xml
<repositories>
    <repository>
        <id>aliyun</id>
        <url>https://maven.aliyun.com/repository/public</url>
    </repository>
</repositories>
```

或在服务器上配置 Maven 镜像 (`~/.m2/settings.xml`)：

```xml
<mirrors>
    <mirror>
        <id>aliyun</id>
        <mirrorOf>central</mirrorOf>
        <name>Aliyun Mirror</name>
        <url>https://maven.aliyun.com/repository/public</url>
    </mirror>
</mirrors>
```

### Q7: 前端构建报错 "cross-env 不是内部命令"

**原因：** Windows 环境下 `cross-env` 未全局安装。

**解决：**

```bash
npm install -g cross-env
# 或
npx cross-env UNI_PLATFORM=h5 vue-cli-service uni-build --mode production
```

### Q8: 投票数据重启后丢失

**原因：** 当前版本使用内存存储投票数据，服务重启后会重置。

**解决：**
1. 当前 `server.js` 中投票数据存储在内存变量 `currentVotes` 中
2. AI 辩论内容 (`aiDebateContent`) 同样存储在内存中
3. 如需持久化，需修改为数据库存储（如 SQLite / MySQL / Redis）

### Q9: Nginx 反向代理后静态资源 404

**原因：** Nginx 配置中 `root` 路径不正确，或 `try_files` 配置有误。

**解决：**
1. 确认构建产物路径：`ls /opt/live-fullstack-demo/frontend/dist/build/h5/`
2. 确认 Nginx 的 `root` 指向该目录
3. 检查文件权限：`sudo chown -R www-data:www-data /opt/live-fullstack-demo/frontend/dist/build/h5/`

### Q10: 如何查看服务日志

```bash
# Backend 日志
sudo journalctl -u live-backend -f

# Gateway 日志
sudo journalctl -u live-gateway -f

# Nginx 日志
sudo tail -f /var/log/nginx/access.log
sudo tail -f /var/log/nginx/error.log
```

---

## 六、方案对比

| 特性 | Vercel + Railway | Cloudflare + Render | 自建服务器 |
|------|------------------|---------------------|-----------|
| 费用 | 免费额度有限 | 免费额度较充裕 | 服务器费用 |
| 部署难度 | 低 | 低 | 中 |
| WebSocket | 需直连 Gateway | 需直连 Gateway | Nginx 代理 |
| 自定义域名 | 支持 | 支持 | 需自行配置 SSL |
| 冷启动 | 无 | Render 有休眠 | 无 |
| 扩展性 | 自动 | 半自动 | 手动 |
| 数据持久化 | 需外接数据库 | 需外接数据库 | 本地即可 |
| 推荐场景 | 快速验证 | 个人项目 | 生产环境 |
