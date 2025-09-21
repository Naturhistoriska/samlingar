<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1em">{{ $t('results.identification') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding">{{ $t('results.determinations') }}</div>
      <div class="col-8 reducePadding">
        {{ determinations }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.identifiedBy') }}</div>
      <div class="col-8 reducePadding">
        {{ identifiedByData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.basionym') }}</div>
      <div class="col-8 reducePadding">
        {{ basionym }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.typeStatus') }}</div>
      <div class="col-8 reducePadding">
        {{ typeStatusData }}
      </div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const basionym = ref()
const identifiedByData = ref()
const determinations = ref()
const typeStatusData = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const { identifiedBy, originalnameBasionym, previousIdentifications, typeStatus } = record

  basionym.value = originalnameBasionym
  if (previousIdentifications) {
    determinations.value = previousIdentifications.join(', ')
  }
  identifiedByData.value = identifiedBy

  typeStatusData.value = typeStatus
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
