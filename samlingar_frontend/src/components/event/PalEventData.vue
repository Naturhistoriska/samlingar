<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1.1em">{{ $t('results.event') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding">{{ $t('results.eventDate') }}</div>
      <div class="col-8 reducePadding">
        {{ verbatimEventDateData }}
      </div>
      <div class="col-4 reducePadding">{{ $t('results.collectors') }}</div>
      <div class="col-8 reducePadding">
        {{ collectors }}
      </div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const collectors = ref()
const verbatimEventDateData = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const { recordedBy, verbatimEventDate } = record

  collectors.value = recordedBy ? recordedBy.toString() : ''
  verbatimEventDateData.value = verbatimEventDate
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
