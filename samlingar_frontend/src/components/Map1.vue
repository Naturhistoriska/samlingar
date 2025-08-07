<template>
  <l-map ref="mapRef" style="height: 60vh" :zoom="zoom" :center="center">
    <l-tile-layer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
  </l-map>
</template>
<script setup>
import { ref, onMounted, watch } from 'vue'
import { useStore } from 'vuex'

import 'leaflet/dist/leaflet.css'
import { LMap, LTileLayer } from '@vue-leaflet/vue-leaflet'
import L from 'leaflet'
import 'leaflet.markercluster'

import Service from '../Service'

const service = new Service()

const store = useStore()

const zoom = ref(3)
const center = ref([59.0, 15.0])
let loading = ref(false)
const mapRef = ref(null)

watch(
  () => store.getters['geoData'],
  () => {
    console.log('map data changed..')
    // initialMap.value.eachLayer((layer) => {
    //   if (layer instanceof L.Marker) {
    //     layer.remove()
    //   } else if (layer instanceof L.Circle) {
    //     layer.remove()
    //   } else {
    //     layer.remove()
    //   }
    // })
    // resetMap()

    // addClusterMarkers()
  }
)

const fetchAndRender = async ({ lat, lng }) => {
  console.log('lat-lon', lat, lng)
  loading.value = true

  const params = new URLSearchParams({
    text: '*',
    pt: `${lat},${lng}`
  })

  await service
    .apiGeoFetch(params)
    .then((response) => {
      const docs = response.response
      // geojson.value = solrToGeoJSON(docs)
      buildMarkers(docs)
    })
    .catch()
    .finally(() => {
      console.log('test 0', loading.value)
    })
}

function buildMarkers(docs) {
  const map = mapRef.value.leafletObject

  console.log('map', map)

  // const bounds = map.getBounds()

  // const bbox = [
  //   bounds.getSouthWest().lng,
  //   bounds.getSouthWest().lat,
  //   bounds.getNorthEast().lng,
  //   bounds.getNorthEast().lat
  // ].join(',')

  // console.log('bbox', bbox)

  const clusterGroup = L.markerClusterGroup()

  docs.forEach((doc) => {
    const [lat, lon] = doc.location.split(',').map(Number)
    const marker = L.marker([lat, lon])
    marker.bindPopup(`<strong>${doc.locality}</strong>`)
    clusterGroup.addLayer(marker)
  })

  clusterGroup.addTo(map)

  console.log('test 1', loading.value)

  setTimeout(() => {
    console.log('test 2', loading.value)
    loading.value = false

    console.log('test 3', loading.value)
  }, 20000)
}

const onMapMove = async (e) => {
  const center = e.target.getCenter()
  await fetchAndRender({ lat: center.lat, lng: center.lng })
}

onMounted(async () => {
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

  await fetchAndRender({ lat: center.value[0], lng: center.value[1] })
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
<style scoped>
.spinner-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.spinner {
  border: 6px solid #f3f3f3;
  border-top: 6px solid #3498db;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>
