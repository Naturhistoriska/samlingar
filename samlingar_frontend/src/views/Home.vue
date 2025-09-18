<template>
  <div>
    <div v-if="loading" class="spinner-overlay">
      <div class="spinner"></div>
    </div>
    <div class="grid homePage">
      <start-page
        @collectionsSearch="collectionsSearch"
        @filterSearch="filterSearch"
        @search="search"
      />
    </div>

    <div class="grid" style="margin-top: 10px">
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
  if (entryType.value === 'first-visit' || entryType.value === 'reload') {
    const collectionCode = 'collectionCode: *'
    fetchInitdata()
    fetchYearChartData(collectionCode)
    fetchMonthChartData(collectionCode)
    initData.value = true
  } else {
    const totalCount = store.getters['totalCount']
    if (totalCount === 0) {
      initData.value = true
    }
  }
  if (initData.value) {
    const collectionCode = 'collectionCode: *'
    fetchInitdata()
    fetchYearChartData(collectionCode)
    fetchMonthChartData(collectionCode)
  }
})

function fetchMonthChartData(collectionCode) {
  service
    .apiChart(collectionCode, false)
    .then((response) => {
      const counts = response.facet_counts.facet_ranges.createdDate.counts

      setMonthChartData(counts)
    })
    .catch()
    .finally(() => {})
}

function fetchYearChartData(collectionCode) {
  service
    .apiChart(collectionCode, true)
    .then((response) => {
      const counts = response.facet_counts.facet_ranges.createdDate.counts

      const totalCount = response.total
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

      store.commit('setSelectedCollection', null)
      store.commit('setSelectedCollectionGroup', null)
      store.commit('setDataResource', null)
    })
    .catch()
    .finally(() => {})
}

function setSearchCommentFacet(facets) {
  const inSweden = facets.inSweden.allBuckets.count
  const typeStatus = facets.typeStatus.allBuckets.count
  const hasImages = facets.associatedMedia.buckets[0].count
  const hasCoordinates = facets.coordinates.allBuckets.count

  store.commit('setHasCoordinatesCount', hasCoordinates)
  store.commit('setInSwedenCount', inSweden)
  store.commit('setIsTypeCount', typeStatus)
  store.commit('setImageCount', hasImages)
}

async function filterSearch(params, start, numPerPage) {
  loading.value = true
  console.log('why 1...', params.toString())
  await service
    .apiFilterSearch(params, start, numPerPage)
    .then((response) => {
      const total = response.facets.count
      const results = response.response

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      store.commit('setFilterImage', false)
      store.commit('setFilterCoordinates', false)
      store.commit('setFilterInSweden', false)
      store.commit('setFilterType', false)

      if (total > 0) {
        const collectionfacet = response.facets.collectionName.buckets
        store.commit('setSelectedCollectionGroup', collectionfacet)

        const collectionCodefacet = response.facets.collectionCode.buckets
        store.commit('setSelectedCollection', collectionCodefacet)
      } else {
        store.commit('setSelectedCollectionGroup', null)
        store.commit('setSelectedCollection', null)
      }
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      store.commit('setIsUrlPush', true)
      store.commit('setSearchParams', params)
      loading.value = false
      router.push('/search')
    })
}

async function collectionsSearch(params, start, numPerPage) {
  loading.value = true
  console.log('params', params)

  await service
    .apiFilterCollectionsSearch(params, start, numPerPage)
    .then((response) => {
      const total = response.facets.count
      const results = response.response

      store.commit('setTotalRecords', total)
      if (total > 0) {
        store.commit('setResults', results)
        const collectionfacet = response.facets.collectionName.buckets
        store.commit('setSelectedCollectionGroup', collectionfacet)

        const collectionCodefacet = response.facets.collectionCode.buckets
        store.commit('setSelectedCollection', collectionCodefacet)
      } else {
        store.commit('setSelectedCollectionGroup', null)
        store.commit('setSelectedCollection', null)
        store.commit('setResults', null)
      }
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      store.commit('setIsUrlPush', true)
      store.commit('setSearchParams', params)
      loading.value = true
      router.push('/search')
    })
}

async function search(value, start, numPerPage) {
  loading.value = true
  const params = new URLSearchParams({
    text: value
  })
  await service
    .apiFreeTextSearch(value, start, numPerPage)
    .then((response) => {
      const total = response.facets.count
      const results = response.response

      store.commit('setSearchText', value)
      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      if (total > 0) {
        const collectionfacet = response.facets.collectionName.buckets
        const collectionCodefacet = response.facets.collectionCode.buckets

        store.commit('setSelectedCollection', collectionCodefacet)
        store.commit('setSelectedCollectionGroup', collectionfacet)
      } else {
        store.commit('setSelectedCollectionGroup', null)
      }
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      store.commit('setIsUrlPush', true)
      store.commit('setSearchParams', params)
      loading.value = false
      router.push('/search')
    })
}
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
