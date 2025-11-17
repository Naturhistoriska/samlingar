<template>
  <div class="wrapper">
    <p class="title">{{ $t('results.dataset') }}</p>

    <div class="grid">
      <template v-for="item in fields" :key="item.label">
        <div class="col-4 reducePadding" v-if="item.show !== false">
          {{ $t(item.label) }}
        </div>
        <div class="col-8 reducePadding" v-if="item.show !== false">
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

const formatDate = (value) => {
  if (!value) return ''
  return moment.tz(value, 'ddd MMM DD HH:mm:ss z YYYY', 'CET').format('YYYY-MM-DD')
}

const isVPCollection = computed(() => record.value.collectionCode === 'vp')

const preparationString = computed(() => record.value.preparations?.join(', ') ?? '')

const fields = computed(() => [
  { label: 'results.dataResource', value: record.value.collectionName },
  {
    label: 'results.institiutionCode',
    value: `${t('common.institution')} [ NRM ]`
  },
  { label: 'results.catalogNumber', value: record.value.catalogNumber },

  { label: 'results.catalogedDate', value: formatDate(record.value.catalogedDate) },
  { label: 'results.modified', value: formatDate(record.value.modified) },

  { label: 'results.preparation', value: preparationString.value },

  {
    label: 'results.herbarium',
    value: record.value.herbarium,
    show: isVPCollection.value
  },

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
