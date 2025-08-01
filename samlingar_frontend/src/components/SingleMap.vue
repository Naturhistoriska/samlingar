<template>
  <div>
    <div id="map" style="height: 60vh" class="custom-popup"></div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'

import * as L from 'leaflet'
import 'leaflet.markercluster'

import markerIconUrl from '/node_modules/leaflet/dist/images/marker-icon.png'
import markerIconRetinaUrl from '/node_modules/leaflet/dist/images/marker-icon-2x.png'
import markerShadowUrl from '/node_modules/leaflet/dist/images/marker-shadow.png'

const emits = defineEmits(['searchDetial', 'resetView', 'search'])

const store = useStore()
const initialMap = ref(null)

onMounted(() => {
  const record = store.getters['selectedRecord']

  const { decimalLatitude, decimalLongitude } = record

  if (decimalLatitude && decimalLongitude) {
    initMap(decimalLatitude, decimalLongitude)
  } else {
    initMap(59.0, 15.0)
  }

  addSamlingarSinglemMarker(record)
})

function initMap(latitude, longitude) {
  initialMap.value = L.map('map', {
    zoomControl: true,
    zoomAnimation: false
  })
    .setView([latitude, longitude], 6)
    .setZoom(5)
  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    minZoom: 1,
    maxZoom: 7,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
  }).addTo(initialMap.value)

  L.Icon.Default.prototype.options.iconUrl = markerIconUrl
  L.Icon.Default.prototype.options.iconRetinaUrl = markerIconRetinaUrl
  L.Icon.Default.prototype.options.shadowUrl = markerShadowUrl
  L.Icon.Default.imagePath = '' // necessary to avoid Leaflet adds some prefix to image path.
}

function addSamlingarSinglemMarker(record) {
  const {
    id,
    catalogNumber,
    collectionName,
    continent,
    country,
    decimalLatitude,
    decimalLongitude,
    locality,
    scientificName,
    stateProvince,
    eventDate
  } = record

  if (decimalLatitude && decimalLongitude) {
    const div = document.createElement('div')

    div.innerHTML = `<strong>Catalogue number: ${catalogNumber}  </strong>
      <br> <strong>Collection</strong>: ${collectionName}
      <br><strong>Scientific Name</strong>: ${scientificName}
      <br>
      <br><strong>Locality</strong>:<br> ${locality}, ${stateProvince}, ${country}, ${continent}
      <br>
      <br><strong>GPS-coordinate</strong>:
      <br>Latitude: ${decimalLatitude}
      <br>Longitude: ${decimalLongitude}
      <br>
      <br><strong>Event date</strong>: ${eventDate}
      <br>
      <br>`

    div.style.cssText = ' overflow-wrap: break-word;   '

    const marker = new L.marker([decimalLatitude, decimalLongitude]).bindPopup(div)

    marker.on('popupopen', () => {
      // You can trigger other actions here
    })

    marker.on('click', () => {})

    marker.addTo(initialMap.value)
  }
}
</script>
<style scoped>
.leaflet-popup-content-wrapper {
  padding: 10px;
  text-align: center;
  background-color: yellow !important;
}

.custom-popup .leaflet-popup-content-wrapper {
  background: #2c3e50;
  color: #fff;
  font-size: 16px;
  line-height: 24px;
}
.custom-popup .leaflet-popup-content-wrapper a {
  color: rgba(255, 255, 255, 0.5);
}
.custom-popup .leaflet-popup-tip-container {
  width: 300px;
  height: 15px;
}
.custom-popup .leaflet-popup-tip {
  border-left: 15px solid transparent;
  border-right: 15px solid transparent;
  border-top: 15px solid #2c3e50;
}

.custom-popup .leaflet-popup-content-wrapper,
.custom-popup .leaflet-popup-tip {
  background-color: #f4913b;
}
</style>
