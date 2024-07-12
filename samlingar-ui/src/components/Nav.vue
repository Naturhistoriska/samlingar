<template>
  <v-bottom-navigation bg-color="secondary" dense class="pl-3">
    <v-tabs v-model="tabs" align-tabs="title">
      <v-tab>
        <router-link to="/">{{ $t('nav.home') }}</router-link>
      </v-tab>
      <v-tab>
        <router-link to="/about">{{ $t('nav.about') }}</router-link>
      </v-tab>
      <v-tab>
        <router-link to="/contact">{{ $t('nav.contactUs') }}</router-link>
      </v-tab>
    </v-tabs>

    <v-spacer></v-spacer>

    <v-menu>
      <template v-slot:activator="{ props }">
        <v-btn prepend-icon="mdi-web" v-bind="props" density="compact">
          {{ t(`locale.${selectedLocale}`) }}
        </v-btn>
      </template>

      <v-list dense>
        <v-list-item
          v-for="(sLocale, i) in supportedLocales"
          :key="i"
          :value="sLocale"
          :title="sLocale"
          @click="toggle(sLocale)"
        ></v-list-item>
      </v-list>
    </v-menu>
  </v-bottom-navigation>
</template>
<script>
import { useI18n } from 'vue-i18n'
import Tr from '@/i18n/translation'
export default {
  data() {
    return {
      selectedLocale: 'en',
      tabs: null
    }
  },
  setup() {
    const { t, locale } = useI18n()
    const supportedLocales = Tr.supportedLocales
    const switchLanguage = async (event) => {
      const newLocale = event.target.value
      await Tr.switchLanguage(newLocale)
    }

    return { t, locale, supportedLocales, switchLanguage }
  },

  watch: {
    $route(to) {
      // document.title = to.meta.title || 'Georg'
      const { name } = to
      this.routeName = name

      console.log('route name : ' + name + ' ... ' + this.tabs)

      switch (name) {
        case 'Home':
          this.tabs = 0
          break
        case 'About':
          this.tabs = 1
          break
        case 'Contact':
          this.tabs = 2
          break
        default:
          this.tabs = 0
      }
    },
    selectedLocale: function () {
      console.log('locale ... ' + this.selectedLocale)
      Tr.switchLanguage(this.selectedLocale)
    }
  },
  computed: {},
  methods: {
    toggle(sLocale) {
      this.selectedLocale = sLocale
    }
  }
}
</script>

<style scoped></style>
