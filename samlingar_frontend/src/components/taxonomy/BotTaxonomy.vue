<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1.1em">{{ $t('results.taxonomy') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding">{{ $t('results.scientificName') }}</div>
      <div class="col-8 reducePadding">{{ taxonName }}</div>

      <div class="col-4 reducePadding">{{ $t('results.scientificNameAuthorship') }}</div>
      <div class="col-8 reducePadding">
        {{ scientificNameAuthorshipData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.family') }}</div>
      <div class="col-8 reducePadding">
        {{ theFamily }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.genus') }}</div>
      <div class="col-8 reducePadding">
        {{ theGenus }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.species') }}</div>
      <div class="col-8 reducePadding">
        {{ theSpecies }}
      </div>

      <div class="col-1 reducePadding">{{ $t('results.synonyms') }}</div>
      <div
        class="col-3 reducePadding"
        style="padding-left: 1rem; cursor: pointer"
        @click="displaySynonyms"
        v-if="hideSynonyms"
      >
        [<small>
          <i>{{ $t('results.displayAll') }}</i>
        </small>
        <small style="padding-left: 0.5em"
          ><i class="pi pi-caret-right" style="vertical-align: sub"></i></small
        >]
      </div>

      <div
        v-else
        class="col-3 reducePadding"
        style="padding-left: 1rem; cursor: pointer"
        @click="displaySynonyms"
      >
        [<small>
          <i>{{ $t('results.hideSynonyms') }}</i>
        </small>
        <small style="padding-left: 0.5em"
          ><i class="pi pi-caret-down" style="vertical-align: sub"></i></small
        >]
      </div>
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
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const hideSynonyms = ref(true)
const taxonName = ref()
const theFamily = ref()
const theGenus = ref()
const theSpecies = ref()
const scientificNameAuthorshipData = ref()
const synonymsData = ref()
const synonymAuthorData = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    collectionCode,
    genus,
    family,
    scientificName,
    scientificNameAuthorship,
    species,
    synonymAuthor
  } = record

  synonymsData.value = synonymAuthor

  synonymAuthorData.value = synonymAuthor ? synonymAuthor[0] : null
  scientificNameAuthorshipData.value = scientificNameAuthorship

  theFamily.value = family
  theGenus.value = genus
  theSpecies.value = species

  if (collectionCode === 'vp' && species) {
    taxonName.value = genus ? genus + ' ' + species : species
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
