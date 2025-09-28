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
          <record-event v-bind:code="code" />
        </div>
        <div class="col-12" no-gutters>
          <Location v-bind:code="code" />
        </div>
        <div class="col-12" no-gutters>
          <geological-context v-if="isPalCollection" />
        </div>

        <div class="col-12" no-gutters>
          <record-identification v-bind:code="code" />
        </div>
        <div class="col-12" no-gutters>
          <Taxonomy v-bind:code="code" />
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
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import Service from '../Service'

import GeologicalContext from '../components/GeologicalContext.vue'
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
const isPalCollection = ref(false)

// const isPalCollection = computed(() => {
//   const record = store.getters['selectedRecord']
//   const collectionCode = record.collectionCode
//   return collectionCode === 'pz' || collectionCode == 'pb'
// })

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
  const {
    collectionCode,
    kingdom,
    phylum,
    clazz,
    order,
    family,
    genus,
    subgenus,
    scientificName,
    species,
    taxonRank
  } = record

  code.value = collectionCode
  isPalCollection.value = collectionCode === 'pz' || collectionCode === 'pb'

  const higherClassification = new Array(kingdom, phylum, clazz, order, family, genus, subgenus)

  classification.value = higherClassification.filter((str) => str !== undefined).join(' > ')

  // const addGenus = collectionCode === 'pz' || collectionCode === 'pb' || collectionCode === 'vp'
  // if (addGenus) {
  //   name.value = taxonRank === 'species' ? genus + ' ' + species : scientificName
  // } else {
  //   name.value = scientificName
  // }
  if (collectionCode === 'pz' || collectionCode === 'pb') {
    name.value = taxonRank === 'species' ? genus + ' ' + species : scientificName
  } else if (collectionCode === 'vp') {
    name.value = genus + ' ' + species
  } else {
    name.value = scientificName
  }

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
