<template>
  <div class="card w-full p-2 max-w-full">
    <Chart type="bar" :data="chartData" :options="chartOptions" class="responsive-chart" />
  </div>
</template>

<script setup>
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
        label: t('chart.yearChartLabel'),
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
<style scoped>
/* Responsive chart sizing */
.responsive-chart {
  width: 100% !important; /* full width */
  height: 250px; /* mobile default height */
}

/* Tablet */
@media (min-width: 640px) {
  .responsive-chart {
    height: 350px;
  }
}

/* Desktop */
@media (min-width: 1024px) {
  .responsive-chart {
    height: 500px;
  }
}
</style>
