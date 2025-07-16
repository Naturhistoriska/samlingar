<template>
  <MultiSelect
    v-model="selectedItems"
    ref="multiSelectRef"
    :options="groupedSelections"
    optionLabel="label"
    :maxSelectedLabels="3"
    filter
    showClear
    size="small"
    optionGroupLabel="label"
    optionGroupChildren="items"
    display="chip"
    placeholder="Add search fields"
    class="w-full md:w-80"
    @change="onSelect(event)"
  >
    <template #optiongroup="slotProps">
      <div class="flex items-center">
        <div>{{ slotProps.option.label }}</div>
      </div>
    </template>
  </MultiSelect>
  <ul v-if="itemSelected">
    <li v-for="val in selectedItems" :key="val">
      {{ val.label }}
    </li>
  </ul>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'

import { useStore } from 'vuex'
const store = useStore()

const selectedItems = ref([])
const multiSelectRef = ref(null)

const groupedSelections = ref([
  {
    label: 'Botanical and mycological collections',
    code: 'bot',
    items: [
      { label: 'Algae Collection', value: 'Algae (S)' },
      { label: 'Fungi Collection', value: 'Fungi (S)' },
      { label: 'Moss Collection', value: 'Mosses (S)' },
      {
        label: 'Phanerogamic Botanical Collections',
        value: 'Phanerogamic Botanical Collections (S)'
      }
    ]
  },
  {
    label: 'Zoological collections',
    code: 'zoo',
    items: [
      {
        label: 'Entomological Collections (NRM)',
        value: 'Entomological Collections (NHRS), Swedish Museum of Natural History (NRM)'
      },
      {
        label: 'Invertebrates Collection NRM',
        value: 'Invertebrates Collection of the Swedish Museum of Natural History'
      },
      {
        label: 'Invertebrates Type Specimen Collection NRM',
        value: 'Invertebrates (Type Specimens) of the Swedish Museum of Natural History'
      },
      { label: 'Fish Collection NRM', value: 'Fish Collection NRM' },

      { label: 'Ornithology Collection NRM', value: 'Ornithology Collection NRM' },
      { label: 'NRM-Mammals', value: 'NRM-Mammals' },
      { label: 'Herpetology Collection NRM', value: 'Herpetology Collection NRM' }
    ]
  },
  {
    label: 'Paleontological collections',
    code: 'paleo',
    items: [
      {
        label: 'Palaeobotanical Collections (PB)',
        value: 'Palaeozoological Collections (PZ), Swedish Museum of Natural History (NRM)'
      },
      {
        label: 'Palaeozoological Collections (PZ)',
        value: 'Palaeobotanical Collections (PB), Swedish Museum of Natural History (NRM)'
      }
    ]
  },
  {
    label: 'Geological collections',
    code: 'geo',
    items: [
      { label: 'NRM Isotope Geology', value: 'NRM Isotope Geology' },
      { label: 'NRM Mineralogy', value: 'NRM Mineralogy' },
      { label: 'NRM Nodules', value: 'NRM Nodules' }
    ]
  }
])

const itemSelected = computed(() => {
  return store.getters['dataResource'] !== null
})

onMounted(() => {
  // const group = store.getters['collectionGroup']
  const dataResource = store.getters['dataResource']

  const botany = import.meta.env.VITE_BOTANY_DATARESOURCE
  const zoo = import.meta.env.VITE_ZOO_DATARESOURCE
  const geo = import.meta.env.VITE_GEO_DATARESOURCE
  const paleao = import.meta.env.VITE_PALEA_DATARESOURCE

  let key
  if (dataResource) {
    if (botany === dataResource) {
      key = 'bot'
    } else if (zoo === dataResource) {
      key = 'zoo'
    } else if (paleao === dataResource) {
      key = 'paleo'
    } else if (geo === dataResource) {
      key = 'geo'
    }
  }
  selectedItems.value = groupedSelections.value
    .filter((group) => group.code === key)
    .map((item) => item.items)[0]
})

function onSelect(event) {
  if (multiSelectRef.value) {
    multiSelectRef.value.hide() // This closes the panel
  }
  if (selectedItems.value && selectedItems.value.length > 0) {
    const value = selectedItems.value.map((obj) => `'${obj.value}'`).join(' ')
    const names = `(${value})`
    store.commit('setDataResource', names)
  } else {
    store.commit('setDataResource', null)
  }
}
</script>
<style scoped></style>
