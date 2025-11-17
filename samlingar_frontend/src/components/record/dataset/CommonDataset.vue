<template>
  <div class="wrapper">
    <p class="title">{{ $t('results.dataset') }}</p>

    <div class="grid">
      <template v-for="item in fields" :key="item.label">
        <div class="col-4 reducePadding">
          {{ $t(item.label) }}
        </div>
        <div class="col-8 reducePadding">
          {{ item.value }}
        </div>
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

const record = computed(() => store.getters['selectedRecord'] ?? {})

const formatDate = (value) =>
  value ? moment.tz(value, 'ddd MMM DD HH:mm:ss z YYYY', 'CET').format('YYYY-MM-DD') : ''

const preparationString = computed(() => record.value.preparations?.join(', ') ?? '')

const fields = computed(() => [
  { label: 'results.dataResource', value: record.value.collectionName },
  {
    label: 'results.institiutionCode',
    value: `${record.value.institutionName} [ ${record.value.institutionCode} ]`
  },

  { label: 'results.catalogNumber', value: record.value.catalogNumber },
  { label: 'results.otherCatalogNumbers', value: record.value.otherCatalogNumbers },

  { label: 'results.catalogedDate', value: formatDate(record.value.catalogedDate) },
  { label: 'results.modified', value: formatDate(record.value.modified) },

  { label: 'results.recordType', value: record.value.basisOfRecord },
  { label: 'results.expeditionName', value: record.value.expeditionName },

  { label: 'results.preparation', value: preparationString.value },
  { label: 'results.preservation', value: record.value.preservation },

  { label: 'results.sex', value: record.value.sex },
  { label: 'results.lifeStage', value: record.value.lifeStage },
  { label: 'results.reproductiveCondition', value: record.value.reproductiveCondition },

  { label: 'results.individualCount', value: record.value.individualCount },
  { label: 'results.license', value: record.value.license },

  { label: 'results.occurrenceAttributeRemarks', value: record.value.occurrenceAttributeRemarks },
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
