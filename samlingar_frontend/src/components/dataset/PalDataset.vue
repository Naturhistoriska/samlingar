<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1em">{{ $t('results.dataset') }}</p>
    <div class="grid">
      <div class="col-3 reducePadding">{{ $t('results.collectionName') }}</div>
      <div class="col-9 reducePadding">
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

      <div class="col-4 reducePadding">{{ $t('results.catalogedDate') }}</div>
      <div class="col-8 reducePadding">
        {{ dateCataloged }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.preservation') }}</div>
      <div class="col-8 reducePadding">
        {{ preservations }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.preservedSpecimen') }}</div>
      <div class="col-9 reducePadding">
        {{ preservedSpecimenData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.earliestAgeOrLowestStage') }}</div>
      <div class="col-8 reducePadding">
        {{ lowStage }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.latestAgeOrHighestStage') }}</div>
      <div class="col-8 reducePadding">
        {{ highestStage }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.earliestEpochOrLowestSeries') }}</div>
      <div class="col-8 reducePadding">
        {{ lowSeries }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.latestEpochOrHighestSeries') }}</div>
      <div class="col-8 reducePadding">
        {{ highestSeries }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.earliestPeriodOrLowestSystem') }}</div>
      <div class="col-8 reducePadding">
        {{ lowSystem }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.latestPeriodOrHighestSystem') }}</div>
      <div class="col-8 reducePadding">
        {{ highestSystem }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.recordedBy') }}</div>
      <div class="col-9 reducePadding">
        {{ collectors }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.lithostratigraphy') }}</div>
      <div class="col-8 reducePadding">
        {{ lithostratigraphy }}
      </div>

      <div class="col-4 reducePadding" v-if="isPbCollection">{{ $t('results.element') }}</div>
      <div class="col-8 reducePadding" v-if="isPbCollection">
        {{ elementData }}
      </div>

      <div class="col-4 reducePadding" v-if="isPbCollection">{{ $t('results.description') }}</div>
      <div class="col-8 reducePadding" v-if="isPbCollection">
        {{ descriptionData }}
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

const descriptionData = ref()
const elementData = ref()

const highestStage = ref()
const highestSeries = ref()
const highestSystem = ref()

const institution = ref()

let isPbCollection = ref(false)

const lithostratigraphy = ref()

const lowStage = ref()
const lowSeries = ref()
const lowSystem = ref()

const preservations = ref()
const preservedSpecimenData = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    catalogedDate,
    catalogNumber,
    collectionCode,
    collectionName,
    earliestAgeOrLowestStage,
    earliestEpochOrLowestSeries,
    earliestPeriodOrLowestSystem,
    element,
    description,
    latestAgeOrHighestStage,
    latestEpochOrHighestSeries,
    latestPeriodOrHighestSystem,
    lithostratigraphicTerms,
    preservation,
    preservedSpecimen,
    recordedBy
  } = record

  if (catalogedDate) {
    dateCataloged.value = moment
      .tz(catalogedDate, 'ddd MMM DD HH:mm:ss z YYYY', 'CET')
      .format('YYYY-MM-DD')
  }
  catNumber.value = catalogNumber

  code.value = collectionCode
  collection.value = collectionName
  collectors.value = recordedBy ? recordedBy.toString() : ''

  elementData.value = element

  descriptionData.value = description

  highestSeries.value = latestEpochOrHighestSeries
  highestSystem.value = latestPeriodOrHighestSystem
  highestStage.value = latestAgeOrHighestStage

  institution.value = t('common.institution') + ' [  NRM  ]'

  isPbCollection.value = collectionCode === 'pb'

  lithostratigraphy.value = lithostratigraphicTerms
  lowSeries.value = earliestEpochOrLowestSeries
  lowStage.value = earliestAgeOrLowestStage
  lowSystem.value = earliestPeriodOrLowestSystem

  preservations.value = preservation
  preservedSpecimenData.value = preservedSpecimen
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
