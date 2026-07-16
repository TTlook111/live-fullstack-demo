# 使用 OpenJDK 17 镜像
FROM eclipse-temurin:17-jdk-alpine

# 设置工作目录
WORKDIR /app

# 安装Maven
RUN apk add --no-cache maven

# 复制 pom.xml
COPY pom.xml .

# 下载依赖（利用Docker缓存）
RUN mvn dependency:go-offline -B -q

# 复制源代码
COPY src ./src

# 打包应用
RUN mvn clean package -DskipTests -B -q

# 设置JVM内存参数（适配免费tier）
ENV JAVA_OPTS="-Xmx256m -Xms128m"

# 暴露端口
EXPOSE 8000

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar target/LiveFullstack-0.0.1-SNAPSHOT.jar"]
