<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1em">{{ $t('results.taxonomy') }}</p>
    <div class="grid">
      <div class="col-3 reducePadding">{{ $t('results.scientificName') }}</div>
      <div class="col-9 reducePadding">
        {{ taxonName }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.rank') }}</div>
      <div class="col-9 reducePadding">
        {{ rank }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.kingdom') }}</div>
      <div class="col-9 reducePadding">
        {{ theKingdom }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.phylum') }}</div>
      <div class="col-9 reducePadding">
        {{ thePhylum }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.order') }}</div>
      <div class="col-9 reducePadding">
        {{ theOrder }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.class') }}</div>
      <div class="col-9 reducePadding">
        {{ theClass }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.family') }}</div>
      <div class="col-9 reducePadding">
        {{ theFamily }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.genus') }}</div>
      <div class="col-9 reducePadding">
        {{ theGenus }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.species') }}</div>
      <div class="col-9 reducePadding">
        {{ theSpecies }}
      </div>

      <div class="col-3 reducePadding">{{ $t('results.vernacularName') }}</div>
      <div class="col-9 reducePadding">
        {{ commonName }}
      </div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const taxonName = ref()
const rank = ref()
const theKingdom = ref()
const thePhylum = ref()
const theOrder = ref()
const theClass = ref()
const theFamily = ref()
const theGenus = ref()
const theSpecies = ref()
const commonName = ref()

onMounted(async () => {
  console.log('onMounted dataset')
  const record = store.getters['selectedRecord']

  const {
    clazz,
    family,
    genus,
    kingdom,
    phylum,
    order,
    scientificName,
    species,
    taxonRank,
    vernacularName
  } = record

  commonName.value = vernacularName
  theKingdom.value = kingdom
  thePhylum.value = phylum
  theOrder.value = order
  theClass.value = clazz
  theFamily.value = family
  theGenus.value = genus
  theSpecies.value = species

  taxonName.value = scientificName
  rank.value = taxonRank
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
