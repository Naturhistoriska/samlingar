<template>
  <div>
    <div class="grid homePage">
      <start-page
        @filterSearch="filterSearch"
        @search="search"
        @collectionsSearch="collectionsSearch"
      />
    </div>

    <div class="grid" style="margin-top: 10px">
      <StatisticCharts />
    </div>
  </div>
</template>
<script setup>
import { onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import StartPage from '../components/StartPage.vue'
import StatisticCharts from '../components/StatisticCharts.vue'

// import { useI18n } from 'vue-i18n'

import moment from 'moment'

import Service from '../Service'
const service = new Service()

const store = useStore()
const router = useRouter()

// const { t } = useI18n()

onMounted(() => {
  fetchInitdata()
  fetchYearChartData()
  fetchMonthChartData()
})

function fetchMonthChartData() {
  service
    .apiChart('*', false)
    .then((response) => {
      const counts = response.facet_counts.facet_ranges.createdDate_dt.counts

      setMonthChartData(counts)
    })
    .catch()
    .finally(() => {})
}

function fetchYearChartData() {
  service
    .apiChart('*', true)
    .then((response) => {
      const counts = response.facet_counts.facet_ranges.createdDate_dt.counts

      const totalCount = response.total
      store.commit('setTotalCount', totalCount)

      setYearChartData(totalCount, counts)
    })
    .catch()
    .finally(() => {})
}

function setMonthChartData(monthData) {
  const cumulativeData = {}
  let key
  for (const [date, value] of Object.entries(monthData)) {
    key = moment(date).format('MMM YYYY')
    cumulativeData[key] = value
  }
  store.commit('setMonthData', cumulativeData)
}

function setYearChartData(total, years) {
  const sum = Object.values(years).reduce((total, num) => total + num, 0)
  let cumulatedTotal = total - sum
  let key
  const cumulativeData = {}

  for (const [date, value] of Object.entries(years)) {
    cumulatedTotal += value
    key = moment(date).year()
    cumulativeData[key] = cumulatedTotal
  }
  store.commit('setYearData', cumulativeData)
}

function fetchInitdata() {
  service
    .apiInitdata()
    .then((response) => {
      const facets = response.facets

      const totalCount = facets.count
      store.commit('setTotalCount', totalCount)

      setSearchCommentFacet(facets)

      store.commit('setFilterImage', false)
      store.commit('setFilterCoordinates', false)
      store.commit('setFilterInSweden', false)
      store.commit('setFilterType', false)

      store.commit('setCollectionGroup', null)
    })
    .catch()
    .finally(() => {})
}

function setSearchCommentFacet(facets) {
  const imageFacet = facets.associatedMedia.buckets
  const imageCount = imageFacet.length > 0 ? imageFacet[0].count : 0
  store.commit('setImageCount', imageCount)

  const typeStatus = facets.typeStatus.allBuckets.count
  store.commit('setIsTypeCount', typeStatus)

  const inSwedenFacet = facets.inSweden.buckets
  const inSwedenCount = inSwedenFacet.length > 0 ? inSwedenFacet[0].count : 0
  store.commit('setInSwedenCount', inSwedenCount)

  const mapCount = facets.map.allBuckets.count
  store.commit('setHasCoordinatesCount', mapCount)
}

async function filterSearch(
  filtCoordinates,
  filtImages,
  filtInSweden,
  filtTypeStatus,
  start,
  numPerPage
) {
  await service
    .apiFilterSearch(filtCoordinates, filtImages, filtInSweden, filtTypeStatus, start, numPerPage)
    .then((response) => {
      const total = response.facets.count
      const results = response.response

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      if (total > 0) {
        const geofacet = response.facets.map.buckets
        const collectionfacet = response.facets.dataResourceName.buckets

        store.commit('setGeoData', geofacet)
        store.commit('setCollections', collectionfacet)
      } else {
        store.commit('setGeoData', null)
        store.commit('setCollections', null)
      }
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      router.push('/search')
    })
}

async function collectionsSearch(value, start, numPerPage) {
  await service
    .apiFilterCollectionsSearch(value, start, numPerPage)
    .then((response) => {
      const total = response.facets.count
      const results = response.response

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      if (total > 0) {
        const geofacet = response.facets.map.buckets
        const collectionfacet = response.facets.dataResourceName.buckets

        store.commit('setGeoData', geofacet)
        store.commit('setCollections', collectionfacet)
      } else {
        store.commit('setGeoData', null)
        store.commit('setCollections', null)
      }
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      router.push('/search')
    })
}

async function search(value, start, numPerPage) {
  await service
    .apiFreeTextSearch(value, start, numPerPage)
    .then((response) => {
      const total = response.facets.count
      const results = response.response

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      if (total > 0) {
        const geofacet = response.facets.map.buckets
        const collectionfacet = response.facets.dataResourceName.buckets

        store.commit('setGeoData', geofacet)
        store.commit('setCollections', collectionfacet)
      } else {
        store.commit('setGeoData', null)
        store.commit('setCollections', null)
      }
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      router.push('/search')
    })
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

// function setChartData(facets) {
//   const total = facets.count
//   const years = facets.catalogedYear.buckets

//   buildMonthChartData(years)
//   setYearChartData(total, years)
// }

// function buildMonthChartData(years) {
//   const currentYear = moment().year()
//   const lastYear = currentYear - 1
//   const currentMonth = moment().month()

//   const currentYearData = years.filter((year) => year.val === currentYear)
//   const lastYearData = years.filter((year) => year.val === lastYear)

//   let lastYearMonthData
//   if (lastYearData[0].count > 0) {
//     lastYearMonthData = lastYearData[0].catalogedMonth.buckets
//   } else {
//     lastYearMonthData = new Array()
//   }

//   let monthLabel
//   let currentYearMonthData
//   if (currentYearData[0].count > 0) {
//     currentYearMonthData = currentYearData[0].catalogedMonth.buckets
//   } else {
//     currentYearMonthData = new Array()
//   }

//   let newMonthData = new Array()

//   for (let i = 0; i < 12; i++) {
//     const filterMonth = moment().month(i).format('MMMM').toUpperCase()

//     if (i <= currentMonth) {
//       monthLabel = moment().month(i).format('MMMM YYYY').toUpperCase()
//     } else {
//       monthLabel = filterMonth + ' ' + lastYear
//     }

//     const month =
//       currentMonth >= i
//         ? currentYearMonthData.filter((month) => month.val === filterMonth)
//         : lastYearMonthData.filter((month) => month.val === filterMonth)

//     if (!month || month.length === 0) {
//       newMonthData.push({ val: monthLabel, count: 0 })
//     } else {
//       newMonthData.push({ val: monthLabel, count: month[0].count })
//     }
//   }
//   newMonthData.sort((a, b) => moment(a.val, 'MMM-yy') - moment(b.val, 'MMM-yy'))
//   store.commit('setMonthData', newMonthData)
// }

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

// SBDI
// function handleAdvanceSearchSbdi() {
//   console.log('handleAdvanceSearch')

//   const speciesGroup = store.getters['speciesGrouup']
//   const dataset = store.getters['dataset']
//   const catalogNumber = store.getters['catalogNumber']
//   const endDate = store.getters['endDate']
//   const scientificName = store.getters['scientificName']
//   const startDate = store.getters['startDate']
//   const isType = store.getters['isType']
//   const start = store.getters['startRecord']
//   const numRows = store.getters['numPerPage']

//   service
//     .advanceSearch(
//       scientificName,
//       speciesGroup,
//       dataset,
//       catalogNumber,
//       startDate,
//       endDate,
//       isType,
//       start,
//       numRows
//     )
//     .then((response) => {
//       const total = response.totalRecords
//       const results = response.occurrences
//       const facetResults = response.facetResults
//       processResult(facetResults, results, total)
//     })
//     .catch()
//     .finally(() => {})
// }

// function processResult(facetResults, results, total) {
//   if (total > 0) {
//     const typeStatusFacet = facetResults.find((facet) => facet.fieldName === 'typeStatus')
//     const typeStatus = typeStatusFacet.fieldResult
//     store.commit('setTypeStatus', typeStatus)

//     const collectionFacet = facetResults.find((facet) => facet.fieldName === 'collectionName')
//     const collections = collectionFacet.fieldResult
//     store.commit('setCollections', collections)

//     const pointFacet = facetResults.find((facet) => facet.fieldName === 'point-0.01')
//     const point = pointFacet.fieldResult
//     store.commit('setLatLong', point)

//     const classFacet = facetResults.find((facet) => facet.fieldName === 'class')
//     const classs = classFacet.fieldResult
//     store.commit('setClasss', classs)
//   } else {
//     store.commit('setCollections', [])
//     store.commit('setLatLong', [])
//     store.commit('setTypeStatus', [])
//   }

//   store.commit('setResults', results)
//   store.commit('setTotalRecords', total)

//   store.commit('setSelectedCollection', null)
//   store.commit('setSelectedType', null)
//   store.commit('setSelectedFamily', null)

//   store.commit('setShowDetail', false)
//   store.commit('setShowResults', true)
//   store.commit('setResetPaging', true)
// }

// function advanceConditionalSearch(value) {
//   const collection = store.getters['selectedCollection']
//   const typeStatus = store.getters['selectedType']
//   const start = store.getters['startRecord']
//   const numPerPage = store.getters['numPerPage']

//   const speciesGroup = store.getters['speciesGrouup']
//   const dataset = store.getters['dataset']
//   const catalogNumber = store.getters['catalogNumber']
//   const scientificName = store.getters['scientificName']
//   const startDate = store.getters['startDate']
//   const endDate = store.getters['endDate']
//   const isType = store.getters['isType']

//   service
//     .advanceConditionalSearch(
//       scientificName,
//       speciesGroup,
//       dataset,
//       catalogNumber,
//       startDate,
//       endDate,
//       isType,
//       collection,
//       typeStatus,
//       start,
//       numPerPage
//     )
//     .then((response) => {
//       const total = response.totalRecords
//       const results = response.occurrences
//       const facetResults = response.facetResults

//       processConditionalSearchResults(facetResults, results, total, value)
//     })
//     .catch()
//     .finally(() => {})
// }

// function processConditionalSearchResults(facetResults, results, total, searchType) {
//   if (total > 0) {
//     if (searchType === 'paginateSearch') {
//     } else {
//       if (searchType === 'collectionSearch') {
//         const typeStatusFacet = facetResults.find((facet) => facet.fieldName === 'typeStatus')
//         const typeStatus = typeStatusFacet.fieldResult
//         store.commit('setTypeStatus', typeStatus)
//       } else if (searchType === 'typeStatusSearch') {
//         console.log('here....')
//         const collectionFacet = facetResults.find((facet) => facet.fieldName === 'collectionName')
//         const collections = collectionFacet.fieldResult
//         store.commit('setCollections', collections)
//       }
//       const latLongFacet = facetResults.find((facet) => facet.fieldName === 'point-0.01')
//       const latLong = latLongFacet.fieldResult
//       store.commit('setLatLong', latLong)
//     }
//   } else {
//     // store.commit('setOccurrenceYears', [])
//     store.commit('setCollections', [])
//     store.commit('setLatLong', [])
//     store.commit('setTypeStatus', [])

//     store.commit('setSelectedCollection', null)
//     store.commit('setSelectedType', null)
//   }

//   store.commit('setResults', results)
//   store.commit('setTotalRecords', total)

//   // store.commit('setSelectedCollection', null)
//   // store.commit('setSelectedType', null)

//   store.commit('setShowDetail', false)
//   store.commit('setShowResults', true)
//   store.commit('setResetPaging', true)
// }

// function handleCoordinatesSearch(coordinates, total) {
//   console.log('handleCoordinatesSearch', coordinates, total)
//   const isAdvanceSearch = store.getters['isAdvanceSearch']
//   if (isAdvanceSearch) {
//     advanceCoordinatesSearch(coordinates, total)
//   } else {
//     simpleCoordinsSearch(coordinates, total)
//   }
// }

// function advanceCoordinatesSearch(coordinates, total) {
//   console.log('advanceCoordinatesSearch', coordinates, total)

//   const selectedCollection = store.getters['selectedCollection']
//   const selectedTypeStatus = store.getters['selectedType']

//   const speciesGroup = store.getters['speciesGrouup']
//   const dataset = store.getters['dataset']
//   const catalogNumber = store.getters['catalogNumber']
//   const scientificName = store.getters['scientificName']
//   const startDate = store.getters['startDate']
//   const endDate = store.getters['endDate']
//   const isType = store.getters['isType']

//   service
//     .advanceCoordinatesSearch(
//       scientificName,
//       speciesGroup,
//       dataset,
//       catalogNumber,
//       startDate,
//       endDate,
//       isType,
//       selectedCollection,
//       selectedTypeStatus,
//       coordinates,
//       total
//     )
//     .then((response) => {
//       const total = response.totalRecords

//       console.log('total :', total)
//       if (total > 0) {
//         const results = response.occurrences
//         store.commit('setMapRecords', results)
//       }
//       setTimeout(() => {
//         // loading.value = false
//       }, 2000)
//     })
//     .catch()
//     .finally(() => {})
// }

// function simpleCoordinsSearch(coordinates, total) {
//   console.log('simpleCoordinsSearch', coordinates, total)

//   const collection = store.getters['selectedCollection']
//   const searchText = store.getters['searchText']
//   const typeStatus = store.getters['selectedType']

//   service
//     .simpleCoordinatesSearch(searchText, collection, typeStatus, coordinates, total)
//     .then((response) => {
//       const total = response.totalRecords

//       console.log('total :', total)
//       if (total > 0) {
//         const results = response.occurrences
//         store.commit('setMapRecords', results)
//       }

//       // store.commit('setShowDetail', true)
//       // store.commit('setShowResults', true)
//       // store.commit('setResetPaging', true)
//       setTimeout(() => {
//         // loading.value = false
//       }, 2000)
//     })
//     .catch()
//     .finally(() => {})
// }

// function handleSingleMarkerSearch(coordinates) {
//   console.log('handleSingleMarkerSearch')
//   const isAdvanceSearch = store.getters['isAdvanceSearch']
//   if (isAdvanceSearch) {
//     advanceMapDataSearch(coordinates)
//   } else {
//     simpleMapDataSearch(coordinates)
//   }
// }

// function advanceMapDataSearch(coordinates) {
//   console.log('simpleMapDataSearch', coordinates)

//   const selectedCollection = store.getters['selectedCollection']
//   const selectedTypeStatus = store.getters['selectedType']

//   const speciesGroup = store.getters['speciesGrouup']
//   const dataset = store.getters['dataset']
//   const catalogNumber = store.getters['catalogNumber']
//   const scientificName = store.getters['scientificName']
//   const startDate = store.getters['startDate']
//   const endDate = store.getters['endDate']
//   const isType = store.getters['isType']

//   catalogNumber, startDate, endDate, isType, selectedCollection, selectedTypeStatus, coordinates

//   service
//     .advancesearchMapData(
//       scientificName,
//       speciesGroup,
//       dataset,
//       catalogNumber,
//       startDate,
//       endDate,
//       isType,
//       selectedCollection,
//       selectedTypeStatus,
//       coordinates
//     )
//     .then((response) => {
//       const total = response.totalRecords
//       console.log('total: ', total)
//       if (total >= 1) {
//         const results = response.occurrences

//         const id = results[0].uuid
//         console.log('id:', id)
//         getDetailById(id)
//       }

//       // store.commit('setShowDetail', true)
//       // store.commit('setShowResults', true)
//       // store.commit('setResetPaging', true)
//       setTimeout(() => {
//         // loading.value = false
//       }, 2000)
//     })
//     .catch()
//     .finally(() => {})
// }

// function simpleMapDataSearch(coordinates) {
//   console.log('simpleMapDataSearch', coordinates)
//   const collection = store.getters['selectedCollection']
//   const searchText = store.getters['searchText']
//   const typeStatus = store.getters['selectedType']

//   service
//     .simplesearchMapData(searchText, collection, typeStatus, coordinates)
//     .then((response) => {
//       const total = response.totalRecords
//       if (total >= 1) {
//         const results = response.occurrences

//         const id = results[0].uuid
//         console.log('id:', id)
//         getDetailById(id)
//       }

//       // store.commit('setShowDetail', true)
//       // store.commit('setShowResults', true)
//       // store.commit('setResetPaging', true)
//       setTimeout(() => {
//         // loading.value = false
//       }, 2000)
//     })
//     .catch()
//     .finally(() => {})
// }

// function getDetailById(id) {
//   service
//     .uuidSearch(id)
//     .then((response) => {
//       store.commit('setShowDetail', true)
//       store.commit('setSelectedResult', response)
//     })
//     .catch()
//     .finally(() => {})
// }

// async function handleExportData() {
//   console.log('exportData')

//   store.commit('setExportData', null)

//   const collection = store.getters['selectedCollection']
//   const typeStatus = store.getters['selectedType']
//   let totalRecords = store.getters['totalRecords']

//   totalRecords = totalRecords <= 5000 ? totalRecords : 5000

//   console.log('total Records', totalRecords)

//   const speciesGroup = store.getters['speciesGrouup']
//   const dataset = store.getters['dataset']
//   const catalogNumber = store.getters['catalogNumber']
//   const scientificName = store.getters['scientificName']
//   const startDate = store.getters['startDate']
//   const endDate = store.getters['endDate']
//   const isType = store.getters['isType']

//   await service
//     .export(
//       scientificName,
//       speciesGroup,
//       dataset,
//       catalogNumber,
//       startDate,
//       endDate,
//       isType,
//       collection,
//       typeStatus,
//       totalRecords
//     )
//     .then((response) => {
//       const results = response.occurrences
//       store.commit('setExportData', results)
//     })
//     .catch()
//     .finally(() => {})
// }
</script>
<style scoped>
.homePage {
  padding: 4rem 2rem;
}
</style>
