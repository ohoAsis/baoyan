# 保研助手系统 - 前端项目

## 项目简介
基于 Vue 3 + Vite 构建的保研助手系统前端。

## 技术栈
- Vue 3
- Vite
- Vue Router
- Axios

## 项目结构
```
frontend/
├── src/
│   ├── components/     # 通用组件
│   ├── views/          # 页面组件
│   ├── router/         # 路由配置
│   ├── assets/         # 静态资源
│   ├── main.js         # 入口文件
│   └── App.vue         # 根组件
├── package.json
├── vite.config.js      # Vite 配置（含后端代理）
└── index.html
```

## 启动项目

### 安装依赖
```bash
npm install
```

### 启动开发服务器
```bash
npm run dev
```

项目将运行在 http://localhost:3000

### 构建生产版本
```bash
npm run build
```

## 后端代理配置
项目已配置代理，所有 `/api` 开头的请求会自动转发到 `http://localhost:8080`

