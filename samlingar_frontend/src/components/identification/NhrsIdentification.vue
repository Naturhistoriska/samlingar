<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1.1em" v-if="!isMineralCollection">
      {{ $t('results.identification') }}
    </p>
    <div class="grid">
      <div class="col-4 reducePadding" v-if="!isMineralCollection">
        {{ $t('results.previousIdentifications') }}
      </div>
      <div class="col-8 reducePadding">
        <span v-for="(item, index) in additionalDetermination" :key="index">
          {{ item }}<br />
        </span>
      </div>

      <div class="col-4 reducePadding" v-if="isMineralCollection">{{ $t('results.minerals') }}</div>
      <div class="col-8 reducePadding" v-if="isMineralCollection">{{ minerals }}</div>

      <div class="col-4 reducePadding">{{ $t('results.identifiedBy') }}</div>
      <div class="col-8 reducePadding">
        {{ identifiedByData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.dateIdentified') }}</div>
      <div class="col-8 reducePadding">
        {{ identifyDate }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.identificationQualifier') }}</div>
      <div class="col-8 reducePadding">
        {{ identificationQualifierData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.typeStatus') }}</div>
      <div class="col-8 reducePadding">
        {{ typeStatusData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.identificationRemarks') }}</div>
      <div class="col-8 reducePadding">
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

const identifyDate = ref()
const identifiedByData = ref()
const identificationQualifierData = ref()
const minerals = ref()
const typeStatusData = ref()
const additionalDetermination = ref()

const isMineralCollection = ref(false)

const remarks = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    associeradeMineral,
    collectionCode,
    dateIdentified,
    identifiedBy,
    identificationQualifier,
    identificationRemarks,
    previousIdentifications,
    typeStatus
  } = record

  isMineralCollection.value = collectionCode === 'NRMMIN'
  // if (previousIdentifications) {
  //   additionalDetermination.value = previousIdentifications.join(' | ')
  // }

  additionalDetermination.value = previousIdentifications

  minerals.value = associeradeMineral ? associeradeMineral.join(' | ') : ''

  if (dateIdentified) {
    identifyDate.value = moment
      .tz(dateIdentified, 'ddd MMM DD HH:mm:ss z YYYY', 'CET')
      .format('YYYY-MM-DD')
  }

  identifiedByData.value = identifiedBy
  identificationQualifierData.value = identificationQualifier

  typeStatusData.value = typeStatus

  remarks.value = identificationRemarks
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
