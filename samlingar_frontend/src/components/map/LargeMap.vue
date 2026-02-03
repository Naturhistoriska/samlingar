<template>
  <div class="map-container">
    <l-map style="height: 500px; width: 100%" :center="center" :zoom="zoom">
      <l-tile-layer
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        attribution="&copy; OpenStreetMap contributors"
      />

      <!-- Draw heatmap polygons -->
      <l-geo-json v-if="geojson" :geojson="geojson" :options-style="styleFeature" />
    </l-map>

    <div v-if="loading" class="loading-overlay">
      <span>{{ mapLoadingText }}</span>
    </div>

    <!-- ✅ Toggle Button -->
    <button class="legend-toggle" @click="showLegend = !showLegend">
      {{ showLegend ? 'Hide Legend' : 'Show Legend' }}
    </button>

    <div v-if="showLegend" class="legend">
      <div><span style="background: #fed398"></span> 1-100</div>
      <div><span style="background: #feb24c"></span> 101-500</div>
      <div><span style="background: #fd8d3c"></span> 501–1000</div>
      <div><span style="background: #fc4e2a"></span> 1001-2000</div>
      <div><span style="background: #e31a1c"></span> 2001-5000</div>
      <div><span style="background: #bd0026"></span> 5001-10000</div>
      <div><span style="background: #800026"></span> 10001+</div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { LMap, LTileLayer, LGeoJson, LMarker, LIcon } from '@vue-leaflet/vue-leaflet'
import Button from 'primevue/button'

const center = ref([59.0, 15.0]) // set to your area
const zoom = ref(2)
const geojson = ref(null)

import 'leaflet/dist/leaflet.css'
import L from 'leaflet'
import 'leaflet.heat'

import { useStore } from 'vuex'

import Service from '../../Service'
const service = new Service()

const store = useStore()

const showLegend = ref(true)
const loading = ref(false)
const mapLoadingText = ref('Fetch data....')

const props = defineProps(['entry', 'from', 'reloadData'])

watch(
  () => store.getters['resetMapData'],
  () => {
    const reset = store.getters['resetMapData']
    const total = store.getters['totalGeoData']

    if (reset && total >= 50000) {
      fetchHeatmapData()
      store.commit('setResetMapData', false)
    }
  }
)

onMounted(async () => {
  console.log('onMounted largeMap')

  const total = store.getters['totalGeoData']
  const resetMapData = store.getters['resetMapData']

  await new Promise((r) => setTimeout(r, 1500))

  // let reloadMap = false
  if (total >= 50000 && resetMapData) {
    await fetchHeatmapData()
    store.commit('setResetMapData', false)
  } else {
    geojson.value = store.getters['geoJson']
  }
})

async function fetchHeatmapData() {
  const totalToFatch = store.getters['totalGeoData']
  // const params = buildParams()
  loading.value = true

  let params = store.getters['searchParams']
  if (params === null) {
    console.log('no params')
    params = buildParams()
  }
  await service
    .apiHeatmap(params, 0, totalToFatch)
    .then(async (response) => {
      const data = response
      if (data) {
        geojson.value = data
        store.commit('setGeoJson', data)
      }
    })

    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      loading.value = false
    })
}

function getColor(count) {
  if (count > 10000) return '#800026'
  if (count > 5000) return '#BD0026'
  if (count > 2000) return '#E31A1C'
  if (count > 1000) return '#FC4E2A'
  if (count > 500) return '#FD8D3C'
  if (count > 100) return '#FEB24C'
  if (count > 0) return '#fed398'
  return 'transparent'
}

function styleFeature(feature) {
  const count = feature.properties.count || 0
  return {
    fillColor: getColor(count),
    color: 'transparent',
    weight: 0.5,
    fillOpacity: 0.6
  }
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

  const endDate = store.getters['endDate']
  const startDate = store.getters['startDate']

  const startYear = store.getters['startYear']
  const endYear = store.getters['endYear']

  const dateFilter = store.getters['dateFilter']

  const selectedCollection = store.getters['selectedCollection']

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

  if (selectedCollection) {
    params.set('collectionCode', selectedCollection)
  }

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

  if (fields) {
    fields
      .filter((field) => field.text)
      .forEach((field) => {
        params.set(field.value, field.text)
      })
  }
  //store.commit('setSearchParams', params)
  return params
}
</script>
<style>
.map-container {
  position: relative;
}

.legend {
  position: absolute;
  bottom: 40px;
  left: 5px;
  background: white;
  padding: 8px 12px;
  font-size: 12px;
  line-height: 18px;
  color: #333;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.3);
  z-index: 1000;
  border-radius: 4px;
}

.legend div {
  display: flex;
  align-items: center;
  margin-bottom: 4px;
}

.legend span {
  display: inline-block;
  width: 16px;
  height: 12px;
  margin-right: 6px;
  border: 1px solid #999;
}

/* ✅ Toggle button style */
.legend-toggle {
  position: absolute;
  bottom: 0px;
  left: 5px;
  z-index: 1100;
  background: #fff;
  border: 1px solid #ccc;
  padding: 6px 10px;
  font-size: 13px;
  border-radius: 4px;
  cursor: pointer;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

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
</style>
