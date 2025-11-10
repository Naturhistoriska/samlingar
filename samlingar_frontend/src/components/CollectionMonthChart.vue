<template>
  <div class="chart-card card">
    <Chart type="bar" :data="chartData" :options="chartOptions" class="chart-wrapper" />
  </div>
</template>

<script setup>
// import Chart from 'primevue/chart'
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'

const { t, locale } = useI18n()

const props = defineProps(['chart'])

const currentLocale = computed(() => locale.value)

const chartData = computed(() => {
  const documentStyle = getComputedStyle(document.documentElement)
  const { chart } = props

  let labels
  let values
  if (chart) {
    labels = Object.keys(chart)
    values = Object.values(chart)

    if (currentLocale.value === 'sv') {
      labels = buildSwedishMonthLabel(labels)
      console.log('nes labels...', labels)
    }
  }

  return {
    labels: labels,
    datasets: [
      {
        label: t('chart.monthChartLabel'),
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

function buildSwedishMonthLabel(keys) {
  const locale = 'sv-SV'
  const formatter = new Intl.DateTimeFormat(locale, { month: 'short', year: 'numeric' })

  const localizedMonths = keys.map((str) => {
    const date = new Date(str) // Parses e.g. 'Nov
    const newDate = formatter.format(date)
    return newDate // e.g. "nov. 2024" in French
  })
  return localizedMonths
}
</script>
<style scoped>
.chart-card {
  display: flex;
  justify-content: center;
  width: 100%;
  margin: 0 auto;
  max-width: 100%; /* let it fill mobile screens */
  padding: 0 1rem; /* padding for mobile */
}

.chart-wrapper {
  width: 100%;
  height: 300px; /* default desktop height */
}

/* 📱 Mobile adjustments */
@media (max-width: 768px) {
  .chart-wrapper {
    height: 200px; /* smaller height for mobile */
  }
}
</style>
