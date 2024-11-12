<template>
  <Card style="background-color: transparent">
    <template #header>
      <Button link @click.prevent="onclick()">
        <small>{{ $t('results.backToResultListView') }}</small>
      </Button>
      <div style="padding-top: 30px">
        <ImageGalleria />
      </div>
    </template>
    <template #content>
      <div class="grid">
        <div class="col-3 textColor">{{ $t('results.catalogNumber') }}:</div>
        <div class="col-9 textColor">
          {{ result.catalogNumber }}
        </div>
        <div class="col-3 textColor" no-gutters>{{ $t('results.collectionName') }}:</div>
        <div class="col-9 textColor">
          {{ result.collectionName }}
        </div>
        <div class="col-3 textColor" no-gutters>{{ $t('results.scientificName') }}:</div>
        <div class="col-9 textColor">{{ scientificName }} {{ result.author }}</div>
        <div class="col-3 textColor" no-gutters>{{ $t('results.vernacularName') }}:</div>
        <div class="col-9 textColor">
          {{ result.commonName }}
        </div>
        <div class="col-3 textColor" no-gutters>{{ $t('results.highClassification') }}:</div>
        <div class="col-9 textColor">
          {{ result.higherTx }}
        </div>
        <div class="col-3 textColor" no-gutters>{{ $t('results.preparation') }}:</div>
        <div class="col-9 textColor">
          {{ result.preparations }}
        </div>
        <div class="col-3 textColor" no-gutters>{{ $t('results.typeStatus') }}:</div>
        <div class="col-9 textColor">
          {{ result.typeStatus }}
        </div>
        <div class="col-3 textColor" no-gutters>{{ $t('results.locality') }}:</div>
        <div class="col-9 textColor">
          {{ result.locality }}
        </div>
        <div class="col-3 textColor" no-gutters>{{ $t('results.continet') }}:</div>
        <div class="col-9 textColor">
          {{ result.continent }}
        </div>
        <div class="col-3 textColor" no-gutters>{{ $t('results.country') }}:</div>
        <div class="col-9 textColor">
          {{ result.country }}
        </div>
        <div class="col-3 textColor" no-gutters>{{ $t('results.state') }}:</div>
        <div class="col-9 textColor">
          {{ result.state }}
        </div>
        <div class="col-3 textColor" no-gutters>county:</div>
        <div class="col-9 textColor">
          {{ result.county }}
        </div>
        <div class="col-3 textColor" no-gutters>{{ $t('results.latitude') }}:</div>
        <div class="col-9 textColor">
          {{ result.latitude }}
        </div>
        <div class="col-3 textColor" no-gutters>{{ $t('results.longitude') }}:</div>
        <div class="col-9 textColor">
          {{ result.longitude }}
        </div>
        <div class="col-3 textColor" no-gutters>Synonym:</div>
        <div class="col-9 textColor">
          {{ result.synonymAuthor }}
        </div>
        <div class="col-3 textColor" no-gutters>Determiner:</div>
        <div class="col-9 textColor">
          {{ result.determiner }}
        </div>
        <div class="col-3 textColor" no-gutters>Collectors:</div>
        <div class="col-9 textColor">
          {{ result.collector }}
        </div>
        <div class="col-3 textColor" no-gutters>{{ $t('results.eventDate') }}:</div>
        <div class="col-9 textColor">
          {{ result.startDate }}
        </div>
        <div class="col-3 textColor" no-gutters>Cataloged Date:</div>
        <div class="col-9 textColor">
          {{ result.catalogedDate }}
        </div>
        <div class="col-3 textColor" no-gutters>Occurrence Remark:</div>
        <div class="col-9 textColor">
          {{ result.remarks }}
        </div>
      </div>
    </template>
  </Card>
</template>
<script setup>
import ImageGalleria from './ImageGalleria.vue'

import { computed, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'

const store = useStore()
const router = useRouter()

const result = computed(() => {
  return store.getters['selectedResult']
})

const scientificName = computed(() => {
  const selectedResult = store.getters['selectedResult']
  const { species, txFullName } = selectedResult

  return txFullName ? txFullName : species
})

onMounted(() => {})

function onclick() {
  store.commit('setShowDetail', false)
  router.push('/results')
}
</script>
<style scoped>
.textColor {
  color: #fff;
  padding-top: 0px;
  padding-bottom: 1px;
  font-size: 12px;
}
.p-button-link {
  color: #0dff5c;
}
</style>
