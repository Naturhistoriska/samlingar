<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1.1em">{{ $t('results.event') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding">{{ $t('results.eventDate') }}</div>
      <div class="col-8 reducePadding">
        {{ eventStartDate }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.collectors') }}</div>
      <div class="col-8 reducePadding">
        {{ collectors }}
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

const collectors = ref()
const eventStartDate = ref()
const remarks = ref()

const eventFieldNumber = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const { eventDate, eventRemarks, fieldNumber, recordedBy } = record

  if (recordedBy) {
    collectors.value = recordedBy.join('  |  ')
  }

  if (eventDate) {
    eventStartDate.value = moment
      .tz(eventDate, 'ddd MMM DD HH:mm:ss z YYYY', 'CET')
      .format('YYYY-MM-DD')
  }
  eventFieldNumber.value = fieldNumber
  remarks.value = eventRemarks
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
