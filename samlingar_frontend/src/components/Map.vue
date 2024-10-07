<template>
  <div>
    <div id="map" style="height: 80vh"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useStore } from 'vuex'

// import 'leaflet/dist/leaflet.css'
// import 'leaflet.markercluster'

import * as L from 'leaflet'
import 'leaflet.markercluster'

const emits = defineEmits(['search'])

const store = useStore()
const initialMap = ref(null)

let loading = false

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
    // initialMap.value._panes.markerPane.remove()

    initialMap.value.eachLayer((layer) => {
      if (layer instanceof L.Marker) {
        layer.remove()
      }
    })
    loading = false
    addClusterMarkers()
  }
)

async function addClusterMarkers() {
  const isAdvanceSearch = store.getters['isAdvanceSearch']

  loading = true
  if (isAdvanceSearch) {
    // do advance search
  } else {
    // emits('search')
  }
  console.log('after search', loading)

  const records = store.getters['mapRecords']
  const markers = L.markerClusterGroup()

  const layerGroup = new L.LayerGroup()

  records.forEach((record) => {
    const { decimalLatitude, decimalLongitude } = record
    if (decimalLatitude !== undefined && decimalLongitude !== undefined) {
      const each_marker = new L.marker([decimalLatitude, decimalLongitude]).addTo(layerGroup)
      // markers.addLayer(each_marker)
      markers.addLayer(layerGroup)
    }
  })
  initialMap.value.addLayer(markers)
}

function addSingleMarker() {
  const record = store.getters['selectedResult']
  const latitude = record.processed.location.decimalLatitude
  const longitude = record.processed.location.decimalLongitude
  L.marker([latitude, longitude]).addTo(initialMap.value)
}
</script>
