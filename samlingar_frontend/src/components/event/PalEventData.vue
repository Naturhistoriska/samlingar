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

const store = useStore()

const eventFieldNumber = ref()
const dayOfYear = ref()
const verbatimEventDateData = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const { fieldNumber, startDayOfYear, verbatimEventDate } = record

  dayOfYear.value = startDayOfYear
  eventFieldNumber.value = fieldNumber
  verbatimEventDateData.value = verbatimEventDate
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
