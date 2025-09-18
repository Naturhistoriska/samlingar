<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1em">{{ $t('results.identification') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding">{{ $t('results.dateIdentified') }}</div>
      <div class="col-8 reducePadding">
        {{ identifyDate }}
      </div>

      <div class="col-4 reducePadding">
        {{ $t('results.identifiedYear') }}
      </div>
      <div class="col-8 reducePadding">
        {{ yearIdentified }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.identifiedBy') }}</div>
      <div class="col-8 reducePadding">
        {{ identifiedByData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.identificationQualifier') }}</div>
      <div class="col-8 reducePadding">
        {{ identificationQualifierData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.typeStatus') }}</div>
      <div class="col-8 reducePadding">
        {{ typeStatusData }}
      </div>

      <div class="col-4 reducePadding">Original Name Usage</div>
      <div class="col-8 reducePadding">
        {{ originalName }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.identificationRemarks') }}</div>
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

const identifyDate = ref()
const identifiedByData = ref()
const identificationQualifierData = ref()
const yearIdentified = ref()
const originalName = ref()
const typeStatusData = ref()

const remarks = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    dateIdentified,
    identifiedBy,
    identificationRemarks,
    identificationQualifier,
    identifiedYear,
    originalNameUsage,
    typeStatus
  } = record

  identifyDate.value = dateIdentified
  identifiedByData.value = identifiedBy
  identificationQualifierData.value = identificationQualifier

  yearIdentified.value = identifiedYear
  originalName.value = originalNameUsage
  typeStatusData.value = typeStatus

  remarks.value = identificationRemarks
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
