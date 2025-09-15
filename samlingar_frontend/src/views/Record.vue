<template>
  <div class="grid" style="padding-left: 10em">
    <div class="col-12" no-gutters>
      <p class="m-0 titleStyle">{{ $t('records.recordTitle') }}</p>
    </div>
    <div class="col-12 classificationText" no-gutters>
      {{ classification }}
    </div>
    <div class="col-12 scientificNameText" no-gutters>
      <i>{{ name }}</i>
    </div>
    <div class="grid" v-if="hasData">
      <div class="col-7" no-gutters>
        <div class="col-12" no-gutters>
          <record-dataset v-bind:code="code" />
        </div>
        <div class="col-12" no-gutters>
          <record-event />
        </div>
        <div class="col-12" no-gutters>
          <Location />
        </div>
        <div class="col-12" no-gutters>
          <record-identification />
        </div>
        <div class="col-12" no-gutters>
          <Taxonomy />
        </div>
      </div>
      <div class="col-5" no-gutters>
        <div class="col-12" no-gutters>
          <single-map />
        </div>
        <div class="col-12" no-gutters>
          <Images />
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import Service from '../Service'

import Images from '../components/Images.vue'
import Location from '../components/Location.vue'
import SingleMap from '../components/SingleMap.vue'
import RecordDataset from '../components/RecordDataset.vue'
import RecordEvent from '../components/RecordEvent.vue'
import RecordIdentification from '../components/RecordIdentification.vue'
import Taxonomy from '../components/Taxonomy.vue'

const store = useStore()
const router = useRouter()
const service = new Service()

const classification = ref()
const name = ref()
const hasData = ref(false)
const clazz = ref()

const code = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  if (record) {
    buildRecordData(record)
  } else {
    const id = router.currentRoute.value.path.slice(8)
    hasData.value = false
    fetchRecord(id)
  }
})

function fetchRecord(id) {
  service
    .apiIdSearch(id)
    .then((response) => {
      const record = response.response[0]

      if (record) {
        buildRecordData(record)
      }
      setTimeout(() => {}, 2000)
    })
    .catch()
    .finally(() => {})
}

function buildRecordData(record) {
  const { collectionCode, kingdom, phylum, order, family, genus, subgenus, scientificName } = record
  clazz.value = record.class

  code.value = collectionCode

  const higherClassification = new Array(
    kingdom,
    phylum,
    clazz.value,
    order,
    family,
    genus,
    subgenus
  )

  classification.value = higherClassification.filter((str) => str !== undefined).join(' > ')

  name.value = scientificName

  store.commit('setSelectedRecord', record)
  hasData.value = true
}
</script>
<style scoped>
.titleStyle {
  font-weight: bold;
  font-size: 2em;
  color: #777676;
}
.classificationText {
  font-size: 1em;
  color: #6fade6;
}
.scientificNameText {
  font-size: 1.5em;
  font-weight: bold;
  color: #6fade6;
}
</style>
