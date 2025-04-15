## 環境変数ファイル（.env）の設定
本アプリケーションでは、データベース接続情報などの機密情報を `.env` ファイルで管理しています。

### `.env` ファイルを『`todo-backend` ディレクトリ』に作成し、以下のように記述してください：
DB_PASSWORD=あなたのPostgreSQLパスワード

### .envファイルの例
DB_PASSWORD=postgres1234

### 注意点
.env ファイルが、存在しない場合、アプリケーション起動時にエラーになります。
機密情報を含むため、絶対に .env ファイルを Git に登録しないでください。

########################################################################################################

# DockerでSpring Bootアプリを起動する手順

このプロジェクトでは、Spring Bootアプリ（todo-backend）をDockerで実行できるように構成されています。以下の手順に従ってDockerコンテナを起動します。

## ✅ 前提条件

- PostgreSQLがホストPC上で起動していること
- `todo-backend/.env` ファイルに正しいDBパスワードが記載されていること

## 📁 ディレクトリ構成（抜粋）

```
todo-app/
├── todo-backend/
│   ├── .env
│   ├── Dockerfile
│   ├── build/libs/todo-backend-0.0.1-SNAPSHOT.jar
│   └── src/main/resources/application.properties
```

## 🧭 実行手順

### 1. `.env` ファイルの作成

`todo-backend/` に `.env` を作成：

```
DB_PASSWORD=あなたのPostgreSQLパスワード
```

### 2. `application.properties` の確認

```properties
# todo-backend/src/main/resources/application.properties
spring.datasource.url=jdbc:postgresql://host.docker.internal:5432/todoapp
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}
```

### 3. JARファイルのビルド

```bash
cd todo-backend
./gradlew bootJar
```

### 4. Dockerイメージのビルド

```bash
docker build -t todo-backend-app .
```

### 5. Dockerコンテナの起動

```bash
docker run -p 8080:8080 --env-file .env todo-backend-app
```

### 6. 動作確認

ブラウザで以下にアクセス：

```
http://localhost:8080/
```

---

以上で、Spring Boot アプリが Docker 上で起動し、React アプリが表示されます。

