<template>
  <div>
    <Results
      v-if="isShowResults"
      @advanceSearch="handleAdvanceSearch"
      @conditionalSearch="handConditionalSearch"
      @coordinatesSearch="handleCoordinatesSearch"
      @exportData="preparaExportData"
      @detailSearch="handleSingleMarkerSearch"
      @fetchMapData="handleFetchMapData"
      @filterSearch="handleFilterSearch"
      @simpleSearch="handleSimpleSearch"
    />
    <start-page
      v-else
      @advanceSearch="handleAdvanceSearch"
      @searchWithFilter="handleSearchWithFilter"
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

function handleStatisticSearch() {
  service
    .apiStatisticSearch()
    .then((response) => {
      const facets = response.facets

      const totalCount = facets.count
      store.commit('setTotalCount', totalCount)

      setCommentFacet(facets)

      const zooGroup = ['ev', 'et', '262144', '655361', '163840', 'ma', 'fish', 'herps', 'va']
      const paleaGroup = ['pb', 'pz']
      const geoGroup = ['557057', '753664', '786432']
      const botanyGroup = ['vp', 'fungi', 'mosses', 'algae']

      let zooCount = 0
      let paleaCount = 0
      let botanyCount = 0
      let geoCount = 0
      const collections = facets.collectionId.buckets

      for (let i = 0; i < collections.length; i++) {
        const collection = collections[i]
        const collectionId = collection.val
        const count = collection.count
        if (zooGroup.includes(collectionId)) {
          zooCount += count
        } else if (paleaGroup.includes(collectionId)) {
          paleaCount += count
        } else if (geoGroup.includes(collectionId)) {
          geoCount += count
        } else if (botanyGroup.includes(collectionId)) {
          botanyCount += collection.count
        }
      }

      store.commit('setBotanyCollectionTotal', botanyCount)
      store.commit('setGeoCollectionTotal', geoCount)
      store.commit('setPaleaCollectionTotal', paleaCount)
      store.commit('setZooCollectionTotal', zooCount)

      store.commit('setFilterImage', false)
      store.commit('setFilterCoordinates', false)
      store.commit('setFilterInSweden', false)
      store.commit('setFilterType', false)

      store.commit('setSelectedCollection', null)
      store.commit('setSelectedType', null)
      store.commit('setSelectedFamily', null)
    })
    .catch()
    .finally(() => {})
}

function handleSimpleSearch() {
  const searchText = store.getters['searchText']

  console.log('search text ...', searchText)
  const start = 0
  const numRows = 10

  service
    .apiSimpleSearch(searchText, start, numRows)
    .then((response) => {
      processAPIdata(response)
    })
    .catch()
    .finally(() => {})
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

  const isTyypeFacet = facets.isType.buckets
  if (isTyypeFacet.length > 0) {
    const isTypeCount = isTyypeFacet[0].count
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

// function handleMapSearch() {
//   const isAdvanceSearch = store.getters['isAdvanceSearch']
//   if (isAdvanceSearch) {
//   } else {
//     handConditionalSearchWithFilter('map:*')
//   }
// }

// function handConditionalSearchWithFilter() {
//   const collection = store.getters['selectedCollection']
//   const searchText = store.getters['searchText']
//   const typeStatus = store.getters['selectedType']
//   const family = store.getters['selectedFamily']

//   service
//     .apiConditinalSearchWithFilter(searchText, collection, typeStatus, family)
//     .then((response) => {
//       processAPIdata(response)
//     })
//     .catch()
//     .finally(() => {})
// }

function handConditionalSearch(value) {
  console.log('handConditionalSearch')
  const isAdvanceSearch = store.getters['isAdvanceSearch']
  if (isAdvanceSearch) {
    advanceConditionalSearch(value)
  } else {
    conditionalSearch(value)
  }
}

function conditionalSearch(value) {
  console.log('conditionalSearch')
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

// Filter link from results page
function handleFilterSearch(value) {
  console.log('handleFilterSearch', value)
  let searchText = store.getters['searchText']

  searchText = searchText + value
  console.log('what...', searchText)

  store.commit('setSearchText', searchText)
  const isAdvanceSearch = store.getters['isAdvanceSearch']

  if (isAdvanceSearch) {
  } else {
    conditionalSearch()
  }
}

// simple search filter with map, type, sweden, coordinates and collections from start page
function handleSearchWithFilter() {
  console.log('handleSearchWithFilter')

  const start = store.getters['startRecord']
  const numRows = store.getters['numPerPage']
  const searchText = store.getters['searchText']

  service
    .apiSimpleSearch(searchText, start, numRows)
    .then((response) => {
      console.log('response...', response)

      processAPIdata(response)
    })
    .catch()
    .finally(() => {})
}

// Fetch data for displaying map (When in map view)
function handleFetchMapData(resetData) {
  const isAdvanceSearch = store.getters['isAdvanceSearch']
  if (isAdvanceSearch) {
  } else {
    fetchMapDataWithSimpleSearch(resetData)
  }
}

function fetchMapDataWithSimpleSearch(resetData, value) {
  console.log('fetchMapDataWithSimpleSearch', resetData, value)
  const collection = store.getters['selectedCollection']
  const searchText = store.getters['searchText']
  const typeStatus = store.getters['selectedType']
  const family = store.getters['selectedFamily']

  service
    .apiGeoDataSearch(searchText, collection, typeStatus, family)
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

async function preparaExportData() {
  console.log('preparaExportData')

  // store.commit('setExportData', null)

  const collection = store.getters['selectedCollection']
  const typeStatus = store.getters['selectedType']
  const family = store.getters['selectedFamily']
  const totalRecords = store.getters['totalRecords']
  const searchText = store.getters['searchText']

  await service
    .apiPreparaExport(searchText, collection, typeStatus, family, totalRecords)
    .then((response) => {
      const results = response

      store.commit('setExportData', results)
    })
    .catch()
    .finally(() => {})
}

// SBDI
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

function handleSingleMarkerSearch(coordinates) {
  console.log('handleSingleMarkerSearch')
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
