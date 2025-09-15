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

      <div class="col-4 reducePadding">{{ $t('results.recordedBy') }}</div>
      <div class="col-8 reducePadding">
        {{ collectors }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.habitat') }}</div>
      <div class="col-8 reducePadding">
        {{ habitatData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.minimumDepthInMeters') }}</div>
      <div class="col-8 reducePadding">
        {{ minimumDepth }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.maximumDepthInMeters') }}</div>
      <div class="col-8 reducePadding">
        {{ maximumDepth }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.individualCount') }}</div>
      <div class="col-8 reducePadding">
        {{ count }}
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

const dateCataloged = ref()
const catNumber = ref()
const code = ref()
const collection = ref()
const collectors = ref()
const count = ref()
const expeditionNameData = ref()
const habitatData = ref()
const maximumDepth = ref()
const minimumDepth = ref()
const institution = ref()
const preparationList = ref()
const preparationString = ref()
const recordType = ref()
const remarks = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    basisOfRecord,
    catalogedDate,
    catalogNumber,
    collectionCode,
    collectionName,
    expeditionName,
    habitat,
    individualCount,
    institutionCode,
    maximumDepthInMeters,
    minimumDepthInMeters,
    occurrenceRemarks,
    preparations,
    recordedBy
  } = record

  catNumber.value = catalogNumber

  if (catalogedDate) {
    dateCataloged.value = moment
      .tz(catalogedDate, 'ddd MMM DD HH:mm:ss z YYYY', 'CET')
      .format('YYYY-MM-DD')
  }

  code.value = collectionCode
  collection.value = collectionName
  collectors.value = recordedBy ? recordedBy.toString() : ''
  count.value = individualCount

  expeditionNameData.value = expeditionName

  habitatData.value = habitat
  institution.value = t('common.institution') + ' [ ' + institutionCode + ' ]'

  maximumDepth.value = maximumDepthInMeters
  minimumDepth.value = minimumDepthInMeters

  if (preparations) {
    preparationList.value = preparations
    preparationString.value = preparationList.value.join(', ')
  }

  recordType.value = basisOfRecord
  remarks.value = occurrenceRemarks
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
