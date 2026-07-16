# 使用 Eclipse Temurin JDK 17 镜像
FROM eclipse-temurin:17-jdk-jammy

# 设置工作目录
WORKDIR /app

# 复制预构建的jar包
COPY target/LiveFullstack-0.0.1-SNAPSHOT.jar app.jar

# 设置JVM内存参数（适配免费tier）
ENV JAVA_OPTS="-Xmx256m -Xms128m"

# 暴露端口
EXPOSE 8000

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
