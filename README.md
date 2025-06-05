# Todo App

シンプルな **Spring Boot + React + PostgreSQL** の Todo アプリケーションです。  
Docker と Docker Compose を使って、アプリとデータベースをまとめて立ち上げられるように構成しています。

---

## ⚙️ フォルダ構成

```
todo-app/                ← リポジトリルート
├── .gitignore
├── README.md            ← 本ファイル
├── .env                 ← 環境変数ファイル（DBパスワード等）
├── Dockerfile           ← マルチステージビルド用
├── docker-compose.yml   ← アプリ + DB 一括起動用
├── todo-frontend/       ← React フロントエンド
│   ├── package.json
│   ├── src/
│   └── …
└── todo-backend/        ← Spring Boot バックエンド
    ├── build.gradle
    ├── src/
    │   └── main/resources/application.properties
    └── …
```

---

## 📋 前提条件

- Docker & Docker Compose がインストール済み
- （ローカル開発時のみ）Java 21、Gradle、Node.js が使える環境
- PostgreSQL がインストール済み
- （todoappデータベースを事前に構築すること）
-   ※　psql -U postgres
-   ※　CREATE DATABASE todoapp;
- PostgreSQL のパスワードを `.env` に設定しておくこと

---

## 🔧 環境変数の設定

プロジェクトルートに **`.env`** を作成し、PostgreSQL のパスワードを記載します。

```env
# todo-app/.env
DB_PASSWORD=あなたのPostgreSQLパスワード
```

> **注意**: `.env` は `.gitignore` に含めているため、Git には絶対コミットしないでください。

---

## 🚀 Docker Compose で一括起動

Docker Compose を使えば、DB とアプリを同時に立ち上げられます。以下のコマンドを実行してください。

```bash
# プロジェクトルートで
docker-compose up --build
```

- `--build`：イメージを再ビルド

サービス起動後、ブラウザで以下にアクセスします。

```
http://localhost:8080/
```

### 停止・削除

```bash
docker-compose down
```

---

## 🏗️ 手動ビルド & 実行

### 1. フロントエンド（React）をビルド

```bash
cd todo-frontend
npm install
npm run build
```

### 2. バックエンド（Spring Boot）をビルド

```bash
cd ../todo-backend
./gradlew clean bootJar
```

### 3. Docker イメージをビルド

プロジェクトルートに戻って、マルチステージ Dockerfile でイメージを作成します。

```bash
cd ..
docker build -t todo-app-full .
```

### 4. コンテナ起動

```bash
docker run -p 8080:8080 --env-file .env todo-app-full
```

---

## 📚 Dockerを使用せずにローカル起動する方法

- その１
  ～/todo-app/todo-backend/src/main/resources/application.properties
  の
  spring.datasource.url の設定を変更する。
  spring.datasource.password の箇所に直接DBパスワードを書く。（こうしないと動かない原因は調査中）

- その２
  ～/todo-app/todo-frontend/dist/
  のファイル一式を
  ～/todo-app/todo-backend/src/main/resources/static/
  にコピーする。

- その３
  ～/todo-app/todo-backend
  で
  ./gradlew bootRun
---

## 🖋️ 変更履歴

- **Multi‑Stage Dockerfile** をルートに追加
- **docker-compose.yml** で PostgreSQL とアプリをまとめて起動
- `.gitignore` を調整し、生成物を Git 管理外に

---

## ⚙️ GitHub Actions / CI 連携のヒント

- `docker-compose up --build` → テスト → `docker-compose down`  
- プルリクごとに自動ビルド＆動作確認を行う設定が可能です。

---

以上です！  
何かご不明点や追加したい情報があれば、お知らせください 😊
