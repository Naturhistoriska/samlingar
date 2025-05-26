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
    <div v-if="hasData">
      <div class="col-12" no-gutters>
        <Dataset v-if="collection == 'NHRS'" />
        <record-dataset v-else />
      </div>
      <div class="col-12" no-gutters>
        <record-event />
      </div>
      <div class="col-12" no-gutters>
        <Location />
      </div>
      <div class="col-12" no-gutters>
        <Taxonomy />
      </div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import Service from '../Service'
import Dataset from '../components/nhrs/Dataset.vue'
import Location from '../components/Location.vue'
import RecordDataset from '../components/RecordDataset.vue'
import RecordEvent from '../components/RecordEvent.vue'
import Taxonomy from '../components/Taxonomy.vue'

const store = useStore()
const router = useRouter()
const service = new Service()

onMounted(async () => {
  console.log('onMounted')
  const record = store.getters['selectedRecord']
  if (record) {
    const { higherClassification, scientificName } = record
    classification.value = higherClassification.replaceAll('/', ' > ')
    name.value = scientificName
    hasData.value = true
    collection.value = record.collectionCode
  } else {
    const id = router.currentRoute.value.path.slice(8)
    hasData.value = false
    fetchRecord(id)
  }
})

const classification = ref()
const name = ref()
const hasData = ref(false)
const collection = ref()

function fetchRecord(id) {
  console.log('id', id)
  service
    .apiIdSearch(id)
    .then((response) => {
      const record = response.response[0]

      if (record) {
        const { higherClassification, scientificName } = record
        classification.value = higherClassification.replaceAll('/', ' > ')
        name.value = scientificName

        store.commit('setSelectedRecord', record)
        hasData.value = true

        collection.value = record.collectionCode

        setTimeout(() => {}, 2000)
      }
    })
    .catch()
    .finally(() => {})
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
