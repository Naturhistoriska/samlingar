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

      <div class="col-4 reducePadding" v-if="isEvCollection">{{ $t('results.stationNumber') }}</div>
      <div class="col-8 reducePadding" v-if="isEvCollection">
        {{ eventStationNumber }}
      </div>

      <div class="col-4 reducePadding" v-if="!isEvCollection">{{ $t('results.fieldNumber') }}</div>
      <div class="col-8 reducePadding" v-if="!isEvCollection">
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

const store = useStore()

const collectors = ref()
const eventStationNumber = ref()
const fieldNumber = ref()
const habitatData = ref()
const verbatimEventDateData = ref()

const isEvCollection = ref(false)

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const { collectionCode, habitat, recordedBy, recordNumber, stationNumber, verbatimEventDate } =
    record

  isEvCollection.value = collectionCode === 'ev' || collectionCode === 'et'

  if (recordedBy) {
    collectors.value = recordedBy.join(' | ')
  }

  eventStationNumber.value = stationNumber

  habitatData.value = habitat

  fieldNumber.value = recordNumber

  verbatimEventDateData.value = verbatimEventDate
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
