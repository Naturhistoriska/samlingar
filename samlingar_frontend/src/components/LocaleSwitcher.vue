<template>
  <div>
    <Button
      :label="$t(`locale.${selectedLocale}`)"
      icon="pi pi-globe"
      iconPos="top"
      @click="toggle"
      aria-haspopup="true"
      aria-controls="overlay_tmenu"
      variant="text"
    />
    <Menu id="overlay_tmenu" ref="menu" :model="items" :popup="true" class="w-min flex">
      <template #item="{ item }" class="w-full">
        <Button class="btn" :label="item.label" text />
      </template>
    </Menu>
  </div>
</template>
<script>
import { useI18n } from 'vue-i18n'
import Tr from '@/i18n/translation'

export default {
  data() {
    return {
      selectedLocale: 'en',
      items: [
        {
          label: 'English',
          command: () => {
            this.select('en')
          }
        },
        {
          label: 'Svenska',
          command: () => {
            this.select('sv')
          }
        }
      ]
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
      Tr.switchLanguage(this.selectedLocale)
    }
  },
  methods: {
    toggle(event) {
      this.$refs.menu.toggle(event)
    },
    select(clocale) {
      console.log('clocale', clocale)
      //
      this.selectedLocale = clocale
    }
  }
}
</script>
<style scoped>
.ui-menu {
  max-width: 50px;
}
.p-button-text {
  /* color: #fff !important; */
  background: transparent !important;
  text-decoration: none;
}

.p-button-text:hover {
  /* color: #9dabf3 !important; */
  color: var(--p-emerald-500) !important;
  text-decoration: none !important;
  background: transparent !important;
}
</style>
