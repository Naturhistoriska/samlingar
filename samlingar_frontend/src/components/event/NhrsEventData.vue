<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1em">{{ $t('results.event') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding">{{ $t('results.eventDate') }}</div>
      <div class="col-8 reducePadding">
        {{ eventStartDate }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.fieldNumber') }}</div>
      <div class="col-8 reducePadding">
        {{ eventFieldNumber }}
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

const eventFieldNumber = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const { eventDate, eventRemarks, fieldNumber } = record

  if (eventDate) {
    eventStartDate.value = moment
      .tz(eventDate, 'ddd MMM DD HH:mm:ss z YYYY', 'CET')
      .format('YYYY-MM-DD')
  }

  remarks.value = eventRemarks
  eventFieldNumber.value = fieldNumber
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
