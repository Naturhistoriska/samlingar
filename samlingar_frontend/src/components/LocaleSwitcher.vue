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
        <Button class="btn" :label="item.label" text style="color: #000 !important" />
      </template>
    </Menu>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import Tr from '@/i18n/translation'
import Button from 'primevue/button'
import Menu from 'primevue/menu'

// i18n and locale state
const { t, locale } = useI18n()

// current selected locale
const selectedLocale = ref('en')

// PrimeVue Menu ref
const menu = ref(null)

// function to open/close the Menu
const toggle = (event) => {
  menu.value.toggle(event)
}

// update locale
const select = (clocale) => {
  selectedLocale.value = clocale
}

// watch locale change and update translation
watch(selectedLocale, (newLocale) => {
  Tr.switchLanguage(newLocale)
})

// menu items
const items = [
  {
    label: 'English',
    command: () => select('en')
  },
  {
    label: 'Svenska',
    command: () => select('sv')
  }
]
</script>
<style scoped>
::v-deep(.p-menu.p-component) {
  background-color: #333 !important;
}
.ui-menu {
  max-width: 50px;
}

.p-button-text {
  color: #fff !important;
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
