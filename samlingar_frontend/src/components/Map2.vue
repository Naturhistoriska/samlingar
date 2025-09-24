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
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'

import { LMap, LTileLayer } from '@vue-leaflet/vue-leaflet'
import 'leaflet/dist/leaflet.css'
import L from 'leaflet'
import 'leaflet.markercluster'

import markerIconUrl from '/node_modules/leaflet/dist/images/marker-icon.png'
import markerIconRetinaUrl from '/node_modules/leaflet/dist/images/marker-icon-2x.png'
import markerShadowUrl from '/node_modules/leaflet/dist/images/marker-shadow.png'

import Service from '../Service'
const service = new Service()

const store = useStore()
const router = useRouter()

let center = ref([59.0, 15.0])
const zoo = 4

const mapRef = ref(null)

const loading = ref(false)
const popupContent = ref('Loading...')

const mapLoadingText = ref('Fetch data....')

// const entry = ref(entryType.value)

const tileUrl = 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'
const attribution = '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
// const tileUrl = "'https://tile.openstreetmap.org/{z}/{x}/{y}.png'"

onMounted(async () => {
  console.log('onMounted')

  mapRef.value = L.map('map', {
    zoomControl: true,
    zoomAnimation: false
  })
    .setView(center.value, 6)
    .setZoom(zoo)

  L.tileLayer(tileUrl, {
    minZoom: 0,
    maxZoom: 17,
    attribution
  }).addTo(mapRef.value)

  L.Icon.Default.prototype.options.iconUrl = markerIconUrl
  L.Icon.Default.prototype.options.iconRetinaUrl = markerIconRetinaUrl
  L.Icon.Default.prototype.options.shadowUrl = markerShadowUrl
  L.Icon.Default.imagePath = '' // necessary to avoid Leaflet adds some prefix to image path.

  await new Promise((r) => setTimeout(r, 1500))

  const total = store.getters['totalGeoData']
  const resetMapData = store.getters['resetMapData']

  if (resetMapData && total < 50000 && total > 0) {
    let params = store.getters['searchParams']
    if (params === null) {
      params = new URLSearchParams({
        catchall: '*'
      })
    }
    await fetchAndRender(params, { lat: center.value[0], lng: center.value[1] })
    store.commit('setResetMapData', false)
  } else {
    onMapReady()
  }

  // let reloadMap = false
  // if (total < 50000) {
  //   if (from === 'first-visit' || from === 'reload') {
  //     reloadMap = true
  //   } else {
  //     if (resetMapData) {
  //       reloadMap = true
  //     }
  //   }
  //   if (resetMapData) {
  //     let params = store.getters['searchParams']
  //     if (params === null) {
  //       params = new URLSearchParams({
  //         catchall: '*'
  //       })
  //     }
  //     await fetchAndRender(params, { lat: center.value[0], lng: center.value[1] })
  //     addClustringPopup()
  //   }
  // }
  //   if (entryType === 'first-visit' || entryType === 'reload') {
  //     console.log('entryType 1', entryType)
  //     let params = new URLSearchParams({
  //       text: '*'
  //     })
  //     await fetchAndRender(params, { lat: center.value[0], lng: center.value[1] })
  //   } else {
  //     console.log('entryType 2', entryType)
  //     const isUrlPush = store.getters['isUrlPush']
  //     if (isUrlPush) {
  //       // const params = buildParams()
  //       const params = store.getters['searchParams']
  //       await fetchAndRender(params, { lat: 59.0, lng: 15.0 })
  //     }
  //     store.commit('setIsUrlPush', false)
  //   }
  //   addClustringPopup()
  // }
})

watch(
  () => store.getters['resetMapData'],
  () => {
    console.log('map 2 map data changed..')
    const reset = store.getters['resetMapData']
    const total = store.getters['totalGeoData'] < 50000
    if (reset && total) {
      const params = store.getters['searchParams']
      removeOldMarkers()
      fetchAndRender(params, { lat: 59.0, lng: 15.0 })
      store.commit('setResetMapData', false)
    }
  }
)

function addClustringPopup() {
  console.log('addClustringPopup')

  const SPIDERFY_THRESHOLD = 3

  mapRef.value.eachLayer((layer) => {
    if (layer instanceof L.MarkerClusterGroup) {
      layer.on('clusterclick', (e) => {
        // Get the actual cluster clicked
        const cluster = e.propagatedFrom || e.target
        const children = cluster.getAllChildMarkers()

        const maxZoom = mapRef.value.getMaxZoom()
        const childCount = children.length

        if (childCount <= SPIDERFY_THRESHOLD) {
          cluster.spiderfy()
        } else {
          mapRef.value.fitBounds(cluster.getBounds())

          if (mapRef.value.getZoom() === maxZoom) {
            const data = children[0]
            const locality = data.myData.locality

            const content =
              `<b>Total: ${children.length} </b> <br> Locality: ${locality} [ ${data._latlng.lat}, ${data._latlng.lng} ]<br><br>` +
              children.map((m, i) => `Scientific name:  ${m.myData.scientificName} `).join('<br>')

            // Open popup at cluster position
            L.popup().setLatLng(cluster.getLatLng()).setContent(content).openOn(mapRef.value)

            e.originalEvent.preventDefault()
            e.originalEvent.stopPropagation()
          }
        }
      })
    }
  })
}

function onMapReady() {
  const markers = store.getters['clusterGroup']

  if (markers) {
    mapRef.value.addLayer(markers)
  }

  const hasMarkers = hasMarkersInView(markers)
  if (hasMarkers) {
    mapRef.value.fitBounds(markers.getBounds(), { padding: [50, 50] })
  }
}

function removeOldMarkers() {
  const groupMarkers = store.getters['clusterGroup']
  if (groupMarkers) {
    groupMarkers.clearLayers()
  }
}

const fetchAndRender = async (params, { lat, lng }) => {
  console.log('lat-lon', lat, lng)

  if (params === null) {
    params = buildParams()
  }

  loading.value = true
  const total = store.getters['totalGeoData']

  await service
    .apiGeoFetch(params, 0, total)
    .then((response) => {
      console.log('response:', response)
      const docs = response.response
      if (docs && docs.length > 0) {
        buildMarkers(docs)
      } else {
        loading.value = false
      }
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      loading.value = false
    })
}

async function fetchRecord(id, marker) {
  popupContent.value = 'Loading...'
  marker.bindPopup(popupContent.value).openPopup()
  // if (response) {
  await service
    .apiIdSearch(id)
    .then((response) => {
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
          displayDetail(data)
        }
        div.style.cssText = ' overflow-wrap: break-word;   '
        div.appendChild(button)

        marker.bindPopup(div).openPopup()
      }
      setTimeout(() => {}, 2000)
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {})
}

function displayDetail(data) {
  store.commit('setSelectedRecord', data)
  router.push(`/record/${data.id}`)
}

async function buildMarkers(docs) {
  console.log('buildMarkers')
  loading.value = true
  await nextTick()

  const markers = L.markerClusterGroup({
    // spiderfyOnMaxZoom: false,
    // showCoverageOnHover: true,
    // zoomToBoundsOnClick: false
  })

  // addMarkersInChunks(docs, markers)
  docs.forEach((doc) => {
    const [lat, lon] = doc.geo.split(',').map(Number)
    const marker = L.marker([lat, lon])
    marker.myData = { id: doc.id, locality: doc.locality, scientificName: doc.scientificName }

    marker.on('click', async () => {
      await fetchRecord(doc.id, marker)
    })
    markers.addLayers(marker)
  })

  mapRef.value.addLayer(markers)
  console.log('mmarker s added')

  const hasMarkers = hasMarkersInView(markers)
  if (!hasMarkers) {
    mapRef.value.fitBounds(markers.getBounds(), { padding: [50, 50] })
  }

  store.commit('setClusterGroup', markers)
  loading.value = false
}

function hasMarkersInView(group) {
  if (!mapRef || !group) return false

  const mapBounds = mapRef.value.getBounds() // current map view bounds
  const markersInView = group.getLayers().filter((marker) => mapBounds.contains(marker.getLatLng()))

  return markersInView.length > 0
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

  const selectedCollection = store.getters['selectedCollection']

  const endDate = store.getters['endDate']
  const startDate = store.getters['startDate']

  const params = new URLSearchParams({
    catchall: searchText
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
    params.set('hasImage', '*')
  }

  if (hasCoordinates) {
    params.set('geo', '*')
  }

  if (startDate) {
    params.set('startDate', startDate)
  }

  if (endDate) {
    params.set('endDate', endDate)
  }

  if (selectedCollection) {
    // let newValue = dataResource.replace(/'/g, '"')
    params.set('collectionCode', selectedCollection)
  }

  if (fields) {
    fields
      .filter((field) => field.text)
      .forEach((field) => {
        params.set(field.value, field.text)
      })
  }
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
