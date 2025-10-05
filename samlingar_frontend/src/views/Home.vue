<template>
  <div>
    <div v-if="loading" class="spinner-overlay">
      <div class="spinner"></div>
    </div>
    <div class="grid homePage">
      <start-page
        @collectionsSearch="filterSearch"
        @filterSearch="filterSearch"
        @freeTextSearch="freeTextSearch"
      />
    </div>

    <div class="grid" style="margin-top: 10px" v-if="false">
      <StatisticCharts />
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { entryType } from '@/router'

import StartPage from '../components/StartPage.vue'
import StatisticCharts from '../components/StatisticCharts.vue'

import moment from 'moment'

// import { useI18n } from 'vue-i18n'
// const { t } = useI18n()

import Service from '../Service'
const service = new Service()

const store = useStore()
const router = useRouter()

const loading = ref(false)
const initData = ref(false)

onMounted(() => {
  console.log('type visit', entryType.value)

  // const collectionCode = 'collectionCode: *'
  if (entryType.value === 'first-visit' || entryType.value === 'reload') {
    // fetchInitdata()
    //  fetchYearChartData(collectionCode)
    //  fetchMonthChartData(collectionCode)
    initData.value = true
  } else {
    const totalCount = store.getters['totalCount']
    if (totalCount === 0) {
      initData.value = true
    }
  }
  if (initData.value) {
    fetchInitdata()

    // fetchYearChartData(collectionCode)
    // fetchMonthChartData(collectionCode)
  }
})

function fetchInitdata() {
  service
    .apiInitdata()
    .then((response) => {
      const facets = response.facets
      const totalCount = facets.count
      store.commit('setTotalCount', totalCount)

      const totalInSweden = facets.inSweden.count
      const totalTypeStatus = facets.typeStatus.count
      const totalSpeciemensHasImages = facets.associatedMedia.count
      const totalSpeciemensHasCoordinates = facets.coordinates.count

      const collectionFacet = facets.collectionName.buckets
      store.commit('setCollections', collectionFacet)

      store.commit('setHasCoordinatesCount', totalSpeciemensHasCoordinates)
      store.commit('setInSwedenCount', totalInSweden)
      store.commit('setIsTypeCount', totalTypeStatus)
      store.commit('setImageCount', totalSpeciemensHasImages)

      store.commit('setFilterImage', false)
      store.commit('setFilterCoordinates', false)
      store.commit('setFilterInSweden', false)
      store.commit('setFilterType', false)

      store.commit('setSelectedCollection', null)
      store.commit('setSelectedCollectionGroup', null)
      store.commit('setDataResource', null)
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      initData.value = false
    })
}

async function filterSearch(params) {
  loading.value = true
  await service
    .apiFilterSearch(params)
    .then((response) => {
      const total = response.facets.count
      const results = response.response

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      if (total > 0) {
        const facet = response.facets
        const totalGeoData = facet.coordinates.count
        const collectionfacet = facet.collectionName.buckets
        store.commit('setSelectedCollectionGroup', collectionfacet)
        store.commit('setTotalGeoData', totalGeoData)
      } else {
        store.commit('setSelectedCollectionGroup', null)
        store.commit('setTotalGeoData', 0)
      }

      store.commit('setChartCode', null)

      // if (total > 0) {
      //   const collectionfacet = response.facets.collectionName.buckets
      //   store.commit('setSelectedCollectionGroup', collectionfacet)

      //   // const collectionCodefacet = response.facets.collectionCode.buckets
      //   // store.commit('setSelectedCollection', collectionCodefacet)
      // } else {
      //   store.commit('setSelectedCollectionGroup', null)
      //   store.commit('setSelectedCollection', null)
      // }
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      store.commit('setResetMapData', true)
      store.commit('setSearchParams', params)
      loading.value = false
      router.push('/search')
    })
}

async function freeTextSearch(value, mode) {
  loading.value = true
  const params = new URLSearchParams({
    catchall: value,
    mode: mode
  })
  await service
    .apiFreeTextSearch(params)
    .then((response) => {
      const total = response.facets.count
      const results = response.response

      store.commit('setSearchText', value)
      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      store.commit('setFilterImage', false)
      store.commit('setFilterCoordinates', false)
      store.commit('setFilterInSweden', false)
      store.commit('setFilterType', false)

      store.commit('setChartCode', null)

      if (total > 0) {
        const facets = response.facets
        const totalGeoData = facets.coordinates.count
        const collectionfacet = facets.collectionName.buckets
        store.commit('setSelectedCollectionGroup', collectionfacet)
        store.commit('setTotalGeoData', totalGeoData)
      } else {
        store.commit('setSelectedCollectionGroup', null)
        store.commit('setSelectedCollection', 0)
      }
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      store.commit('setResetMapData', true)
      store.commit('setSearchParams', params)
      loading.value = false
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

function fetchMonthChartData(collectionCode) {
  service
    .apiChart(collectionCode, false)
    .then((response) => {
      // const counts = response.facet_counts.facet_ranges.createdDate.counts

      let facet = response.facet_counts.facet_ranges.catalogedDate
      // if (facet === undefined) {
      //   facet = response.facet_counts.facet_ranges.createdDate
      // }
      // const counts = facet.counts
      setMonthChartData(facet.counts)
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {})
}

function fetchYearChartData(collectionCode) {
  service
    .apiChart(collectionCode, true)
    .then((response) => {
      // const counts = response.facet_counts.facet_ranges.createdDate.counts
      let facet = response.facet_counts.facet_ranges.catalogedDate
      if (facet === undefined) {
        facet = response.facet_counts.facet_ranges.createdDate
      }
      const counts = facet.counts
      const totalCount = response.total
      setYearChartData(totalCount, counts)
    })
    .catch((error) => {
      console.log('error', error)
    })
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

// async function collectionsSearch(params) {
//   loading.value = true
//   console.log('params', params)

//   await service
//     .apiFilterSearch(params)
//     .then((response) => {
//       const total = response.facets.count
//       const results = response.response

//       store.commit('setTotalRecords', total)
//       if (total > 0) {
//         store.commit('setResults', results)
//         const collectionfacet = response.facets.collectionName.buckets
//         store.commit('setSelectedCollectionGroup', collectionfacet)

//         const collectionCodefacet = response.facets.collectionCode.buckets
//         store.commit('setSelectedCollection', collectionCodefacet)
//       } else {
//         store.commit('setSelectedCollectionGroup', null)
//         store.commit('setSelectedCollection', null)
//         store.commit('setResults', null)
//       }
//     })
//     .catch((error) => {
//       console.log('error', error)
//     })
//     .finally(() => {
//       store.commit('setIsUrlPush', true)
//       store.commit('setSearchParams', params)
//       loading.value = true
//       router.push('/search')
//     })
// }
</script>
<style scoped>
.homePage {
  padding: 4rem 2rem;
}

.spinner-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.spinner {
  border: 6px solid #f3f3f3;
  border-top: 6px solid #3498db;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>
