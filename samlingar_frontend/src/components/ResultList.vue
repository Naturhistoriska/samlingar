<template>
  <div>
    <div id="resultList">
      <template v-for="(result, index) in results" :key="result.uuid">
        <Result v-bind:result="result" />
        <Divider
          v-if="index + 1 < results.length"
          :key="`devider-${index}`"
          style="background-color: #ffffff"
        />
      </template>
    </div>

    <Paginator
      :rows="10"
      :totalRecords="totalRecords"
      :rowsPerPageOptions="[10, 20, 30]"
      @page="onPage($event)"
    ></Paginator>
  </div>
</template>
<script setup>
import { computed } from 'vue'
import { useStore } from 'vuex'
import Result from './Result.vue'

const store = useStore()
const emits = defineEmits(['search'])

const results = computed(() => {
  return store.getters['results']
})

const totalRecords = computed(() => {
  return store.getters['totalRecords']
})

function onPage(event) {
  console.log('page change...', event)

  const start = event.first
  const numPerPage = event.rows

  store.commit('setNumPerPage', numPerPage)
  store.commit('setStartRecord', start)

  emits('search')
}
</script>
<style scoped></style>
