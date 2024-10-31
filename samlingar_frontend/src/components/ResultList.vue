<template>
  <div v-if="false">
    <ImageGalleria />
  </div>
  <div v-else>
    <div id="resultList">
      <template v-for="(result, index) in results" :key="result.id">
        <Result v-bind:result="result" />
        <Divider
          v-if="index + 1 < results.length"
          :key="`devider-${index}`"
          style="padding-bottom: 20px"
        />
      </template>
    </div>

    <Paginator
      v-if="results.length > 0"
      v-model:first="first"
      :rows="10"
      :totalRecords="totalRecords"
      :rowsPerPageOptions="[10, 20, 30]"
      @page="onPage($event)"
    ></Paginator>
  </div>
</template>
<script setup>
import { computed, ref, watch } from 'vue'
import { useStore } from 'vuex'
import ImageGalleria from './ImageGalleria.vue'
import Result from './Result.vue'

const first = ref(0)
const store = useStore()
const emits = defineEmits(['search'])

watch(
  () => store.getters['startRecord'],
  () => {
    const startRecord = store.getters['startRecord']
    console.log('resetPaging change...', startRecord)
    if (startRecord === 0) {
      first.value = 0
    }
  }
)

const results = computed(() => {
  return store.getters['results']
})

const totalRecords = computed(() => {
  return store.getters['totalRecords']
})

const showGalleria = computed(() => {
  return store.getters['openGalleria']
})

function onPage(event) {
  const start = event.first
  const numPerPage = event.rows

  store.commit('setNumPerPage', numPerPage)
  store.commit('setStartRecord', start)

  emits('search')
}
</script>
<style scoped></style>
