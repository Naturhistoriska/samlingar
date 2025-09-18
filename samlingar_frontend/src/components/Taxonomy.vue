<template>
  <div style="font-size: 12px">
    <nhrs-taxonomy v-if="isNhrsCollection" />
    <pal-taxonomy v-else-if="isPalCollection" />
    <common-taxonomy v-else />
    <!-- <ev-location v-if="isEvCollection" />

    <pal-location v-else-if="isPalCollection" />
    <bird-location v-else-if="isBirdAndMammalCollection" />
    <fish-location v-else-if="isFishAndHerpCollection" />
    <bot-location v-else-if="isBotCollection" />
    <common-location v-else /> -->
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'

import CommonTaxonomy from './taxonomy/CommonTaxonomy.vue'
import NhrsTaxonomy from './taxonomy/NhrsTaxonomy.vue'
import PalTaxonomy from './taxonomy/PalTaxonomy.vue'

// import BirdLocation from './taxonomy/BirdLocation.vue'
// import BotLocation from './location/BotLocation.vue'
//
// import EvLocation from './location/EvLocation.vue'
// import FishLocation from './location/FishLocation.vue'
//
//

const props = defineProps(['code'])

const isBirdAndMammalCollection = ref(false)
const isEvCollection = ref(false)
const isFishAndHerpCollection = ref(false)

const isBotCollection = ref(false)

const isNhrsCollection = ref(false)
const isPalCollection = ref(false)

onMounted(async () => {
  isBirdAndMammalCollection.value = props.code === 'AV' || props.code === 'MA'
  isEvCollection.value = props.code === 'ev' || props.code === 'et'
  isFishAndHerpCollection.value = props.code === 'PI' || props.code === 'HE'

  isNhrsCollection.value =
    props.code === 'NHRS' ||
    props.code === 'SMTP_INV' ||
    props.code === 'SMTP_SPPLST' ||
    props.code === 'NRMLIG' ||
    props.code === 'NRMMIN' ||
    props.code === 'NRMNOD'

  isPalCollection.value = props.code === 'pb' || props.code === 'pz'

  isBotCollection.value =
    props.code === 'algae' || props.code === 'fungi' || props.code === 'mosses'
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
