<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1.1em">{{ $t('results.dataset') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding">{{ $t('results.collectionName') }}</div>
      <div class="col-8 reducePadding">
        {{ collection }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.institiutionCode') }}</div>
      <div class="col-8 reducePadding">
        {{ institution }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.catalogNumber') }}</div>
      <div class="col-8 reducePadding">
        {{ catNumber }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.otherCatalogNumbers') }}</div>
      <div class="col-8 reducePadding">
        {{ otherCatNumbers }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.catalogedDate') }}</div>
      <div class="col-8 reducePadding">
        {{ dateCataloged }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.modified') }}</div>
      <div class="col-8 reducePadding">
        {{ modifiedDate }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.recordType') }}</div>
      <div class="col-8 reducePadding">
        {{ recordType }}
      </div>

      <div class="col-4 reducePadding" v-if="isMineralCollection">{{ $t('results.serie') }}</div>
      <div class="col-8 reducePadding" v-if="isMineralCollection">
        {{ serieData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.preparation') }}</div>
      <div class="col-8 reducePadding">
        {{ preparationString }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.license') }}</div>
      <div class="col-8 reducePadding">
        {{ specimenLicense }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.occurrenceAttributeRemarks') }}</div>
      <div class="col-8 reducePadding">
        {{ occurrenceAttRemarks }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.occurrenceRemarks') }}</div>
      <div class="col-8 reducePadding">
        {{ remarks }}
      </div>
    </div>
  </div>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'
import { useStore } from 'vuex'

import moment from 'moment-timezone'

const store = useStore()

const catNumber = ref()
const count = ref()
const collection = ref()

const dateCataloged = ref()

const institution = ref()

const modifiedDate = ref()

const occurrenceAttRemarks = ref()
const otherCatNumbers = ref()

const preparationList = ref()
const preparationString = ref()

const recordType = ref()
const remarks = ref()

const specimenLicense = ref()
const serieData = ref()

const isMineralCollection = computed(() => {
  const record = store.getters['selectedRecord']

  return record.collectionCode === 'NRMMIN'
})

onMounted(async () => {
  console.log('onMounted dataset')
  const record = store.getters['selectedRecord']

  const {
    basisOfRecord,
    catalogNumber,
    collectionName,
    catalogedDate,
    individualCount,
    institutionCode,
    institutionName,
    license,
    modified,
    occurrenceAttributeRemarks,
    occurrenceRemarks,
    otherCatalogNumbers,
    prepCount,
    serie
  } = record

  catNumber.value = catalogNumber
  collection.value = collectionName
  count.value = individualCount

  if (catalogedDate) {
    dateCataloged.value = moment
      .tz(catalogedDate, 'ddd MMM DD HH:mm:ss z YYYY', 'CET')
      .format('YYYY-MM-DD')
  }

  institution.value = institutionName + ' [ ' + institutionCode + ' ] '

  if (modified) {
    modifiedDate.value = moment
      .tz(modified, 'ddd MMM DD HH:mm:ss z YYYY', 'CET')
      .format('YYYY-MM-DD')
  }

  occurrenceAttRemarks.value = occurrenceAttributeRemarks
  otherCatNumbers.value = otherCatalogNumbers ? otherCatalogNumbers.join(', ') : ''

  preparationList.value = prepCount ? prepCount : ''
  preparationString.value = preparationList.value.join(', ')

  remarks.value = occurrenceRemarks
  recordType.value = basisOfRecord

  serieData.value = serie
  specimenLicense.value = license
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
