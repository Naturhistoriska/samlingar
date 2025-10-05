<template>
  <div class="card" style="min-width: 80%">
    <Chart type="bar" :data="chartData" :options="chartOptions" class="h-[30rem]" />
  </div>
</template>

<script setup>
// import Chart from 'primevue/chart'
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'

import { useStore } from 'vuex'

const store = useStore()
const { t } = useI18n()

const chartData = computed(() => {
  const documentStyle = getComputedStyle(document.documentElement)
  const data = store.getters['yearData']

  const labels = Object.keys(data)
  const values = Object.values(data)
  return {
    labels: labels,
    datasets: [
      {
        label: t('startPage.yearChartLabel'),
        data: values,
        color: 'white',
        backgroundColor: documentStyle.getPropertyValue('--p-emerald-500'),
        borderWidth: 1
      }
    ]
  }
})
const chartOptions = computed(() => {
  const documentStyle = getComputedStyle(document.documentElement)

  return {
    plugins: {
      legend: {
        labels: {
          color: 'black'
        }
      }
    },
    scales: {
      x: {
        ticks: {
          color: 'black',
          font: {
            weight: 500
          }
        },
        grid: {
          display: false,
          drawBorder: false
        }
      },
      y: {
        ticks: {
          color: 'black'
        },
        grid: {
          color: '#888'
        }
      }
    }
  }
})
</script>
