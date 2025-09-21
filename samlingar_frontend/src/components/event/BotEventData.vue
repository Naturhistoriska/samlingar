<template>
  <div style="font-size: 12px">
    <p style="font-weight: bold; font-size: 1em">{{ $t('results.event') }}</p>
    <div class="grid">
      <div class="col-4 reducePadding">{{ $t('results.eventDate') }}</div>
      <div class="col-8 reducePadding">
        {{ verbatimEventDateData }}
      </div>

      <div class="col-4 reducePadding">{{ $t('results.habitat') }}</div>
      <div class="col-8 reducePadding">
        {{ habitatData }}
      </div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'
// import moment from 'moment-timezone'

const store = useStore()

const habitatData = ref()
const verbatimEventDateData = ref()

onMounted(async () => {
  const record = store.getters['selectedRecord']

  const { habitat, verbatimEventDate } = record

  habitatData.value = habitat
  verbatimEventDateData.value = verbatimEventDate
})
</script>
<style scoped>
.reducePadding {
  padding-top: 0px;
  padding-bottom: 1px;
}
</style>
