<template>
  <v-layout class="overflow-visible">
    <v-bottom-navigation bg-color="secondary" dense class="pl-3">
      <v-btn dense>
        <router-link to="/">{{ $t('nav.home') }}</router-link>
      </v-btn>

      <v-btn dense>
        <router-link to="/about">{{ $t('nav.about') }}</router-link>
      </v-btn>

      <v-btn dense>
        <router-link to="/contact">{{ $t('nav.contactUs') }}</router-link>
      </v-btn>
      <v-spacer></v-spacer>
      <v-select
        @change="switchLanguage"
        :items="supportedLocales"
        :label="$t('common.selectLanguage')"
        :value="selectedLocale"
        :selected="locale === selectedLocale"
        class="pr-5"
      >
        <option
          v-for="sLocale in supportedLocales"
          :key="`locale-${sLocale}`"
          :value="sLocale"
          :selected="locale === sLocale"
        >
          {{ t(`locale.${sLocale}`) }}
        </option>
      </v-select>
    </v-bottom-navigation>
  </v-layout>

  <!-- <v-toolbar dark color="primary" class="pa-0" density="compact">
    <v-tabs>
      <v-tab
        ><router-link to="/">{{ $t('nav.home') }}</router-link></v-tab
      >
      <v-tab
        ><router-link to="/about">{{ $t('nav.about') }}</router-link></v-tab
      >
      <v-tab
        ><router-link to="/contact">{{ $t('nav.contactUs') }}</router-link></v-tab
      >
    </v-tabs>
    <v-spacer></v-spacer>
    <v-tabs>
      <v-tab>en </v-tab>
      <v-tab>sv</v-tab>
    </v-tabs>
  </v-toolbar> -->

  <!-- <v-layout class="overflow-visible justify-end" justify-end>
    <v-bottom-navigation v-model="value" bg-color="primary" dense>
      <v-row no-gutters>
        <v-col class="pl-3" cols="10" dense>
          <v-tabs>
            <v-tab
              ><router-link to="/">{{ $t('nav.home') }}</router-link></v-tab
            >
            <v-tab
              ><router-link to="/about">{{ $t('nav.about') }}</router-link></v-tab
            >
            <v-tab
              ><router-link to="/contact">{{ $t('nav.contactUs') }}</router-link></v-tab
            >
          </v-tabs>
        </v-col>

        <v-col class="pl-3 text-right" cols="2">
          <select @change="switchLanguage" class="white--text">
            <option
              v-for="sLocale in supportedLocales"
              :key="`locale-${sLocale}`"
              :value="sLocale"
              :selected="locale === sLocale"
            >
              {{ t(`locale.${sLocale}`) }}
            </option>
          </select>
        </v-col>
      </v-row>
    </v-bottom-navigation>
  </v-layout> -->
</template>
<script>
import { useI18n } from 'vue-i18n'
import Tr from '@/i18n/translation'
export default {
  setup() {
    const { t, locale } = useI18n()
    const supportedLocales = Tr.supportedLocales
    const switchLanguage = async (event) => {
      const newLocale = event.target.value
      await Tr.switchLanguage(newLocale)
    }
    return { t, locale, supportedLocales, switchLanguage }
  }
  // mounted() {
  //   // Reformat vuetify Headers
  //   $('.v-expansion-panel__header').toggleClass('pl-2 pa-0 ma-0', true)
  //   $('.v-expansion-panel__header').css('min-height', '30px')
  // }
}
</script>

<!-- <template>
  <v-row no-gutters>
    <v-col class="pl-3" cols="10" dense>
      <v-tabs>
        <v-tab
          ><router-link to="/">{{ $t('nav.home') }}</router-link></v-tab
        >
        <v-tab
          ><router-link to="/about">{{ $t('nav.about') }}</router-link></v-tab
        >
        <v-tab
          ><router-link to="/contact">{{ $t('nav.contactUs') }}</router-link></v-tab
        >
      </v-tabs>
    </v-col>

    <v-col class="pl-3 text-right" cols="2">
      <select @change="switchLanguage">
        <option
          v-for="sLocale in supportedLocales"
          :key="`locale-${sLocale}`"
          :value="sLocale"
          :selected="locale === sLocale"
        >
          {{ t(`locale.${sLocale}`) }}
        </option>
      </select>
    </v-col>
  </v-row>
</template> -->

<!-- <script>
// import LanguageSwitcher from '@/components/LanguageSwitcher.vue'
import { useI18n } from 'vue-i18n'
import Tr from '@/i18n/translation'
export default {
  // components: { LanguageSwitcher }
  setup() {
    const { t, locale } = useI18n()
    const supportedLocales = Tr.supportedLocales
    const switchLanguage = async (event) => {
      const newLocale = event.target.value
      await Tr.switchLanguage(newLocale)
    }
    return { t, locale, supportedLocales, switchLanguage }
  }
}
</script> -->
<style scoped>
/* .v-toolbar__content,
.v-toolbar__extension {
  align-items: self-end !important;
} */

.v-expansion-panels.condensed .v-expansion-panel-header {
  padding-top: 2px;
  padding-bottom: 2px;
  min-height: auto;
}
.v-expansion-panels.condensed .v-expansion-panel--active .v-expansion-panel-header {
  padding-top: 8px;
  padding-bottom: 8px;
}
.v-expansion-panels.condensed .v-expansion-panel--active:not(:first-child),
.v-expansion-panels.condensed .v-expansion-panel--active + .v-expansion-panel {
  margin-top: 4px;
}
.v-tab.v-tab.v-btn {
  height: 30px;
}
</style>
