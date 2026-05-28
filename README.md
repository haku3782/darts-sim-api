graph TD
    %% 登場人物
    Dev((あなた<br/>開発者))
    User((ユーザー<br/>ブラウザ / スマホ))

    %% クラウドインフラ
    subgraph "Frontend Hosting (Vercel)"
        React[React / TypeScript<br/>UI & 3D描画]
    end

    subgraph "Backend Hosting (Render)"
        Spring[Spring Boot / Java 21<br/>物理演算 API]
        Docker((Docker Container))
        Spring --- Docker
    end

    subgraph "CI/CD & Source Control"
        Repo[(GitHub<br/>Repositories)]
        Actions[GitHub Actions<br/>自動テスト & ビルド]
    end

    %% デプロイのフロー（裏側）
    Dev -->|1. コードをPush| Repo
    Repo -->|2. トリガー| Actions
    Actions -->|3. テスト合格でデプロイ| Spring
    Repo -->|Vercelが自動検知してデプロイ| React

    %% ユーザーのフロー（表側）
    User -->|A. サイトにアクセス| React
    React -->|B. REST API (JSON)<br/>ダーツの計算を依頼| Spring
    Spring -.->|C. 計算結果を返す| React
