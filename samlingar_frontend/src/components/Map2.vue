<template>
  <div class="map-wrapper">
    <div id="map" style="height: 500px; width: 100%" class="custom-popup"></div>

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
import moment from 'moment-timezone'

import { useI18n } from 'vue-i18n'
const { t } = useI18n()

const service = new Service()

const store = useStore()
const router = useRouter()

let center = ref([59.0, 15.0])
const zoom = ref(4)

const mapRef = ref(null)

const loading = ref(false)
const popupContent = ref('Loading...')

const mapLoadingText = ref('Fetch data....')

// const entry = ref(entryType.value)

const tileUrl = 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'
const attribution = '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
// const tileUrl = "'https://tile.openstreetmap.org/{z}/{x}/{y}.png'"

onMounted(async () => {
  mapRef.value = L.map('map', {
    zoomControl: true,
    zoomAnimation: false
  })
    .setView(center.value, 6)
    .setZoom(4)

  L.tileLayer(tileUrl, {
    minZoom: 1,
    maxZoom: 12,
    attribution
  }).addTo(mapRef.value)

  L.Icon.Default.prototype.options.iconUrl = markerIconUrl
  L.Icon.Default.prototype.options.iconRetinaUrl = markerIconRetinaUrl
  L.Icon.Default.prototype.options.shadowUrl = markerShadowUrl
  L.Icon.Default.imagePath = '' // necessary to avoid Leaflet adds some prefix to image path.

  await new Promise((r) => setTimeout(r, 1500))

  const resetMapData = store.getters['resetMapData']

  if (resetMapData) {
    const total = store.getters['totalGeoData']
    if (total < 50000 && total > 0) {
      let params = store.getters['searchParams']
      if (params === null) {
        params = new URLSearchParams({
          catchall: '*'
        })
      }
      await fetchAndRender(params, { lat: center.value[0], lng: center.value[1] })
      store.commit('setResetMapData', false)
    }
  } else {
    onMapReady()
  }
})

watch(
  () => store.getters['resetMapData'],
  () => {
    console.log('map 2 map data changed..')
    const reset = store.getters['resetMapData']
    if (reset) {
      const total = store.getters['totalGeoData']
      removeOldMarkers()
      if (total < 50000 && total > 0) {
        const params = store.getters['searchParams']
        fetchAndRender(params, { lat: 59.0, lng: 15.0 })
      }

      store.commit('setResetMapData', false)
    }
  }
)

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
      store.commit('setResetMapData', false)
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
        const div = document.createElement('div')
        if (data) {
          const {
            catalogNumber,
            collectionName,
            country,
            decimalLatitude,
            decimalLongitude,
            eventDate,
            locality,
            stateProvince
          } = data

          const taxon = getTaxon(data)

          const collectingDate = eventDate
            ? moment.tz(eventDate, 'ddd MMM DD HH:mm:ss z YYYY', 'CET').format('YYYY-MM-DD')
            : ''

          div.innerHTML = `<strong>${t('labels.catalogNumber')}: ${catalogNumber}  </strong>
          <br> <strong>${t('labels.collectionName')}</strong>: ${collectionName}
          <br><strong>${t('labels.scientificName')}</strong>: ${taxon}
          <br>
          <br><strong>${t('labels.locality')}</strong>:<br> ${locality}, ${stateProvince}, ${country}
          <br>
          <br><strong>${t('labels.gpsCoordinates')}</strong>: <br>
          ${decimalLatitude.toFixed(5)} -- ${decimalLongitude.toFixed(5)}
          <br>
          <br><strong>${t('labels.eventDate')}</strong>: ${collectingDate}
          <br>
          <br>`

          const button = document.createElement('button')
          button.innerHTML = `${t('labels.showDetail')}`

          // button.onclick = function () {
          //   displayDetail(data)
          // }
          // This handles both click and Enter/Space
          button.addEventListener('click', () => {
            displayDetail(data)
          })

          div.style.cssText = ' overflow-wrap: break-word;   '
          div.appendChild(button)

          marker.bindPopup(div).openPopup()
        } else {
          div.innerHTML = `<strong>${t('labels.noData')}</strong>`
          marker.bindPopup(div).openPopup()
        }
      }
      setTimeout(() => {}, 2000)
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {})
}

function getTaxon(data) {
  const { collectionCode, genus, species, scientificName, taxonRank } = data

  const rank = taxonRank?.toLowerCase()
  const codes = ['pz', 'pb', 'PI', 'HE', 'vp']

  if (codes.includes(collectionCode)) {
    return rank === 'species' ? `${genus} ${species}` : scientificName
  }
  return scientificName
}
// function getTaxon(data) {
//   const { collectionCode, genus, scientificName, species, taxonRank } = data

//   if (collectionCode === 'pz' || collectionCode === 'pb') {
//     return taxonRank === 'species' ? genus + ' ' + species : scientificName
//   } else if (collectionCode === 'PI' || collectionCode === 'HE') {
//     return taxonRank === 'Species' ? genus + ' ' + species : scientificName
//   } else if (collectionCode === 'vp') {
//     if (species) {
//       return genus ? genus + ' ' + species : species
//     }
//   } else {
//     return scientificName
//   }
// }

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

  const isType = store.getters['filterType']
  const isInSweden = store.getters['filterInSweden']
  const hasCoordinates = store.getters['filterCoordinates']
  const hasImages = store.getters['filterImage']

  let searchText = store.getters['searchText']
  searchText = searchText ? searchText : '*'
  const fullTextSearchMode = store.getters['fullTextSearchMode']

  const selectedCollection = store.getters['selectedCollection']

  // const endDate = store.getters['endDate']
  // const startDate = store.getters['startDate']

  const endDate = store.getters['endDate']
  const startDate = store.getters['startDate']

  const startYear = store.getters['startYear']
  const endYear = store.getters['endYear']

  const dateFilter = store.getters['dateFilter']

  const params = new URLSearchParams({
    catchall: searchText,
    mode: fullTextSearchMode
  })

  if (scientificName) {
    params.set('scientificName', scientificName)
    params.set('searchMode', searchMode)
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

  // if (startDate) {
  //   params.set('startDate', startDate)
  // }

  // if (endDate) {
  //   params.set('endDate', endDate)
  // }

  if (dateFilter === 'date') {
    if (startDate) {
      params.set('startDate', startDate)
    }
    if (endDate) {
      params.set('endDate', endDate)
    }
  } else {
    if (startYear && endYear) {
      const yearQuery = `[${startYear.getFullYear()} TO ${endYear.getFullYear()}]`
      params.set('year', yearQuery)
    }
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
