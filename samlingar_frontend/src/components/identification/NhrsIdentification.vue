<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1em">{{ $t('results.identification') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding">{{ $t('results.dateIdentified') }}</div>
      <div class="col-8 reducePadding">
        {{ identifyDate }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.identifiedBy') }}</div>
      <div class="col-8 reducePadding">
        {{ identifiedByData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.identificationQualifier') }}</div>
      <div class="col-8 reducePadding">
        {{ identificationQualifierData }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.previousIdentifications') }}</div>
      <div class="col-9 reducePadding">
        {{ additionalDetermination }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.typeStatus') }}</div>
      <div class="col-8 reducePadding">
        {{ typeStatusData }}
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
const typeStatusData = ref()
const additionalDetermination = ref()

const remarks = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    dateIdentified,
    identifiedBy,
    identificationQualifier,
    identificationRemarks,
    previousIdentifications,
    typeStatus
  } = record

  if (previousIdentifications) {
    additionalDetermination.value = previousIdentifications.join(', ')
  }

  identifyDate.value = dateIdentified
  identifiedByData.value = identifiedBy
  identificationQualifierData.value = identificationQualifier

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
