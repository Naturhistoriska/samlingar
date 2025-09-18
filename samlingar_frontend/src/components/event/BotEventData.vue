<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1em">{{ $t('results.event') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding">{{ $t('results.eventDate') }}</div>
      <div class="col-8 reducePadding">
        {{ verbatimEventDateData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.startDayOfYear') }}</div>
      <div class="col-8 reducePadding">
        {{ dayOfYear }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.eventDayOfYear') }}</div>
      <div class="col-8 reducePadding">
        {{ eventEndDayOfYear }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.eventEndDay') }}</div>
      <div class="col-8 reducePadding">
        {{ eventEndDay }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.eventEndMonth') }}</div>
      <div class="col-8 reducePadding">
        {{ eventEndMonth }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.eventEndYear') }}</div>
      <div class="col-8 reducePadding">
        {{ eventEndYear }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.habitat') }}</div>
      <div class="col-8 reducePadding">
        {{ habitatData }}
      </div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'
import moment from 'moment-timezone'

const store = useStore()

const eventEndDayOfYear = ref()
const eventEndDay = ref()
const eventEndMonth = ref()
const eventEndYear = ref()

const dayOfYear = ref()

const habitatData = ref()
const verbatimEventDateData = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const { habitat, endday, endmonth, endyear, endDayOfYear, startDayOfYear, verbatimEventDate } =
    record

  dayOfYear.value = startDayOfYear
  eventEndDayOfYear.value = endDayOfYear
  eventEndDay.value = endday
  eventEndMonth.value = endmonth
  eventEndYear.value = endyear

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
