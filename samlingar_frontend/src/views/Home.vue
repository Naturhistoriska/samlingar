<template>
  <div>
    <div v-if="loading" class="spinner-overlay">
      <div class="spinner"></div>
    </div>
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
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { entryType } from '@/router'

import StartPage from '../components/StartPage.vue'
import StatisticCharts from '../components/StatisticCharts.vue'

// import { useI18n } from 'vue-i18n'

import moment from 'moment'

import Service from '../Service'
const service = new Service()

const store = useStore()
const router = useRouter()

const loading = ref(false)

// const { t } = useI18n()

onMounted(() => {
  console.log('type visit', entryType.value)
  if (entryType.value === 'first-visit' || entryType.value === 'reload') {
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
      const facets = response.facet_counts.facet_queries

      console.log('what...', facets)
      // const totalCount = facets.count
      // store.commit('setTotalCount', totalCount)

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

function setSearchCommentFacet(facetQueries) {
  for (const [key, value] of Object.entries(facetQueries)) {
    if (key === 'point-1: *') {
      store.commit('setHasCoordinatesCount', value)
    }

    if (key === 'country: Sweden') {
      store.commit('setInSwedenCount', value)
    }

    if (key === 'typeStatus: *') {
      store.commit('setIsTypeCount', value)
    }

    if (key === 'hasImage: *') {
      store.commit('setImageCount', value)
    }
  }

  // const imageFacet = facets.associatedMedia.buckets
  // const imageCount = imageFacet.length > 0 ? imageFacet[0].count : 0
  // store.commit('setImageCount', imageCount)
  //
  // const typeStatus = facets.typeStatus.allBuckets.count
  // store.commit('setIsTypeCount', typeStatus)
  //
  // const inSwedenFacet = facets.inSweden.buckets
  // const inSwedenCount = inSwedenFacet.length > 0 ? inSwedenFacet[0].count : 0
  // store.commit('setInSwedenCount', inSwedenCount)
  //
  // const mapCount = facets.coordinates.allBuckets.count
  // store.commit('setHasCoordinatesCount', mapCount)
}

async function filterSearch(
  filtCoordinates,
  filtImages,
  filtInSweden,
  filtTypeStatus,
  start,
  numPerPage
) {
  loading.value = true
  await service
    .apiFilterSearch(filtCoordinates, filtImages, filtInSweden, filtTypeStatus, start, numPerPage)
    .then((response) => {
      const total = response.facets.count
      const results = response.response

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      if (total > 0) {
        const collectionfacet = response.facets.collectionName.buckets

        store.commit('setCollectionGroup', collectionfacet)
      } else {
        store.commit('setCollectionGroup', null)
      }
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      store.commit('setIsUrlPush', true)
      loading.value = false
      router.push('/search')
    })
}

async function collectionsSearch(value, start, numPerPage) {
  loading.value = true
  await service
    .apiFilterCollectionsSearch(value, start, numPerPage)
    .then((response) => {
      const total = response.facets.count
      const results = response.response

      store.commit('setTotalRecords', total)

      if (total > 0) {
        store.commit('setResults', results)
        const collectionfacet = response.facets.collectionName.buckets
        store.commit('setCollectionGroup', collectionfacet)
      } else {
        store.commit('setCollectionGroup', null)
      }
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      store.commit('setIsUrlPush', true)
      loading.value = true
      router.push('/search')
    })
}

async function search(value, start, numPerPage) {
  loading.value = true
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

        store.commit('setCollectionGroup', collectionfacet)
      } else {
        store.commit('setCollectionGroup', null)
      }
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      store.commit('setIsUrlPush', true)
      loading.value = true
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
