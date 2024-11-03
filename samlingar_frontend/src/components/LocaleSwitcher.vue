<template>
  <div>
    <Button
      :label="$t(`locale.${selectedLocale}`)"
      icon="pi pi-globe"
      iconPos="top"
      @click="toggle"
      aria-haspopup="true"
      aria-controls="overlay_tmenu"
      plain
      text
    />
    <Menu id="overlay_tmenu" ref="menu" :model="items" :popup="true" class="w-min flex">
      <template #item="{ item }">
        <Button :label="item.label" @click="select(item.label)" plain text />
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
          label: 'English'
        },
        {
          label: 'Svenska'
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
      this.selectedLocale = clocale === 'English' ? 'en' : 'sv'
    }
  }
}
</script>
<style scoped>
.ui-menu {
  max-width: 50px;
}
</style>
