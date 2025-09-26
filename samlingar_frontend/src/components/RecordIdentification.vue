<template>
  <div style="font-size: 12px">
    <zoo-identification v-if="isZooCollection" />
    <bot-identification v-else-if="isBotCollection" />
    <pal-identification v-else-if="isPalCollection" />
    <nhrs-identification v-else-if="isNhrsCollection" />
    <common-identification v-else />
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'

import ZooIdentification from './identification/ZooIndentification.vue'
import CommonIdentification from './identification/CommonIdentification.vue'
import PalIdentification from './identification/PalIdentification.vue'
import NhrsIdentification from './identification/NhrsIdentification.vue'

import BotIdentification from './identification/BotIdentification.vue'

const props = defineProps(['code'])

const isZooCollection = ref(false)
const isEvCollection = ref(false)

const isBotCollection = ref(false)

const isNhrsCollection = ref(false)
const isPalCollection = ref(false)

onMounted(async () => {
  isZooCollection.value =
    props.code === 'AV' ||
    props.code === 'MA' ||
    props.code === 'PI' ||
    props.code === 'HE' ||
    props.code === 'ev' ||
    props.code === 'et'

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
