<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1.1em">{{ $t('results.event') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding">{{ $t('results.eventDate') }}</div>
      <div class="col-8 reducePadding">
        {{ collectingDate }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.collectors') }}</div>
      <div class="col-8 reducePadding">
        {{ collectorList }}
      </div>

      <div class="col-4 reducePadding" v-if="isEvCollection">{{ $t('results.stationNumber') }}</div>
      <div class="col-8 reducePadding" v-if="isEvCollection">
        {{ eventStationNumber }}
      </div>

      <div class="col-4 reducePadding" v-if="!isEvCollection">{{ $t('results.fieldNumber') }}</div>
      <div class="col-8 reducePadding" v-if="!isEvCollection">
        {{ fieldNumber }}
      </div>

      <div class="col-4 reducePadding" v-if="isEvCollection">{{ $t('results.habitat') }}</div>
      <div class="col-8 reducePadding">
        {{ habitatData }}
      </div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const collectingDate = ref()
const collectorList = ref()
const eventStationNumber = ref()
const fieldNumber = ref()
const habitatData = ref()
const verbatimEventDateData = ref()

const isEvCollection = ref(false)

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    collectionCode,
    collectors,
    day,
    habitat,
    month,
    recordNumber,
    stationNumber,
    verbatimEventDate,
    year
  } = record

  isEvCollection.value = collectionCode === 'ev' || collectionCode === 'et'

  if (collectors) {
    collectorList.value = collectors.join(' | ')
  }

  eventStationNumber.value = stationNumber

  habitatData.value = habitat

  fieldNumber.value = recordNumber

  collectingDate.value = formatDate(year, month, day)
  console.log('collectingDate', collectingDate)
  verbatimEventDateData.value = verbatimEventDate
})

function formatDate(year, month, day) {
  let mm
  if (month) {
    mm = String(month).padStart(2, '0')
  }
  let dd
  if (day) {
    dd = String(day).padStart(2, '0')
  }
  const date = new Array(year, mm, dd)
  return date.filter((str) => str !== undefined).join('-')

  // return `${year}-${mm}-${dd}`
}
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
