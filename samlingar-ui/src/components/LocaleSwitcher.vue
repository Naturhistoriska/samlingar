<template>
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
</template>
<script>
import { useI18n } from 'vue-i18n'
import Tr from '@/i18n/translation'

export default {
  name: 'LocaleSwitcher',
  props: ['dialogStatus'],
  data() {
    return {
      selectedLocale: 'en'
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
<style scoped>
.locale-select {
  width: 250px;
}
</style>
