<template>
  <div class="card">
    <div class="grid">
      <div class="flex-col gap-2 searchInput">
        <InputGroup>
          <InputText
            id="simpleSearchInput1"
            v-model="value"
            @keydown.enter="onSearch"
            :placeholder="$t('search.freeTextSearch')"
            aria-describedby="simpleSearchInput-help"
            class="w-full"
          />
          <Button
            icon="pi pi-search"
            style="max-width: 30px; min-height: 30px"
            :loading="loading"
            @click="onSearch"
          />
        </InputGroup>
      </div>
    </div>
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
          <!-- <ProgressSpinner
            v-if="isLoading"
            aria-label="Loading"
            style="width: 50px; height: 50px; position: relative; padding-left: 80%"
            strokeWidth="8"
            fill="transparent"
          /> -->
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
import { computed, ref, watch } from 'vue'
import { onBeforeRouteLeave, onBeforeRouteUpdate, useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { useI18n } from 'vue-i18n'

import Download from '../components/Download.vue'
import ResultDetail from '../components/ResultDetail.vue'
import ResultList from '../components/ResultList.vue'
import SearchFilter from '../components/SearchFilter.vue'
import Map from '../components/Map.vue'

import Service from '../Service'
const service = new Service()

const router = useRouter()

const { t } = useI18n()

const store = useStore()

let showMap = ref(false)
// const isLoading = ref(false)
const loading = ref(false)

const value = ref()

watch(
  () => router.currentRoute.value.name,
  () => {
    const currentRouteName = router.currentRoute.value.name
    if (currentRouteName !== 'Map') {
      showMap.value = false
    }
  }
)

onBeforeRouteUpdate(async (to, from) => {
  // react to route changes...
  console.log('onBeforeRouteUpdate', to, from)
  // userData.value = await fetchUser(to.params.id)
})

onBeforeRouteLeave((to, from) => {
  console.log('onBeforeRouteLeave', to, from)
  const { name } = to
  if (name === 'Home') {
    store.commit('setShowResults', false)
  }
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

function preparaExportData() {
  console.log('preparaExportData')
  const collection = store.getters['selectedCollection']
  const typeStatus = store.getters['selectedType']
  const family = store.getters['selectedFamily']
  const totalRecords = store.getters['totalRecords']
  const searchText = store.getters['searchText']

  service
    .apiPreparaExport(searchText, collection, typeStatus, family, totalRecords)
    .then((response) => {
      const results = response

      store.commit('setExportData', results)
    })
    .catch()
    .finally(() => {})
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
      processAdvanceSearch(0, 10)
    } else {
      processSearch(0, 10)
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
      processAdvanceSearch(0, 10, 'filterByType')
    } else {
      processSearch(0, 10, 'filterByType')
    }
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
    if (isAdvanceSearch) {
      processAdvanceSearch(0, 10, 'filterByCollection')
    } else {
      processSearch(0, 10, 'filterByCollection')
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
      processAdvanceSearch(0, 10, 'filterByFamily')
    } else {
      processSearch(0, 10, 'filterByFamily')
    }
  }
}

function handlePaginateSearch() {
  const numPerPage = store.getters['numPerPage']
  const start = store.getters['startRecord']
  const isAdvanceSearch = store.getters['isAdvanceSearch']
  if (isAdvanceSearch) {
    processAdvanceSearch(start, numPerPage, 'paginateSearch')
  } else {
    processSearch(start, numPerPage, 'paginateSearch')
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
      processAdvanceSearch(0, 10)
    } else {
      processSearch(0, 10)
    }
  }
}

function processAdvanceSearch(start, numPerPage, value) {
  const searchText = store.getters['searchText']

  const collection = store.getters['selectedCollection']
  const typeStatus = store.getters['selectedType']
  const family = store.getters['selectedFamily']

  const hasCoordinates = store.getters['filterCoordinates']
  const hasImages = store.getters['filterImage']
  const isType = store.getters['filterType']
  const isInSweden = store.getters['filterInSweden']

  service
    .apiAdvanceSearchWithFilters(
      searchText,
      collection,
      typeStatus,
      family,
      hasCoordinates,
      hasImages,
      isType,
      isInSweden,
      start,
      numPerPage
    )
    .then((response) => {
      processAPIdata(response, value)
    })
    .catch()
    .finally(() => {
      router.push('/records')
    })
}

function processSearch(start, numPerPage, value) {
  const selectedCollection = store.getters['selectedCollection']

  let collectionGroup = selectedCollection ? null : store.getters['collectionGroup']

  console.log('collectionGroup', collectionGroup)

  const typeStatus = store.getters['selectedType']
  const family = store.getters['selectedFamily']

  const hasCoordinates = store.getters['filterCoordinates']
  const hasImages = store.getters['filterImage']
  const isType = store.getters['filterType']
  const isInSweden = store.getters['filterInSweden']

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
    })
    .catch()
    .finally(() => {
      router.push('/records')
    })
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

    if (value === undefined) {
      console.log('value', value)

      const collections = facets.collectionName.buckets
      store.commit('setCollections', collections)

      const typeStatus = facets.typeStatus.buckets
      typeStatus.sort((a, b) => (a.val.toLowerCase() > b.val.toLowerCase() ? 1 : -1))
      console.log('typeStatus length', typeStatus.length)
      store.commit('setTypeStatus', typeStatus)
    } else {
      if (value !== 'paginateSearch') {
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
    }
  } else {
    store.commit('setFamily', [])
    store.commit('setGenus', [])
    store.commit('setCollections', [])
    store.commit('setTypeStatus', [])
    store.commit('setImageCount', 0)
    store.commit('setIsTypeCount', 0)
    store.commit('setHasCoordinatesCount', 0)
    store.commit('setInSwedenCount', 0)
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

function handleSearchDetail(id) {
  console.log('handleSearchDetail', id)

  const isDetailView = store.getters['showDetail']
  if (isDetailView) {
    showMap.value = !showMap.value
    router.push(`/result/${id}`)
  } else {
    // emits('detailSearch', coordinates)
  }
}

function handleResetView(coordinates, total) {
  console.log('handleResetView', coordinates, total)

  // emits('coordinatesSearch', coordinates, total)
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
      const facets = response.facets
      const array = facets.geohash.buckets
      // const count = response.count

      store.commit('setGeoData', array)

      if (resetData) {
        console.log('resetData...', resetData)

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
function onSearch() {
  loading.value = true

  let searchText = '*:*'
  if (value.value) {
    searchText = '%2B(text:' + value.value + '*' + ' text:"' + value.value + '")'
  }

  const isAdvanceSearch = store.getters['isAdvanceSearch']
  if (isAdvanceSearch) {
    searchText = searchText + ' ' + store.getters['searchText']
  }
  console.log('search text : ', searchText)
  store.commit('setSearchText', searchText)

  handleSearch()

  // emits('simpleSearch')

  setTimeout(() => {
    loading.value = false
  }, 2000)
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

.searchInput {
  min-width: 60%;
  margin-bottom: 40px;
  text-align: left;
  float: left;
}
</style>
