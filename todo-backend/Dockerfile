# ベースイメージ（JDK 21）
FROM eclipse-temurin:21-jdk

# 作業ディレクトリを作成
WORKDIR /app

# JARファイルをコンテナにコピー
COPY build/libs/*.jar app.jar

# Webポートを公開
EXPOSE 8080

# アプリケーション起動コマンド
ENTRYPOINT ["java", "-jar", "app.jar"]
