<template>
  <div style="font-size: 12px">
    <component :is="currentTaxonomy" />
  </div>
</template>

<script setup>
import { computed, defineAsyncComponent } from 'vue'

const props = defineProps({ code: String })

const TAXONOMY_MAP = {
  nhrs: defineAsyncComponent(() => import('./taxonomy/NhrsTaxonomy.vue')),
  bot: defineAsyncComponent(() => import('./taxonomy/BotTaxonomy.vue')),
  pal: defineAsyncComponent(() => import('./taxonomy/PalTaxonomy.vue')),
  zoo: defineAsyncComponent(() => import('./taxonomy/ZooTaxonomy.vue')),
  common: defineAsyncComponent(() => import('./taxonomy/CommonTaxonomy.vue'))
}

const CODE_MAP = {
  nhrs: ['NHRS', 'SMTP_INV', 'SMTP_SPPLST', 'NRMLIG', 'NRMMIN', 'NRMNOD'],
  pal: ['pb', 'pz'],
  bot: ['algae', 'fungi', 'mosses', 'vp'],
  zoo: ['AV', 'MA', 'ev', 'et', 'PI', 'HE']
}

const currentTaxonomy = computed(() => {
  const code = props.code

  if (CODE_MAP.nhrs.includes(code)) return TAXONOMY_MAP.nhrs
  if (CODE_MAP.bot.includes(code)) return TAXONOMY_MAP.bot
  if (CODE_MAP.pal.includes(code)) return TAXONOMY_MAP.pal
  if (CODE_MAP.zoo.includes(code)) return TAXONOMY_MAP.zoo

  return TAXONOMY_MAP.common
})
</script>

<style scoped>
.reducePadding {
  padding-top: 0;
  padding-bottom: 1px;
}
</style>
