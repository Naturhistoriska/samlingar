<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1em">{{ $t('results.dataset') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding">{{ $t('results.dataResource') }}</div>
      <div class="col-8 reducePadding">
        {{ dataResource }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.institiutionCode') }}</div>
      <div class="col-8 reducePadding">
        {{ institution }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.collectionName') }}</div>
      <div class="col-8 reducePadding">
        {{ collection }}
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
        {{ catalogedDate }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.recordedType') }}</div>
      <div class="col-8 reducePadding">
        {{ recordType }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.biome') }}</div>
      <div class="col-8 reducePadding">
        {{ biomeData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.expeditionName') }}</div>
      <div class="col-8 reducePadding">
        {{ expeditionName }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.preparation') }}</div>
      <div class="col-8 reducePadding">
        {{ preparationList }}
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

      <div class="col-4 reducePadding">{{ $t('results.occurrenceStatus') }}</div>
      <div class="col-8 reducePadding">
        {{ status }}
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
const biomeData = ref()
const catalogedDate = ref()
const catNumber = ref()
const code = ref()
const collection = ref()
const collectors = ref()
const count = ref()
const dataResource = ref()
const expeditionName = ref()
const identifiedDate = ref()
const identifier = ref()
const institution = ref()
const modifiedDate = ref()
const occurrenceAttRemarks = ref()
const otherCatNumbers = ref()
const specimenLicense = ref()
const preparationList = ref()
const preservations = ref()
const recordType = ref()
const remarks = ref()
const reproductCondition = ref()
const specimenSex = ref()
const stage = ref()
const status = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    basisOfRecord,
    biome,
    catalogNumber,
    collectionCode,
    collectionName,
    createdDate_dt,
    dataResourceName,
    dateIdentified,
    dynamicProperties_expeditionName,
    dynamicProperties_preservation,
    identifiedBy,
    institutionCode,
    institutionName,
    individualCount,
    license,
    lifeStage,
    modified,
    occurrenceAttributeRemarks,
    occurrenceRemarks,
    occurrenceStatus,
    previousIdentifications,
    preparations,
    recordedBy,
    reproductiveCondition,
    otherCatalogNumbers,
    sex
  } = record

  additionalDetermination.value = previousIdentifications
  biomeData.value = biome
  catNumber.value = catalogNumber

  if (createdDate_dt) {
    catalogedDate.value = moment
      .tz(createdDate_dt, 'ddd MMM DD HH:mm:ss z YYYY', 'CET')
      .format('YYYY-MM-DD')
  }

  code.value = collectionCode
  collection.value = collectionName
  collectors.value = recordedBy ? recordedBy.toString() : ''
  count.value = individualCount

  dataResource.value = dataResourceName

  expeditionName.value = dynamicProperties_expeditionName

  identifier.value = identifiedBy
  identifiedDate.value = dateIdentified

  institution.value = institutionName + ' [ ' + institutionCode + ' ] '

  modifiedDate.value = modified

  occurrenceAttRemarks.value = occurrenceAttributeRemarks
  otherCatNumbers.value = otherCatalogNumbers
  preparationList.value = preparations
  preservations.value = dynamicProperties_preservation

  recordType.value = basisOfRecord
  remarks.value = occurrenceRemarks
  reproductCondition.value = reproductiveCondition

  specimenSex.value = sex
  specimenLicense.value = license
  stage.value = lifeStage
  status.value = occurrenceStatus
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
