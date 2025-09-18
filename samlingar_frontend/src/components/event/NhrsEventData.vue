<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1em">{{ $t('results.event') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding">{{ $t('results.eventDate') }}</div>
      <div class="col-8 reducePadding">
        {{ eventStartDate }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.eventTime') }}</div>
      <div class="col-8 reducePadding">
        {{ eventStartTime }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.startDayOfYear') }}</div>
      <div class="col-8 reducePadding">
        {{ dayOfYear }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.eventDayOfYear') }}</div>
      <div class="col-8 reducePadding">
        {{ eventEndDayOfYear }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.verbatimEventDate') }}</div>
      <div class="col-8 reducePadding">
        {{ verbatimEventDateData }}
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

const eventStartDate = ref()
const remarks = ref()

const eventEndDayOfYear = ref()

const eventStartTime = ref()

const eventFieldNumber = ref()

const dayOfYear = ref()

const habitatData = ref()
const verbatimEventDateData = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    endDayOfYear,
    eventDate,
    eventTime,
    eventRemarks,
    fieldNumber,
    habitat,
    startDayOfYear,
    verbatimEventDate
  } = record

  if (eventDate) {
    eventStartDate.value = moment
      .tz(eventDate, 'ddd MMM DD HH:mm:ss z YYYY', 'CET')
      .format('YYYY-MM-DD')
  }
  dayOfYear.value = startDayOfYear
  eventEndDayOfYear.value = endDayOfYear

  remarks.value = eventRemarks
  eventStartTime.value = eventTime
  eventFieldNumber.value = fieldNumber

  habitatData.value = habitat

  verbatimEventDateData.value = verbatimEventDate
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
