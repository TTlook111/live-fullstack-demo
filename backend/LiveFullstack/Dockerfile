# 使用 Maven 镜像进行构建
FROM maven:3.9-eclipse-temurin-17 AS builder

# 设置工作目录
WORKDIR /app

# 复制 pom.xml
COPY pom.xml .

# 下载依赖（利用Docker缓存）
RUN mvn dependency:go-offline -B

# 复制源代码
COPY src ./src

# 打包应用
RUN mvn clean package -DskipTests -B

# 使用 Eclipse Temurin JDK 17 运行时镜像
FROM eclipse-temurin:17-jdk

# 设置工作目录
WORKDIR /app

# 从构建阶段复制 jar 文件
COPY --from=builder /app/target/LiveFullstack-0.0.1-SNAPSHOT.jar app.jar

# 设置JVM内存参数（适配免费tier）
ENV JAVA_OPTS="-Xmx256m -Xms128m"

# 暴露端口
EXPOSE 8000

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
