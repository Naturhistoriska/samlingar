<template>
  <div>
    <Results
      v-if="isShowResults"
      @advanceSearch="handleAdvanceSearch"
      @filterSearch="handleFilterSearch"
      @mapSearch="handleMapSearch"
      @simpleSearch="handleSimpleSearch"
    />
    <start-page v-else @advanceSearch="handleAdvanceSearch" @simpleSearch="handleSimpleSearch" />
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

function handleSimpleSearch() {
  // console.log('handleSimpleSearch')
  const searchText = store.getters['searchText']
  const start = store.getters['startRecord']
  const numRows = store.getters['numPerPage']

  service
    .quickSearch(searchText, start, numRows)
    .then((response) => {
      const total = response.totalRecords
      const results = response.occurrences
      const facetResults = response.facetResults
      processResult(facetResults, results, total)
    })
    .catch()
    .finally(() => {})
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

    const collectionFacet = facetResults.find((facet) => facet.fieldName === 'collectionName')
    const collections = collectionFacet.fieldResult
    store.commit('setCollections', collections)

    // const yearFacet = facetResults.find((facet) => facet.fieldName === 'year')
    // const occurrenceYears = yearFacet.fieldResult
    // store.commit('setOccurrenceYears', occurrenceYears)

    // const latLongFacet = facetResults.find((facet) => facet.fieldName === 'lat_long')
    // const latLong = latLongFacet.fieldResult
    // store.commit('setLatLong', latLong)

    const pointFacet = facetResults.find((facet) => facet.fieldName === 'point-1')
    const point = pointFacet.fieldResult
    store.commit('setLatLong', point)

    console.log('length : ', point.length)

    // if (start === 1) {
    //   store.commit('setResetPaging', true)
    // } else {
    //   store.commit('setResetPaging', false)
    // }
  } else {
    // store.commit('setOccurrenceYears', [])
    store.commit('setCollections', [])
    store.commit('setLatLong', [])
    store.commit('setTypeStatus', [])
  }

  store.commit('setResults', results)
  store.commit('setTotalRecords', total)

  store.commit('setSelectedCollection', null)
  store.commit('setSelectedType', null)

  store.commit('setShowDetail', false)
  store.commit('setShowResults', true)
  store.commit('setResetPaging', true)
}

function handleFilterSearch(value) {
  console.log('handleFilterSearch', value)

  const isAdvanceSearch = store.getters['isAdvanceSearch']
  console.log('isAdvanceSearch', isAdvanceSearch)
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

  service
    .conditionalSearch(searchText, start, collection, typeStatus, numPerPage)
    .then((response) => {
      const total = response.totalRecords
      const results = response.occurrences
      const facetResults = response.facetResults

      processConditionalSearchResults(facetResults, results, total, value)
    })
    .catch()
    .finally(() => {})
}

function advanceConditionalSearch(value) {
  const collection = store.getters['selectedCollection']
  const typeStatus = store.getters['selectedType']
  // const year = store.getters['year']
  // const searchText = store.getters['searchText']
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
      //   if (total > 0) {
      //     const results = response.occurrences
      //     const facetResults = response.facetResults

      //     if ('paginateSearch') {
      //     } else {
      //       if (value === 'collectionSearch') {
      //         const yearFacet = facetResults.find((facet) => facet.fieldName === 'year')
      //         const occurrenceYears = yearFacet.fieldResult
      //         store.commit('setOccurrenceYears', occurrenceYears)
      //       } else if (value === 'yearSearch') {
      //         const collectionFacet = facetResults.find(
      //           (facet) => facet.fieldName === 'collectionName'
      //         )
      //         const collections = collectionFacet.fieldResult
      //         store.commit('setCollections', collections)
      //       }
      //       const latLongFacet = facetResults.find((facet) => facet.fieldName === 'lat_long')
      //       const latLong = latLongFacet.fieldResult
      //       store.commit('setLatLong', latLong)
      //     }

      //     store.commit('setResults', results)
      //     store.commit('setTotalRecords', total)
      //   }
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
      const latLongFacet = facetResults.find((facet) => facet.fieldName === 'lat_long')
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

// function paginatorSearch() {
//   console.log('paginatorSearch....')
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

function handleMapSearch() {
  console.log('handleMapSearch')
  const isAdvanceSearch = store.getters['isAdvanceSearch']
  if (isAdvanceSearch) {
  } else {
    simpleMapDataSearch()
  }
}

function simpleMapDataSearch() {
  const collection = store.getters['selectedCollection']
  const totalRecords = store.getters['totalRecords']
  const searchText = store.getters['searchText']
  const year = store.getters['year']

  service
    .searchMapData(searchText, collection, year, totalRecords)
    .then((response) => {
      const results = response.occurrences

      store.commit('setMapRecords', results)
      setTimeout(() => {
        // loading.value = false
      }, 2000)
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
<!-- <script>
import Results from '../components/Results.vue'
import StartPage from '../components/StartPage.vue'

import { mapGetters } from 'vuex'

export default {
  name: 'Home',
  components: {
    Results,
    StartPage
  },
  data: () => ({
    // isAdvanceSearch: false
  }),

  computed: {
    ...mapGetters(['showResults']),

    isShowResults: function () {
      return this.showResults
    }
  },
  watch: {},
  methods: {
    handleAdvanceSearch() {
      console.log('handleAdvanceSearch....')
    }
  }
}
</script> -->
