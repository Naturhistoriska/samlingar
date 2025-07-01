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
import { ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const botanicalCollection = ref('startPage.botanicalCollection')
const botImageSource = ref('/paucidentatus.jpg')

const zooCollection = ref('startPage.zooCollection')
const zooImageSource = ref('/Zoologiska.jpg')

const paleoCollection = ref('startPage.palaeCollection')
const paleoImageSource = ref('/Angelinoceras.jpg')

const geoCollection = ref('startPage.geoCollection')
const geoImageSource = ref('/Bergkristall.jpg')

const emits = defineEmits(['searchCollections'])

function handleBotCollectionSearch() {
  const botanyGroup = import.meta.env.VITE_BOTANY_GROUP
  search(botanyGroup)
}

function handleZooCollectionSearch() {
  const zooGroup = import.meta.env.VITE_ZOO_GROUP
  search(zooGroup)
}

function handlePaleoCollectionSearch() {
  const paleaGroup = import.meta.env.VITE_PALEA_GROUP
  search(paleaGroup)
}

function handleGeoCollectionSearch() {
  const geoGroup = import.meta.env.VITE_GEO_GROUP
  search(geoGroup)
}

function search(value) {
  const searchText = '*'
  store.commit('setSearchText', searchText)
  store.commit('setScientificName', null)

  store.commit('setFilterCoordinates', false)
  store.commit('setFilterInSweden', false)
  store.commit('setFilterImage', false)
  store.commit('setFilterType', false)

  store.commit('setCollectionGroup', value)

  emits('searchCollections')
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
