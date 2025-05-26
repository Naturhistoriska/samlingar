<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1em">{{ $t('results.event') }}</p>
    <div class="grid">
      <div class="col-3 reducePadding">{{ $t('results.occurrenceDate') }}</div>
      <div class="col-9 reducePadding">
        {{ eventStartDate }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.startDayOfYear') }}</div>
      <div class="col-9 reducePadding">
        {{ dayOfYear }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.startTime') }}</div>
      <div class="col-9 reducePadding">
        {{ eventStartTime }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.eventDayOfYear') }}</div>
      <div class="col-9 reducePadding">
        {{ eventEndDayOfYear }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.fieldNumber') }}</div>
      <div class="col-9 reducePadding">
        {{ eventFieldNumber }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.eventRemarks') }}</div>
      <div class="col-9 reducePadding">
        {{ eventRemark }}
      </div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const eventEndDayOfYear = ref()
const eventStartDate = ref()
const eventStartTime = ref()
const eventRemark = ref()
const eventFieldNumber = ref()
const dayOfYear = ref()
const occurrenceDate = ref()

onMounted(async () => {
  console.log('onMounted dataset')
  const record = store.getters['selectedRecord']

  const {
    endDayOfYear,
    eventDate,
    eventRemarks,
    eventTime,
    fieldNumber,
    startDayOfYear,
    verbatimEventDate
  } = record

  dayOfYear.value = startDayOfYear

  eventEndDayOfYear.value = endDayOfYear
  eventRemark.value = eventRemarks
  eventFieldNumber.value = fieldNumber

  eventStartDate.value = eventDate
  eventStartTime.value = eventTime

  occurrenceDate.value = verbatimEventDate
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
