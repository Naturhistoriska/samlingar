<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1.1em">{{ $t('results.geologicalContext') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding">{{ $t('results.lithostratigraphy') }}</div>
      <div class="col-8 reducePadding">
        {{ lithostratigraphy }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.site') }}</div>
      <div class="col-8 reducePadding">
        {{ site }}
      </div>
      <div class="col-4 reducePadding">{{ $t('results.earliestAgeOrLowestStage') }}</div>
      <div class="col-8 reducePadding">
        {{ stageMin }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.latestAgeOrHighestStage') }}</div>
      <div class="col-8 reducePadding">
        {{ stageMax }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.earliestPeriodOrLowestSystem') }}</div>
      <div class="col-8 reducePadding">
        {{ periodMin }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.latestPeriodOrHighestSystem') }}</div>
      <div class="col-8 reducePadding">
        {{ periodMax }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.earliestEpochOrLowestSeries') }}</div>
      <div class="col-8 reducePadding">
        {{ epochMin }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.latestEpochOrHighestSeries') }}</div>
      <div class="col-8 reducePadding">
        {{ epochMax }}
      </div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const lithostratigraphy = ref()

const periodMin = ref()
const periodMax = ref()
const epochMax = ref()
const epochMin = ref()
const stageMax = ref()
const stageMin = ref()

const site = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    earliestPeriodOrLowestSystem,
    latestPeriodOrHighestSystem,
    latestEpochOrHighestSeries,
    earliestEpochOrLowestSeries,
    latestAgeOrHighestStage,
    earliestAgeOrLowestStage,
    fieldNumber,
    lithostratigraphicTerms
  } = record

  lithostratigraphy.value = lithostratigraphicTerms

  periodMin.value = earliestPeriodOrLowestSystem
  periodMax.value = latestPeriodOrHighestSystem

  epochMin.value = earliestEpochOrLowestSeries
  epochMax.value = latestEpochOrHighestSeries

  stageMin.value = earliestAgeOrLowestStage
  stageMax.value = latestAgeOrHighestStage
  site.value = fieldNumber
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
