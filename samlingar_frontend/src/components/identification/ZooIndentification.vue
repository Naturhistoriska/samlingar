<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1em">{{ $t('results.identification') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding" v-if="hasDateIdentified">
        {{ $t('results.dateIdentified') }}
      </div>
      <div class="col-8 reducePadding" v-if="hasDateIdentified">
        {{ identifyDate }}
      </div>

      <div class="col-4 reducePadding" v-if="hasIdentifiedYear">
        {{ $t('results.identifiedYear') }}
      </div>
      <div class="col-8 reducePadding" v-if="hasIdentifiedYear">
        {{ yearIdentified }}
      </div>

      <div class="col-4 reducePadding" v-if="hasIdentifiedBy">{{ $t('results.identifiedBy') }}</div>
      <div class="col-8 reducePadding" v-if="hasIdentifiedBy">
        {{ identifiedByData }}
      </div>

      <div class="col-4 reducePadding" v-if="hasIdentificationQualifier">
        {{ $t('results.identificationQualifier') }}
      </div>
      <div class="col-8 reducePadding" v-if="hasIdentificationQualifier">
        {{ identificationQualifierData }}
      </div>

      <div class="col-4 reducePadding" v-if="hasTypeStatus">{{ $t('results.typeStatus') }}</div>
      <div class="col-8 reducePadding" v-if="hasTypeStatus">
        {{ typeStatusData }}
      </div>

      <div class="col-4 reducePadding" v-if="hasTypeStatus">Original Name Usage</div>
      <div class="col-8 reducePadding" v-if="hasTypeStatus">
        {{ originalName }}
      </div>
    </div>
  </div>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const identifyDate = ref()
const identifiedByData = ref()
const identificationQualifierData = ref()
const yearIdentified = ref()
const originalName = ref()
const typeStatusData = ref()

const hasIdentifiedYear = computed(() => {
  const record = store.getters['selectedRecord']
  const collectionCode = record.collectionCode
  return collectionCode === 'AV' || collectionCode === 'ev'
})

const hasDateIdentified = computed(() => {
  const record = store.getters['selectedRecord']
  const collectionCode = record.collectionCode
  return collectionCode === 'MA' || collectionCode === 'HE' || collectionCode === 'PI'
})

const hasIdentifiedBy = computed(() => {
  const record = store.getters['selectedRecord']
  return record.collectionCode !== 'et'
})

const hasIdentificationQualifier = computed(() => {
  const record = store.getters['selectedRecord']
  const collectionCode = record.collectionCode
  return collectionCode === 'et' || collectionCode === 'ev'
})

const hasTypeStatus = computed(() => {
  const record = store.getters['selectedRecord']
  return record.collectionCode !== 'ev'
})

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    dateIdentified,
    identifiedBy,
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
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
