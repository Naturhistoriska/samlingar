<template>
  <div class="card w-full p-2">
    <Chart type="bar" :data="chartData" :options="chartOptions" class="h-[30rem]" />
  </div>
</template>

<script setup>
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
<style scoped>
/* Make chart fully responsive */
.responsive-chart {
  width: 100% !important;
  height: 250px; /* default height for mobile */
}

/* Increase height slightly for tablets/desktops */
@media (min-width: 768px) {
  .responsive-chart {
    height: 400px;
  }
}

@media (min-width: 1024px) {
  .responsive-chart {
    height: 500px;
  }
}
</style>
