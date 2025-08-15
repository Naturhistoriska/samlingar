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

const AsyncMonthChart = defineAsyncComponent({
  loader: () => import('../components/CollectionMonthChart.vue')
})

const AsyncYearChart = defineAsyncComponent({
  loader: () => import('../components/CollectionYearChart.vue')
})
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

const props = defineProps(['dataset', 'dataGroup'])

const tabs = computed(() => {
  return props.dataset.split(':')
  // const data = props.dataset.split(':')
  // return data
})

function getMonthData(tab) {
  const { dataGroup } = props
  if (dataGroup === 'startPage.palaeCollection') {
    return palMonth.value
  } else if (dataGroup === 'startPage.botanicalCollection') {
    if (tab === 'Algae') {
      return algaeMonth.value
    } else if (tab === 'S-Fungi') {
      return fungiMonth.value
    } else if (tab === 'S-Bryophytes') {
      return mossesMonth.value
    } else {
      return vpMonth.value
    }
  } else if (dataGroup === 'startPage.geoCollection') {
    if (tab === 'NRMLIG') {
      return isotopeMonth.value
    } else if (tab === 'NRMMIN') {
      return mineralMonth.value
    } else {
      return noduleMonth.value
    }
  } else if (dataGroup === 'startPage.zooCollection') {
    if (tab === 'NRM-Herps') {
      return herpMonth.value
    } else if (tab === 'Birds') {
      return birdMonth.value
    } else if (tab === 'NRM-Fish') {
      return fishMonth.value
    } else if (tab === 'EVmain') {
      return evMonth.value
    } else if (tab === 'EVtype') {
      return etMonth.value
    } else if (tab === 'Mammals') {
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
  const { dataGroup } = props
  if (dataGroup === 'startPage.palaeCollection') {
    return palYear.value
  } else if (dataGroup === 'startPage.botanicalCollection') {
    if (tab === 'Algae') {
      return algaeYear.value
    } else if (tab === 'S-Fungi') {
      return fungiYear.value
    } else if (tab === 'S-Bryophytes') {
      return mossesYear.value
    } else {
      return vpYear.value
    }
  } else if (dataGroup === 'startPage.geoCollection') {
    if (tab === 'NRMLIG') {
      return isotopeYear.value
    } else if (tab === 'NRMMIN') {
      return mineralYear.value
    } else {
      return noduleYear.value
    }
  } else if (dataGroup === 'startPage.zooCollection') {
    if (tab === 'NRM-Herps') {
      return herpYear.value
    } else if (tab === 'Birds') {
      return birdYear.value
    } else if (tab === 'NRM-Fish') {
      return fishYear.value
    } else if (tab === 'EVmain') {
      return evYear.value
    } else if (tab === 'EVtype') {
      return etYear.value
    } else if (tab === 'Mammals') {
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
  console.log('onTabClick tab :  ', tab)

  if (active.value.includes(tab)) {
    const collection = `collectionCode: ${tab}`
    getChartData(collection, tab, false)
    getChartData(collection, tab, true)
  }
}

async function getChartData(collection, tab, isYear) {
  console.log('tab', collection, tab)
  await service
    .apiChart(collection, isYear)
    .then((response) => {
      const counts = response.facet_counts.facet_ranges.createdDate_dt.counts
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
  console.log('tab..', tab)
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
    case 'NRM-Herps':
      herpYear.value = year
      break
    case 'Birds':
      birdYear.value = year
      break
    case 'NRM-Fish':
      fishYear.value = year
      break
    case 'EVmain':
      evYear.value = year
      break
    case 'EVtype':
      etYear.value = year
      break
    case 'Mammals':
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
    case 'PAL':
      palYear.value = year
      break
    case 'pz':
      pzYear.value = year
      break
    case 'pb':
      pbYear.value = year
      break
    case 'Algae':
      algaeYear.value = year
      break
    case 'S-Fungi':
      fungiYear.value = year
      break
    case 'S-Bryophytes':
      mossesYear.value = year
      break
    case 'VascularPlants':
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
    case 'NRM-Herps':
      herpMonth.value = month
      break
    case 'Birds':
      birdMonth.value = month
      break
    case 'NRM-Fish':
      fishMonth.value = month
      break
    case 'EVmain':
      evMonth.value = month
      break
    case 'EVtype':
      etMonth.value = month
      break
    case 'Mammals':
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
    case 'PAL':
      palMonth.value = month
      break
    case 'pz':
      pzMonth.value = month
      break
    case 'pb':
      pbMonth.value = month
      break
    case 'Algae':
      algaeMonth.value = month
      break
    case 'S-Fungi':
      fungiMonth.value = month
      break
    case 'S-Bryophytes':
      mossesMonth.value = month
      break
    case 'VascularPlants':
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
