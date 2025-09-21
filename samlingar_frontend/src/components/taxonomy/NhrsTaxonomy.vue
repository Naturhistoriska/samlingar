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

      <div class="col-4 reducePadding">{{ $t('results.rank') }}</div>
      <div class="col-8 reducePadding">
        {{ rank }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.kingdom') }}</div>
      <div class="col-8 reducePadding">
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

      <div class="col-4 reducePadding">{{ $t('results.subgenus') }}</div>
      <div class="col-8 reducePadding">
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
      <div class="col-4 reducePadding">{{ $t('results.specificEpithet') }}</div>
      <div class="col-8 reducePadding">
        {{ specificEpithetData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.vernacularName') }}</div>
      <div class="col-8 reducePadding">
        {{ commonName }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.synonyms') }}</div>
      <div class="col-8 reducePadding">
        {{ synonymsData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.taxonRemarks') }}</div>
      <div class="col-8 reducePadding">
        {{ remarks }}
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
const theClass = ref()
const theOrder = ref()
const theFamily = ref()
const theGenus = ref()
const theSubgenus = ref()
const theSpecies = ref()
const commonName = ref()
const infraspecificEpithetData = ref()
const specificEpithetData = ref()
const scientificNameAuthorshipData = ref()
const synonymsData = ref()
const remarks = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  console.log('record', record)
  const {
    clazz,
    genus,
    family,
    infraspecificEpithet,
    kingdom,
    order,
    phylum,
    scientificName,
    scientificNameAuthorship,
    species,
    specificEpithet,
    subgenus,
    synonyms,
    taxonRank,
    taxonRemarks,
    vernacularName
  } = record

  theClass.value = clazz

  if (vernacularName) {
    commonName.value = vernacularName.join(' | ')
  }

  infraspecificEpithetData.value = infraspecificEpithet

  rank.value = taxonRank
  remarks.value = taxonRemarks

  specificEpithetData.value = specificEpithet
  scientificNameAuthorshipData.value = scientificNameAuthorship
  if (synonyms) {
    synonymsData.value = synonyms.join(' | ')
  }

  taxonName.value = scientificName

  theKingdom.value = kingdom
  thePhylum.value = phylum
  theOrder.value = order
  theFamily.value = family
  theGenus.value = genus
  theSpecies.value = species
  theSubgenus.value = subgenus
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
