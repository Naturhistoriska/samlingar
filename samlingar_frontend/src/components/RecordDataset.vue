<template>
  <div style="font-size: 12px">
    <GeoDataset v-if="isGeoCollection" />
    <invertebrate-dataset v-else-if="isInvertebrate" />
    <nhrs-dataset v-else-if="isNhrsCollection" />
    <pal-dataset v-else-if="isPalCollection" />
    <common-dataset v-else />
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'

import CommonDataset from './dataset/CommonDataset.vue'
import GeoDataset from './dataset/GeoDataset.vue'
import InvertebrateDataset from './dataset/InvertebrateDataset.vue'
import NhrsDataset from './dataset/NhrsDataset.vue'
import PalDataset from './dataset/PalDataset.vue'

const props = defineProps(['code'])

const isGeoCollection = ref(false)
const isInvertebrate = ref(false)

const isNhrsCollection = ref(false)
const isPalCollection = ref(false)
const isCommonCollection = ref(false)

onMounted(async () => {
  console.log('code...', props.code)

  isGeoCollection.value =
    props.code === 'NRMMIN' || props.code === 'NRMNOD' || props.code === 'NRMLIG'
  isInvertebrate.value = props.code === 'ev' || props.code === 'et'
  isNhrsCollection.value = props.code === 'NHRS'
  isPalCollection.value = props.code === 'pb' || props.code === 'pz'

  isCommonCollection.value =
    !isNhrsCollection.value && !isPalCollection.value && !isInvertebrate.value

  // console.log(
  //   'code...',
  //   props.code,
  //   isNhrsCollection.value,
  //   isPalCollection.value,
  //   isInvertebrate.value,
  //   isCommonCollection.value
  // )
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
