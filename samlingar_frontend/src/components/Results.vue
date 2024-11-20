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
          <div class="col-12" style="font-size: 20px; float: left">
            <legend>
              {{ $t('results.searchResults') }} [{{ $t('results.num_results', totalRecords) }}]
              <br />
              <div class="col-12" style="float: left; text-align: left">
                <Download @exportData="preparaExportData" />
              </div>

              <div>
                <Button link @click="onMapLinkClick">
                  <small>{{ mapLinkText }}</small>
                </Button>
              </div>
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

        <!-- <LargeImage v-if="openLargeImage" /> -->
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

import Download from '../components/Download.vue'
import ResultDetail from '../components/ResultDetail.vue'
import ResultList from '../components/ResultList.vue'
import SearchFilter from '../components/SearchFilter.vue'
import Map from '../components/Map.vue'

import LargeImage from '../components/LargeImage.vue'

import Service from '../Service'
const service = new Service()

const router = useRouter()

const { t } = useI18n()

const store = useStore()

const emits = defineEmits(['advSearch', 'exportData', 'typeSearch', 'search', 'filterSearch'])

// const emits = defineEmits([
// 'advanceSearch',
// 'exportData',
// 'filterSearch',
//
// 'collectionSearch',
// 'conditionalSearch',
// 'detailSearch',
// 'coordinatesSearch',
//
// 'fetchMapData',
//
// 'simpleSearch'
// ])

let showMap = ref(false)

const isLoading = ref(false)

// const showDetail = computed(() => {
//   return store.getters['showDetail']
// })

// watch(
//   () => router.currentRoute.value.name,
//   () => {
//     const currentRouteName = router.currentRoute.value.name
//     console.log('current router name', currentRouteName)
//     if (currentRouteName !== 'Map') {
//       showMap.value = false
//     } else {
//       showMap.value = true
//     }
//   }
// )

// const showDetail = computed(() => {
//   console.log('router namme', router.currentRoute.value.name)

//   const currentRouteName = router.currentRoute.value.name
//   return currentRouteName === 'Result'
//   // return store.getters['showDetail']
// })

const showDetail = computed(() => {
  console.log('router namme', router.currentRoute.value.name)

  const currentRouteName = router.currentRoute.value.name
  return currentRouteName === 'Result'
})

const totalRecords = computed(() => {
  return store.getters['totalRecords']
})

const mapLinkText = computed(() => {
  // showDetail.value = store.getters['showDertail']
  if (showMap.value) {
    return showDetail ? t('results.backToReslutView') : t('results.backToResultListView')
  }
  return t('results.showMapView')
})

function handleCollectionSearch() {
  console.log('handleCollectionSearch')
  const isAdvanceSearch = store.getters['isAdvanceSearch']

  console.log('isAdvance', isAdvanceSearch)
  if (showMap.value) {
    if (isAdvanceSearch) {
    } else {
      fetchMapDataWithSimpleSearch(true, 'filterByCollection')
    }
  } else {
    if (isAdvanceSearch) {
      handleAdvanceSearch('filterByCollection')
    } else {
      processSearch('filterByCollection')
    }
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
    if (isAdvanceSearch) {
      handleAdvanceSearch('filterByType')
    } else {
      processSearch('filterByType')
    }
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
    if (isAdvanceSearch) {
      handleAdvanceSearch('filterByFamily')
    } else {
      processSearch('filterByFamily')
    }
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
    if (isAdvanceSearch) {
      handleAdvanceSearch()
    } else {
      processSearch()
    }
  }
}

function handleFilterSearch() {
  const isAdvanceSearch = store.getters['isAdvanceSearch']
  console.log('isAdvanceSearch', isAdvanceSearch)
  if (showMap.value) {
    if (isAdvanceSearch) {
    } else {
      fetchMapDataWithSimpleSearch(true)
    }
  } else {
    if (isAdvanceSearch) {
      handleAdvanceSearch()
    } else {
      processSearch()
    }
  }
}

function handlePaginateSearch() {
  const isAdvanceSearch = store.getters['isAdvanceSearch']
  if (isAdvanceSearch) {
    handleAdvanceSearch()
  } else {
    processSearch()
  }
}

function handleAdvanceSearch(value) {
  console.log('handleAdvanceSearch', value)
  emits('advSearch', value)
}

function processSearch(value) {
  emits('filterSearch', value)
}

function preparaExportData() {
  console.log('preparaExportData')
  emits('exportData')
}

// From map view
function handleSearchDetail(id) {
  console.log('handleSearchDetail', id)

  const isDetailView = store.getters['showDetail']
  if (isDetailView) {
    showMap.value = false
    router.push(`/record/${id}`)
  } else {
    emits('detailSearch', coordinates)
  }
}

function onMapLinkClick() {
  showMap.value = !showMap.value
  console.log('onMapLinkClick', showMap.value)

  const isDetailView = store.getters['showDetail']
  const isAdvanceSearch = store.getters['isAdvanceSearch']
  const record = store.getters['selectedResult']
  console.log('record', record)

  if (showMap.value) {
    if (!isDetailView) {
      if (isAdvanceSearch) {
      } else {
        fetchMapDataWithSimpleSearch(false)
      }
    }
    router.push('/map')
  } else {
    if (isDetailView) {
      router.push(`/record/${record.id}/`)
    } else {
      router.push('/records')
    }
  }

  // if (showMap.value) {
  //   if (!isDetailView) {
  //     if (isAdvanceSearch) {
  //     } else {
  //       fetchMapDataWithSimpleSearch(false)
  //     }
  //   }
  //   router.push('/map')
  // } else {
  // }
}

//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

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

// const openLargeImage = computed(() => {
//   console.log('openLarge image ', store.getters['openGalleria'])
//   return store.getters['openGalleria']
// })

// const showDetail = computed(() => {
//   console.log('router namme', router.currentRoute.value.name)

//   const currentRouteName = router.currentRoute.value.name
//   return currentRouteName === 'Result'
//   // return store.getters['showDetail']
// })

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
