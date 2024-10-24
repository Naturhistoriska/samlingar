<template>
  <div>
    <Results
      v-if="isShowResults"
      @advanceSearch="handleAdvanceSearch"
      @coordinatesSearch="handleCoordinatesSearch"
      @exportData="handleExportData"
      @filterSearch="handleFilterSearch"
      @detailSearch="handleMapSearch"
      @simpleSearch="handleSimpleSearch"
    />
    <start-page
      v-else
      @advanceSearch="handleAdvanceSearch"
      @statiscSearch="handleStatisticSearch"
      @simpleSearch="handleSimpleSearch"
    />
  </div>
</template>
<script setup>
import { computed } from 'vue'
import Results from '../components/Results.vue'
import StartPage from '../components/StartPage.vue'

import Service from '../Service'
const service = new Service()

import { useStore } from 'vuex'
const store = useStore()

// let loading = ref(false)

const isShowResults = computed(() => {
  return store.getters['showResults']
})

function setFacet(facets) {
  const imageFacet = facets.image.buckets
  if (imageFacet !== null && imageFacet.length > 0) {
    const imageCount = imageFacet[0].count
    store.commit('setImageCount', imageCount)
  } else {
    store.commit('setImageCount', 0)
  }

  const isTyypeFacet = facets.isType.buckets
  if (isTyypeFacet !== null && isTyypeFacet.length > 0) {
    const isTypeCount = isTyypeFacet[0].count
    store.commit('setIsTypeCount', isTypeCount)
  } else {
    store.commit('setIsTypeCount', 0)
  }

  const inSwedenFacet = facets.inSweden.buckets
  if (inSwedenFacet !== null && inSwedenFacet.length > 0) {
    const inSwedenCount = inSwedenFacet[0].count
    store.commit('setInSwedenCount', inSwedenCount)
  } else {
    store.commit('setInSwedenCount', 0)
  }

  const mapFacet = facets.map.buckets
  if (mapFacet !== null && mapFacet.length > 0) {
    const mapCount = mapFacet[0].count
    store.commit('setHasCoordinatesCount', mapCount)
  } else {
    store.commit('setHasCoordinatesCount', 0)
  }
}

function handleStatisticSearch() {
  console.log('handleStatisticSearch')
  service
    .apiStatisticSearch()
    .then((response) => {
      console.log('response...', response)
      const facets = response.facets

      const totalCount = facets.count
      store.commit('setTotalCount', totalCount)

      setFacet(facets)

      store.commit('setSelectedCollection', null)
      store.commit('setSelectedType', null)
      store.commit('setSelectedFamily', null)
    })
    .catch()
    .finally(() => {})
}

function handleSimpleSearch() {
  const searchText = store.getters['searchText']
  const start = store.getters['startRecord']
  const numRows = store.getters['numPerPage']

  service
    .apiSimpleSearch(searchText, start, numRows)
    .then((response) => {
      console.log('response...', response)

      processAPIdata(response)
    })
    .catch()
    .finally(() => {})
}

function handleFilterSearch(value) {
  console.log('handleFilterSearch', value)
  const isAdvanceSearch = store.getters['isAdvanceSearch']
  if (isAdvanceSearch) {
    advanceConditionalSearch(value)
  } else {
    conditionalSearch(value)
  }
}

function conditionalSearch(value) {
  console.log('conditionalSearch', value)
  const collection = store.getters['selectedCollection']
  const numPerPage = store.getters['numPerPage']
  const searchText = store.getters['searchText']
  const start = store.getters['startRecord']
  const typeStatus = store.getters['selectedType']
  const family = store.getters['selectedFamily']

  service
    .apiConditionalSearch(searchText, collection, typeStatus, family, start, numPerPage)
    .then((response) => {
      processAPIdata(response, value)
    })
    .catch()
    .finally(() => {})
}

function processAPIdata(response, value) {
  const total = response.response.numFound
  const results = response.response.docs
  console.log('why....', resultssimpleSearch)
  const facets = response.facets

  setFacet(facets)

  if (value === 'filterByFamily') {
    const genus = facets.genus.buckets
    store.commit('setGenus', genus)
  } else {
    const family = facets.family.buckets
    store.commit('setFamily', family)
  }

  if (value !== 'filterByCollection') {
    const collections = facets.collectionName.buckets
    store.commit('setCollections', collections)
  }

  if (value != 'filterByType') {
    const typeStatus = facets.typeStatus.buckets
    store.commit('setTypeStatus', typeStatus)
  }

  store.commit('setTotalRecords', total)
  store.commit('setResults', results)

  store.commit('setShowDetail', false)
  store.commit('setShowResults', true)
  store.commit('setResetPaging', true)
}

function handleAdvanceSearch() {
  console.log('handleAdvanceSearch')

  const speciesGroup = store.getters['speciesGrouup']
  const dataset = store.getters['dataset']
  const catalogNumber = store.getters['catalogNumber']
  const endDate = store.getters['endDate']
  const scientificName = store.getters['scientificName']
  const startDate = store.getters['startDate']
  const isType = store.getters['isType']
  const start = store.getters['startRecord']
  const numRows = store.getters['numPerPage']

  service
    .advanceSearch(
      scientificName,
      speciesGroup,
      dataset,
      catalogNumber,
      startDate,
      endDate,
      isType,
      start,
      numRows
    )
    .then((response) => {
      const total = response.totalRecords
      const results = response.occurrences
      const facetResults = response.facetResults
      processResult(facetResults, results, total)
    })
    .catch()
    .finally(() => {})
}

function processResult(facetResults, results, total) {
  if (total > 0) {
    const typeStatusFacet = facetResults.find((facet) => facet.fieldName === 'typeStatus')
    const typeStatus = typeStatusFacet.fieldResult
    store.commit('setTypeStatus', typeStatus)

    const collectionFacet = facetResults.find((facet) => facet.fieldName === 'collectionName')
    const collections = collectionFacet.fieldResult
    store.commit('setCollections', collections)

    const pointFacet = facetResults.find((facet) => facet.fieldName === 'point-0.01')
    const point = pointFacet.fieldResult
    store.commit('setLatLong', point)

    console.log('length : ', point.length)

    const classFacet = facetResults.find((facet) => facet.fieldName === 'class')
    const classs = classFacet.fieldResult
    store.commit('setClasss', classs)

    console.log('classs length : ', classs.length)
    // store.commit('setLatLong', point)
  } else {
    store.commit('setCollections', [])
    store.commit('setLatLong', [])
    store.commit('setTypeStatus', [])
  }

  store.commit('setResults', results)
  store.commit('setTotalRecords', total)

  store.commit('setSelectedCollection', null)
  store.commit('setSelectedType', null)
  store.commit('setSelectedFamily', null)

  store.commit('setShowDetail', false)
  store.commit('setShowResults', true)
  store.commit('setResetPaging', true)
}

// function handleFilterSearch(value) {
//   console.log('handleFilterSearch', value)

//   const isAdvanceSearch = store.getters['isAdvanceSearch']
//   console.log('isAdvanceSearch', isAdvanceSearch)
//   if (isAdvanceSearch) {
//     advanceConditionalSearch(value)
//   } else {
//     conditionalSearch(value)
//   }
// }

function advanceConditionalSearch(value) {
  const collection = store.getters['selectedCollection']
  const typeStatus = store.getters['selectedType']
  const start = store.getters['startRecord']
  const numPerPage = store.getters['numPerPage']

  const speciesGroup = store.getters['speciesGrouup']
  const dataset = store.getters['dataset']
  const catalogNumber = store.getters['catalogNumber']
  const scientificName = store.getters['scientificName']
  const startDate = store.getters['startDate']
  const endDate = store.getters['endDate']
  const isType = store.getters['isType']

  service
    .advanceConditionalSearch(
      scientificName,
      speciesGroup,
      dataset,
      catalogNumber,
      startDate,
      endDate,
      isType,
      collection,
      typeStatus,
      start,
      numPerPage
    )
    .then((response) => {
      const total = response.totalRecords
      const results = response.occurrences
      const facetResults = response.facetResults

      processConditionalSearchResults(facetResults, results, total, value)
    })
    .catch()
    .finally(() => {})
}

function processConditionalSearchResults(facetResults, results, total, searchType) {
  if (total > 0) {
    if (searchType === 'paginateSearch') {
    } else {
      if (searchType === 'collectionSearch') {
        const typeStatusFacet = facetResults.find((facet) => facet.fieldName === 'typeStatus')
        const typeStatus = typeStatusFacet.fieldResult
        store.commit('setTypeStatus', typeStatus)
      } else if (searchType === 'typeStatusSearch') {
        console.log('here....')
        const collectionFacet = facetResults.find((facet) => facet.fieldName === 'collectionName')
        const collections = collectionFacet.fieldResult
        store.commit('setCollections', collections)
      }
      const latLongFacet = facetResults.find((facet) => facet.fieldName === 'point-0.01')
      const latLong = latLongFacet.fieldResult
      store.commit('setLatLong', latLong)
    }
  } else {
    // store.commit('setOccurrenceYears', [])
    store.commit('setCollections', [])
    store.commit('setLatLong', [])
    store.commit('setTypeStatus', [])

    store.commit('setSelectedCollection', null)
    store.commit('setSelectedType', null)
  }

  store.commit('setResults', results)
  store.commit('setTotalRecords', total)

  // store.commit('setSelectedCollection', null)
  // store.commit('setSelectedType', null)

  store.commit('setShowDetail', false)
  store.commit('setShowResults', true)
  store.commit('setResetPaging', true)
}

function handleCoordinatesSearch(coordinates, total) {
  console.log('handleCoordinatesSearch', coordinates, total)
  const isAdvanceSearch = store.getters['isAdvanceSearch']
  if (isAdvanceSearch) {
    advanceCoordinatesSearch(coordinates, total)
  } else {
    simpleCoordinsSearch(coordinates, total)
  }
}

function advanceCoordinatesSearch(coordinates, total) {
  console.log('advanceCoordinatesSearch', coordinates, total)

  const selectedCollection = store.getters['selectedCollection']
  const selectedTypeStatus = store.getters['selectedType']

  const speciesGroup = store.getters['speciesGrouup']
  const dataset = store.getters['dataset']
  const catalogNumber = store.getters['catalogNumber']
  const scientificName = store.getters['scientificName']
  const startDate = store.getters['startDate']
  const endDate = store.getters['endDate']
  const isType = store.getters['isType']

  service
    .advanceCoordinatesSearch(
      scientificName,
      speciesGroup,
      dataset,
      catalogNumber,
      startDate,
      endDate,
      isType,
      selectedCollection,
      selectedTypeStatus,
      coordinates,
      total
    )
    .then((response) => {
      const total = response.totalRecords

      console.log('total :', total)
      if (total > 0) {
        const results = response.occurrences
        store.commit('setMapRecords', results)
      }
      setTimeout(() => {
        // loading.value = false
      }, 2000)
    })
    .catch()
    .finally(() => {})
}

function simpleCoordinsSearch(coordinates, total) {
  console.log('simpleCoordinsSearch', coordinates, total)

  const collection = store.getters['selectedCollection']
  const searchText = store.getters['searchText']
  const typeStatus = store.getters['selectedType']

  service
    .simpleCoordinatesSearch(searchText, collection, typeStatus, coordinates, total)
    .then((response) => {
      const total = response.totalRecords

      console.log('total :', total)
      if (total > 0) {
        const results = response.occurrences
        store.commit('setMapRecords', results)
      }

      // store.commit('setShowDetail', true)
      // store.commit('setShowResults', true)
      // store.commit('setResetPaging', true)
      setTimeout(() => {
        // loading.value = false
      }, 2000)
    })
    .catch()
    .finally(() => {})
}

function handleMapSearch(coordinates) {
  console.log('handleMapSearch')
  const isAdvanceSearch = store.getters['isAdvanceSearch']
  if (isAdvanceSearch) {
    advanceMapDataSearch(coordinates)
  } else {
    simpleMapDataSearch(coordinates)
  }
}

function advanceMapDataSearch(coordinates) {
  console.log('simpleMapDataSearch', coordinates)

  const selectedCollection = store.getters['selectedCollection']
  const selectedTypeStatus = store.getters['selectedType']

  const speciesGroup = store.getters['speciesGrouup']
  const dataset = store.getters['dataset']
  const catalogNumber = store.getters['catalogNumber']
  const scientificName = store.getters['scientificName']
  const startDate = store.getters['startDate']
  const endDate = store.getters['endDate']
  const isType = store.getters['isType']

  catalogNumber, startDate, endDate, isType, selectedCollection, selectedTypeStatus, coordinates

  service
    .advancesearchMapData(
      scientificName,
      speciesGroup,
      dataset,
      catalogNumber,
      startDate,
      endDate,
      isType,
      selectedCollection,
      selectedTypeStatus,
      coordinates
    )
    .then((response) => {
      const total = response.totalRecords
      console.log('total: ', total)
      if (total >= 1) {
        const results = response.occurrences

        const id = results[0].uuid
        console.log('id:', id)
        getDetailById(id)
      }

      // store.commit('setShowDetail', true)
      // store.commit('setShowResults', true)
      // store.commit('setResetPaging', true)
      setTimeout(() => {
        // loading.value = false
      }, 2000)
    })
    .catch()
    .finally(() => {})
}

function simpleMapDataSearch(coordinates) {
  console.log('simpleMapDataSearch', coordinates)
  const collection = store.getters['selectedCollection']
  const searchText = store.getters['searchText']
  const typeStatus = store.getters['selectedType']

  service
    .simplesearchMapData(searchText, collection, typeStatus, coordinates)
    .then((response) => {
      const total = response.totalRecords
      if (total >= 1) {
        const results = response.occurrences

        const id = results[0].uuid
        console.log('id:', id)
        getDetailById(id)
      }

      // store.commit('setShowDetail', true)
      // store.commit('setShowResults', true)
      // store.commit('setResetPaging', true)
      setTimeout(() => {
        // loading.value = false
      }, 2000)
    })
    .catch()
    .finally(() => {})
}

function getDetailById(id) {
  service
    .uuidSearch(id)
    .then((response) => {
      store.commit('setShowDetail', true)
      store.commit('setSelectedResult', response)
    })
    .catch()
    .finally(() => {})
}

async function handleExportData() {
  console.log('exportData')

  store.commit('setExportData', null)

  const collection = store.getters['selectedCollection']
  const typeStatus = store.getters['selectedType']
  let totalRecords = store.getters['totalRecords']

  totalRecords = totalRecords <= 5000 ? totalRecords : 5000

  console.log('total Records', totalRecords)

  const speciesGroup = store.getters['speciesGrouup']
  const dataset = store.getters['dataset']
  const catalogNumber = store.getters['catalogNumber']
  const scientificName = store.getters['scientificName']
  const startDate = store.getters['startDate']
  const endDate = store.getters['endDate']
  const isType = store.getters['isType']

  await service
    .export(
      scientificName,
      speciesGroup,
      dataset,
      catalogNumber,
      startDate,
      endDate,
      isType,
      collection,
      typeStatus,
      totalRecords
    )
    .then((response) => {
      const results = response.occurrences
      store.commit('setExportData', results)
    })
    .catch()
    .finally(() => {})
}

// function handleAdvanceSearchForList() {
//   console.log('handleAdvanceSearchForList')
//   handleAdvanceSearch1(10)
// }

// function handleAdvanceSearchForMap() {
//   console.log('handleAdvanceSearchForMap')
//   const totalRecords = store.getters['totalRecords']
//   handleAdvanceSearch(totalRecords)
// }

// function handleAdvanceSearch1(row) {
//   console.log('handleAdvanceSearch....')

//   const speciesGroup = store.getters['speciesGrouup']
//   const dataset = store.getters['dataset']
//   const catalogNumber = store.getters['catalogNumber']
//   const scientificName = store.getters['scientificName']
//   const startDate = store.getters['startDate']
//   const endDate = store.getters['endDate']
//   const isType = store.getters['isType']

//   // loading = true
//   service
//     .advanceSearch(
//       scientificName,
//       speciesGroup,
//       dataset,
//       catalogNumber,
//       startDate,
//       endDate,
//       isType,
//       1,
//       row
//     )
//     .then((response) => {
//       const total = response.totalRecords

//       if (total > 0) {
//         const results = response.occurrences
//         const facetResults = response.facetResults

//         const yearFacet = facetResults.find((facet) => facet.fieldName === 'year')
//         const occurrenceYears = yearFacet.fieldResult
//         store.commit('setOccurrenceYears', occurrenceYears)

//         const collectionFacet = facetResults.find((facet) => facet.fieldName === 'collectionName')
//         const collections = collectionFacet.fieldResult
//         store.commit('setCollections', collections)

//         const latLongFacet = facetResults.find((facet) => facet.fieldName === 'lat_long')
//         const latLong = latLongFacet.fieldResult
//         store.commit('setLatLong', latLong)

//         store.commit('setResults', results)
//         store.commit('setTotalRecords', total)

//         store.commit('setSelectedCollection', null)
//         store.commit('setYear', null)
//       }

//       store.commit('setShowDetail', false)
//       store.commit('setShowResults', true)
//       store.commit('setResetPaging', true)

//       // setTimeout(() => {
//       // loading = false
//       // }, 2000)
//     })
//     .catch()
//     .finally(() => {})
// }
</script>
