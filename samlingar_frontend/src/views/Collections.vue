<template>
  <Card class="w-full p-2">
    <template #title>
      <h2>{{ $t('statisticData.statisticData') }}</h2>
      <statistic-chart />
    </template>
    <template #content>
      <div class="grid gap-4">
        <div class="w-full">
          <CollectionStatisticChart
            id="zoo"
            v-bind:dataset="zooCollections"
            v-bind:dataGroup="zooDataGroup"
            v-bind:dataResource="zooResource"
          />
        </div>

        <div class="w-full">
          <CollectionStatisticChart
            id="botany"
            v-bind:dataset="botanyCollections"
            v-bind:dataGroup="botanyDataGroup"
            v-bind:dataResource="botResource"
          />
        </div>

        <div class="w-full">
          <CollectionStatisticChart
            id="paleo"
            v-bind:dataset="paleoCollections"
            v-bind:dataGroup="paleoDataGroup"
            v-bind:dataResource="palResource"
          />
        </div>
        <div class="w-full">
          <CollectionStatisticChart
            id="geo"
            v-bind:dataset="geoCollections"
            v-bind:dataGroup="geoDataGroup"
            v-bind:dataResource="geoResource"
          />
        </div>
      </div>
    </template>
  </Card>
</template>
<script setup>
import { computed } from 'vue'
import CollectionStatisticChart from '../components/CollectionStatisticChart.vue'
import StatisticChart from '../components/collections/CollectionsChart.vue'

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
  return 'dataResource.zooCollection'
})
const botanyDataGroup = computed(() => {
  return 'dataResource.botCollection'
})
const paleoDataGroup = computed(() => {
  return 'dataResource.palCollection'
})
const geoDataGroup = computed(() => {
  return 'dataResource.geoCollection'
})

const zooResource = computed(() => {
  return 'zoo'
})
const botResource = computed(() => {
  return 'bot'
})
const palResource = computed(() => {
  return 'pal'
})
const geoResource = computed(() => {
  return 'geo'
})
</script>
<style scoped>
/* Make charts responsive */
:deep(.chart-container),
:deep(canvas) {
  width: 100% !important;
  height: auto !important;
}
</style>
