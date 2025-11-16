<template>
  <div class="grid record-container">
    <record-header :classification="classification" :name="name" />

    <!-- <div class="col-12">
      <p class="m-0 titleStyle">{{ $t('records.recordTitle') }}</p>
    </div>

    <div class="col-12 classificationText">
      {{ classification }}
    </div>

    <div class="col-12 scientificNameText">
      <i>{{ name }}</i>
    </div> -->

    <!-- IMAGE VIEW MODE -->
    <!-- <div v-if="showImages && hasData" class="col-12">
      <image-view />
    </div> -->

    <image-view v-if="showImages && hasData" />

    <div v-else-if="hasData" class="grid col-12">
      <record-left :code="code" :isPalCollection="isPalCollection" />
      <record-right :showThumb="showThumb" />
    </div>

    <!-- DEFAULT VIEW -->
    <div v-else-if="hasData" class="grid col-12">
      <!-- Left column -->
      <div class="col-12 md:col-7">
        <record-dataset :code="code" />
        <record-event :code="code" />
        <location :code="code" />

        <geological-context v-if="isPalCollection" />

        <record-identification :code="code" />
        <taxonomy :code="code" />
      </div>

      <!-- Right column -->
      <div class="col-12 md:col-5">
        <single-map />
        <image-thumbnails v-if="showThumb" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import Service from '../Service'

// import GeologicalContext from '../components/GeologicalContext.vue'
// import ImageView from '../components/ImageView.vue'
// import ImageThumbnails from '../components/ImageThumbnails.vue'
// import Location from '../components/Location.vue'
// import SingleMap from '../components/SingleMap.vue'
// import RecordDataset from '../components/RecordDataset.vue'
// import RecordEvent from '../components/RecordEvent.vue'

import RecordHeader from '../components/record/RecordHeader.vue'
import RecordLeft from '../components/record/RecordLeft.vue'
import RecordRight from '../components/record/RecordRight.vue'

// import RecordIdentification from '../components/RecordIdentification.vue'
// import Taxonomy from '../components/Taxonomy.vue'

const service = new Service()
const store = useStore()
const router = useRouter()

const classification = ref('')
const name = ref('')
const code = ref('')
const hasData = ref(false)

const isPalCollection = ref(false)
const showImages = ref(false)

watch(
  () => store.getters['showImageView'],
  (value) => (showImages.value = value)
)

const showThumb = computed(() => {
  const rec = store.getters['selectedRecord']
  return rec?.associatedMedia !== null
})

// --- MOUNT LOGIC ---
onMounted(async () => {
  const record = store.getters['selectedRecord']

  if (record) {
    buildRecordData(record)
  } else {
    const id = router.currentRoute.value.path.slice(8)
    hasData.value = false
    await fetchRecord(id)
  }
})

async function fetchRecord(id) {
  try {
    const response = await service.apiIdSearch(id)
    const record = response?.response?.[0]

    if (record) {
      buildRecordData(record)
    }
  } catch (err) {
    console.error('Record fetch failed:', err)
  }
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
  isPalCollection.value = ['pz', 'pb'].includes(collectionCode)

  // Build classification string
  const higher = [kingdom, phylum, clazz, order, family, genus, subgenus]
  classification.value = higher.filter(Boolean).join(' > ')

  if (['pz', 'pb'].includes(collectionCode)) {
    name.value = taxonRank === 'species' ? `${genus} ${species}` : scientificName
  } else if (['PI', 'HE', 'vp'].includes(collectionCode)) {
    name.value = taxonRank === 'Species' ? `${genus} ${species}` : scientificName
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
  padding-left: 2rem;
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
    padding-left: 1rem;
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
