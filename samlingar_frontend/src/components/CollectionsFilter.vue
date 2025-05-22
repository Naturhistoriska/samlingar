<template>
  <div class="grid" no-gutters>
    <image-filt-link
      v-bind:text="botanicalCollection"
      v-bind:imageSource="botImageSource"
      @searchCollection="handleBotCollectionSearch"
    />
    <image-filt-link
      v-bind:text="zooCollection"
      v-bind:imageSource="zooImageSource"
      @searchCollection="handleZooCollectionSearch"
    />

    <image-filt-link
      v-bind:text="paleoCollection"
      v-bind:imageSource="paleoImageSource"
      @searchCollection="handlePaleoCollectionSearch"
    />

    <image-filt-link
      v-bind:text="geoCollection"
      v-bind:imageSource="geoImageSource"
      @searchCollection="handleGeoCollectionSearch"
    />
  </div>
</template>
<script setup>
import ImageFiltLink from './baseComponents/ImageFiltLink.vue'
import { computed, ref } from 'vue'

const botanicalCollection = ref('startPage.botanicalCollection')
const botImageSource = ref('/paucidentatus.jpg')

const zooCollection = ref('startPage.zooCollection')
const zooImageSource = ref('/Zoologiska.jpg')

const paleoCollection = ref('startPage.palaeCollection')
const paleoImageSource = ref('/Angelinoceras.jpg')

const geoCollection = ref('startPage.geoCollection')
const geoImageSource = ref('/Bergkristall.jpg')

const emits = defineEmits([
  'searchBotanyCollections',
  'searchZooCollections',
  'searchPaleaCollections',
  'searchGeoCollections'
])

function handleBotCollectionSearch() {
  emits('searchBotanyCollections')
}

function handleZooCollectionSearch() {
  emits('searchZooCollections')
}
function handlePaleoCollectionSearch() {
  emits('searchPaleaCollections')
}
function handleGeoCollectionSearch() {
  emits('searchGeoCollections')
}
</script>
<style scoped>
.collectionDivLink {
  min-width: 100%;
  cursor: pointer;
}
.collectionTextLink {
  float: left;
  padding-top: 50px;
}
.p-button-link {
  font-size: 18px;
}
</style>
