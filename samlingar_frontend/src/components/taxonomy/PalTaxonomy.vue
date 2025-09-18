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

      <div class="col-4 reducePadding">{{ $t('results.phylum') }}</div>
      <div class="col-8 reducePadding">
        {{ thePhylum }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.class') }}</div>
      <div class="col-8 reducePadding">
        {{ theClazz }}
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

      <div class="col-4 reducePadding">{{ $t('results.species') }}</div>
      <div class="col-8 reducePadding">
        {{ theSpecies }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.synonyms') }}</div>
      <div class="col-8 reducePadding">
        {{ synonymsData }}
      </div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const taxonName = ref()
const thePhylum = ref()
const theClazz = ref()
const theOrder = ref()
const theFamily = ref()
const theGenus = ref()
const theSpecies = ref()
const scientificNameAuthorshipData = ref()
const synonymsData = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const {
    clazz,
    genus,
    family,
    order,
    phylum,
    scientificName,
    scientificNameAuthorship,
    species,
    synonyms
  } = record

  theClazz.value = clazz
  scientificNameAuthorshipData.value = scientificNameAuthorship
  synonymsData.value = synonyms
  thePhylum.value = phylum
  theOrder.value = order
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
