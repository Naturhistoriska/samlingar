<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1.1em">{{ $t('results.event') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding">{{ $t('results.eventDate') }}</div>
      <div class="col-8 reducePadding">
        {{ eventStartDate }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.verbatimEventDate') }}</div>
      <div class="col-8 reducePadding">
        {{ verbatimEventDateData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.collectors') }}</div>
      <div class="col-8 reducePadding">
        {{ collectorList }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.fieldNumber') }}</div>
      <div class="col-8 reducePadding">
        {{ eventFieldNumber }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.habitat') }}</div>
      <div class="col-8 reducePadding">
        {{ habitatData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.eventRemarks') }}</div>
      <div class="col-8 reducePadding">
        {{ remarks }}
      </div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'
import moment from 'moment-timezone'

const store = useStore()

const collectorList = ref()
const eventStartDate = ref()
const eventFieldNumber = ref()
const eventEndDay = ref()
const eventEndMonth = ref()
const eventEndYear = ref()

const habitatData = ref()
const remarks = ref()
const verbatimEventDateData = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    collectors,
    endday,
    endmonth,
    endyear,
    eventDate,
    eventRemarks,
    fieldNumber,
    habitat,
    recordedBy,
    verbatimEventDate
  } = record

  if (eventDate) {
    eventStartDate.value = moment
      .tz(eventDate, 'ddd MMM DD HH:mm:ss z YYYY', 'CET')
      .format('YYYY-MM-DD')
  }

  eventEndDay.value = endday
  eventEndMonth.value = endmonth
  eventEndYear.value = endyear

  remarks.value = eventRemarks
  eventFieldNumber.value = fieldNumber

  habitatData.value = habitat

  let collector
  if (collectors) {
    collector = collectors
  } else if (recordedBy) {
    collector = recordedBy
  }
  if (collector) {
    collectorList.value = collector.join(' | ')
  }

  verbatimEventDateData.value = verbatimEventDate
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
