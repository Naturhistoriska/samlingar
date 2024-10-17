<template>
  <div>
    <Results
      v-if="isShowResults"
      @advanceSearch="handleAdvanceSearchForList"
      @filterSearch="handleFilterSearch"
      @mapSearch="handleMapSearch"
      @simpleSearch="handleSimpleSearch"
    />
    <start-page
      v-else
      @advanceSearch="handleAdvanceSearchForList"
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

function handleSimpleSearch() {
  console.log('handleSimpleSearch')
  const searchText = store.getters['searchText']
  const start = store.getters['startRecord']
  const numRows = store.getters['numPerPage']

  service
    .quickSearch(searchText, start, numRows)
    .then((response) => {
      const total = response.totalRecords

      if (total > 0) {
        const results = response.occurrences

        const facetResults = response.facetResults
        const collectionFacet = facetResults.find((facet) => facet.fieldName === 'collectionName')
        const collections = collectionFacet.fieldResult

        const yearFacet = facetResults.find((facet) => facet.fieldName === 'year')
        const occurrenceYears = yearFacet.fieldResult

        const latLongFacet = facetResults.find((facet) => facet.fieldName === 'lat_long')
        const latLong = latLongFacet.fieldResult

        store.commit('setCollections', collections)
        store.commit('setOccurrenceYears', occurrenceYears)
        store.commit('setLatLong', latLong)

        // let totalCoordinates = 0
        // latLong.forEach((lat_long) => {
        //   totalCoordinates += lat_long.count
        // })
        // console.log('totalCoordinates : {}', totalCoordinates)

        store.commit('setResults', results)
        store.commit('setTotalRecords', total)

        store.commit('setSearchText', searchText)
        store.commit('setSelectedCollection', null)

        store.commit('setYear', null)
        store.commit('setShowDetail', false)
        store.commit('setShowResults', true)

        if (start === 1) {
          store.commit('setResetPaging', true)
        } else {
          store.commit('setResetPaging', false)
        }
      }
    })
    .catch()
    .finally(() => {})
}

function handleFilterSearch(value) {
  console.log('handleFilterSearch', value)

  const collection = store.getters['selectedCollection']
  const numPerPage = store.getters['numPerPage']
  const searchText = store.getters['searchText']
  const start = store.getters['startRecord']
  const year = store.getters['year']

  service
    .conditionalSearch(searchText, start, collection, year, numPerPage)
    .then((response) => {
      const total = response.totalRecords

      if (total > 0) {
        const results = response.occurrences
        const facetResults = response.facetResults

        if ('paginateSearch') {
        } else {
          if (value === 'collectionSearch') {
            const yearFacet = facetResults.find((facet) => facet.fieldName === 'year')
            const occurrenceYears = yearFacet.fieldResult
            store.commit('setOccurrenceYears', occurrenceYears)
          } else if (value === 'yearSearch') {
            const collectionFacet = facetResults.find(
              (facet) => facet.fieldName === 'collectionName'
            )
            const collections = collectionFacet.fieldResult
            store.commit('setCollections', collections)
          }
          const latLongFacet = facetResults.find((facet) => facet.fieldName === 'lat_long')
          const latLong = latLongFacet.fieldResult
          store.commit('setLatLong', latLong)
        }

        store.commit('setResults', results)
        store.commit('setTotalRecords', total)
      }
    })
    .catch()
    .finally(() => {})
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

function handleAdvanceSearchForList() {
  console.log('handleAdvanceSearchForList')
  handleAdvanceSearch(10)
}

function handleAdvanceSearchForMap() {
  console.log('handleAdvanceSearchForMap')
  const totalRecords = store.getters['totalRecords']
  handleAdvanceSearch(totalRecords)
}

function handleAdvanceSearch(row) {
  console.log('handleAdvanceSearch....')

  const speciesGroup = store.getters['speciesGrouup']
  const dataset = store.getters['dataset']
  const catalogNumber = store.getters['catalogNumber']
  const scientificName = store.getters['scientificName']
  const startDate = store.getters['startDate']
  const endDate = store.getters['endDate']
  const isType = store.getters['isType']

  // loading = true
  service
    .advanceSearch(
      scientificName,
      speciesGroup,
      dataset,
      catalogNumber,
      startDate,
      endDate,
      isType,
      1,
      row
    )
    .then((response) => {
      const total = response.totalRecords

      if (total > 0) {
        const results = response.occurrences
        const facetResults = response.facetResults

        const yearFacet = facetResults.find((facet) => facet.fieldName === 'year')
        const occurrenceYears = yearFacet.fieldResult
        store.commit('setOccurrenceYears', occurrenceYears)

        const collectionFacet = facetResults.find((facet) => facet.fieldName === 'collectionName')
        const collections = collectionFacet.fieldResult
        store.commit('setCollections', collections)

        const latLongFacet = facetResults.find((facet) => facet.fieldName === 'lat_long')
        const latLong = latLongFacet.fieldResult
        store.commit('setLatLong', latLong)

        store.commit('setResults', results)
        store.commit('setTotalRecords', total)

        store.commit('setSelectedCollection', null)
        store.commit('setYear', null)
      }

      store.commit('setShowDetail', false)
      store.commit('setShowResults', true)
      store.commit('setResetPaging', true)

      // setTimeout(() => {
      // loading = false
      // }, 2000)
    })
    .catch()
    .finally(() => {})
}
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
