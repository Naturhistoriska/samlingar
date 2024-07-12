<template>
  <v-dialog v-model="theDialogStatus" max-width="320" persistent>
    <v-card>
      <v-card-title>
        {{ $t('common.languageSetting') }}
      </v-card-title>

      <v-col cols="auto" class="pl-4 ml-4">
        <v-col cols="auto" class="pl-4 ml-4">
          <select @change="switchLanguage" :label="$t('common.selectLanguage')">
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

        <v-select
          class="locale-select"
          v-model="selectedLocale"
          :items="supportedLocales"
          :label="$t('common.selectLanguage')"
          :selected="locale === selectedLocale"
          :value="selectedLocale"
          @change="switchLanguage"
          hide-details
          prepend-inner-icon="mdi-web"
        ></v-select>
      </v-col>
      <v-card-actions>
        <v-btn
          :aria-label="$t('common.close')"
          right
          color="blue darken-1"
          text
          @click="closeDialog"
        >
          {{ $t('common.ok') }}
        </v-btn>
        <v-spacer></v-spacer>
      </v-card-actions>
    </v-card>
  </v-dialog>
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
  setup(props) {
    let theDialogStatus = props.dialogStatus
    const { t, locale } = useI18n()
    const supportedLocales = Tr.supportedLocales
    const switchLanguage = async (event) => {
      const newLocale = event.target.value
      await Tr.switchLanguage(newLocale)
    }

    return { theDialogStatus, t, locale, supportedLocales, switchLanguage }
  },
  watch: {
    dialogStatus: function () {
      this.theDialogStatus = this.dialogStatus
    }
  },
  computed: {},
  methods: {
    switchLocale() {
      const { t, locale } = useI18n()
      const supportedLocales = Tr.supportedLocales
      const switchLanguage = async (event) => {
        const newLocale = event.target.value
        await Tr.switchLanguage(newLocale)
      }
      return { t, locale, supportedLocales, switchLanguage }
    },
    closeDialog() {
      this.theDialogStatus = false
      this.$emit('close-dialog', this.selectedLocale)
    },
    openDialog() {
      this.theDialogStatus = true
    }
  }
}
</script>
<style scoped>
.locale-select {
  width: 250px;
}
</style>
