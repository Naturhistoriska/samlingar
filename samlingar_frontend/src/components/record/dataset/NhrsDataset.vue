<template>
  <div class="wrapper">
    <p class="title">{{ $t('results.dataset') }}</p>

    <div class="grid">
      <template v-for="item in fields" :key="item.label">
        <div class="col-4 reducePadding" v-if="item.show !== false">
          {{ $t(item.label) }}
        </div>

        <div
          class="col-8 reducePadding"
          :class="{ 'multiline-text': item.multiline }"
          v-if="item.show !== false"
        >
          {{ item.value }}
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useStore } from 'vuex'
import moment from 'moment-timezone'

const store = useStore()
const record = computed(() => store.getters['selectedRecord'] ?? {})

const isNHRS = computed(() => record.value.collectionCode === 'NHRS')

const formatDate = (value) => {
  if (!value) return ''
  return moment.tz(value, 'ddd MMM DD HH:mm:ss z YYYY', 'CET').format('YYYY-MM-DD')
}

const preparation = computed(() => {
  const { collectionCode, prepCount, preparations } = record.value

  if (collectionCode === 'NHRS' && prepCount) return prepCount.join(' | ')

  if (preparations) return preparations.join(' | ')

  return ''
})

const fields = computed(() => [
  { label: 'results.collectionName', value: record.value.collectionName },
  {
    label: 'results.institiutionCode',
    value: `${record.value.institutionName} [ ${record.value.institutionCode} ]`
  },
  { label: 'results.catalogNumber', value: record.value.catalogNumber },
  { label: 'results.otherCatalogNumbers', value: record.value.otherCatalogNumbers },

  { label: 'results.catalogedDate', value: formatDate(record.value.catalogedDate) },
  { label: 'results.modified', value: formatDate(record.value.modified) },

  { label: 'results.recordType', value: record.value.basisOfRecord },
  { label: 'results.preparation', value: preparation.value },

  { label: 'results.sex', value: record.value.sex },
  { label: 'results.lifeStage', value: record.value.lifeStage },
  { label: 'results.reproductiveCondition', value: record.value.reproductiveCondition },
  { label: 'results.individualCount', value: record.value.individualCount },

  { label: 'results.license', value: record.value.license },

  // NHRS conditional field
  {
    label: 'results.accession',
    value: record.value.accessionNumber,
    show: isNHRS.value
  },

  {
    label: 'results.occurrenceAttributeRemarks',
    value: record.value.occurrenceAttributeRemarks,
    multiline: true
  },
  {
    label: 'results.occurrenceRemarks',
    value: record.value.occurrenceRemarks,
    multiline: true
  }
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

.multiline-text {
  white-space: pre-line;
}
</style>
