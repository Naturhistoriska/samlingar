<template>
  <div style="font-size: 12px">
    <bird-dataset v-if="isBirdCollection" />
    <bot-dataset v-else-if="isBotCollection" />
    <fish-dataset v-else-if="isFishCollection" />
    <GeoDataset v-else-if="isGeoCollection" />
    <invertebrate-dataset v-else-if="isInvertebrate" />
    <nhrs-dataset v-else-if="isNhrsCollection" />
    <pal-dataset v-else-if="isPalCollection" />
    <common-dataset v-else />
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'

import BirdDataset from './dataset/BirdAndMammlDataset.vue'
import BotDataset from './dataset/BotDataset.vue'

import CommonDataset from './dataset/CommonDataset.vue'
import FishDataset from './dataset/FishDataset.vue'
import GeoDataset from './dataset/GeoDataset.vue'
import InvertebrateDataset from './dataset/InvertebrateDataset.vue'
import NhrsDataset from './dataset/NhrsDataset.vue'
import PalDataset from './dataset/PalDataset.vue'

const props = defineProps(['code'])

const isBirdCollection = ref(false)
const isBotCollection = ref(false)
const isFishCollection = ref(false)
const isGeoCollection = ref(false)
const isInvertebrate = ref(false)

const isNhrsCollection = ref(false)
const isPalCollection = ref(false)

onMounted(async () => {
  console.log('code...', props.code)

  isBirdCollection.value = props.code === 'AV' || props.code === 'MA'
  isBotCollection.value =
    props.code === 'algae' || props.code === 'fungi' || props.code === 'mosses'
  isFishCollection.value = props.code === 'PI' || props.code === 'HE'
  isGeoCollection.value =
    props.code === 'NRMMIN' || props.code === 'NRMNOD' || props.code === 'NRMLIG'
  isInvertebrate.value = props.code === 'ev' || props.code === 'et'
  isNhrsCollection.value =
    props.code === 'NHRS' || props.code === 'SMTP_INV' || props.code === 'SMTP_SPPLST'
  isPalCollection.value = props.code === 'pb' || props.code === 'pz'
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
