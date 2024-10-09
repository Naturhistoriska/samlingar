<template>
  <div>
    <div id="map" style="height: 60vh"></div>
    <div v-if="isLoading">
      <ProgressSpinner style="width: 50px; height: 50px" animationDuration=".5s" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useStore } from 'vuex'

// import 'leaflet/dist/leaflet.css'
// import 'leaflet.markercluster'

import ProgressSpinner from 'primevue/progressspinner'

import * as L from 'leaflet'
import 'leaflet.markercluster'

const emits = defineEmits(['search'])

const store = useStore()
const initialMap = ref(null)

const isLoading = ref(true)

onMounted(() => {
  initMap()
  const isDetailView = store.getters['showDetail']
  if (isDetailView) {
    addSingleMarker()
  } else {
    addClusterMarkers()
  }
})

function initMap() {
  initialMap.value = L.map('map').setView([59.0, 15.0], 6)
  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 4,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
  }).addTo(initialMap.value)

  L.Marker.prototype._animateZoom = function (opt) {
    if (!this._map) {
      return
    }
    const pos = this._map._latLngToNewLayerPoint(this._latlng, opt.zoom, opt.center).round()
    this._setPos(pos)
  }
}

watch(
  () => store.getters['mapRecords'],
  () => {
    console.log('map changed...')
    initialMap.value.eachLayer((layer) => {
      if (layer instanceof L.Marker) {
        layer.remove()
      }
    })
    addClusterMarkers()
  }
)

watch(
  () => isLoading,
  (newValue, oldValue) => {
    console.log('showloading changed...', isLoading)
    isLoading.value = newValue
  }
)

function addClusterMarkers() {
  console.log('addClusterMarkers')
  const isAdvanceSearch = store.getters['isAdvanceSearch']

  isLoading.value = true
  if (isAdvanceSearch) {
    // do advance search
  } else {
    // emits('search')
  }
  console.log('after search', isLoading.value)

  const records = store.getters['mapRecords']
  const markers = L.markerClusterGroup()

  // const layerGroup = new L.LayerGroup()

  records.forEach((record) => {
    const { decimalLatitude, decimalLongitude } = record
    if (decimalLatitude !== undefined && decimalLongitude !== undefined) {
      // const each_marker = new L.marker([decimalLatitude, decimalLongitude]).addTo(layerGroup)
      const each_marker = new L.marker([decimalLatitude, decimalLongitude]).bindPopup(
        `<strong> Hello Bangladesh!  </strong> <br> `
      )
      markers.addLayer(each_marker)
      // markers.addLayer(layerGroup)
    }
  })
  initialMap.value.addLayer(markers)

  // sleep(1000).then(() => {
  //   isLoading.value = false
  // })
  isLoading.value = false
}

// function sleep(ms) {
//   return new Promise((resolve) => setTimeout(resolve, ms))
// }

function addSingleMarker() {
  const record = store.getters['selectedResult']
  const latitude = record.processed.location.decimalLatitude
  const longitude = record.processed.location.decimalLongitude
  L.marker([latitude, longitude]).addTo(initialMap.value)
}
</script>
<style scoped>
/* @import 'leaflet/dist/leaflet.css';
#leaflet-map {
  height: 100vh;
  width: 100%;
  overflow: hidden;
} */

.p-progressspinner {
  position: fixed;
  z-index: 999;
  height: 2em;
  width: 2em;
  overflow: show;
  margin: auto;
  top: -200px;
  left: 0;
  bottom: 0;
  right: 0;
}
</style>
