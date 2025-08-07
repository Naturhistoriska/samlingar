<template>
  <!-- <div ref="mapContainer" style="height: 400px"></div> -->
  <l-map ref="mapRef" :zoom="zoo" :center="center" style="height: 80vh">
    <l-tile-layer :url="tileUrl" />
  </l-map>

  <!-- <l-map ref="mapRef" style="height: 60vh" :zoom="zoom" :center="center"> -->
  <!-- <l-tile-layer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" /> -->
  <!-- </l-map> -->
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
import { previousRoute } from '../router'
import { useRouter } from 'vue-router'

import { LMap, LTileLayer } from '@vue-leaflet/vue-leaflet'
import 'leaflet/dist/leaflet.css'
import L from 'leaflet'
import 'leaflet.markercluster'

import { useStore } from 'vuex'
import Service from '../Service'
const service = new Service()

const store = useStore()
const router = useRouter()

const mapRef = ref(null)
const mapContainer = ref(null)
const center = ref([59.0, 15.0])
const zoo = 5

const popupContent = ref('Loading...')

const tileUrl = 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'

onMounted(async () => {
  console.log('onMounted')

  if (previousRoute && previousRoute.fullPath) {
    console.log('Previous route was:', previousRoute.fullPath)
  }

  // if (mapRef.value) return // Prevent re-initialization

  // console.log('init map....')
  // mapRef.value = L.map(mapContainer.value).setView(center.value, 5)

  // L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  //   attribution: '&copy; OpenStreetMap contributors'
  // }).addTo(mapRef.value)

  await fetchAndRender({ lat: center.value[0], lng: center.value[1] })
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

const fetchAndRender = async ({ lat, lng }) => {
  console.log('lat-lon', lat, lng)

  const total = store.getters['totalRecords']
  console.log('total', total)

  const params = new URLSearchParams({
    text: '*',
    pt: `${lat},${lng}`
  })

  if (total > 10000) {
  }

  await service
    .apiGeoFetch(params)
    .then((response) => {
      const docs = response.response
      // geojson.value = solrToGeoJSON(docs)

      buildMarkers(docs)
      console.log('data fetched...')
    })
    .catch()
    .finally(() => {})
}

function addMarkersInChunks(markers, clusterGroup, chunkSize = 5000) {
  let index = 0

  function processChunk() {
    const chunk = markers.slice(index, index + chunkSize)

    chunk.forEach((doc) => {
      const [lat, lon] = doc.location.split(',').map(Number)
      const marker = L.marker([lat, lon])

      let content = 'Loading...'
      // marker.bindPopup('Loading...').openPopup()
      marker.on('click', async () => {
        await fetchRecord(doc.id, marker)

        // if (response) {
        //   const data = await response.response[0]

        //   content = `<b>${data.id}</b><br>${data.locality}`
        //   marker.setPopupContent(content)
        //   console.log(data)
        // }
      })
      // marker.setPopupContent(content)
      // marker.bindPopup(`<strong>${doc.locality}</strong>`)
      clusterGroup.addLayers(marker)
    })
    // chunk.forEach((m) => clusterGroup.addLayer(L.marker([m.lat, m.lng])))
    index += chunkSize

    if (index < markers.length) {
      setTimeout(processChunk, 0) // Allow UI thread to breathe
    }
  }

  if (index < markers.length) {
    console.log('index...', index)
    processChunk()
  }
}

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

        popupContent.value = `<b>${data.catalogNumber}</b><br>${data.locality}<br>${data.scientificName}<br>${data.locality}`

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

function buildMarkers(docs) {
  console.log('buildMarkers')
  const map = mapRef.value.leafletObject
  console.log(map)

  const markers = L.markerClusterGroup()

  addMarkersInChunks(docs, markers)

  map.addLayer(markers)
  console.log('mmarker s added')
}
</script>
