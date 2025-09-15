<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1em">{{ $t('results.dataset') }}</p>
    <div class="grid">
      <div class="col-3 reducePadding">{{ $t('results.collectionName') }}</div>
      <div class="col-9 reducePadding">
        {{ collection }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.institiutionCode') }}</div>
      <div class="col-9 reducePadding">
        {{ institution }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.catalogNumber') }}</div>
      <div class="col-9 reducePadding">
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

      <div class="col-3 reducePadding">{{ $t('results.recordType') }}</div>
      <div class="col-9 reducePadding">
        {{ recordType }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.preparation') }}</div>
      <div class="col-9 reducePadding">
        {{ preparationString }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.recordedBy') }}</div>
      <div class="col-9 reducePadding">
        {{ collectors }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.sex') }}</div>
      <div class="col-9 reducePadding">
        {{ specimenSex }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.lifeStage') }}</div>
      <div class="col-9 reducePadding">
        {{ stage }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.reproductiveCondition') }}</div>
      <div class="col-9 reducePadding">
        {{ reproductCondition }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.individualCount') }}</div>
      <div class="col-9 reducePadding">
        {{ count }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.license') }}</div>
      <div class="col-9 reducePadding">
        {{ specimenLicense }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.modified') }}</div>
      <div class="col-9 reducePadding">
        {{ modifiedDate }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.accession') }}</div>
      <div class="col-9 reducePadding">
        {{ accession }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.previousIdentifications') }}</div>
      <div class="col-9 reducePadding">
        {{ additionalDetermination }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.occurrenceAttributeRemarks') }}</div>
      <div class="col-9 reducePadding">
        {{ occurrenceAttRemarks }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.occurrenceRemarks') }}</div>
      <div class="col-9 reducePadding">
        {{ remarks }}
      </div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'

import moment from 'moment-timezone'

const store = useStore()

const accession = ref()
const additionalDetermination = ref()
const catNumber = ref()
const dateCataloged = ref()

const collection = ref()
const collectors = ref()

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
    institutionID,
    license,
    lifeStage,
    modified,
    occurrenceAttributeRemarks,
    occurrenceRemarks,
    otherCatalogNumbers,
    preparations,
    prepCount,
    previousIdentifications,
    recordedBy,
    reproductiveCondition,
    sex
  } = record

  accession.value = accessionNumber
  additionalDetermination.value = previousIdentifications

  catNumber.value = catalogNumber
  collection.value = collectionName
  collectors.value = recordedBy ? recordedBy.toString() : ''
  count.value = individualCount

  if (catalogedDate) {
    dateCataloged.value = moment
      .tz(catalogedDate, 'ddd MMM DD HH:mm:ss z YYYY', 'CET')
      .format('YYYY-MM-DD')
  }

  institution.value = institutionID + ' [ ' + institutionCode + ' ] '

  if (modified) {
    modifiedDate.value = moment
      .tz(modified, 'ddd MMM DD HH:mm:ss z YYYY', 'CET')
      .format('YYYY-MM-DD')
  }

  occurrenceAttRemarks.value = occurrenceAttributeRemarks
  otherCatNumbers.value = otherCatalogNumbers

  preparationList.value =
    collectionCode == 'NHRS' ? (prepCount ? prepCount : '') : preparations ? preparations : ''
  preparationString.value = preparationList.value.join(', ')

  remarks.value = occurrenceRemarks
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
</style>
