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
import { entryType } from '@/router'

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
      // store.commit('setTotalCount', totalCount)

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

  const mapCount = facets.coordinates.allBuckets.count
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
        const collectionfacet = response.facets.collectionName.buckets

        store.commit('setCollections', collectionfacet)
      } else {
        store.commit('setCollections', null)
      }
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      store.commit('setIsUrlPush', true)
      router.push('/search')
    })
}

async function collectionsSearch(value, start, numPerPage) {
  console.log('collectionsSearch', value)
  await service
    .apiFilterCollectionsSearch(value, start, numPerPage)
    .then((response) => {
      const total = response.facets.count
      const results = response.response

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)
      store.commit('setSelectedCollection', value)

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
        const collectionfacet = response.facets.collectionName.buckets

        store.commit('setCollections', collectionfacet)
      } else {
        store.commit('setCollections', null)
      }
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      store.commit('setIsUrlPush', true)
      router.push('/search')
    })
}
</script>
<style scoped>
.homePage {
  padding: 4rem 2rem;
}
</style>
