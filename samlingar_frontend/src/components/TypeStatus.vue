<template>
  <div class="card flex justify-center">
    <MultiSelect
      v-model="selectedTypes"
      display="chip"
      :options="types"
      optionLabel="label"
      optionValue="label"
      filter
      placeholder="Select type status"
      :maxSelectedLabels="3"
      class="w-full md:w-80"
      key="label"
      :selectAll="selectAll"
      optionDisabled="disable"
      @selectall-change="onSelectAllChange($event)"
      @change="onChange($event)"
    >
    </MultiSelect>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import MultiSelect from 'primevue/multiselect'

import Service from '../Service'
const service = new Service()

import { useStore } from 'vuex'
const store = useStore()

const selectedTypes = ref()
const types = ref([])
const selectAll = ref(false)
let disableSelect = ref(false)

onMounted(() => {
  service
    .apiSearchTypeStatus()
    .then((response) => {
      const facets = response.facets.typeStatus
      if (facets) {
        types.value = facets.buckets.map((a) => ({ label: a.val, count: a.count, disable: false }))
      }
    })
    .catch()
    .finally(() => {})
})
function onChange(event) {
  selectAll.value = event.value !== null ? event.value.length === types.value.length : false

  if (event.value) {
    if (event.value.length === 3) {
      types.value.forEach((type) => {
        type.disable = true
      })
      disableSelect = true
    } else {
      if (disableSelect) {
        types.value.forEach((type) => {
          type.disable = false
        })
        disableSelect = false
      }
    }
  }
  store.commit('setSelectedTypes', types.value)
}

function onSelectAllChange(event) {
  console.log('event 1', event.value)
  selectedTypes.value = event.checked ? types.value.map((type) => type.label) : []
  selectAll.value = event.checked

  if (disableSelect) {
    disableSelect = false
    types.value.forEach((type) => {
      type.disable = false
    })
  }
  if (selectAll.value) {
    store.commit('setIsType', true)
  }
}
</script>
<style>
.p-multiselect {
  width: 18rem;
  background: #000 !important;
}

.p-multiselect-label:not(.p-placeholder) {
  padding-top: 0.25rem;
  padding-bottom: 0.25rem;
  color: #fff !important;
}

/* .multiselect-custom .country-item-value {
  padding: 0.25rem 0.5rem;
  border-radius: 3px;
  display: inline-flex;
  margin-right: 0.5rem;
  background-color: var(--primary-color);
  color: var(--primary-color-text);
}

.multiselect-custom .country-item-value img.flag {
  width: 17px;
} */

@media screen and (max-width: 640px) {
  .p-multiselect {
    width: 100%;
  }
}
</style>
<!-- <style scoped>
.p-multiselect {
  background: #000 !important;
}

.p-multiselect .p-multiselect-label.p-placeholder {
  color: red !important;
}

.p-multiselect-label {
  display: flex;
  flex-wrap: wrap;
  color: red !important;
}
.overlyStyle {
  background: #000 !important;
  color: red !important;
}
.p-multiselect-overlay {
  background: var(--p-emerald-500) !important;
  color: red !important;
}
.p-multiselect-option {
  background: red !important;
}
.p-chip {
  background: #000 !important;
  color: red !important;
}
</style> -->
