<template>
  <div class="card" style="min-width: 80%; min-height: 300px">
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

const chartData = computed(() => {
  const documentStyle = getComputedStyle(document.documentElement)
  const data = store.getters['monthData']

  const labels = data.map((d) => {
    const val = d.val
    const mon = val.split(' ')

    return t('chart.' + mon[0]) + ' ' + mon[1]
    // console.log('mon', t('chart.' + mon[0]) + ' ' + mon[1])
  })

  console.log('labels' + labels)

  const monthData = data.map((d) => d.count)

  return {
    labels: labels,
    datasets: [
      {
        label: t('startPage.monthChartLabel'),
        data: monthData,
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
          color: 'white'
        }
      }
    },
    scales: {
      x: {
        ticks: {
          color: 'white',
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
          color: '#fff'
        },
        grid: {
          color: '#888'
        }
      }
    }
  }
})
</script>
