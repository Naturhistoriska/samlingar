<template>
  <div id="myMap">
    <div id="map" style="height: 70vh" class="custom-popup"></div>
  </div>
</template>
<script setup>
import { ref, onMounted, watch } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

import * as L from 'leaflet'
import 'leaflet.markercluster'

import markerIconUrl from '/node_modules/leaflet/dist/images/marker-icon.png'
import markerIconRetinaUrl from '/node_modules/leaflet/dist/images/marker-icon-2x.png'
import markerShadowUrl from '/node_modules/leaflet/dist/images/marker-shadow.png'

const store = useStore()
const initialMap = ref(null)

const router = useRouter()

const isMapFetch = ref(false)

onMounted(() => {
  console.log('onMounted')
  initMap()

  addClusterMarkers()
})

watch(
  () => store.getters['geoData'],
  () => {
    initialMap.value.eachLayer((layer) => {
      if (layer instanceof L.Marker) {
        layer.remove()
      } else if (layer instanceof L.Circle) {
        layer.remove()
      } else {
        layer.remove()
      }
    })
    resetMap()

    addClusterMarkers()
  }
)

function addClusterMarkers() {
  console.log('addClusterMarkers')
  const latLongArray = store.getters['geoData']

  if (latLongArray) {
    const markers = L.markerClusterGroup()

    latLongArray.forEach((lat_long) => {
      const latLong = lat_long.val
      const count = lat_long.count

      const array = JSON.parse('[' + latLong + ']')

      const latitude = array[0]
      const longitude = array[1]

      for (let i = 0; i < count; i++) {
        const marker = new L.marker([latitude, longitude])
        markers.addLayer(marker)
      }
    })
    initialMap.value.addLayer(markers)
  }
}

function resetMap() {
  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    minZoom: 1,
    maxZoom: 7,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
  }).addTo(initialMap.value)

  L.Marker.prototype._animateZoom = function (opt) {
    if (!this._map) {
      return
    }
    const pos = this._map._latLngToNewLayerPoint(this._latlng, opt.zoom, opt.center).round()
    this._setPos(pos)
  }

  L.circle.prototype._animateZoom = function (opt) {
    if (!this._map) {
      return
    }
    const pos = this._map._latLngToNewLayerPoint(this._latlng, opt.zoom, opt.center).round()
    this._setPos(pos)
  }
}

function initMap() {
  console.log('initmap')
  initialMap.value = L.map('map', {
    zoomControl: true,
    zoomAnimation: false
  })
    .setView([20.0, 15.0], 2)
    .setZoom(2)
  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    minZoom: 1,
    maxZoom: 7,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
  }).addTo(initialMap.value)

  L.Icon.Default.prototype.options.iconUrl = markerIconUrl
  L.Icon.Default.prototype.options.iconRetinaUrl = markerIconRetinaUrl
  L.Icon.Default.prototype.options.shadowUrl = markerShadowUrl
  L.Icon.Default.imagePath = '' // necessary to avoid Leaflet adds some prefix to image path.

  isMapFetch.value = true
}
</script>
