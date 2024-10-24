<template>
  <div class="card">
    <div class="grid">
      <div class="flex flex-column col-4">
        <SearchFilter class="divBg" @search="handleSearch" @filterSearch="handleFilterSearch" />
      </div>
      <div class="col-8">
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
            <!-- <div>
              <Loading1
                id="myLoad"
                :active="isLoading"
                :can-cancel="true"
                color="#34d399"
                background-color="#333333"
              >
              </Loading1>
            </div> -->
          </div>
        </div>

        <Map v-if="showMap" @searchDetial="handleSearchDetail" @resetView="handleResetView" />
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

// import Loading1 from 'vue3-loading-overlay'
// // import { useLoading } from 'vue3-loading-overlay'
// import 'vue3-loading-overlay/dist/vue3-loading-overlay.css'
// Import stylesheet
// import 'vue3-loading-overlay/dist/vue3-loading.css'

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
  'detailSearch',
  'coordinatesSearch',
  'exportData',
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

// watch(
//   () => showMap.value,
//   (newValue, oldValue) => {
//     console.log('showmap changed...', showMap)
//     showMap.value = newValue
//   }
// )

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

function handleSearch() {
  // this search after clear all filter
  console.log('handleSearch [clear filter search]')

  const isAdvanceSearch = store.getters['isAdvanceSearch']
  if (isAdvanceSearch) {
    emits('advanceSearch')
  } else {
    emits('simpleSearch')
  }
}

function handleFilterSearch(value) {
  console.log('handleFilterSearch')

  emits('filterSearch', value)
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
  const isMap = showMap.value

  // let loader = useLoading()
  // loader.show({
  //   // Optional parameters
  //   // container: this.fullPage ? null : formContainer.value,
  //   canCancel: true
  //   // onCancel: onCancel
  // })
  // // simulate AJAX
  // setTimeout(() => {
  //   loader.hide()
  // }, 5000)

  isLoading.value = true
  setTimeout(() => {
    isLoading.value = false
  }, 2000)

  showMap.value = !isMap
}

function handlePaginateSearch() {
  emits('filterSearch', 'paginateSearch')
}

// async function handleMapSearch() {

// const collection = store.getters['selectedCollection']
// const totalRecords = store.getters['totalRecords']
// const searchText = store.getters['searchText']
// const year = store.getters['year']
//
// service
// .conditionalSearch(searchText, 1, collection, year, totalRecords)
// .then((response) => {
// const results = response.occurrences
//
// store.commit('setMapRecords', results)
// setTimeout(() => {
// }, 2000)
// })
// .catch()
// .finally(() => {})
// }

// function search(value) {
//   console.log('search....')
//   const collection = store.getters['selectedCollection']
//   const numPerPage = store.getters['numPerPage']
//   const searchText = store.getters['searchText']
//   const start = store.getters['startRecord']
//   const year = store.getters['year']

//   service
//     .searchFiltByCollection(searchText, start, collection, year, numPerPage)
//     .then((response) => {
//       const total = response.totalRecords
//       const results = response.occurrences
//       const facetResults = response.facetResults

//       const latLongFacet = facetResults.find((facet) => facet.fieldName === 'lat_long')
//       const latLong = latLongFacet.fieldResult
//       store.commit('setLatLong', latLong)

//       if (value === 'paginateSearch') {
//         store.commit('setNumPerPage', numPerPage)
//         store.commit('setStartRecord', start)
//       } else if (value === 'collectionSearch') {
//         const yearFacet = facetResults.find((facet) => facet.fieldName === 'year')
//         const occurrenceYears = yearFacet.fieldResult

//         store.commit('setOccurrenceYears', occurrenceYears)
//         store.commit('setSelectedCollection', collection)
//       } else if (value === 'yearSearch') {
//         const collectionFacet = facetResults.find((facet) => facet.fieldName === 'collectionName')
//         const collections = collectionFacet.fieldResult

//         store.commit('setCollections', collections)
//         store.commit('setYear', year)
//       }
//       store.commit('setResults', results)
//       store.commit('setTotalRecords', total)
//       setTimeout(() => {
//         // loading = false
//       }, 2000)
//     })
//     .catch()
//     .finally(() => {})
// }
</script>
<style scoped>
.divBg {
  background: transparent;
}
</style>
