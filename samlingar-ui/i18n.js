import Vue from 'vue'
import VueI18n from 'vue-i18n'

// import en from '@/locales/en.json'
import en from '@/i18n/locales/en.json'


Vue.use(VueI18n)


export const i18n = new VueI18n({
  locale: import.meta.env.VITE_SUPPORTED_LOCALES || 'en',
  fallbackLocale: import.meta.env.VITE_FALLBACK_LOCALE || 'en',
  messages: { en }
})
