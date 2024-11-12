<template>
  <div class="card">
    <div class="grid">
      <div class="flex flex-column col-5">
        <SearchFilter
          class="divBg"
          @collectionSearch="handleCollectionSearch"
          @typeSearch="handleTypeSearch"
          @familySearch="handleFamilySearch"
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

        <LargeImage v-if="openLargeImage" />
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
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'

import ResultDetail from '../components/ResultDetail.vue'
import ResultList from '../components/ResultList.vue'
import SearchFilter from '../components/SearchFilter.vue'
import Map from '../components/Map.vue'

import LargeImage from '../components/LargeImage.vue'

import Service from '../Service'
const service = new Service()

// // import { useLoading } from 'vue3-loading-overlay'
import 'vue3-loading-overlay/dist/vue3-loading-overlay.css'
// Import stylesheet
// import 'vue3-loading-overlay/dist/vue3-loading.css'

// import Loading from 'vue-loading-overlay'
// import 'vue-loading-overlay/dist/vue-loading.css'

const router = useRouter()

import Loading from 'vue3-loading-overlay'

// import downloadexcel from 'vue-json-excel3'

// import Service from '../Service'
// const service = new Service()

const { t } = useI18n()

const store = useStore()
const emits = defineEmits([
  'advanceSearch',
  'collectionSearch',
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

watch(
  () => router.currentRoute.value.name,
  () => {
    const currentRouteName = router.currentRoute.value.name
    if (currentRouteName !== 'Map') {
      showMap.value = false
    }
  }
)

// const json_data = computed(() => {
//   const data = store.getters['exportData']
//   console.log('data: ', data.length)

//   return toRaw(data)
// })

function handleSearchDetail(id) {
  console.log('handleSearchDetail', id)

  const isDetailView = store.getters['showDetail']
  if (isDetailView) {
    showMap.value = !showMap.value
    router.push(`/result/${id}`)
  } else {
    emits('detailSearch', coordinates)
  }
}

function onMapLinkClick() {
  showMap.value = !showMap.value

  if (showMap.value) {
    const isDetailView = store.getters['showDetail']
    if (!isDetailView) {
      const isAdvanceSearch = store.getters['isAdvanceSearch']
      if (isAdvanceSearch) {
      } else {
        fetchMapDataWithSimpleSearch(false)
      }
    }
    router.push('/map')
  }
}

function handleTypeSearch() {
  const isAdvanceSearch = store.getters['isAdvanceSearch']
  if (showMap.value) {
    if (isAdvanceSearch) {
    } else {
      fetchMapDataWithSimpleSearch(true, 'filterByType')
    }
  } else {
    processSearch('filterByType')
  }
}

function handleCollectionSearch() {
  const isAdvanceSearch = store.getters['isAdvanceSearch']
  if (showMap.value) {
    if (isAdvanceSearch) {
    } else {
      fetchMapDataWithSimpleSearch(true, 'filterByCollection')
    }
  } else {
    processSearch('filterByCollection')
  }
}

function handleFamilySearch() {
  const isAdvanceSearch = store.getters['isAdvanceSearch']
  if (showMap.value) {
    if (isAdvanceSearch) {
    } else {
      fetchMapDataWithSimpleSearch(true, 'filterByFamily')
    }
  } else {
    processSearch('filterByFamily')
  }
}

function fetchMapDataWithSimpleSearch(resetData, value) {
  console.log('fetchMapDataWithSimpleSearch', resetData, value)
  const collection = store.getters['selectedCollection']
  const searchText = store.getters['searchText']
  const typeStatus = store.getters['selectedType']
  const family = store.getters['selectedFamily']

  let collectionGroup = collection ? null : store.getters['collectionGroup']

  console.log('collectionGroup', collectionGroup)

  const hasCoordinates = store.getters['filterCoordinates']
  const hasImages = store.getters['filterImage']
  const isType = store.getters['filterType']
  const isInSweden = store.getters['filterInSweden']

  service
    .apiGeoDataSearch(
      searchText,
      collection,
      typeStatus,
      family,
      collectionGroup,
      hasCoordinates,
      hasImages,
      isType,
      isInSweden
    )
    .then((response) => {
      const array = response.geoData
      const count = response.count

      store.commit('setGeoData', array)
      store.commit('setTotalGeoData', count)

      if (resetData) {
        console.log('resetData...', resetData)
        const facets = response.facets
        setCommentFacet(facets)

        const results = response.docs
        const total = facets.count

        const familyFacet = facets.family
        if (familyFacet !== undefined) {
          const family = familyFacet.buckets
          console.log('family length', family.length)
          family.sort((a, b) => (a.val.toLowerCase() > b.val.toLowerCase() ? 1 : -1))
          store.commit('setFamily', family)
        } else {
          store.commit('setFamily', [])
        }

        const genusFacet = facets.genus
        if (genusFacet !== undefined) {
          const genus = genusFacet.buckets
          console.log('genus length', genus.length)
          genus.sort((a, b) => (a.val.toLowerCase() > b.val.toLowerCase() ? 1 : -1))
          store.commit('setGenus', genus)
        } else {
          store.commit('setGenus', [])
        }

        if (value !== 'filterByCollection') {
          const collections = facets.collectionName.buckets
          store.commit('setCollections', collections)
        }

        if (value != 'filterByType') {
          const typeStatus = facets.typeStatus.buckets
          console.log('typeStatus length', typeStatus.length)
          store.commit('setTypeStatus', typeStatus)
        }

        store.commit('setTotalRecords', total)
        store.commit('setResults', results)
      }
    })
    .catch()
    .finally(() => {})
}

function processSearch(value) {
  const selectedCollection = store.getters['selectedCollection']

  let collectionGroup = selectedCollection ? null : store.getters['collectionGroup']

  console.log('collectionGroup', collectionGroup)

  const typeStatus = store.getters['selectedType']
  const family = store.getters['selectedFamily']

  const hasCoordinates = store.getters['filterCoordinates']
  const hasImages = store.getters['filterImage']
  const isType = store.getters['filterType']
  const isInSweden = store.getters['filterInSweden']

  const numPerPage = store.getters['numPerPage']
  const start = store.getters['startRecord']

  const searchText = store.getters['searchText']
  console.log('search text', searchText)

  // let path
  service
    .apiFilterSearch(
      searchText,
      selectedCollection,
      typeStatus,
      family,
      collectionGroup,
      hasCoordinates,
      hasImages,
      isType,
      isInSweden,
      start,
      numPerPage
    )
    .then((response) => {
      processAPIdata(response, value)

      // if (value === 'filterByCollection') {
      //   path = `filter/${selectedCollection}`
      // }

      // if (value === 'filterByType') {
      //   path = `type/${typeStatus}`
      // }
    })
    .catch()
    .finally(() => {
      router.push('/results')
      // router.push(`/results/${path}`)
    })
}

function handleFilterSearch() {
  const isAdvanceSearch = store.getters['isAdvanceSearch']
  if (showMap.value) {
    if (isAdvanceSearch) {
    } else {
      fetchMapDataWithSimpleSearch(true)
    }
  } else {
    processSearch()
  }
}

// this search after clear all filter
function handleSearch() {
  console.log('handleSearch [clear filter search]')

  const isAdvanceSearch = store.getters['isAdvanceSearch']
  if (showMap.value) {
    if (isAdvanceSearch) {
    } else {
      fetchMapDataWithSimpleSearch(true)
    }
  } else {
    processSearch()
  }
}

function handlePaginateSearch() {
  processSearch()
}

function processAPIdata(response, value) {
  const total = response.response.numFound
  const results = response.response.docs

  if (total > 0) {
    const facets = response.facets

    setCommentFacet(facets)

    const familyFacet = facets.family
    if (familyFacet !== undefined) {
      const family = familyFacet.buckets
      console.log('family length', family.length)
      family.sort((a, b) => (a.val.toLowerCase() > b.val.toLowerCase() ? 1 : -1))
      store.commit('setFamily', family)
    } else {
      store.commit('setFamily', [])
    }

    const genusFacet = facets.genus
    if (genusFacet !== undefined) {
      const genus = genusFacet.buckets
      console.log('genus length', genus.length)
      genus.sort((a, b) => (a.val.toLowerCase() > b.val.toLowerCase() ? 1 : -1))
      store.commit('setGenus', genus)
    } else {
      store.commit('setGenus', [])
    }

    if (value !== 'filterByCollection') {
      const collections = facets.collectionName.buckets
      store.commit('setCollections', collections)
    }

    if (value != 'filterByType') {
      const typeStatus = facets.typeStatus.buckets
      typeStatus.sort((a, b) => (a.val.toLowerCase() > b.val.toLowerCase() ? 1 : -1))
      console.log('typeStatus length', typeStatus.length)
      store.commit('setTypeStatus', typeStatus)
    }
  }

  store.commit('setTotalRecords', total)
  store.commit('setResults', results)

  store.commit('setShowDetail', false)
  store.commit('setShowResults', true)
  store.commit('setResetPaging', true)
}

function setCommentFacet(facets) {
  const imageFacet = facets.image.buckets
  if (imageFacet.length > 0) {
    const imageCount = imageFacet[0].count
    store.commit('setImageCount', imageCount)
  } else {
    store.commit('setImageCount', 0)
  }

  const isTypeFacet = facets.isType.buckets
  if (isTypeFacet.length > 0) {
    const isTypeCount = isTypeFacet[0].count
    store.commit('setIsTypeCount', isTypeCount)
  } else {
    store.commit('setIsTypeCount', 0)
  }

  const inSwedenFacet = facets.inSweden.buckets
  if (inSwedenFacet.length > 0) {
    const inSwedenCount = inSwedenFacet[0].count
    store.commit('setInSwedenCount', inSwedenCount)
  } else {
    store.commit('setInSwedenCount', 0)
  }

  const mapFacet = facets.map.buckets
  if (mapFacet.length > 0) {
    const mapCount = mapFacet[0].count
    store.commit('setHasCoordinatesCount', mapCount)
  } else {
    store.commit('setHasCoordinatesCount', 0)
  }
}

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
function handleFilterSearch1() {
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

const openLargeImage = computed(() => {
  console.log('openLarge image ', store.getters['openGalleria'])
  return store.getters['openGalleria']
})

const mapLinkText = computed(() => {
  if (showMap.value) {
    return showDetail.value ? t('results.backToReslutView') : t('results.backToResultListView')
  }
  return t('results.showMapView')
})

const showDetail = computed(() => {
  console.log('router namme', router.currentRoute.value.name)

  const currentRouteName = router.currentRoute.value.name
  return currentRouteName === 'Result'
  // return store.getters['showDetail']
})

const totalRecords = computed(() => {
  return store.getters['totalRecords']
})

// function handleConditionSearch(value) {
//   console.log('handleConditionSearch')

//   if (showMap.value) {
//     emits('fetchMapData', true, value)
//   } else {
//     emits('conditionalSearch', value)
//   }
// }

function handleResetView(coordinates, total) {
  console.log('handleResetView', coordinates, total)

  emits('coordinatesSearch', coordinates, total)
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
