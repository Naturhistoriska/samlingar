<template>
  <div class="grid record-container">
    <div class="col-12" no-gutters>
      <p class="m-0 titleStyle">{{ $t('records.recordTitle') }}</p>
    </div>
    <div class="col-12 classificationText" no-gutters>
      {{ classification }}
    </div>
    <div class="col-12 scientificNameText" no-gutters>
      <i>{{ name }}</i>
    </div>
    <div v-if="showImages" class="col-12">
      <div class="grid" v-if="hasData">
        <div class="col-12">
          <image-view />
        </div>
      </div>
    </div>

    <div v-else class="grid" v-if="hasData">
      <div class="col-12 md:col-7">
        <div class="col-12">
          <record-dataset v-bind:code="code" />
        </div>
        <div class="col-12">
          <record-event v-bind:code="code" />
        </div>
        <div class="col-12">
          <Location v-bind:code="code" />
        </div>
        <div class="col-12" v-if="isPalCollection">
          <geological-context />
        </div>

        <div class="col-12">
          <record-identification v-bind:code="code" />
        </div>
        <div class="col-12">
          <Taxonomy v-bind:code="code" />
        </div>
      </div>
      <div class="col-12 md:col-5">
        <div class="col-12">
          <single-map />
        </div>
        <div class="col-12">
          <image-thumbnails />
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import Service from '../Service'

import GeologicalContext from '../components/GeologicalContext.vue'
// import Images from '../components/Images.vue'
// import ImageView from '../components/IiifViewer.vue'
import ImageView from '../components/ImageView.vue'
import ImageThumbnails from '../components/ImageThumbnails.vue'
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
// const clazz = ref()

const code = ref()
const isPalCollection = ref(false)
const showImages = ref(false)

watch(
  () => store.getters['showImageView'],
  (newValue, oldValue) => {
    showImages.value = newValue
  }
)

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
  } else if (collectionCode === 'PI' || collectionCode === 'HE') {
    name.value = taxonRank === 'Species' ? genus + ' ' + species : scientificName
  } else if (collectionCode === 'vp') {
    if (species) {
      name.value = genus ? genus + ' ' + species : species
    }
  } else if (collectionCode === 'PI') {
    name.value = taxonRank === 'Species' ? genus + ' ' + species : scientificName
  } else {
    name.value = scientificName
  }

  store.commit('setSelectedRecord', record)
  store.commit('setShowImageView', false)
  hasData.value = true
}
</script>
<style scoped>
.record-container {
  padding-left: 2rem; /* smaller default padding */
  padding-right: 2rem;
}

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

@media (max-width: 768px) {
  .record-container {
    padding-left: 1rem; /* remove big left padding */
    padding-right: 1rem;
  }

  .titleStyle {
    font-size: 1.2rem;
    text-align: center;
  }

  .classificationText,
  .scientificNameText {
    text-align: center;
  }
}
</style>
