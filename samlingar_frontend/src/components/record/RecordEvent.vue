<template>
  <div style="font-size: 12px">
    <component :is="currentEvent" />
  </div>
</template>

<script setup>
import { computed, defineAsyncComponent } from 'vue'

const props = defineProps({ code: String })

const EVENT_MAP = {
  bird: defineAsyncComponent(() => import('./event/ZooEventData.vue')),
  bot: defineAsyncComponent(() => import('./event/BotEventData.vue')),
  common: defineAsyncComponent(() => import('./event/CommonEventData.vue')),

  nhrs: defineAsyncComponent(() => import('./event/NhrsEventData.vue')),
  pal: defineAsyncComponent(() => import('./event/PalEventData.vue')),
  zoo: defineAsyncComponent(() => import('./event/EvEventData.vue'))
}

const CODE_MAP = {
  bird: ['AV', 'MA'],
  bot: ['algae', 'fungi', 'mosses', 'vp'],
  zoo: ['ev', 'et', 'PI', 'HE'],
  nhrs: ['NHRS', 'SMTP_INV', 'SMTP_SPPLST', 'NRMLIG', 'NRMMIN', 'NRMNOD'],
  pal: ['pb', 'pz']
}

const currentEvent = computed(() => {
  const code = props.code

  if (CODE_MAP.bird.includes(code)) return EVENT_MAP.bird
  if (CODE_MAP.zoo.includes(code)) return EVENT_MAP.zoo
  if (CODE_MAP.nhrs.includes(code)) return EVENT_MAP.nhrs
  if (CODE_MAP.bot.includes(code)) return EVENT_MAP.bot

  if (CODE_MAP.pal.includes(code)) return EVENT_MAP.pal

  return EVENT_MAP.common
})
</script>

<style scoped></style>
