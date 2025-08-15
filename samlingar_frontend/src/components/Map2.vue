<template>
  <div class="map-wrapper">
    <div id="map" style="height: 50vh" class="custom-popup"></div>
    <!-- <keep-alive>
      <l-map ref="mapRef" :zoom="zoo" :center="center" style="height: 60vh" @ready="onMapReady">
        <l-tile-layer :url="tileUrl" />
      </l-map>
    </keep-alive> -->

    <!-- <l-map ref="mapRef" :zoom="zoo" :center="center" style="height: 60vh">
      <l-tile-layer :url="tileUrl" />
    </l-map> -->

    <!-- Loader Overlay -->
    <div v-if="loading" class="loading-overlay">
      <span>{{ mapLoadingText }}</span>
    </div>
  </div>
</template>

<script setup>
import { nextTick, onMounted, ref, watch } from 'vue'
// import { previousRoute } from '../router'

// import { entryType, previousRoute, currentRoute } from '@/router'

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

const props = defineProps(['entry', 'from'])

const center = ref([59.0, 15.0])
const zoo = 4

const mapRef = ref(null)

const loading = ref(false)
let isDataReady = ref(false)
const popupContent = ref('Loading...')

const mapLoadingText = ref('Fetch data....')

const tileUrl = 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'
const attribution = '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
// const tileUrl = "'https://tile.openstreetmap.org/{z}/{x}/{y}.png'"

onMounted(async () => {
  console.log('onMounted', props.entry, props.from)

  mapRef.value = L.map('map', {
    zoomControl: true,
    zoomAnimation: false
  })
    .setView([59.0, 15.0], 6)
    .setZoom(zoo)

  L.tileLayer(tileUrl, {
    minZoom: 0,
    maxZoom: 7,
    attribution
  }).addTo(mapRef.value)

  await new Promise((r) => setTimeout(r, 1500))

  const entryType = props.entry
  if (entryType === 'first-visit' || entryType === 'reload') {
    let params = new URLSearchParams({
      text: '*'
    })
    await fetchAndRender(params, { lat: center.value[0], lng: center.value[1] })
  } else if (props.from === '/') {
    const isUrlPush = store.getters['isUrlPush']
    if (isUrlPush) {
      const params = buildParams()
      await fetchAndRender(params, { lat: center.value[0], lng: center.value[1] })
    }
  } else {
    onMapReady()
  }
})

watch(
  isDataReady,
  (newValue, oldValue) => {
    console.log(`count changed from ${oldValue} to ${newValue}`)

    if (newValue) {
      console.log('newValue is true')
      mapLoadingText.value = 'Loading markers....'
    }
  },
  { immediate: true }
)

watch(
  () => store.getters['totalRecords'],
  () => {
    console.log('map data changed..')
    const params = store.getters['searchParams']

    removeOldMarkers()
    fetchAndRender(params, { lat: center.value[0], lng: center.value[1] })

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

function onMapReady() {
  const markers = store.getters['clusterGroup']

  if (markers) {
    mapRef.value.addLayer(markers)
  }
  mapRef.value.fitBounds(markers.getBounds(), { padding: [50, 50] })
}

function removeOldMarkers() {
  const groupMarkers = store.getters['clusterGroup']
  if (groupMarkers) {
    groupMarkers.clearLayers()
  }
}

const fetchAndRender = async (params, { lat, lng }) => {
  console.log('lat-lon', lat, lng)

  // const params = store.getters['searchParams']

  if (params != null) {
    params.set('pt', `${lat},${lng}`)
  }

  loading.value = true
  isDataReady.value = false

  const totalRecords = store.getters['totalRecords']

  await service
    .apiGeoFetch(params, 0, totalRecords)
    .then((response) => {
      const docs = response.response

      if (docs) {
        console.log('docs', docs.length)

        isDataReady.value = true
        buildMarkers(docs)
      }
    })
    .catch()
    .finally(() => {})
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

  await nextTick()

  // const map = mapRef.value.leafletObject

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

  mapRef.value.addLayer(markers)
  console.log('mmarker s added')

  mapRef.value.fitBounds(markers.getBounds(), { padding: [50, 50] })

  store.commit('setClusterGroup', markers)
  loading.value = false
}

function buildParams() {
  const fields = store.getters['fields']

  const scientificName = store.getters['scientificName']
  const searchMode = store.getters['searchMode']
  const isFuzzy = store.getters['isFuzzySearch']

  const isType = store.getters['filterType']
  const isInSweden = store.getters['filterInSweden']
  const hasCoordinates = store.getters['filterCoordinates']
  const hasImages = store.getters['filterImage']
  let searchText = store.getters['searchText']
  searchText = searchText ? searchText : '*'

  const dataResource = store.getters['dataResource']

  const endDate = store.getters['endDate']
  const startDate = store.getters['startDate']

  // const collectionGroup = store.getters['collectionGroup']

  const params = new URLSearchParams({
    text: searchText
  })

  if (scientificName) {
    params.set('scientificName', scientificName)
    params.set('searchMode', searchMode)
    params.set('fuzzySearch', isFuzzy)
  }

  if (isType) {
    params.set('typeStatus', '*')
  }

  if (isInSweden) {
    params.set('country', 'Sweden')
  }

  if (hasImages) {
    params.set('associatedMedia', '*')
  }

  if (hasCoordinates) {
    params.set('lat_long', '*')
  }

  // if (collectionGroup) {
  //   params.set('collectionCode', collectionGroup)
  // }

  if (startDate) {
    params.set('startDate', startDate)
  }

  if (endDate) {
    params.set('endDate', endDate)
  }

  if (dataResource) {
    let newValue = dataResource.replace(/'/g, '"')
    params.set('dataResourceName', newValue)
  }

  if (fields) {
    fields
      .filter((field) => field.text)
      .forEach((field) => {
        params.set(field.value, field.text)
      })
  }
  store.commit('setSearchParams', params)
  return params
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
