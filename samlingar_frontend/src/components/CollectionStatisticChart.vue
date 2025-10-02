<template>
  <card>
    <template #title>{{ $t(dataGroup) }} </template>
    <template #content>
      <Accordion multiple class="p-accordion-header-variant-a" v-model:value="active">
        <AccordionPanel v-for="tab in tabs" :key="tab" :value="tab" style="font-size: 12px">
          <AccordionHeader style="background: transparent" @click="onTabClick(tab)">
            {{ $t('collectionName.' + tab + '.name') }}
          </AccordionHeader>
          <AccordionContent style="background: transparent" :unstyled="true">
            <div class="grid">
              <div class="col-6" no-gutters>
                <CollectionMonthChart v-bind:chart="getMonthData(tab)" />
              </div>
              <div class="col-6" no-gutters>
                <CollectionYearChart v-bind:chart="getYearData(tab)" />
              </div>
            </div>
          </AccordionContent>
        </AccordionPanel>
      </Accordion>
    </template>
  </card>
</template>
<script setup>
import { computed, defineAsyncComponent, ref } from 'vue'
import Accordion from 'primevue/accordion'
import AccordionPanel from 'primevue/accordionpanel'
import AccordionHeader from 'primevue/accordionheader'
import AccordionContent from 'primevue/accordioncontent'
import CollectionMonthChart from '../components/CollectionMonthChart.vue'
import CollectionYearChart from '../components/CollectionYearChart.vue'

import { useI18n } from 'vue-i18n'

const { locale } = useI18n()

// const AsyncMonthChart = defineAsyncComponent({
//   loader: () => import('../components/CollectionMonthChart.vue')
// })

// const AsyncYearChart = defineAsyncComponent({
//   loader: () => import('../components/CollectionYearChart.vue')
// })
import Service from '../Service'
const service = new Service()

import moment from 'moment'

const active = ref()

let herpMonth = ref()
let birdMonth = ref()
let fishMonth = ref()
let evMonth = ref()
let etMonth = ref()
let mammalMonth = ref()
let entomologyMonth = ref()
let smtpObjectMonth = ref()
let smtpListMonth = ref()

let herpYear = ref()
let birdYear = ref()
let fishYear = ref()
let evYear = ref()
let etYear = ref()
let mammalYear = ref()
let entomologyYear = ref()
let smtpObjectYear = ref()
let smtpListYear = ref()

let pbMonth = ref()
let pzMonth = ref()
let pbYear = ref()
let pzYear = ref()

let palMonth = ref()
let palYear = ref()

let algaeMonth = ref()
let fungiMonth = ref()
let mossesMonth = ref()
let vpMonth = ref()
let algaeYear = ref()
let fungiYear = ref()
let mossesYear = ref()
let vpYear = ref()

let isotopeMonth = ref()
let mineralMonth = ref()
let noduleMonth = ref()
let isotopeYear = ref()
let mineralYear = ref()
let noduleYear = ref()

const props = defineProps(['dataset', 'dataGroup', 'dataResource'])

const tabs = computed(() => {
  return props.dataset.split(':')
})

function getMonthData(tab) {
  const { dataGroup, dataResource } = props

  if (dataResource === 'bot') {
    if (tab === 'algae') {
      return algaeMonth.value
    } else if (tab === 'fungi') {
      return fungiMonth.value
    } else if (tab === 'mosses') {
      return mossesMonth.value
    } else {
      return vpMonth.value
    }
  } else if (dataResource === 'geo') {
    if (tab === 'NRMLIG') {
      return isotopeMonth.value
    } else if (tab === 'NRMMIN') {
      return mineralMonth.value
    } else {
      return noduleMonth.value
    }
  } else if (dataResource === 'pal') {
    if (tab === 'pb') {
      return pbMonth.value
    } else if (tab === 'pz') {
      return pzMonth.value
    }
  } else if (dataResource === 'zoo') {
    if (tab === 'HE') {
      return herpMonth.value
    } else if (tab === 'AV') {
      return birdMonth.value
    } else if (tab === 'PI') {
      return fishMonth.value
    } else if (tab === 'ev') {
      return evMonth.value
    } else if (tab === 'et') {
      return etMonth.value
    } else if (tab === 'MA') {
      return mammalMonth.value
    } else if (tab === 'NHRS') {
      return entomologyMonth.value
    } else if (tab === 'SMTP_INV') {
      return smtpObjectMonth.value
    } else {
      return smtpListMonth.value
    }
  }
}

function getYearData(tab) {
  const { dataGroup, dataResource } = props
  if (dataResource === 'bot') {
    if (tab === 'algae') {
      return algaeYear.value
    } else if (tab === 'fungi') {
      return fungiYear.value
    } else if (tab === 'mosses') {
      return mossesYear.value
    } else {
      return vpYear.value
    }
  } else if (dataResource === 'geo') {
    if (tab === 'NRMLIG') {
      return isotopeYear.value
    } else if (tab === 'NRMMIN') {
      return mineralYear.value
    } else {
      return noduleYear.value
    }
  } else if (dataResource === 'pal') {
    if (tab === 'pb') {
      return pbYear.value
    } else if (tab === 'pz') {
      return pzYear.value
    }
  } else if (dataResource === 'zoo') {
    if (tab === 'HE') {
      return herpYear.value
    } else if (tab === 'AV') {
      return birdYear.value
    } else if (tab === 'PI') {
      return fishYear.value
    } else if (tab === 'ev') {
      return evYear.value
    } else if (tab === 'et') {
      return etYear.value
    } else if (tab === 'MA') {
      return mammalYear.value
    } else if (tab === 'NHRS') {
      return entomologyYear.value
    } else if (tab === 'SMTP_INV') {
      return smtpObjectYear.value
    } else {
      return smtpListYear.value
    }
  }
}

async function onTabClick(tab) {
  if (active.value.includes(tab)) {
    const collection = `collectionCode: ${tab}`
    getChartData(collection, tab, false)
    getChartData(collection, tab, true)
  }
}

async function getChartData(collection, tab, isYear) {
  await service
    .apiChart(collection, isYear)
    .then((response) => {
      let facet = response.facet_counts.facet_ranges.catalogedDate
      if (facet === undefined) {
        facet = response.facet_counts.facet_ranges.createdDate
      }

      const counts = facet.counts
      if (isYear) {
        const totalCount = response.total
        setYearChartData(tab, totalCount, counts)
      } else {
        setMonthChartData(tab, counts)
      }
    })
    .catch()
    .finally(() => {})
}

function setMonthChartData(tab, monthData) {
  const cumulativeData = {}
  let key
  for (const [date, value] of Object.entries(monthData)) {
    key = moment(date).format('MMM YYYY')
    cumulativeData[key] = value
  }
  setMonthData(tab, cumulativeData)
}

function setYearChartData(tab, total, years) {
  const sum = Object.values(years).reduce((total, num) => total + num, 0)
  let cumulatedTotal = total - sum

  let key
  const cumulativeData = {}

  for (const [date, value] of Object.entries(years)) {
    cumulatedTotal += value
    key = moment(date).year()
    cumulativeData[key] = cumulatedTotal
  }

  setYearData(tab, cumulativeData)
}

function setYearData(tab, year) {
  switch (tab) {
    case 'HE':
      herpYear.value = year
      break
    case 'AV':
      birdYear.value = year
      break
    case 'PI':
      fishYear.value = year
      break
    case 'ev':
      evYear.value = year
      break
    case 'et':
      etYear.value = year
      break
    case 'MA':
      mammalYear.value = year
      break
    case 'NHRS':
      entomologyYear.value = year
      break
    case 'SMTP_INV':
      smtpObjectYear.value = year
      break
    case 'SMTP_SPPLST':
      smtpListYear.value = year
      break
    case 'pz':
      pzYear.value = year
      break
    case 'pb':
      pbYear.value = year
      break
    case 'algae':
      algaeYear.value = year
      break
    case 'fungi':
      fungiYear.value = year
      break
    case 'mosses':
      mossesYear.value = year
      break
    case 'vp':
      vpYear.value = year
      break
    case 'NRMLIG':
      isotopeYear.value = year
      break
    case 'NRMMIN':
      mineralYear.value = year
      break
    case 'NRMNOD':
      noduleYear.value = year
      break
    default:
      break
  }
}

function setMonthData(tab, month) {
  switch (tab) {
    case 'HE':
      herpMonth.value = month
      break
    case 'AV':
      birdMonth.value = month
      break
    case 'PI':
      fishMonth.value = month
      break
    case 'ev':
      evMonth.value = month
      break
    case 'et':
      etMonth.value = month
      break
    case 'MA':
      mammalMonth.value = month
      break
    case 'NHRS':
      entomologyMonth.value = month
      break
    case 'SMTP_INV':
      smtpObjectMonth.value = month
      break
    case 'SMTP_SPPLST':
      smtpListMonth.value = month
      break
    case 'pz':
      pzMonth.value = month
      break
    case 'pb':
      pbMonth.value = month
      break
    case 'algae':
      algaeMonth.value = month
      break
    case 'fungi':
      fungiMonth.value = month
      break
    case 'mosses':
      mossesMonth.value = month
      break
    case 'vp':
      vpMonth.value = month
      break
    case 'NRMLIG':
      isotopeMonth.value = month
      break
    case 'NRMMIN':
      mineralMonth.value = month
      break
    case 'NRMNOD':
      noduleMonth.value = month
      break
    default:
      break
  }
}
</script>
<style scoped>
.p-accordionpanel:not(.p-active).p-accordionpanel > .p-accordionheader {
  background: var(--p-accordion-header-active-background);
  color: #838282;
  font-size: 1rem;
}
</style>
