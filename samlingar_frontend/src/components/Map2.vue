<template>
  <div class="map-wrapper">
    <l-map ref="mapRef" :zoom="zoo" :center="center" style="height: 60vh" @ready="onMapReady">
      <l-tile-layer :url="tileUrl" />
    </l-map>
    <!-- Loader Overlay -->
    <div v-if="loading" class="loading-overlay">
      <span>{{ loadingText }}</span>
    </div>
  </div>
</template>

<script setup>
import { nextTick, onMounted, ref, watch } from 'vue'
import { previousRoute } from '../router'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'

import { LMap, LTileLayer } from '@vue-leaflet/vue-leaflet'
import 'leaflet/dist/leaflet.css'
import L from 'leaflet'
import 'leaflet.markercluster'

import Service from '../Service'
const service = new Service()

const store = useStore()
const router = useRouter()

const mapRef = ref(null)
const center = ref([59.0, 15.0])
const zoo = 4

const loading = ref(false)
let loadingText = ref()
const popupContent = ref('Loading...')

const tileUrl = 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'

onMounted(async () => {
  console.log('onMounted')
  // const map = mapRef.value.leafletObject
  // console.log('map...', map)

  if (previousRoute && previousRoute.fullPath) {
    console.log('Previous route was:', previousRoute.fullPath)
    if (!previousRoute.fullPath.includes('record')) {
      await fetchAndRender({ lat: center.value[0], lng: center.value[1] })
    } else {
      // loading.value = true
      // const clusterGroup = store.getters['clusterGroup']
      // const map = mapRef.value.leafletObject
      // console.log('map 1...', map)
      // if (map) {
      //   console.log('map 1...', map)
      //   map.addLayer(clusterGroup)
      // }
      // loadingText.value = 'Loading markers...'
      // loading.value = false
    }

    // else {
    //   const map = mapRef.value.leafletObject

    //   const markers = store.getters['clusterGroup']

    //   map.addLayer(markers)
    // }
  }
})

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

function onMapReady(mapInstance) {
  const markers = store.getters['clusterGroup']

  if (markers) {
    mapInstance.addLayer(markers)
  }
}

const fetchAndRender = async ({ lat, lng }) => {
  console.log('lat-lon', lat, lng)

  const params = store.getters['searchParams']
  console.log('params', params)

  if (params != null) {
    params.set('pt', `${lat},${lng}`)
  }

  loading.value = true
  loadingText.value = 'Fetching data....'

  const total = store.getters['totalRecords']
  console.log('total', total)

  // const params = new URLSearchParams({
  //   text: '*',
  //   pt: `${lat},${lng}`
  // })

  await service
    .apiGeoFetch(params, 0, total)
    .then((response) => {
      const docs = response.response

      if (docs) {
        console.log('docs', docs.length)
        buildMarkers(docs)
      }
    })
    .catch()
    .finally(() => {})

  // const batch = 100000
  // const step = 99999
  // if (total > 100000) {
  //   for (let i = 0; i < total; i += batch) {
  //     await service
  //       .apiGeoFetch(params, i, i + step)
  //       .then((response) => {
  //         const docs = response.response

  //         // buildMarkers(docs)
  //       })
  //       .catch()
  //       .finally(() => {})

  //     function delay(ms) {
  //       return new Promise((resolve) => setTimeout(resolve, ms))
  //     }

  //     // Inside loop:
  //     await delay(1000)

  //   }
  // }
}

// function addMarkersInChunks(docs, clusterGroup, chunkSize = 50000) {
//   let index = 0

//   function processChunk() {
//     console.log('processChunk.....')
//     const chunk = docs.slice(index, index + chunkSize)

//     chunk.forEach((doc) => {
//       const [lat, lon] = doc.location.split(',').map(Number)
//       const marker = L.marker([lat, lon])

//       marker.on('click', async () => {
//         await fetchRecord(doc.id, marker)
//       })
//       clusterGroup.addLayers(marker)
//     })
//     index += chunkSize

//     if (index < docs.length) {
//       console.log('index', index)
//       setTimeout(processChunk, 0) // Allow UI thread to breathe
//     } else {
//       console.log('addMarkersInChunks done.....')
//       store.commit('setClusterGroup', clusterGroup)
//     }
//   }

//   if (index < docs.length) {
//     console.log('index', index)
//     processChunk()
//   }
// }

async function fetchRecord(id, marker) {
  popupContent.value = 'Loading...'
  marker.bindPopup(popupContent.value).openPopup()
  // if (response) {
  await service
    .apiIdSearch(id)
    .then((response) => {
      console.log(response)
      if (response) {
        const data = response.response[0]

        const {
          catalogNumber,
          collectionName,
          country,
          decimalLatitude,
          decimalLongitude,
          eventDate,
          locality,
          stateProvince,
          scientificName
        } = data

        const div = document.createElement('div')

        div.innerHTML = `<strong> Catalogue number: ${catalogNumber}  </strong>
          <br> <strong>Collection</strong>: ${collectionName}
          <br><strong>Scientific Name</strong>: ${scientificName}
          <br>
          <br><strong>Locality</strong>:<br> ${locality}, ${stateProvince}, ${country}
          <br>
          <br><strong>GPS-coordinate</strong>: <br>
          ${decimalLatitude} -- ${decimalLongitude}
          <br>
          <br><strong>Event date</strong>: ${eventDate}
          <br>
          <br>`

        const button = document.createElement('button')
        button.innerHTML = 'More details'

        button.onclick = function () {
          displayDetail(id)
        }
        div.style.cssText = ' overflow-wrap: break-word;   '
        div.appendChild(button)

        marker.bindPopup(div).openPopup()
      }
      setTimeout(() => {}, 2000)
    })
    .catch()
    .finally(() => {})
}

function displayDetail(id) {
  router.push(`/record/${id}`)
}

async function buildMarkers(docs) {
  console.log('buildMarkers')

  loading.value = true
  loadingText.value = 'Loading markers...'

  await nextTick()

  const map = mapRef.value.leafletObject
  console.log(map)

  const markers = L.markerClusterGroup()

  // addMarkersInChunks(docs, markers)
  docs.forEach((doc) => {
    const [lat, lon] = doc.location.split(',').map(Number)
    const marker = L.marker([lat, lon])

    marker.on('click', async () => {
      await fetchRecord(doc.id, marker)
    })
    markers.addLayers(marker)
  })

  map.addLayer(markers)
  console.log('mmarker s added')

  store.commit('setClusterGroup', markers)
  loading.value = false
}
</script>
<style scoped>
.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  background: rgba(255, 255, 255, 0.7);
  font-size: 1.2em;
  z-index: 1000;
}
.map-wrapper {
  position: relative;
}
</style>
