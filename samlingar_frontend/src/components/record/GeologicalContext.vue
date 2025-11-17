<template>
  <div class="wrapper">
    <p class="title">{{ $t('results.geologicalContext') }}</p>

    <div class="grid">
      <template v-for="item in fields" :key="item.label">
        <div class="col-4 reducePadding">{{ $t(item.label) }}</div>
        <div class="col-8 reducePadding">{{ item.value }}</div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const record = computed(() => store.getters['selectedRecord'] ?? {})

const fields = computed(() => [
  { label: 'results.lithostratigraphy', value: record.value.lithostratigraphicTerms },
  { label: 'results.site', value: record.value.fieldNumber },

  { label: 'results.earliestAgeOrLowestStage', value: record.value.earliestAgeOrLowestStage },
  { label: 'results.latestAgeOrHighestStage', value: record.value.latestAgeOrHighestStage },

  {
    label: 'results.earliestPeriodOrLowestSystem',
    value: record.value.earliestPeriodOrLowestSystem
  },
  { label: 'results.latestPeriodOrHighestSystem', value: record.value.latestPeriodOrHighestSystem },

  { label: 'results.earliestEpochOrLowestSeries', value: record.value.earliestEpochOrLowestSeries },
  { label: 'results.latestEpochOrHighestSeries', value: record.value.latestEpochOrHighestSeries }
])
</script>

<style scoped>
.wrapper {
  font-size: 12px;
}

.title {
  font-weight: bold;
  font-size: 1.1em;
}

.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
