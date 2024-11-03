<template>
  <div class="card flex justify-center">
    <MultiSelect
      v-model="selectedTypes"
      display="chip"
      :options="types"
      optionLabel="name"
      filter
      placeholder="Select type status"
      :maxSelectedLabels="3"
      class="w-full md:w-80"
    />
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import MultiSelect from 'primevue/multiselect'

import { useStore } from 'vuex'
const store = useStore()

const selectedTypes = ref()

const types = computed(() => {
  return store.getters['typeStatus']
})

// const cities = ref([
//   { name: 'New York', code: 'NY' },
//   { name: 'Rome', code: 'RM' },
//   { name: 'London', code: 'LDN' },
//   { name: 'Istanbul', code: 'IST' },
//   { name: 'Paris', code: 'PRS' }

// ])
</script>
