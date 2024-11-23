<template>
  <Card>
    <template #title>
      <h2>Statistic Data</h2>
    </template>
    <template #content>
      <CollectionStatisticChart v-bind:dataset="zooCollections" v-bind:dataGroup="zooDataGroup" />
    </template>
  </Card>
</template>
<script setup>
import { computed } from 'vue'
import CollectionStatisticChart from '../components/CollectionStatisticChart.vue'

const zooCollections = computed(() => {
  return import.meta.env.VITE_SUPPORTED_ZOO_DATASET
})
const botanyCollections = computed(() => {
  return import.meta.env.VITE_SUPPORTED_BOTANY_DATASET
})
const geoCollections = computed(() => {
  return import.meta.env.VITE_SUPPORTED_GEO
})
const paleoCollections = computed(() => {
  return import.meta.env.VITE_SUPPORTED_PALEO_DATASET
})

const zooDataGroup = computed(() => {
  return 'startPage.zooCollection'
})
</script>
<style scoped>
.footerDiv {
  width: 500px;
}

.footerlabel {
  font-weight: bold;
}
</style>
