# 使用 Node.js 18 镜像
FROM node:18-alpine

# 设置工作目录
WORKDIR /app

# 复制 package.json
COPY package*.json ./

# 安装依赖（使用--legacy-peer-deps解决依赖冲突）
RUN npm install --legacy-peer-deps

# 复制源代码
COPY . .

# 暴露端口
EXPOSE 8080

# 启动应用（直接运行Node.js服务器）
CMD ["npm", "start"]
