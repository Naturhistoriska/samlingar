<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1.1em">{{ $t('results.locality') }}</p>
    <div class="grid">
      <template v-for="(field, index) in displayFields" :key="index">
        <div class="col-4 reducePadding" v-if="field.show !== false">
          {{ $t(field.label) }}
        </div>
        <div class="col-8 reducePadding" v-if="field.show !== false">
          <span v-if="field.multiline">{{ field.value }}</span>
          <span v-else>{{ field.value }}</span>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, computed } from 'vue'
import { useStore } from 'vuex'

const store = useStore()
const recordData = ref({})

// Check for mineral collection
const isMineralCollection = computed(() => {
  const code = recordData.value.collectionCode
  return code === 'NRMLIG' || code === 'NRMMIN' || code === 'NRMNOD'
})

// Format decimal coordinates
const formatCoord = (value) => (value != null ? value.toFixed(5) : '')

const displayFields = ref([])

onMounted(() => {
  const record = store.getters['selectedRecord']
  if (!record) return

  recordData.value = record

  displayFields.value = [
    { label: 'results.locality', value: record.locality },
    { label: 'results.continet', value: record.continent },
    { label: 'results.country', value: record.country },
    { label: 'results.stateProvince', value: record.stateProvince },
    { label: 'results.county', value: record.county },
    { label: 'results.higherGeography', value: record.higherGeography },
    { label: 'results.latitude', value: formatCoord(record.decimalLatitude) },
    { label: 'results.longitude', value: formatCoord(record.decimalLongitude) },
    { label: 'results.coordinateUncertaintyInMeters', value: record.coordinateUncertaintyInMeters },
    { label: 'results.geodeticDatum', value: record.geodeticDatum },
    { label: 'results.minElevationInMeters', value: record.minimumElevationInMeters },
    { label: 'results.maxElevationInMeters', value: record.maximumElevationInMeters },
    { label: 'results.waterBody', value: record.waterBody, show: !isMineralCollection.value },
    { label: 'results.island', value: record.island, show: !isMineralCollection.value },
    { label: 'results.islandGroup', value: record.islandGroup, show: !isMineralCollection.value },
    { label: 'results.locationRemarks', value: record.locationRemarks, multiline: true }
  ]
})
</script>

<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
.multiline-text {
  white-space: pre-line;
}
</style>
