<template>
  <div style="font-size: 12px">
    <component :is="currentIdentification" />
  </div>
</template>

<script setup>
import { computed, defineAsyncComponent } from 'vue'

const props = defineProps({ code: String })

const IDENTIFICATION_MAP = {
  nhrs: defineAsyncComponent(() => import('./identification/NhrsIdentification.vue')),
  bot: defineAsyncComponent(() => import('./identification/BotIdentification.vue')),
  pal: defineAsyncComponent(() => import('./identification/PalIdentification.vue')),
  zoo: defineAsyncComponent(() => import('./identification/ZooIndentification.vue')),
  common: defineAsyncComponent(() => import('./identification/CommonIdentification.vue'))
}

const CODE_MAP = {
  nhrs: ['NHRS', 'SMTP_INV', 'SMTP_SPPLST', 'NRMLIG', 'NRMMIN', 'NRMNOD'],
  pal: ['pb', 'pz'],
  bot: ['algae', 'fungi', 'mosses', 'vp'],
  zoo: ['AV', 'MA', 'ev', 'et', 'PI', 'HE']
}

const currentIdentification = computed(() => {
  const code = props.code

  if (CODE_MAP.nhrs.includes(code)) return IDENTIFICATION_MAP.nhrs
  if (CODE_MAP.bot.includes(code)) return IDENTIFICATION_MAP.bot
  if (CODE_MAP.pal.includes(code)) return IDENTIFICATION_MAP.pal
  if (CODE_MAP.zoo.includes(code)) return IDENTIFICATION_MAP.zoo

  return IDENTIFICATION_MAP.common
})
</script>

<style scoped></style>
