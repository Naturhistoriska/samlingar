<template>
  <div class="grid" style="padding-left: 10em">
    <div class="col-12" no-gutters>
      <p class="m-0 titleStyle">{{ $t('records.recordTitle') }}</p>
    </div>
    <div class="col-12 classificationText" no-gutters>
      {{ record.higherClassification.replaceAll('/', ' > ') }}
    </div>
    <div class="col-12 scientificNameText" no-gutters>
      <i>{{ record.scientificName }}</i>
    </div>
  </div>
</template>
<script setup>
import { computed } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const record = computed(() => {
  return store.getters['selectedRecord']
})
</script>
<style scoped>
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
</style>
