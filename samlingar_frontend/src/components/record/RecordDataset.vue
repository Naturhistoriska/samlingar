<template>
  <div style="font-size: 12px">
    <component :is="currentDataset" />
  </div>
</template>

<script setup>
import { computed, defineAsyncComponent } from 'vue'

const props = defineProps({ code: String })

const DATASET_MAP = {
  bird: defineAsyncComponent(() => import('./dataset/BirdAndMammlDataset.vue')),
  fish: defineAsyncComponent(() => import('./dataset/FishDataset.vue')),
  nhrs: defineAsyncComponent(() => import('./dataset/NhrsDataset.vue')),
  ev: defineAsyncComponent(() => import('./dataset/InvertebrateDataset.vue')),
  geo: defineAsyncComponent(() => import('./dataset/FishDataset.vue')),
  bot: defineAsyncComponent(() => import('./dataset/BotDataset.vue')),
  pal: defineAsyncComponent(() => import('./dataset/GeoDataset.vue')),
  common: defineAsyncComponent(() => import('./dataset/CommonDataset.vue'))
}

const CODE_MAP = {
  bird: ['AV', 'MA'],
  fish: ['PI', 'HE'],
  ev: ['ev', 'et'],
  nhrs: ['NHRS', 'SMTP_INV', 'SMTP_SPPLST'],
  geo: ['NRMLIG', 'NRMMIN', 'NRMNOD'],
  pal: ['pb', 'pz'],
  bot: ['algae', 'fungi', 'mosses', 'vp']
}

const currentDataset = computed(() => {
  const code = props.code

  if (CODE_MAP.bird.includes(code)) return DATASET_MAP.bird
  if (CODE_MAP.fish.includes(code)) return DATASET_MAP.fish
  if (CODE_MAP.nhrs.includes(code)) return DATASET_MAP.nhrs
  if (CODE_MAP.ev.includes(code)) return DATASET_MAP.ev
  if (CODE_MAP.geo.includes(code)) return DATASET_MAP.geo
  if (CODE_MAP.bot.includes(code)) return DATASET_MAP.bot
  if (CODE_MAP.pal.includes(code)) return DATASET_MAP.pal

  return DATASET_MAP.common
})
</script>

<style scoped></style>
