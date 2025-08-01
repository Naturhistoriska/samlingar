<template>
  <l-map ref="mapRef" style="height: 60vh" :zoom="zoom" :center="center">
    <l-tile-layer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
  </l-map>
</template>
<script setup>
import { ref, onMounted } from 'vue'

import 'leaflet/dist/leaflet.css'
import { LMap, LTileLayer } from '@vue-leaflet/vue-leaflet'
import L from 'leaflet'
import 'leaflet.markercluster'

import Service from '../Service'

const service = new Service()

const zoom = ref(6)
const center = ref([59.0, 15.0])
let loading = ref(false)
const mapRef = ref(null)

const fetchAndRender = async ({ lat, lng }) => {
  loading.value = true
  service
    .apiGeoFetch()
    .then((response) => {
      const docs = response.response.docs
      // geojson.value = solrToGeoJSON(docs)
      buildMarkers(docs)
      setTimeout(() => {
        loading.value = false
      }, 2000)
    })
    .catch()
    .finally(() => {})
}

function buildMarkers(docs) {
  const map = mapRef.value.leafletObject

  console.log('map', map)

  const bounds = map.getBounds()

  const bbox = [
    bounds.getSouthWest().lng,
    bounds.getSouthWest().lat,
    bounds.getNorthEast().lng,
    bounds.getNorthEast().lat
  ].join(',')

  console.log('bbox', bbox)

  const clusterGroup = L.markerClusterGroup()

  docs.forEach((doc) => {
    const [lat, lon] = doc.location.split(',').map(Number)
    const marker = L.marker([lat, lon])
    marker.bindPopup(`<strong>${doc.locality}</strong>`)
    clusterGroup.addLayer(marker)
  })

  clusterGroup.addTo(map)
}

const onMapMove = async (e) => {
  const center = e.target.getCenter()
  await fetchAndRender({ lat: center.lat, lng: center.lng })
}

onMounted(() => {
  // const map = mapRef.value.leafletObject
  // console.log('map', map)

  // const bounds = map.getBounds()

  // const bbox = [
  //   bounds.getSouthWest().lng,
  //   bounds.getSouthWest().lat,
  //   bounds.getNorthEast().lng,
  //   bounds.getNorthEast().lat
  // ].join(',')

  // console.log('bbox', bbox)

  fetchAndRender({ lat: center.value[0], lng: center.value[1] })
})

// function solrToGeoJSON(docs) {
//   return {
//     type: 'FeatureCollection',
//     features: docs.map((doc) => {
//       const [lat, lon] = doc.location.split(',').map(Number)
//       return {
//         type: 'Feature',
//         geometry: {
//           type: 'Point',
//           coordinates: [lon, lat]
//         },
//         properties: {
//           id: doc.id,
//           name: doc.locality
//         }
//       }
//     })
//   }
// }
</script>
<style scoped></style>
