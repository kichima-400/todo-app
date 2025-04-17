
# ========================
# 🌐 フロントエンドビルドステージ
# ========================
FROM node:20 AS frontend-builder

WORKDIR /app/frontend
COPY todo-frontend/ ./

RUN npm install
RUN npm run build


# ========================
# ☕ バックエンドビルドステージ
# ========================
FROM eclipse-temurin:21-jdk AS backend-builder

WORKDIR /app/backend
COPY todo-backend/ ./
RUN ./gradlew clean bootJar


# ========================
# 🚀 実行ステージ
# ========================
FROM eclipse-temurin:21-jdk

WORKDIR /app

# フロントエンドのビルド成果物をコピーして Spring Boot で配信
COPY --from=frontend-builder /app/frontend/dist/ /app/static/

# JARファイルをコピーしてアプリとして実行
COPY --from=backend-builder /app/backend/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
