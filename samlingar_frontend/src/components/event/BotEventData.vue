<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1.1em">{{ $t('results.event') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding">{{ $t('results.eventDate') }}</div>
      <div class="col-8 reducePadding">
        {{ verbatimEventDateData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.collectors') }}</div>
      <div class="col-8 reducePadding">
        {{ collectors }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.recordNumber') }}</div>
      <div class="col-8 reducePadding">
        {{ fieldNumber }}
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
// import moment from 'moment-timezone'

const store = useStore()

const collectors = ref()
const fieldNumber = ref()
const habitatData = ref()
const verbatimEventDateData = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const { endday, endmonth, endyear, habitat, recordedBy, recordNumber, verbatimEventDate } = record

  const endDate = new Array(endyear, endmonth, endday)
  const eventEndDate = endDate.filter((str) => str !== undefined).join('-')

  fieldNumber.value = recordNumber
  habitatData.value = habitat
  collectors.value = recordedBy ? recordedBy.toString() : ''
  verbatimEventDateData.value = eventEndDate
    ? verbatimEventDate + ' - ' + eventEndDate
    : verbatimEventDate
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
