<template>
  <div class="grid record-container">
    <record-header :classification="classification" :name="name" />

    <image-view v-if="showImages && hasData" />

    <div v-else-if="hasData" class="grid col-12">
      <record-left :code="code" :isPalCollection="isPalCollection" />
      <record-right :showThumb="showThumb" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import Service from '../Service'

import RecordHeader from '../components/record/RecordHeader.vue'
import RecordLeft from '../components/record/RecordLeft.vue'
import RecordRight from '../components/record/RecordRight.vue'
import ImageView from '../components/ImageView.vue'

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
  store.commit('setShowImageView', false)
})

async function fetchRecord(id) {
  try {
    const response = await service.apiIdSearch(id)
    const record = response?.response?.[0]

    if (record) {
      buildRecordData(record)
      store.commit('setSelectedRecord', record)
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
