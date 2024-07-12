import { createI18n } from 'vue-i18n'
import pluralRules from './rules/pluralization'

import en from "./locales/en.json" // <--- add this
import sv from "./locales/sv.json" // <--- add this

// const messages = {
//   en: {
//     nav: {
//       home: 'Home',
//       about: 'About',
//       contactUs: 'Contact us'
//     },
//     title: 'The Swedish Museum of Natural History Collections',
//     search: 'Search',
//     home: {
//       header: 'Welcome to the Vue 3 I18n tutorial!',
//       created_by: 'This tutorial was brought to you by Lokalise.'
//     },
//     about: {
//       header: 'About us'
//     },
//     results: {
//       searchResults: 'Search Results',
//       num_results: '0 record | {n} record | {n} records'
//     }
//   },
//   sv: {
//     nav: {
//       home: 'Hem',
//       about: 'Om',
//       contactUs: 'Kontakta oss'
//     },
//     title: 'Naturhistoriska riksmuseets samlingar',
//     search: 'Sök',
//     home: {
//       header: 'Welcome to the Vue 3 I18n tutorial!',
//       created_by: 'This tutorial was brought to you by Lokalise.'
//     },
//     about: {
//       header: 'About us'
//     },
//     results: {
//       searchResults: 'Search Results',
//       num_results: '0 record | {n} record | {n} records'
//     }
//   }
// }


export default createI18n({
  locale: import.meta.env.VITE_DEFAULT_LOCALE,
  fallbackLocale: import.meta.env.VITE_FALLBACK_LOCALE,
  legacy: false,
  globalInjection: true,
  messages: {
    en,
    sv
  },
  pluralRules
})

