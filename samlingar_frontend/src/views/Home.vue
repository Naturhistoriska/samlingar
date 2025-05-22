<template>
  <div>
    <div class="grid">
      <!-- <AdvanceSearch v-if="isAdvanceSearch" @advanceSearch="handleSimpleSearch" /> -->
      <start-page
        @freeTextSearch="handleFreeTextSearch"
        @filterWithCoordinates="handleFilterWithCoordinates"
        @filterWithImages="handleFilterWithImages"
        @filterWithInSweden="handleFilterWIthInSweden"
        @filterWithType="handleFilterWithType"
        @searchBotanyCollections="handleSearchBotanyCollections"
        @searchZooCollections="handleSearchZooCollections"
        @searchPaleaCollections="handleSearchPaleaCollections"
        @searchGeCololections="handleSearchGeCololectionss"
        v-bind:loading="loading"
        v-bind:filterSearchLoading="filterSearchLoading"
      />
    </div>

    <div class="grid" style="margin-top: 10px">
      <StatisticCharts />
    </div>
  </div>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'
import { onBeforeRouteLeave, onBeforeRouteUpdate, useRouter } from 'vue-router'
import { useStore } from 'vuex'
import StartPage from '../components/StartPage.vue'
// import AdvanceSearch from '../components/AdvanceSearch.vue'
import StatisticCharts from '../components/StatisticCharts.vue'

import { useI18n } from 'vue-i18n'

import moment from 'moment'

import Service from '../Service'
const service = new Service()

const store = useStore()

const router = useRouter()

const { t } = useI18n()

onBeforeRouteUpdate(async (to, from) => {
  // react to route changes...
  console.log('onBeforeRouteUpdate', to, from)
  // userData.value = await fetchUser(to.params.id)
})

onBeforeRouteLeave((to, from) => {
  console.log('onBeforeRouteLeave', to, from)
  const { name } = to
  if (name === 'Home') {
    // store.commit('setShowResults', false)
  }
})

onMounted(() => {
  fetchInitdata()
})

let loading = ref(false)
let filterSearchLoading = ref(false)
const collectionSearchLoading = ref(false)

function handleFreeTextSearch(value, start, numPerPage) {
  loading.value = true

  service
    .apiFreeTextSearch(value, start, numPerPage)
    .then((response) => {
      const results = response.response.docs
      const total = response.response.numFound

      // if (total > 0) {
      //   const facets = response.facets

      //   const geoFacet = facets.geohash.buckets
      //   console.log('geo', geoFacet, geoFacet.length)
      // }

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      setTimeout(() => {
        loading.value = false
      }, 2000)
    })
    .catch()
    .finally(() => {
      router.push('/search')
    })
}

function handleFilterWithCoordinates() {
  store.commit('setFilterCoordinates', true)
  filterSearch(true, false, false, false)
}

function handleFilterWIthInSweden() {
  store.commit('setFilterInSweden', true)
  filterSearch(false, false, false, true)
}

function handleFilterWithImages() {
  store.commit('setFilterImage', true)
  filterSearch(false, true, false, false)
}

function handleFilterWithType() {
  store.commit('setFilterType', true)
  filterSearch(false, false, true, false)
}

function filterSearch(hasCoordinates, hasImages, isType, isInSweden) {
  filterSearchLoading.value = true
  const searchText = '*'
  store.commit('setSearchText', searchText)
  service
    .apiFreeTextSearchWithFilter(searchText, hasCoordinates, hasImages, isType, isInSweden, 0, 50)
    .then((response) => {
      const results = response.response.docs
      const total = response.response.numFound

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)
      setTimeout(() => {
        filterSearchLoading.value = false
      }, 2000)
    })
    .catch()
    .finally(() => {
      router.push('/search')
    })
}

function handleSearchBotanyCollections() {
  const botanyGroup = import.meta.env.VITE_BOTANY_GROUP

  store.commit('setCollectionGroup', botanyGroup)
  searchCollectionGroup(botanyGroup)
}

function searchCollectionGroup(value) {
  collectionSearchLoading.value = true
  const searchText = '*'
  store.commit('setSearchText', '*')
  service
    .apiCollectionGroupSearch(searchText, value, 0, 50)
    .then((response) => {
      const results = response.response.docs
      const total = response.response.numFound

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      setTimeout(() => {
        collectionSearchLoading.value = false
      }, 2000)
    })
    .catch()
    .finally(() => {
      router.push('/search')
    })
}

function fetchInitdata() {
  service
    .apiInitdata()
    .then((response) => {
      const facets = response.facets

      const totalCount = facets.count
      store.commit('setTotalCount', totalCount)

      setSearchCommentFacet(facets)
      setChartData(facets)

      store.commit('setFilterImage', false)
      store.commit('setFilterCoordinates', false)
      store.commit('setFilterInSweden', false)
      store.commit('setFilterType', false)

      // const zooGroup = import.meta.env.VITE_ZOO_GROUP
      // const geoGroup = import.meta.env.VITE_GEO_GROUP
      // const paleaGroup = import.meta.env.VITE_PALEA_GROUP
      // const botanyGroup = import.meta.env.VITE_BOTANY_GROUP

      // let zooCount = 0
      // let paleaCount = 0
      // let botanyCount = 0
      // let geoCount = 0
      // const collections = facets.collectionCode.buckets

      // for (let i = 0; i < collections.length; i++) {
      //   const collection = collections[i]
      //   const collectionId = collection.val

      //   const count = collection.count
      //   if (zooGroup.includes(collectionId)) {
      //     zooCount += count
      //   } else if (paleaGroup.includes(collectionId)) {
      //     paleaCount += count
      //   } else if (geoGroup.includes(collectionId)) {
      //     geoCount += count
      //   } else if (botanyGroup.includes(collectionId)) {
      //     botanyCount += collection.count
      //   }
      // }

      // store.commit('setBotanyCollectionTotal', botanyCount)
      // store.commit('setGeoCollectionTotal', geoCount)
      // store.commit('setPaleaCollectionTotal', paleaCount)
      // store.commit('setZooCollectionTotal', zooCount)

      // store.commit('setSelectedCollection', null)
      // store.commit('setSelectedType', null)
      // store.commit('setSelectedFamily', null)
    })
    .catch()
    .finally(() => {})
}

function setChartData(facets) {
  const total = facets.count
  const years = facets.catalogedYear.buckets

  buildMonthChartData(years)
  setYearChartData(total, years)
}

function buildMonthChartData(years) {
  const currentYear = moment().year()
  const lastYear = currentYear - 1
  const currentMonth = moment().month()

  const currentYearData = years.filter((year) => year.val === currentYear)
  const lastYearData = years.filter((year) => year.val === lastYear)

  let lastYearMonthData
  if (lastYearData[0].count > 0) {
    lastYearMonthData = lastYearData[0].catalogedMonth.buckets
  } else {
    lastYearMonthData = new Array()
  }

  let monthLabel
  let currentYearMonthData
  if (currentYearData[0].count > 0) {
    currentYearMonthData = currentYearData[0].catalogedMonth.buckets
  } else {
    currentYearMonthData = new Array()
  }

  let newMonthData = new Array()

  for (let i = 0; i < 12; i++) {
    const filterMonth = moment().month(i).format('MMMM').toUpperCase()

    if (i <= currentMonth) {
      monthLabel = moment().month(i).format('MMMM YYYY').toUpperCase()
    } else {
      monthLabel = filterMonth + ' ' + lastYear
    }

    const month =
      currentMonth >= i
        ? currentYearMonthData.filter((month) => month.val === filterMonth)
        : lastYearMonthData.filter((month) => month.val === filterMonth)

    if (!month || month.length === 0) {
      newMonthData.push({ val: monthLabel, count: 0 })
    } else {
      newMonthData.push({ val: monthLabel, count: month[0].count })
    }
  }
  newMonthData.sort((a, b) => moment(a.val, 'MMM-yy') - moment(b.val, 'MMM-yy'))
  store.commit('setMonthData', newMonthData)
}

function setYearChartData(total, years) {
  // cumulatedTotal from last ten years
  const sum = years.reduce((accumulator, currentValue) => accumulator + currentValue.count, 0)

  // cumulatedTotal before last ten years
  let cumulatedTotal = total - sum
  // Setup cumulatedTotal for last ten years
  years.map((year) => {
    const count = year.count
    cumulatedTotal += count
    year.count = cumulatedTotal
  })
  store.commit('setYearData', years)
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

function handleSimpleSearch() {
  router.push('/search')
  // service
  //   .apiFreeTextSearch(searchText, true, start, numRows)
  //   .then((response) => {
  //     processSearchData(response)
  //   })
  //   .catch()
  //   .finally(() => {
  //     router.push('/search')
  //   })
}

const isAdvanceSearch = computed(() => {
  console.log('router namme', router.currentRoute.value.name)

  const currentRouteName = router.currentRoute.value.name
  const isAdvance = currentRouteName === 'AdvanceSearch'
  if (isAdvance) {
    store.commit('setIsAdvanceSearch', true)
  } else {
    store.commit('setIsAdvanceSearch', false)
  }
  return isAdvance
})

// function setCollectionsChartData(collectionId, chartData) {
//   switch (collectionId) {
//     case 'vp':
//       store.commit('setChartDataVp', chartData)
//       break
//     case 'fungi':
//       store.commit('setChartDataFungi', chartData)
//       break
//     case 'pz':
//       store.commit('setChartDataPz', chartData)
//       break
//     case 'mosses':
//       store.commit('setChartDataMosses', chartData)
//       break
//     case '163840':
//       store.commit('setChartDataEntomology', chartData)
//       break
//     case 'ev':
//       store.commit('setChartDataEv', chartData)
//       break
//     case '557057':
//       store.commit('setChartDataMineralogy', chartData)
//       break
//     case '262144':
//       store.commit('setChartDataSmtpObject', chartData)
//       break
//     case 'va':
//       store.commit('setChartDataBird', chartData)
//       break
//     case 'pb':
//       store.commit('setChartDataPb', chartData)
//       break
//     case 'fish':
//       store.commit('setChartDataFish', chartData)
//       break
//     case 'ma':
//       store.commit('setChartDataMammals', chartData)
//       break
//     case 'algae':
//       store.commit('setChartDataAlgae', chartData)
//       break
//     case '655361':
//       store.commit('setChartDataSmtpList', chartData)
//       break
//     case 'et':
//       store.commit('setChartDataEt', chartData)
//       break
//     case 'herps':
//       store.commit('setChartDataHerps', chartData)
//       break
//     case '753664':
//       store.commit('setChartDataIsotope', chartData)
//       break
//     case '786432':
//       store.commit('setChartDataNodules', chartData)
//       break
//     default:
//       break
//   }
// }

function handleSearchPaleaCollections() {
  const paleaGroup = import.meta.env.VITE_PALEA_GROUP
  store.commit('setSearchText', '*:*')
  store.commit('setCollectionGroup', paleaGroup)
  searchCollectionGroup(paleaGroup)
}

function handleSearchGeCololectionss() {
  const geoGroup = import.meta.env.VITE_GEO_GROUP
  store.commit('setSearchText', '*:*')
  store.commit('setCollectionGroup', geoGroup)
  searchCollectionGroup(geoGroup)
}

function handleSearchZooCollections() {
  const zooGroup = import.meta.env.VITE_ZOO_GROUP
  store.commit('setSearchText', '*:*')
  store.commit('setCollectionGroup', zooGroup)
  searchCollectionGroup(zooGroup)
}

function processSearchData(response, value) {
  const total = response.response.numFound
  const results = response.response.docs

  if (total > 0) {
    const facets = response.facets

    setSearchCommentFacet(facets)

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
  } else {
    store.commit('setFamily', [])
    store.commit('setGenus', [])
    store.commit('setCollections', [])
    store.commit('setTypeStatus', [])
    store.commit('setImageCount', 0)
    store.commit('setIsTypeCount', 0)
    store.commit('setHasCoordinatesCount', 0)
    store.commit('setInSwedenCount', 0)
  }

  store.commit('setTotalRecords', total)
  store.commit('setResults', results)

  store.commit('setShowDetail', false)
  store.commit('setShowResults', true)
  store.commit('setResetPaging', true)
}

function setSearchCommentFacet(facets) {
  const imageFacet = facets.hasImage.buckets
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

function handleAdvanchSearch(value) {
  console.log('handleAdvanceSearch', value)
  const searchText = store.getters['searchText']
  console.log('handleAdvanchSearch', searchText)

  const selectedCollection = store.getters['selectedCollection']
  const typeStatus = store.getters['selectedType']
  const family = store.getters['selectedFamily']

  const hasCoordinates = store.getters['filterCoordinates']
  const hasImages = store.getters['filterImage']
  const isType = store.getters['filterType']
  const isInSweden = store.getters['filterInSweden']

  const numPerPage = store.getters['numPerPage']
  const start = store.getters['startRecord']

  service
    .apiAdvanceSearchWithFilters(
      searchText,
      selectedCollection,
      typeStatus,
      family,
      hasCoordinates,
      hasImages,
      isType,
      isInSweden,
      start,
      numPerPage
    )
    .then((response) => {
      processSearchData(response, value)
    })
    .catch()
    .finally(() => {
      router.push('/records')
    })
}

// Filter link from results page
function handleFilterSearch(value) {
  const selectedCollection = store.getters['selectedCollection']

  let collectionGroup = selectedCollection ? null : store.getters['collectionGroup']

  console.log('collectionGroup', collectionGroup)

  const typeStatus = store.getters['selectedType']
  const family = store.getters['selectedFamily']

  const hasCoordinates = store.getters['filterCoordinates']
  const hasImages = store.getters['filterImage']
  const isType = store.getters['filterType']
  const isInSweden = store.getters['filterInSweden']

  const numPerPage = store.getters['numPerPage']
  const start = store.getters['startRecord']

  const searchText = store.getters['searchText']
  console.log('search text', searchText)

  // let path
  service
    .apiFilterSearch(
      searchText,
      selectedCollection,
      typeStatus,
      family,
      collectionGroup,
      hasCoordinates,
      hasImages,
      isType,
      isInSweden,
      start,
      numPerPage
    )
    .then((response) => {
      processSearchData(response, value)

      // if (value === 'filterByCollection') {
      //   path = `filter/${selectedCollection}`
      // }

      // if (value === 'filterByType') {
      //   path = `type/${typeStatus}`
      // }
    })
    .catch()
    .finally(() => {
      router.push('/records')
      // router.push(`/results/${path}`)
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

function fetchStatisticData() {
  const now = new Date()
  const currentMonth = now.getMonth()

  const monStr = moment().month(currentMonth).format('MMMM')

  const currentYear = now.getFullYear()
  console.log('currentyear', currentYear, monStr)

  service
    .apiMonthChartData()
    .then((response) => {
      const facets = response.facets
      const years = facets.catalogedYear.buckets

      console.log('years1', years)

      const total = facets.count

      const sum = years.reduce((accumulator, currentValue) => accumulator + currentValue.count, 0)
      console.log('sum', sum)

      let cumulatedTotal = total - sum
      console.log('previous', cumulatedTotal)

      years.map((year) => {
        const count = year.count
        cumulatedTotal += count
        year.count = cumulatedTotal
      })

      console.log('years....', years)

      const currentYear = moment().year()
      const currentMonth = moment().format('MMMM')

      const currentYearData = years.filter((year) => year.val === currentYear)
      console.log(
        'currentYearData Date:',
        currentYearData[0].val,
        currentYearData[0].catalogedMonth
      )
      let currentYearMonthData = currentYearData[0].catalogedMonth.buckets
      currentYearMonthData.map((m) => {
        if (moment(m.val, 'MMM') > moment(monStr, 'MMM')) {
          m.val = m.val + '-' + (currentYear - 1)
        } else {
          m.val = m.val + '-' + currentYear
        }
      })
      currentYearMonthData.sort((a, b) => moment(a.val, 'MMM-yy') - moment(b.val, 'MMM-yy'))
      const numOfCurrentMonths = currentYearMonthData.length
      if (numOfCurrentMonths < 12) {
        const lastYear = currentYear - 1
        const lastYearData = years.filter((year) => year.val === lastYear)
        console.log('lastYearData', lastYearData)

        let lastYearMonthData = lastYearData[0].catalogedMonth.buckets
        if (numOfCurrentMonths === 11) {
          const data = lastYearMonthData.filter((month) => month.val === 'DECEMBER')
          console.log('data', data[0])
          currentYearMonthData.unshift({ val: data[0].val + '-' + lastYear, count: data[0].count })
        }
      }
      store.commit('setMonthData', currentYearMonthData)
      store.commit('setYearData', years)

      console.log('currentMonthData', currentYearMonthData, currentYearMonthData.length)

      console.log('Current Date:', moment().toString())
      console.log('Current year is:', moment().year(), currentMonth)
    })
    .catch()
    .finally(() => {})
}

// function handleAdvanceSearch() {
//   // const scientificName = store.getters['scientificName']
//   // const catalogNumber = store.getters['catalogNumber']
//   // const synonym = store.getters['synonym']
//   // const selectedDataset = store.getters['selectedDataset']
//   // const dateRange = store.getters['dateRange']
//   // const types = store.getters['selectedTypes']

//   const searchText = store.getters['searchText']

//   service
//     .apiSimpleSearch(searchText, 0, 10)
//     .then((response) => {
//       processSearchData(response)
//     })
//     .catch()
//     .finally(() => {
//       router.push('/records')
//     })
// }

// function processAPIdata(response, value) {
//   const total = response.response.numFound
//   const results = response.response.docs

//   if (total > 0) {
//     const facets = response.facets

//     setCommentFacet(facets)

//     const familyFacet = facets.family
//     if (familyFacet !== undefined) {
//       const family = familyFacet.buckets
//       console.log('family length', family.length)
//       family.sort((a, b) => (a.val.toLowerCase() > b.val.toLowerCase() ? 1 : -1))
//       store.commit('setFamily', family)
//     } else {
//       store.commit('setFamily', [])
//     }

//     const genusFacet = facets.genus
//     if (genusFacet !== undefined) {
//       const genus = genusFacet.buckets
//       console.log('genus length', genus.length)
//       genus.sort((a, b) => (a.val.toLowerCase() > b.val.toLowerCase() ? 1 : -1))
//       store.commit('setGenus', genus)
//     } else {
//       store.commit('setGenus', [])
//     }

//     if (value !== 'filterByCollection') {
//       const collections = facets.collectionName.buckets
//       store.commit('setCollections', collections)
//     }

//     if (value != 'filterByType') {
//       const typeStatus = facets.typeStatus.buckets
//       typeStatus.sort((a, b) => (a.val.toLowerCase() > b.val.toLowerCase() ? 1 : -1))
//       console.log('typeStatus length', typeStatus.length)
//       store.commit('setTypeStatus', typeStatus)
//     }
//   } else {
//     store.commit('setFamily', [])
//     store.commit('setGenus', [])
//     store.commit('setCollections', [])
//     store.commit('setTypeStatus', [])
//     store.commit('setImageCount', 0)
//     store.commit('setIsTypeCount', 0)
//     store.commit('setHasCoordinatesCount', 0)
//     store.commit('setInSwedenCount', 0)
//   }

//   store.commit('setTotalRecords', total)
//   store.commit('setResults', results)

//   store.commit('setShowDetail', false)
//   store.commit('setShowResults', true)
//   store.commit('setResetPaging', true)
// }

// statistic data when page mounted

// function setCommentFacet(facets) {
// const imageFacet = facets.image.buckets
// if (imageFacet.length > 0) {
// const imageCount = imageFacet[0].count
// store.commit('setImageCount', imageCount)
// } else {
// store.commit('setImageCount', 0)
// }
//
// const isTypeFacet = facets.isType.buckets
// if (isTypeFacet.length > 0) {
// const isTypeCount = isTypeFacet[0].count
// store.commit('setIsTypeCount', isTypeCount)
// } else {
// store.commit('setIsTypeCount', 0)
// }
//
// const inSwedenFacet = facets.inSweden.buckets
// if (inSwedenFacet.length > 0) {
// const inSwedenCount = inSwedenFacet[0].count
// store.commit('setInSwedenCount', inSwedenCount)
// } else {
// store.commit('setInSwedenCount', 0)
// }
//
// const mapFacet = facets.map.buckets
// if (mapFacet.length > 0) {
// const mapCount = mapFacet[0].count
// store.commit('setHasCoordinatesCount', mapCount)
// } else {
// store.commit('setHasCoordinatesCount', 0)
// }
// }

// function handleSimpleSearch() {
//   const searchText = store.getters['searchText']

//   console.log('search text ...', searchText)
//   const start = 0
//   const numRows = 10

//   service
//     .apiSimpleSearch(searchText, start, numRows)
//     .then((response) => {
//       processAPIdata(response)
//     })
//     .catch()
//     .finally(() => {
//       router.push('/results')
//     })
// }

// function processAPIdata(response, value) {
//   const total = response.response.numFound
//   const results = response.response.docs

//   if (total > 0) {
//     const facets = response.facets

//     setCommentFacet(facets)

//     const familyFacet = facets.family
//     if (familyFacet !== undefined) {
//       const family = familyFacet.buckets
//       console.log('family length', family.length)
//       family.sort((a, b) => (a.val.toLowerCase() > b.val.toLowerCase() ? 1 : -1))
//       store.commit('setFamily', family)
//     } else {
//       store.commit('setFamily', [])
//     }

//     const genusFacet = facets.genus
//     if (genusFacet !== undefined) {
//       const genus = genusFacet.buckets
//       console.log('genus length', genus.length)
//       genus.sort((a, b) => (a.val.toLowerCase() > b.val.toLowerCase() ? 1 : -1))
//       store.commit('setGenus', genus)
//     } else {
//       store.commit('setGenus', [])
//     }

//     if (value !== 'filterByCollection') {
//       const collections = facets.collectionName.buckets
//       store.commit('setCollections', collections)
//     }

//     if (value != 'filterByType') {
//       const typeStatus = facets.typeStatus.buckets
//       typeStatus.sort((a, b) => (a.val.toLowerCase() > b.val.toLowerCase() ? 1 : -1))
//       console.log('typeStatus length', typeStatus.length)
//       store.commit('setTypeStatus', typeStatus)
//     }
//   }

//   store.commit('setTotalRecords', total)
//   store.commit('setResults', results)

//   store.commit('setShowDetail', false)
//   store.commit('setShowResults', true)
//   store.commit('setResetPaging', true)
// }

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
function preparaExportData() {
  console.log('preparaExportData')

  const collection = store.getters['selectedCollection']
  const typeStatus = store.getters['selectedType']
  const family = store.getters['selectedFamily']
  const totalRecords = store.getters['totalRecords']
  const searchText = store.getters['searchText']

  service
    .apiPreparaExport(searchText, collection, typeStatus, family, totalRecords)
    .then((response) => {
      const results = response

      store.commit('setExportData', results)
    })
    .catch()
    .finally(() => {})
}

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

// SBDI
function handleAdvanceSearchSbdi() {
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
</script>
