<template>
  <div>
    <div id="map" style="height: 60vh" class="custom-popup"></div>
  </div>
</template>
<script setup>
import { ref, onMounted, watch } from 'vue'
import { useStore } from 'vuex'
import moment from 'moment'

import * as L from 'leaflet'
import 'leaflet.markercluster'

import markerIconUrl from '/node_modules/leaflet/dist/images/marker-icon.png'
import markerIconRetinaUrl from '/node_modules/leaflet/dist/images/marker-icon-2x.png'
import markerShadowUrl from '/node_modules/leaflet/dist/images/marker-shadow.png'

const store = useStore()
const initialMap = ref(null)

onMounted(() => {
  initMap()
  const searchText = store.getters['searchText']
  console.log('searchText', searchText)

  const scientificName = store.getters['scientificName']
  console.log('scientificName', scientificName)
})

function initMap() {
  console.log('initmap')
  initialMap.value = L.map('map', {
    zoomControl: true,
    zoomAnimation: false
  })
    .setView([59.0, 15.0], 6)
    .setZoom(3)
  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    minZoom: 1,
    maxZoom: 7,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
  }).addTo(initialMap.value)

  L.Icon.Default.prototype.options.iconUrl = markerIconUrl
  L.Icon.Default.prototype.options.iconRetinaUrl = markerIconRetinaUrl
  L.Icon.Default.prototype.options.shadowUrl = markerShadowUrl
  L.Icon.Default.imagePath = '' // necessary to avoid Leaflet adds some prefix to image path.

  initialMap.value.on('zoomend', function (e) {
    onZoomChanged()
  })
}
</script>
