<template>
  <div class="card" style="margin-left: 2rem; max-width: 800px; max-height: 800px">
    <Chart type="bar" :data="chartData" :options="chartOptions" class="h-[30rem]" />
  </div>
</template>

<script setup>
import Chart from 'primevue/chart'
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'

import { useStore } from 'vuex'

const store = useStore()
const { t } = useI18n()

const props = defineProps(['chart'])

const chartData = computed(() => {
  const documentStyle = getComputedStyle(document.documentElement)
  const { chart } = props

  let labels
  let values
  if (chart) {
    labels = Object.keys(chart)
    values = Object.values(chart)
  }

  return {
    labels: labels,
    datasets: [
      {
        label: t('startPage.yearChartLabel'),
        data: values,
        color: 'white',
        backgroundColor: '#1d634a',
        // backgroundColor: documentStyle.getPropertyValue('--p-emerald-500'),
        borderWidth: 1
      }
    ]
  }
})
const chartOptions = computed(() => {
  // const documentStyle = getComputedStyle(document.documentElement)
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
