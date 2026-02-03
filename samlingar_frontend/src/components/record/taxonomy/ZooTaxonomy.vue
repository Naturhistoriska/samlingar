<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1.1em">{{ $t('results.taxonomy') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding">{{ $t('results.scientificName') }}</div>
      <div class="col-8 reducePadding">{{ taxonName }}</div>

      <div class="col-4 reducePadding">{{ $t('results.rank') }}</div>
      <div class="col-8 reducePadding">
        {{ rank }}
      </div>

      <div class="col-4 reducePadding" v-if="hasKindomAndSynonyms">
        {{ $t('results.kingdom') }}
      </div>
      <div class="col-8 reducePadding" v-if="hasKindomAndSynonyms">
        {{ theKingdom }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.phylum') }}</div>
      <div class="col-8 reducePadding">
        {{ thePhylum }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.class') }}</div>
      <div class="col-8 reducePadding">
        {{ theClass }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.order') }}</div>
      <div class="col-8 reducePadding">
        {{ theOrder }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.family') }}</div>
      <div class="col-8 reducePadding">
        {{ theFamily }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.genus') }}</div>
      <div class="col-8 reducePadding">
        {{ theGenus }}
      </div>

      <div class="col-4 reducePadding" v-if="isEvCollection">{{ $t('results.subgenus') }}</div>
      <div class="col-8 reducePadding" v-if="isEvCollection">
        {{ theSubgenus }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.species') }}</div>
      <div class="col-8 reducePadding">
        {{ theSpecies }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.infraspecificEpithet') }}</div>
      <div class="col-8 reducePadding">
        {{ infraspecificEpithetData }}
      </div>

      <div class="col-4 reducePadding" v-if="isEvCollection">
        {{ $t('results.infragenericEpithet') }}
      </div>
      <div class="col-8 reducePadding" v-if="isEvCollection">
        {{ infragenericEpithetData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.specificEpithet') }}</div>
      <div class="col-8 reducePadding">
        {{ specificEpithetData }}
      </div>

      <div class="col-4 reducePadding" v-if="hasCommonName">
        {{ $t('results.vernacularName') }}
      </div>
      <div class="col-8 reducePadding" v-if="hasCommonName">
        {{ commonName }}
      </div>
    </div>
    <div class="grid" style="margin-top: 1px" v-if="hasKindomAndSynonyms">
      <div class="col-1 reducePadding">{{ $t('results.synonyms') }}</div>
      <button
        type="button"
        class="col-3 reducePadding"
        style="
          padding-left: 1rem;
          cursor: pointer;
          background: none;
          border: none;
          text-align: left;
          font-family: inherit;
          font-size: inherit;
        "
        @click="displaySynonyms"
        v-if="hideSynonyms"
      >
        [<small>
          <i>{{ $t('results.displayAll') }}</i>
        </small>
        <small style="padding-left: 0.5em"
          ><i class="pi pi-caret-right" style="vertical-align: sub"></i></small
        >]
      </button>

      <button
        type="button"
        v-else
        class="col-3 reducePadding"
        style="
          padding-left: 1rem;
          cursor: pointer;
          background: none;
          border: none;
          text-align: left;
          font-family: inherit;
          font-size: inherit;
        "
        @click="displaySynonyms"
      >
        [<small>
          <i>{{ $t('results.hideSynonyms') }}</i>
        </small>
        <small style="padding-left: 0.5em"
          ><i class="pi pi-caret-down" style="vertical-align: sub"></i></small
        >]
      </button>
      <div class="col-8 reducePadding" v-if="hideSynonyms">
        {{ synonymAuthorData }}
      </div>
      <div class="col-8 reducePadding" v-else>
        <span v-for="(item, index) in synonymsData" :key="index"> {{ item }}<br /> </span>
      </div>
    </div>
  </div>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'
import { useStore } from 'vuex'
import Button from 'primevue/button'

const store = useStore()

const hideSynonyms = ref(true)

const commonName = ref()
const theClass = ref()
const theGenus = ref()
const theFamily = ref()
const theKingdom = ref()
const theOrder = ref()
const thePhylum = ref()
const taxonName = ref()
const theSpecies = ref()
const theSubgenus = ref()
const infraspecificEpithetData = ref()
const infragenericEpithetData = ref()

const specificEpithetData = ref()
const synonymsData = ref()
const synonymAuthorData = ref()
const rank = ref()

const isEvCollection = computed(() => {
  const record = store.getters['selectedRecord']
  const collectionCode = record.collectionCode
  return collectionCode === 'ev' || collectionCode === 'et'
})

const hasKindomAndSynonyms = computed(() => {
  const record = store.getters['selectedRecord']
  const collectionCode = record.collectionCode
  return (
    collectionCode === 'ev' ||
    collectionCode === 'et' ||
    collectionCode === 'PI' ||
    collectionCode === 'HE'
  )
})

const hasCommonName = computed(() => {
  const record = store.getters['selectedRecord']
  const collectionCode = record.collectionCode
  return collectionCode === 'MA' || collectionCode === 'AV'
})

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    clazz,
    collectionCode,
    genus,
    family,
    infraspecificEpithet,
    infragenericEpithet, // ev, et
    kingdom, // ev, et, pi, he
    order,
    phylum,
    scientificName,
    species,
    specificEpithet,
    subgenus, // ev, et
    synonyms, // ev, et, pi, he
    taxonRank,
    vernacularName // ma, av
  } = record

  if (vernacularName) {
    commonName.value = vernacularName.join(' | ')
    console.log('commonName', commonName.value)
  }

  infragenericEpithetData.value = infragenericEpithet
  infraspecificEpithetData.value = infraspecificEpithet

  synonymsData.value = synonyms
  synonymAuthorData.value = synonyms ? synonyms[0] : null

  specificEpithetData.value = specificEpithet

  theClass.value = clazz

  theKingdom.value = kingdom
  thePhylum.value = phylum
  theOrder.value = order
  theFamily.value = family
  theGenus.value = genus
  theSpecies.value = species
  theSubgenus.value = subgenus

  rank.value = taxonRank == 'Clazz' ? 'Class' : taxonRank

  if (collectionCode === 'PI' || collectionCode === 'HE') {
    taxonName.value = taxonRank === 'Species' ? genus + ' ' + species : scientificName
  } else {
    taxonName.value = scientificName
  }
})

function displaySynonyms() {
  console.log('displaySynonyms')
  hideSynonyms.value = !hideSynonyms.value
}
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
