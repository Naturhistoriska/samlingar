<template>
  <div class="card">
    <div class="grid">
      <div class="flex flex-column col-5">
        <SearchFilter
          class="divBg"
          @conditionalSearch="handleConditionSearch"
          @filterSearch="handleFilterSearch"
          @search="handleSearch"
        />
      </div>
      <div class="col-7">
        <div class="grid">
          <div class="col-7" style="font-size: 20px">
            <legend>
              {{ $t('results.searchResults') }} [{{ $t('results.num_results', totalRecords) }}]
              <br />

              <Button link @click="exportData" v-if="!dataPrepared" style="width: 420px">
                <small>{{ $t('exportData.export') }}</small>
              </Button>
              <download-excel
                v-else
                @click="downloadFile"
                class="btn btn-default"
                :data="json_data"
                :fields="json_fields"
                type="csv"
                name="data.csv"
                :before-finish="finishDownload"
                :before-generate="startDownload"
                style="color: #0dff5c; font-size: 14px; cursor: pointer; padding-left: 10px"
              >
                <small>Download Data</small>
              </download-excel>
              <br />
              <Button link @click="onMapLinkClick">
                <small>{{ mapLinkText }}</small>
              </Button>
            </legend>
          </div>
          <ProgressSpinner
            v-if="isLoading"
            aria-label="Loading"
            style="width: 50px; height: 50px; position: relative; padding-left: 80%"
            strokeWidth="8"
            fill="transparent"
          />
        </div>

        <Map v-if="showMap" @resetView="handleResetView" @searchDetial="handleSearchDetail" />
        <div v-else>
          <ResultDetail v-if="showDetail" />
          <ResultList v-else @search="handlePaginateSearch" />
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { computed, ref, toRaw, isProxy, watch } from 'vue'
import { useStore } from 'vuex'
import { useI18n } from 'vue-i18n'

import Loading from 'vue3-loading-overlay'
// // import { useLoading } from 'vue3-loading-overlay'
import 'vue3-loading-overlay/dist/vue3-loading-overlay.css'
// Import stylesheet
// import 'vue3-loading-overlay/dist/vue3-loading.css'

// import Loading from 'vue-loading-overlay'
// import 'vue-loading-overlay/dist/vue-loading.css'

import ResultDetail from './ResultDetail.vue'
import ResultList from './ResultList.vue'
import SearchFilter from './SearchFilter.vue'
import Map from './Map.vue'

// import downloadexcel from 'vue-json-excel3'

// import Service from '../Service'
// const service = new Service()

const { t } = useI18n()

const store = useStore()
const emits = defineEmits([
  'advanceSearch',
  'conditionalSearch',
  'detailSearch',
  'coordinatesSearch',
  'exportData',
  'fetchMapData',
  'filterSearch',
  'simpleSearch'
])

let showMap = ref(false)
let dataPrepared = ref(false)
let json_data = ref()
const isLoading = ref(false)

let json_fields = {
  'Collection Name': 'collectionName',
  'Catalogue Number': 'catalogNumber',
  Locality: 'locality',
  District: 'district',
  'State Province': 'state',
  Country: 'country',
  Continent: 'continent',
  Latitude: 'latitudeText',
  Longitude: 'longitudeText',
  'Station field number': 'stationFieldNumber',
  Determiner: 'determiner',
  Family: 'family',
  Genus: 'genus',
  Species: 'species',
  'High classification': 'higherTx',
  Prepration: 'prepration',
  Preservation: 'preservation',
  'Vernacular Name': 'commonName',
  'Cataloged date': 'catalogedDate',
  'Event date': 'startDate',
  Collectors: 'collector',
  Author: 'author'
}

watch(
  () => store.getters['exportData'],
  () => {
    console.log('data changed')
    json_data.value = store.getters['exportData']
    setTimeout(() => {
      dataPrepared.value = true
      isLoading.value = false
    }, 2000)
  }
)

// const json_data = computed(() => {
//   const data = store.getters['exportData']
//   console.log('data: ', data.length)

//   return toRaw(data)
// })

function exportData() {
  console.log('export data')

  isLoading.value = true

  emits('exportData')
}

function downloadFile() {
  console.log('downloadFile')

  isLoading.value = true
}

function startDownload() {
  console.log('start...')
}

function finishDownload() {
  console.log('done....')
  dataPrepared.value = false
  isLoading.value = false
}

// search when filter link (coordinates, images, type, inSweden clicked)
function handleFilterSearch(value) {
  console.log('handleFilterSearch', value)
  if (showMap.value) {
    let searchText = store.getters['searchText']

    searchText = searchText + value
    store.commit('setSearchText', searchText)
    emits('fetchMapData', true)
  } else {
    emits('filterSearch', value)
  }
}

const mapLinkText = computed(() => {
  if (showMap.value) {
    return showDetail.value ? t('results.backToReslutView') : t('results.backToResultListView')
  }
  return t('results.showMapView')
})

const showDetail = computed(() => {
  return store.getters['showDetail']
})

const totalRecords = computed(() => {
  return store.getters['totalRecords']
})

// this search after clear all filter
function handleSearch() {
  console.log('handleSearch [clear filter search]')

  const isAdvanceSearch = store.getters['isAdvanceSearch']

  if (showMap.value) {
    emits('fetchMapData', true)
  } else {
    if (isAdvanceSearch) {
      emits('advanceSearch')
    } else {
      emits('simpleSearch')
    }
  }
}

function handleConditionSearch(value) {
  console.log('handleConditionSearch')

  if (showMap.value) {
    emits('fetchMapData', true, value)
  } else {
    emits('conditionalSearch', value)
  }
}

function handleSearchDetail(coordinates) {
  console.log('handleSearchDetail', coordinates)

  const isDetailView = store.getters['showDetail']
  if (isDetailView) {
    showMap.value = !showMap.value
  } else {
    emits('detailSearch', coordinates)
  }
}

function handleResetView(coordinates, total) {
  console.log('handleResetView', coordinates, total)

  emits('coordinatesSearch', coordinates, total)
}

function onMapLinkClick() {
  showMap.value = !showMap.value

  if (showMap.value) {
    const isDetailView = store.getters['showDetail']
    if (!isDetailView) {
      emits('fetchMapData', false)
    }
    // isLoading.value = true
    // setTimeout(() => {
    //   isLoading.value = false
    // }, 5000)
  }
}

function handlePaginateSearch() {
  emits('conditionalSearch', 'paginateSearch')
}
</script>
<style scoped>
.divBg {
  background: transparent;
}

.p-button-link {
  color: #0dff5c;
}

.ui-button-text {
  padding: 0px !important;
}
</style>
