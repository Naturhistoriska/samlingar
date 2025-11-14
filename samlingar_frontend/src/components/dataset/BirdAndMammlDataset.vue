<template>
  <div class="wrapper">
    <p class="title">{{ $t('results.dataset') }}</p>

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
import { useI18n } from 'vue-i18n'
import moment from 'moment-timezone'

const { t } = useI18n()
const store = useStore()

const record = computed(() => store.getters['selectedRecord'] || {})

const formattedDate = computed(() => {
  if (!record.value.catalogedDate) return ''
  return moment
    .tz(record.value.catalogedDate, 'ddd MMM DD HH:mm:ss z YYYY', 'CET')
    .format('YYYY-MM-DD')
})

const preparationString = computed(() => record.value.preparations?.join(', ') || '')

const fields = computed(() => [
  { label: 'results.dataResource', value: record.value.collectionName },
  {
    label: 'results.institiutionCode',
    value: `${t('common.institution')} [ ${record.value.institutionCode} ]`
  },
  { label: 'results.catalogNumber', value: record.value.catalogNumber },
  { label: 'results.catalogedDate', value: formattedDate.value },
  { label: 'results.recordType', value: record.value.basisOfRecord },
  { label: 'results.expeditionName', value: record.value.expeditionName },
  { label: 'results.preparation', value: preparationString.value },
  { label: 'results.preservation', value: record.value.preservation },
  { label: 'results.lifeStage', value: record.value.lifeStage },
  { label: 'results.sex', value: record.value.sex },
  { label: 'results.occurrenceRemarks', value: record.value.occurrenceRemarks }
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
