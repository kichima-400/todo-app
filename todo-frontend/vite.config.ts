// todo-frontend/vite.config.ts

import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  build: {
    outDir: 'dist', // ← React 内部に dist フォルダとして出力（標準）
  },
})

