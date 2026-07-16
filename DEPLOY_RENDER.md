# Render 部署指南

## 方式一：使用 Dashboard 手动创建（推荐新手）

### 步骤 1：登录 Render
1. 访问 https://render.com
2. 使用 GitHub 账号登录

### 步骤 2：创建 Web Service
1. 点击右上角 **"New +"** 按钮
2. 选择 **"Web Service"**

### 步骤 3：连接 GitHub 仓库
1. 选择你的仓库 `live-fullstack-demo`
2. 如果没有看到，点击 "Configure account" 授权

### 步骤 4：填写服务配置

```
Name:               live-debate-frontend
Region:             Oregon (美国) / Frankfurt (欧洲) / Singapore (亚洲)
Branch:             main
Runtime:            Node
Build Command:      npm install && npm run build:h5
Start Command:      npm start
Instance Type:      Free
```

### 步骤 5：添加环境变量
点击 **"Advanced"** 按钮，然后添加：

| Key | Value |
|-----|-------|
| `USE_MOCK` | `true` |
| `NODE_ENV` | `production` |

### 步骤 6：创建并部署
1. 点击 **"Create Web Service"**
2. 等待构建完成（约 2-5 分钟）

---

## 方式二：使用 render.yaml（一键部署）

### 步骤 1：推送代码到 GitHub
```bash
cd D:\Java_study\live-fullstack-demo
git add render.yaml
git commit -m "Add Render deployment config"
git push origin main
```

### 步骤 2：在 Render 导入
1. 登录 https://render.com
2. 点击 **"New +"** → **"Blueprint"**
3. 选择你的仓库
4. Render 会自动识别 `render.yaml` 并创建服务

---

## 部署后访问

部署成功后，Render 会给你一个 URL，格式如：
```
https://live-debate-frontend.onrender.com
```

---

## 常见问题

### Q: 构建失败怎么办？
A: 检查 Render 的 Logs 页面，查看具体错误信息

### Q: 访问 404？
A: 确认 `dist/build/h5/` 目录存在且有 `index.html`

### Q: 如何更新部署？
A: 推送代码到 main 分支会自动触发重新部署

---

## 后续：连接真实后端

当你准备好 Java 后端后，修改环境变量：

| Key | Value |
|-----|-------|
| `USE_MOCK` | `false` |
| `BACKEND_SERVER_URL` | `https://your-java-backend.onrender.com` |
