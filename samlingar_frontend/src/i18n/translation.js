import i18n from '@/i18n'

const Trans = {
  get defaultLocale() {
    returnimport.meta.env.VITE_DEFAULT_LOCALE
  },
  get supportedLocales() {
    return import.meta.env.VITE_SUPPORTED_LOCALES.split(',')
  },
  set currentLocale(newLocale) {
    i18n.global.locale.value = newLocale
  },
  async switchLanguage(newLocale) {
    Trans.currentLocale = newLocale
    document.querySelector('html').setAttribute('lang', newLocale)
  },
  i18nRoute(to) {
    return {
      ...to,
      params: {
        locale: this.currentLocale,
        ...to.params
      }
    }
  }
}

export default Trans
