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
