<template>
  <div class="grid" no-gutters>
    <image-filt-link
      v-bind:imageSource="botImageSource"
      v-bind:text="botanicalCollection"
      @searchCollection="handleBotCollectionSearch"
    />
    <image-filt-link
      v-bind:imageSource="zooImageSource"
      v-bind:text="zooCollection"
      @searchCollection="handleZooCollectionSearch"
    />
    <image-filt-link
      v-bind:imageSource="paleoImageSource"
      v-bind:text="paleoCollection"
      @searchCollection="handlePaleoCollectionSearch"
    />
    <image-filt-link
      v-bind:imageSource="geoImageSource"
      v-bind:text="geoCollection"
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
  search(botanyGroup, 'bot')
}

function handleZooCollectionSearch() {
  const zooGroup = import.meta.env.VITE_ZOO_GROUP
  search(zooGroup, 'zoo')
}

function handlePaleoCollectionSearch() {
  const paleaGroup = import.meta.env.VITE_PALEA_GROUP
  search(paleaGroup, 'pal')
}

function handleGeoCollectionSearch() {
  const geoGroup = import.meta.env.VITE_GEO_GROUP
  search(geoGroup, 'geo')
}

function search(value, dataResource) {
  const searchText = '*'
  store.commit('setSearchText', searchText)
  store.commit('setScientificName', null)

  store.commit('setFilterCoordinates', false)
  store.commit('setFilterInSweden', false)
  store.commit('setFilterImage', false)
  store.commit('setFilterType', false)

  store.commit('setSelectedCollection', value)
  store.commit('setDataResource', dataResource)

  store.commit('setFields', [])
  store.commit('setDates', null)

  const params = new URLSearchParams({
    text: searchText,
    collectionCode: value
  })

  store.commit('setSearchParams', params)

  emits('searchCollections', value)
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
