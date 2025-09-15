<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1em">{{ $t('results.dataset') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding">{{ $t('results.dataResource') }}</div>
      <div class="col-8 reducePadding">
        {{ collection }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.institiutionCode') }}</div>
      <div class="col-8 reducePadding">
        {{ institution }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.collectionCode') }}</div>
      <div class="col-8 reducePadding">
        {{ code }}
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

      <div class="col-4 reducePadding">{{ $t('results.recordType') }}</div>
      <div class="col-8 reducePadding">
        {{ recordType }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.expeditionName') }}</div>
      <div class="col-8 reducePadding">
        {{ expeditionNameData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.preparation') }}</div>
      <div class="col-8 reducePadding">
        {{ preparationString }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.preservation') }}</div>
      <div class="col-8 reducePadding">
        {{ preservations }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.identifiedBy') }}</div>
      <div class="col-8 reducePadding">
        {{ identifier }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.dateIdentified') }}</div>
      <div class="col-8 reducePadding">
        {{ identifiedDate }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.recordedBy') }}</div>
      <div class="col-8 reducePadding">
        {{ collectors }}
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

      <div class="col-4 reducePadding">{{ $t('results.modified') }}</div>
      <div class="col-8 reducePadding">
        {{ modifiedDate }}
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
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'
import { useI18n } from 'vue-i18n'
import moment from 'moment-timezone'

const { t } = useI18n()

const store = useStore()

const additionalDetermination = ref()
const dateCataloged = ref()
const dateCreated = ref()
const catNumber = ref()
const code = ref()
const collection = ref()
const collectors = ref()
const count = ref()
const expeditionNameData = ref()
const identifiedDate = ref()
const identifier = ref()
const institution = ref()
const modifiedDate = ref()
const occurrenceAttRemarks = ref()
const otherCatNumbers = ref()
const specimenLicense = ref()
const preparationList = ref()
const preparationString = ref()
const preservations = ref()
const recordType = ref()
const remarks = ref()
const reproductCondition = ref()
const specimenSex = ref()
const stage = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    basisOfRecord,
    catalogNumber,
    collectionCode,
    collectionName,
    catalogedDate,
    createdDate,
    dateIdentified,
    expeditionName,
    preservation,
    identifiedBy,
    institutionCode,
    institutionName,
    individualCount,
    license,
    lifeStage,
    modified,
    occurrenceAttributeRemarks,
    occurrenceRemarks,
    previousIdentifications,
    preparations,
    recordedBy,
    otherCatalogNumbers,
    sex
  } = record

  additionalDetermination.value = previousIdentifications
  catNumber.value = catalogNumber

  if (catalogedDate) {
    dateCataloged.value = moment
      .tz(catalogedDate, 'ddd MMM DD HH:mm:ss z YYYY', 'CET')
      .format('YYYY-MM-DD')
  }

  // if (createdDate) {
  //   dateCreated.value = moment
  //     .tz(createdDate, 'ddd MMM DD HH:mm:ss z YYYY', 'CET')
  //     .format('YYYY-MM-DD')
  // }

  code.value = collectionCode
  collection.value = collectionName
  collectors.value = recordedBy ? recordedBy.toString() : ''
  count.value = individualCount

  expeditionNameData.value = expeditionName

  identifier.value = identifiedBy
  identifiedDate.value = dateIdentified

  institution.value = institutionName + ' [ ' + institutionCode + ' ] '

  if (modified) {
    modifiedDate.value = moment
      .tz(modified, 'ddd MMM DD HH:mm:ss z YYYY', 'CET')
      .format('YYYY-MM-DD')
  }

  occurrenceAttRemarks.value = occurrenceAttributeRemarks
  otherCatNumbers.value = otherCatalogNumbers

  if (preparations) {
    preparationList.value = preparations
    preparationString.value = preparationList.value.join(', ')
  }

  preservations.value = preservation

  recordType.value = basisOfRecord
  remarks.value = occurrenceRemarks

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
