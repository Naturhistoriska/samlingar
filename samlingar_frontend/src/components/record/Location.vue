<template>
  <div style="font-size: 12px">
    <component :is="currentLocation" />
  </div>
</template>

<script setup>
import { computed, defineAsyncComponent } from 'vue'

const props = defineProps({ code: String })

const LOCATION_MAP = {
  bird: defineAsyncComponent(() => import('./location/BirdLocation.vue')),
  bot: defineAsyncComponent(() => import('./location/BotLocation.vue')),
  ev: defineAsyncComponent(() => import('./location/EvLocation.vue')),
  fish: defineAsyncComponent(() => import('./location/FishLocation.vue')),
  nhrs: defineAsyncComponent(() => import('./location/NhrsLocation.vue')),
  pal: defineAsyncComponent(() => import('./location/PalLocation.vue')),
  common: defineAsyncComponent(() => import('./location/CommonLocation.vue'))
}

const CODE_MAP = {
  bird: ['AV', 'MA'],
  fish: ['PI', 'HE'],
  ev: ['ev', 'et'],
  nhrs: ['NHRS', 'SMTP_INV', 'SMTP_SPPLST', 'NRMLIG', 'NRMMIN', 'NRMNOD'],
  pal: ['pb', 'pz'],
  bot: ['algae', 'fungi', 'mosses', 'vp']
}

const currentLocation = computed(() => {
  const code = props.code

  if (CODE_MAP.bird.includes(code)) return LOCATION_MAP.bird
  if (CODE_MAP.fish.includes(code)) return LOCATION_MAP.fish
  if (CODE_MAP.ev.includes(code)) return LOCATION_MAP.ev
  if (CODE_MAP.nhrs.includes(code)) return LOCATION_MAP.nhrs
  if (CODE_MAP.bot.includes(code)) return LOCATION_MAP.bot
  if (CODE_MAP.pal.includes(code)) return LOCATION_MAP.pal

  return LOCATION_MAP.common
})
</script>

<style scoped></style>
