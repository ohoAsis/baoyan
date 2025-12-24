import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
	plugins: [vue()],
	server: {
		port: 3000,
		proxy: {
			// 为登录请求添加单独的代理规则，确保它直接请求到后端的/api/auth/login
			'/api/auth/login': {
				target: 'http://localhost:8081',
				changeOrigin: true,
				// 不重写路径，保留/api前缀
				followRedirects: false // 不跟随302重定向
			},
			// 其他/api请求的代理规则
			'/api': {
				target: 'http://localhost:8081',
				changeOrigin: true,
				// 不重写路径，保留/api前缀，因为后端API路径包含/api
			}
		}
	}
})
