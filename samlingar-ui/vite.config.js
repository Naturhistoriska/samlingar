import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import Vue from '@vitejs/plugin-vue'
import { resolve, dirname } from 'node:path'
import VueI18nPlugin from '@intlify/unplugin-vue-i18n/vite'

import Components from 'unplugin-vue-components/vite'
import Vuetify, { transformAssetUrls } from 'vite-plugin-vuetify'
// import ViteFonts from 'unplugin-fonts/vite'

// https://vitejs.dev/config/

export default defineConfig({
  plugins: [
    Vue({
      template: { transformAssetUrls }
    }),
    Vuetify(),
    Components(),
    VueI18nPlugin({
      // runtimeOnly: false, // <--- add this if localization in production not working
      include: resolve(dirname(fileURLToPath(import.meta.url)), './src/i18n/locales/**') // provide a path to the folder where you'll store translation data (see below)
    })
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})
