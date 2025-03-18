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
            <CollectionMonthChart v-bind:collection="tab" v-bind:chart="getMonthData(tab)" />
            <CollectionYearChart v-bind:chart="getYearData(tab)" style="margin-top: 2rem" />
          </AccordionContent>
        </AccordionPanel>
      </Accordion>
    </template>
  </card>
</template>
<script setup>
import { computed, ref } from 'vue'
import Accordion from 'primevue/accordion'
import AccordionPanel from 'primevue/accordionpanel'
import AccordionHeader from 'primevue/accordionheader'
import AccordionContent from 'primevue/accordioncontent'
import CollectionMonthChart from '../components/CollectionMonthChart.vue'
import CollectionYearChart from '../components/CollectionYearChart.vue'

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
let entomologYear = ref()
let smtpObjectYear = ref()
let smtpListYear = ref()

let pbMonth = ref()
let pzMonth = ref()
let pbYear = ref()
let pzYear = ref()

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
  const data = props.dataset.split(':')
  return data
})

function getMonthData(tab) {
  const { dataGroup } = props
  console.log('dataGroup', dataGroup)
  if (dataGroup === 'startPage.palaeCollection') {
    return tab === 'pz' ? pzMonth.value : pbMonth.value
  } else if (dataGroup === 'startPage.botanicalCollection') {
    if (tab === 'algae') {
      return algaeMonth.value
    } else if (tab === 'fungi') {
      return fungiMonth.value
    } else if (tab === 'mosses') {
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
    if (tab === 'he') {
      return herpMonth.value
    } else if (tab === 'av') {
      return birdMonth.value
    } else if (tab === 'pi') {
      return fishMonth.value
    } else if (tab === 'ev') {
      return evMonth.value
    } else if (tab === 'et') {
      return etMonth.value
    } else if (tab === 'ma') {
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
    return tab === 'pz' ? pzYear.value : pbYear.value
  } else if (dataGroup === 'startPage.botanicalCollection') {
    if (tab === 'algae') {
      return algaeYear.value
    } else if (tab === 'fungi') {
      return fungiYear.value
    } else if (tab === 'mosses') {
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
    if (tab === 'he') {
      return herpYear.value
    } else if (tab === 'av') {
      return birdYear.value
    } else if (tab === 'pi') {
      return fishYear.value
    } else if (tab === 'ev') {
      return evYear.value
    } else if (tab === 'et') {
      return etYear.value
    } else if (tab === 'ma') {
      return mammalYear.value
    } else if (tab === 'NHRS') {
      return entomologYear.value
    } else if (tab === 'SMTP_INV') {
      return smtpObjectYear.value
    } else {
      return smtpListYear.value
    }
  }
}

function onTabClick(tab) {
  console.log('tab : {}', tab)
  if (active.value.includes(tab)) {
    service
      .apiCollectionsChartData(tab)
      .then((response) => {
        const facets = response.facets

        const totalCount = facets.count
        const collectionFacet = facets.collectionCode
        console.log('collectionFacet', collectionFacet)
        let collection
        if (collectionFacet !== undefined) {
          collection = facets.collectionCode.buckets
        }
        const monthChartData = buildMonthChartData(collection)
        const yearChartData = buildMYearChartData(totalCount, collection)
        setData(tab, monthChartData, yearChartData)
      })
      .catch()
      .finally(() => {})
  }
}

function buildMYearChartData(totalCount, years) {
  const sum = years.reduce((accumulator, currentValue) => accumulator + currentValue.count, 0)
  console.log('sum', sum)

  let cumulatedTotal = totalCount - sum
  console.log('cumulatedTotal', cumulatedTotal)
  years.map((year) => {
    console.log('year', year)
    const count = year.count
    cumulatedTotal += count
    year.count = cumulatedTotal
  })
  return years
}

function buildMonthChartData(collection) {
  const currentYear = moment().year()
  const lastYear = currentYear - 1
  const currentMonth = moment().month()

  const currentYearData = collection.filter((year) => year.val === currentYear)
  const lastYearData = collection.filter((year) => year.val === lastYear)

  let lastYearMonthData
  if (lastYearData[0].count > 0) {
    lastYearMonthData = lastYearData[0].catalogedMonth.buckets
  } else {
    lastYearMonthData = new Array()
  }

  let monthLabel
  let currentYearMonthData
  if (currentYearData[0].count > 0) {
    currentYearMonthData = currentYearData[0].catalogedMonth.buckets
  } else {
    currentYearMonthData = new Array()
  }

  let newMonthData = new Array()

  for (let i = 0; i < 12; i++) {
    const filterMonth = moment().month(i).format('MMMM').toUpperCase()

    if (i <= currentMonth) {
      monthLabel = moment().month(i).format('MMMM YYYY').toUpperCase()
    } else {
      monthLabel = filterMonth + ' ' + lastYear
    }

    const month =
      currentMonth >= i
        ? currentYearMonthData.filter((month) => month.val === filterMonth)
        : lastYearMonthData.filter((month) => month.val === filterMonth)

    if (!month || month.length === 0) {
      newMonthData.push({ val: monthLabel, count: 0 })
    } else {
      newMonthData.push({ val: monthLabel, count: month[0].count })
    }
  }
  newMonthData.sort((a, b) => moment(a.val, 'MMM-yy') - moment(b.val, 'MMM-yy'))
  return newMonthData
}

function setData(tab, month, year) {
  switch (tab) {
    case 'he':
      herpMonth.value = month
      herpYear.value = year
    case 'av':
      birdMonth.value = month
      birdYear.value = year
    case 'pi':
      fishMonth.value = month
      fishYear.value = year
    case 'ev':
      evMonth.value = month
      evYear.value = year
    case 'et':
      etMonth.value = month
      etYear.value = year
    case 'ma':
      mammalMonth.value = month
      mammalYear.value = year
    case 'NHRS':
      entomologyMonth.value = month
      entomologYear.value = year
    case 'SMTP_INV':
      smtpObjectMonth.value = month
      smtpObjectYear.value = year
    case 'SMTP_SPPLST':
      smtpListMonth.value = month
      smtpListYear.value = year
    case 'pz':
      pzMonth.value = month
      pzYear.value = year
      break
    case 'pb':
      pbMonth.value = month
      pbYear.value = year
      break
    case 'algae':
      algaeMonth.value = month
      algaeYear.value = year
      break
    case 'Fungi/Lichens':
      fungiMonth.value = month
      fungiYear.value = year
      break
    case 'mosses':
      mossesMonth.value = month
      mossesYear.value = year
      break
    case 'vp':
      vpMonth.value = month
      vpYear.value = year
      break
    case 'NRMLIG':
      isotopeMonth.value = month
      isotopeYear.value = year
      break
    case 'NRMMIN':
      mineralMonth.value = month
      mineralYear.value = year
      break
    case 'NRMNOD':
      noduleMonth.value = month
      noduleYear.value = year
      break
    default:
      break
  }
}
</script>
<style scoped>
.p-accordionpanel:not(.p-active).p-accordionpanel > .p-accordionheader {
  background: var(--p-accordion-header-active-background);
  color: #cbcad1;
  font-size: 1rem;
}
</style>
