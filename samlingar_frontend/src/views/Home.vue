<template>
  <div>
    <Results v-if="isShowResults" @advanceSearch="handleAdvanceSearchForMap" />
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
  const searchText = store.getters['searchText']

  service
    .quickSearch(searchText, 1, 10)
    .then((response) => {
      const total = response.totalRecords
      const results = response.occurrences

      let facetResults = response.facetResults
      let collectionFacet = facetResults.find((facet) => facet.fieldName === 'collectionName')
      const collections = collectionFacet.fieldResult

      const yearFacet = facetResults.find((facet) => facet.fieldName === 'year')
      const occurrenceYears = yearFacet.fieldResult

      const latLongFacet = facetResults.find((facet) => facet.fieldName === 'lat_long')
      const latLong = latLongFacet.facetResults

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      store.commit('setCollections', collections)
      store.commit('setOccurrenceYears', occurrenceYears)
      store.commit('setLatLong', latLong)

      store.commit('setSearchText', searchText)
      store.commit('setSelectedCollection', null)

      store.commit('setYear', null)
      store.commit('setShowDetail', false)
      store.commit('setShowResults', true)

      setTimeout(() => {}, 2000)
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
        store.commit('setResults', results)

        store.commit('setSelectedCollection', null)
        store.commit('setYear', null)
      }
      store.commit('setTotalRecords', total)
      store.commit('setShowDetail', false)
      store.commit('setShowResults', true)

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
