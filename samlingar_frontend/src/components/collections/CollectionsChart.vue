<template>
  <div class="chart-card card">
    <Chart type="doughnut" :data="chartData" :options="chartOptions" class="chart-wrapper" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

import Service from '../../Service'
const service = new Service()

const store = useStore()
const router = useRouter()

const chartData = ref()
const chartOptions = ref(null)
const collections = ref([
  {
    label: 'Algae Collection',
    code: 'algae'
  },
  {
    label: 'Fungi Collection',
    code: 'fungi'
  },
  {
    label: 'Mosses Collection',
    code: 'mosses'
  },
  {
    label: 'Vascular Plants Collection',
    code: 'vp'
  },

  {
    label: 'NRM Entomology Collection Objects',
    code: 'NHRS'
  },
  {
    label: 'Swedish Malaise Trap Project (SMTP) Collection Obj',
    code: 'SMTP_INV'
  },
  {
    label: 'Swedish Malaise Trap Project (SMTP) Species Lists',
    code: 'SMTP_SPPLST'
  },
  {
    label: 'Invertebrate main collection',
    code: 'ev'
  },
  {
    label: 'Invertebrate type collection',
    code: 'et'
  },
  {
    label: 'Fish collection',
    code: 'PI'
  },
  {
    label: 'Bird collection',
    code: 'AV'
  },
  {
    label: 'Mammal collection',
    code: 'MA'
  },
  {
    label: 'Amphibians and reptiles collections',
    code: 'HE'
  },
  {
    label: 'Paleozoology Collection',
    code: 'pz'
  },
  {
    label: 'Paleobotany Collection',
    code: 'pb'
  },
  {
    label: 'NRM Isotope Geology',
    code: 'NRMLIG'
  },
  {
    label: 'NRM Mineralogy',
    code: 'NRMMIN'
  },
  {
    label: 'NRM Nodules',
    code: 'NRMNOD'
  }
])

onMounted(() => {
  const data = store.getters['collections']
  console.log('collections....', data)
  if (data === null) {
    loadData()
  } else {
    chartData.value = setChartData(data)
    chartOptions.value = setChartOptions()
  }
})

async function loadData() {
  console.log('loadData...')
  await service
    .apiInitdata()
    .then((response) => {
      const facets = response.facets
      const collectionFacet = facets.collectionName.buckets
      store.commit('setCollections', collectionFacet)
      chartData.value = setChartData(collectionFacet)
      chartOptions.value = setChartOptions()
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {})
}

const setChartData = (data) => {
  const documentStyle = getComputedStyle(document.documentElement)
  const counts = data.map((item) => item.count)
  const values = data.map((item) => item.val)
  return {
    labels: values,
    datasets: [
      {
        data: counts,
        backgroundColor: [
          documentStyle.getPropertyValue('--p-primary-900'),
          documentStyle.getPropertyValue('--p-red-500'),
          documentStyle.getPropertyValue('--p-blue-500'),
          documentStyle.getPropertyValue('--p-green-500'),
          documentStyle.getPropertyValue('--p-yellow-500'),
          documentStyle.getPropertyValue('--p-cyan-500'),
          documentStyle.getPropertyValue('--p-pink-500'),
          documentStyle.getPropertyValue('--p-indigo-500'),
          documentStyle.getPropertyValue('--p-orange-500'),
          documentStyle.getPropertyValue('--p-teal-500'),
          documentStyle.getPropertyValue('--p-bluegray-500'),
          documentStyle.getPropertyValue('--p-purple-500'),
          documentStyle.getPropertyValue('--p-gray-500'),
          documentStyle.getPropertyValue('--p-rose-500'),
          documentStyle.getPropertyValue('--p-primary-500'),
          documentStyle.getPropertyValue('--p-lightblue-500'),
          documentStyle.getPropertyValue('--p-amber-500'),
          documentStyle.getPropertyValue('--p-lime-500'),
          documentStyle.getPropertyValue('--p-brown-500')
        ]
      }
    ]
  }
}

function search(label) {
  const data = collections.value.find((item) => item.label === label)
  const code = data ? data.code : null
  store.commit('setChartCode', code)
  reset()
  router.push('/search')
}

function reset() {
  console.log('CollectionChart.reset...')
  store.commit('setSearchText', null)
  store.commit('setFullTextSearchMode', 'contains')
  store.commit('setScientificName', null)
  store.commit('setSearchMode', 'contains')

  store.commit('setCollectionGroup', null)
  store.commit('setSelectedCollection', null)

  store.commit('setFilterCoordinates', false)
  store.commit('setFilterInSweden', false)
  store.commit('setFilterImage', false)
  store.commit('setFilterType', false)
  store.commit('setFilterNordicCountry', false)

  store.commit('setStartDate', null)
  store.commit('setEndDate', null)
  store.commit('setDates', null)

  store.commit('setStartYear', null)
  store.commit('setEndYear', null)
  store.commit('setDateFilter', 'date')

  store.commit('setSearchParams', null)

  store.commit('setFields', [])
  store.commit('setDataResource', null)
}

const setChartOptions = () => {
  const documentStyle = getComputedStyle(document.documentElement)
  const textColor = documentStyle.getPropertyValue('--p-text-color')
  return {
    responsive: true,
    maintainAspectRatio: false,
    onClick: (event, elements, chart) => {
      if (elements.length > 0) {
        const elementIndex = elements[0].index
        const label = chart.data.labels[elementIndex]
        search(label)
      }
    },
    plugins: {
      legend: {
        display: true,
        position: 'right',
        labels: {
          boxWidth: 10,
          // padding: 10, // Adds padding between items
          usePointStyle: false,
          font: {
            size: 11 // 👈 change this to make legend labels bigger/smaller
          }
        }
      }
    }
  }
}
</script>
<style scoped>
.chart-card {
  display: flex;
  justify-content: center;
  width: 100%;
  max-width: 500px; /* limits chart width on larger screens */
  margin: 0 auto; /* center horizontally */
}

.chart-wrapper {
  width: 100%;
  height: 300px; /* default desktop height */
}

/* Mobile adjustments */
@media (max-width: 768px) {
  .chart-wrapper {
    height: 200px; /* smaller height for mobile */
  }

  .chart-card {
    padding: 0 1rem; /* small padding on mobile */
  }
}
</style>
