<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1em">{{ $t('results.taxonomy') }}</p>
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

      <div class="col-4 reducePadding">{{ $t('results.synonyms') }}</div>
      <div class="col-8 reducePadding">
        <span v-for="(item, index) in synonymsData" :key="index"> {{ item }}<br /> </span>
      </div>

      <div class="col-4 reducePadding">{{ $t('results.species') }}</div>
      <div class="col-8 reducePadding">
        {{ theSpecies }}
      </div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

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
    genus,
    family,
    scientificName,
    scientificNameAuthorship,
    species,
    synonyms,
    synonymAuthor
  } = record

  synonymsData.value = synonymAuthor

  synonymAuthorData.value = synonymAuthor
  scientificNameAuthorshipData.value = scientificNameAuthorship

  theFamily.value = family
  theGenus.value = genus
  theSpecies.value = species

  taxonName.value = scientificName
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
