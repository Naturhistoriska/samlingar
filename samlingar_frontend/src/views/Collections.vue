<template>
  <Card>
    <template #title>
      <h2>Statistic Data</h2>
    </template>
    <template #content>
      <CollectionStatisticChart
        id="zoo"
        v-bind:dataset="zooCollections"
        v-bind:dataGroup="zooDataGroup"
      />
      <CollectionStatisticChart
        id="botany"
        v-bind:dataset="botanyCollections"
        v-bind:dataGroup="botanyDataGroup"
      />
      <CollectionStatisticChart
        id="paleo"
        v-bind:dataset="paleoCollections"
        v-bind:dataGroup="paleoDataGroup"
      />
      <CollectionStatisticChart
        id="geo"
        v-bind:dataset="geoCollections"
        v-bind:dataGroup="geoDataGroup"
      />
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
const botanyDataGroup = computed(() => {
  return 'startPage.botanicalCollection'
})
const paleoDataGroup = computed(() => {
  return 'startPage.palaeCollection'
})
const geoDataGroup = computed(() => {
  return 'startPage.geoCollection'
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
