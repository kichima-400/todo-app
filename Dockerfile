########################################
# ステージ1: React をビルド（開発用ツールは最終イメージに不要）
########################################
FROM node:20 AS frontend-build
WORKDIR /app/frontend

# 依存だけ先にコピーしてキャッシュ
COPY todo-frontend/package*.json ./
RUN npm install

# ソースをコピーしてビルド
COPY todo-frontend .
RUN npm run build       # Vite なら dist/ が生成される

########################################
# ステージ2: Spring Boot をビルド
########################################
FROM eclipse-temurin:21-jdk AS backend-build
WORKDIR /app/backend

# バックエンド全ソースをコピー
COPY todo-backend .
# JAR を生成
RUN ./gradlew bootJar --no-daemon

########################################
# ステージ3: 実行用の最終イメージ
########################################
FROM eclipse-temurin:21-jre
WORKDIR /app

# バックエンドの JAR を配置
COPY --from=backend-build /app/backend/build/libs/*.jar app.jar

# フロントエンドの静的ファイルを配置
COPY --from=frontend-build /app/frontend/dist ./static

# 本番プロファイルを有効に（必要に応じて）
ENV SPRING_PROFILES_ACTIVE=prod

# 起動コマンド
ENTRYPOINT ["java", "-jar", "app.jar"]
