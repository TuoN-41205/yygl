# 启孕企鹅小程序 AI 后端 MVP

## 启动前提

- Java 17
- Maven 3.9+

## 启动

```bash
cd /Users/xz/Documents/Codex/院外管理/启孕企鹅小程序AI版/backend
mvn spring-boot:run
```

## 主要接口

- `POST /api/v1/chat/send`
- `POST /api/v1/profile/{userId}`
- `GET /api/v1/profile/{userId}`
- `GET /api/v1/knowledge/search?q=`
- `POST /api/v1/knowledge`
- `GET /api/v1/experts`
- `POST /api/v1/report/analyze`
- `GET /api/v1/health`

## 模型接入

默认先走本地 fallback。要接真实模型，配置环境变量：

- `QYP_AI_BASE_URL`
- `QYP_AI_API_KEY`
- `QYP_AI_MODEL=5.4-mini-low`
- `QYP_AI_CHAT_PATH=/v1/chat/completions`

## 联调建议

- 前端当前默认请求 `http://127.0.0.1:8080/api/v1`
- 先不接真实模型时，后端会自动走本地 fallback，页面也能完整演示
- 等你补知识库后，只需要把知识条目打到 `POST /api/v1/knowledge` 即可
