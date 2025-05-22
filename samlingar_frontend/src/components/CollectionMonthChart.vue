<template>
  <div class="card" style="margin-left: 5rem; max-width: 800px; max-height: 600px">
    <Chart type="bar" :data="chartData" :options="chartOptions" class="h-[30rem]" />
  </div>
</template>

<script setup>
import Chart from 'primevue/chart'
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()

const props = defineProps(['chart', 'collection'])

const chartData = computed(() => {
  const documentStyle = getComputedStyle(document.documentElement)
  const { chart } = props

  let labels
  let monthData
  if (chart) {
    labels = chart.map((d) => {
      const val = d.val
      const mon = val.split(' ')

      return t('chart.' + mon[0]) + ' ' + mon[1]
    })
    monthData = chart.map((d) => d.count)
  }
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
