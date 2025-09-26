<template>
  <div style="font-size: 12px">
    <zoo-event-data v-if="isZooCollection" />
    <bot-event-data v-else-if="isBotCollection" />
    <ev-event-data v-else-if="isEvCollection" />
    <nhrs-event-data v-else-if="isNhrsCollection" />
    <pal-event-data v-else-if="isPalCollection" />
    <common-event-data v-else />
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'

import BotEventData from './event/BotEventData.vue'
import ZooEventData from './event/ZooEventData.vue'
import CommonEventData from './event/CommonEventData.vue'
import EvEventData from './event/EvEventData.vue'
import NhrsEventData from './event/NhrsEventData.vue'
import PalEventData from './event/PalEventData.vue'

const props = defineProps(['code'])

const isZooCollection = ref(false)
const isEvCollection = ref(false)

const isBotCollection = ref(false)

const isNhrsCollection = ref(false)
const isPalCollection = ref(false)

onMounted(async () => {
  console.log('code...', props.code)

  isZooCollection.value =
    props.code === 'AV' || props.code === 'MA' || props.code === 'PI' || props.code === 'HE'
  isEvCollection.value = props.code === 'ev' || props.code === 'et'
  isNhrsCollection.value =
    props.code === 'NHRS' ||
    props.code === 'SMTP_INV' ||
    props.code === 'SMTP_SPPLST' ||
    props.code === 'NRMLIG' ||
    props.code === 'NRMMIN' ||
    props.code === 'NRMNOD'

  isPalCollection.value = props.code === 'pb' || props.code === 'pz'

  isBotCollection.value =
    props.code === 'algae' ||
    props.code === 'fungi' ||
    props.code === 'mosses' ||
    props.code === 'vp'
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
