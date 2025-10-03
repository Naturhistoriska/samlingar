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

      <div class="col-4 reducePadding">{{ $t('results.preparation') }}</div>
      <div class="col-8 reducePadding">
        {{ preparationString }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.sex') }}</div>
      <div class="col-8 reducePadding">
        {{ specimenSex }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.lifeStage') }}</div>
      <div class="col-8 reducePadding">
        {{ stage }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.reproductiveCondition') }}</div>
      <div class="col-8 reducePadding">
        {{ reproductCondition }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.individualCount') }}</div>
      <div class="col-8 reducePadding">
        {{ count }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.license') }}</div>
      <div class="col-8 reducePadding">
        {{ specimenLicense }}
      </div>

      <div class="col-4 reducePadding" v-if="isNHRS">{{ $t('results.accession') }}</div>
      <div class="col-8 reducePadding" v-if="isNHRS">
        {{ accession }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.occurrenceAttributeRemarks') }}</div>
      <div class="col-8 reducePadding multiline-text">
        {{ occurrenceAttRemarks }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.occurrenceRemarks') }}</div>
      <div class="col-8 reducePadding multiline-text">
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

const accession = ref()
const catNumber = ref()
const dateCataloged = ref()

const collection = ref()

const recordType = ref()
const preparationList = ref()
const preparationString = ref()

const specimenSex = ref()
const stage = ref()
const count = ref()
const specimenLicense = ref()
const modifiedDate = ref()

const institution = ref()

const occurrenceAttRemarks = ref()
const otherCatNumbers = ref()

const reproductCondition = ref()
const remarks = ref()

// const isSmty = computed(() => {
//   const record = store.getters['selectedRecord']
//   const collectionCode = record.collectionCode
//   return collectionCode === 'SMTP_INV' || collectionCode === 'SMTP_SPPLST'
// })

const isNHRS = computed(() => {
  const record = store.getters['selectedRecord']
  const collectionCode = record.collectionCode
  return collectionCode === 'NHRS'
})

onMounted(async () => {
  console.log('onMounted dataset')
  const record = store.getters['selectedRecord']

  const {
    accessionNumber,
    basisOfRecord,
    catalogNumber,
    collectionCode,
    collectionName,
    catalogedDate,
    individualCount,
    institutionCode,
    institutionName,
    license,
    lifeStage,
    modified,
    occurrenceAttributeRemarks,
    occurrenceRemarks,
    otherCatalogNumbers,
    preparations,
    prepCount,
    reproductiveCondition,
    sex
  } = record

  accession.value = accessionNumber

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

  if (occurrenceAttributeRemarks) {
    occurrenceAttRemarks.value = occurrenceAttributeRemarks
  }

  otherCatNumbers.value = otherCatalogNumbers

  if (collectionCode === 'NHRS' && prepCount) {
    preparationList.value = prepCount.join(' | ')
  } else if (preparations) {
    preparationString.value = preparations.join(' |')
  }

  if (occurrenceRemarks) {
    remarks.value = occurrenceRemarks
  }

  recordType.value = basisOfRecord
  reproductCondition.value = reproductiveCondition

  specimenSex.value = sex
  specimenLicense.value = license
  stage.value = lifeStage
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
.multiline-text {
  white-space: pre-line;
}
</style>
