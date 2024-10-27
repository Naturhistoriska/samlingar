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
              <Button
                link
                @click="exportData"
                v-if="!dataPrepared"
                style="color: #34d399; font-size: 14px; cursor: pointer"
              >
                <small>Export data [Click to prepare data for download (max: 5000 records)]</small>
              </Button>
              <download-excel
                v-else
                @click="downloadFile"
                class="btn btn-default"
                :data="json_data"
                :fields="json_fields"
                type="csv"
                name="data.csv"
                style="color: #34d399; font-size: 14px; cursor: pointer; padding-left: 10px"
              >
                <small>Download Data</small>
              </download-excel>
              <Button link @click="onMapLinkClick">
                <small>{{ mapLinkText }}</small>
              </Button>
            </legend>
          </div>
          <div class="col-6">
            <div>
              <Loading
                id="myLoad"
                :active="isLoading"
                :can-cancel="true"
                color="#34d399"
                background-color="#333333"
              >
              </Loading>
            </div>
          </div>
        </div>

        <Map v-if="showMap" @resetView="handleResetView" @searchDetial="handleSearchDetail" />
        <!-- <Map v-if="showMap" @search="handleMapSearch" /> -->
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
  'mapSearch',
  'simpleSearch'
])

let showMap = ref(false)
let dataPrepared = ref(false)
const isLoading = ref(false)

let json_fields = {
  'Scientific Name': 'scientificName',
  'Catalogue Number': 'raw_catalogNumber',
  'Vernacular Name': 'vernacularName',
  Kingdom: 'kingdom',
  Phylum: 'phylum',
  Classs: 'classs',
  Order: 'order',
  Family: 'family',
  Genus: 'genus',
  Species: 'species',
  'Taxon Rank': 'taxonRank',
  'Collection Name': 'collectionName',
  Country: 'country',
  'State Province': 'stateProvince',
  'Decimal Latitude': 'decimalLatitude',
  'Decimal Longitude': 'decimalLongitude',
  'Recorded By': 'recordedBy',
  Collectors: 'collectors'
}

const json_data = computed(() => {
  const data = store.getters['exportData']
  console.log('data: ', data.length)

  return toRaw(data)
})

function exportData() {
  console.log('export data')

  emits('exportData')

  setTimeout(() => {
    dataPrepared.value = true
  }, 5000)
}

function downloadFile() {
  console.log('downloadFile')
  setTimeout(() => {
    dataPrepared.value = false
  }, 5000)
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

// function fetchData() {
//   console.log('fetchData')
//   exportData()
// }

function startDownload() {
  setTimeout(() => {
    // loading.value = false
  }, 5000)
}

function finishDownload() {
  alert('hide loading')
}

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
  onMapLinkClick()
  emits('detailSearch', coordinates)
}

function handleResetView(coordinates, total) {
  console.log('handleResetView', coordinates, total)

  emits('coordinatesSearch', coordinates, total)
}

// function handleSearchByYear() {
// search('handleSearchByYear')
// emits('filterSearch', 'yearSearch')
// }

function onMapLinkClick() {
  showMap.value = !showMap.value

  if (showMap.value) {
    const isDetailView = store.getters['showDetail']
    if (!isDetailView) {
      emits('fetchMapData', false)
    }
  }

  isLoading.value = true
  setTimeout(() => {
    isLoading.value = false
  }, 5000)
}

function handlePaginateSearch() {
  emits('conditionalSearch', 'paginateSearch')
}
</script>
<style scoped>
.divBg {
  background: transparent;
}
</style>
