<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1em">{{ $t('results.dataset') }}</p>
    <div class="grid">
      <div class="col-3 reducePadding">{{ $t('results.dataResource') }}</div>
      <div class="col-9 reducePadding">
        {{ dataResource }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.institiution') }}</div>
      <div class="col-9 reducePadding">
        {{ institution }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.collectionName') }}</div>
      <div class="col-9 reducePadding">
        {{ collection }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.catalogNumber') }}</div>
      <div class="col-9 reducePadding">
        {{ catNumber }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.recordedType') }}</div>
      <div class="col-9 reducePadding">
        {{ recordType }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.preparation') }}</div>
      <div class="col-9 reducePadding">
        {{ preparationList }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.identifiedBy') }}</div>
      <div class="col-9 reducePadding">
        {{ identifier }}
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

      <div class="col-3 reducePadding">{{ $t('results.dateIdentified') }}</div>
      <div class="col-9 reducePadding">
        {{ identifiedDate }}
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
import { useI18n } from 'vue-i18n'

const { t } = useI18n()

const store = useStore()

const additionalDetermination = ref()

const catNumber = ref()
const code = ref()
const collection = ref()
const collectors = ref()
const catalogedDate = ref()

const dataResource = ref()

const recordType = ref()
const preparationList = ref()
const identifier = ref()

const specimenSex = ref()
const stage = ref()
const count = ref()
const specimenLicense = ref()
const modifiedDate = ref()
const identifiedDate = ref()
const institution = ref()

const occurrenceAttRemarks = ref()
const reproductCondition = ref()
const remarks = ref()

onMounted(async () => {
  console.log('onMounted dataset')
  const record = store.getters['selectedRecord']

  const {
    accessionNumber,
    accessionRemarks,
    basisOfRecord,
    catalogNumber,
    collectionCode,
    collectionName,

    dataResourceName,

    dynamicProperties_createdDate,
    dateIdentified,

    earliestAgeOrLowestStage,

    identifiedBy,
    individualCount,
    institutionCode,
    institutionName,

    license,
    lifeStage,
    modified,
    occurrenceAttributeRemarks,
    occurrenceRemarks,
    preparations,
    prepCount,
    previousIdentifications,
    recordedBy,
    reproductiveCondition,
    sex
  } = record

  additionalDetermination.value = previousIdentifications

  catNumber.value = catalogNumber
  catalogedDate = dynamicProperties_createdDate
  collection.value = collectionName
  code.value = collectionCode

  // dataResource.value = t('common.institution') + ' -' + collectionName
  dataResource.value = dataResourceName
  institution.value = institutionName + ' [ ' + institutionCode + ' ] '

  recordType.value = basisOfRecord
  preparationList.value =
    collectionCode == 'NHRS'
      ? prepCount
        ? prepCount.toString()
        : ''
      : preparations
        ? preparations.toString()
        : ''

  identifier.value = identifiedBy
  collectors.value = recordedBy ? recordedBy.toString() : ''
  specimenSex.value = sex
  stage.value = lifeStage
  count.value = individualCount
  specimenLicense.value = license
  modifiedDate.value = modified
  identifiedDate.value = dateIdentified

  // institution.value = institutionID + ' [ ' + institutionCode + ' ] '
  // dataResource.value = institutionID + ' -' + collectionName
  occurrenceAttRemarks.value = occurrenceAttributeRemarks
  reproductCondition.value = reproductiveCondition
  remarks.value = occurrenceRemarks
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
