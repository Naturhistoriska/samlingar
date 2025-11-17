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

      <div class="col-4 reducePadding">{{ $t('results.catalogedDate') }}</div>
      <div class="col-8 reducePadding">
        {{ dateCataloged }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.preservation') }}</div>
      <div class="col-8 reducePadding">
        {{ preservations }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.preservedSpecimen') }}</div>
      <div class="col-8 reducePadding">
        {{ preservedSpecimenData }}
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

const descriptionData = ref()
const elementData = ref()

const institution = ref()

let isPbCollection = ref(false)

const preservations = ref()
const preservedSpecimenData = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    catalogedDate,
    catalogNumber,
    collectionCode,
    collectionName,
    element,
    description,
    preservation,
    preservedSpecimen
  } = record

  if (catalogedDate) {
    dateCataloged.value = moment
      .tz(catalogedDate, 'ddd MMM DD HH:mm:ss z YYYY', 'CET')
      .format('YYYY-MM-DD')
  }

  catNumber.value = catalogNumber

  code.value = collectionCode
  collection.value = collectionName

  elementData.value = element

  descriptionData.value = description

  institution.value = t('common.institution') + ' [  NRM  ]'

  isPbCollection.value = collectionCode === 'pb'

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
