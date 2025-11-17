import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
	plugins: [vue()],
	server: {
		port: 3000,
		proxy: {
			'/api': {
				target: 'http://localhost:8081',
				changeOrigin: true,
				// 不重写路径，保留/api前缀，因为后端API路径包含/api
			}
		}
	}
})
