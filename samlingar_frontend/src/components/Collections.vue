<template>
  <MultiSelect
    v-model="selectedItems"
    :options="groupedSelections"
    optionLabel="label"
    filter
    showClear
    size="small"
    optionGroupLabel="label"
    optionGroupChildren="items"
    display="chip"
    placeholder="Add search fields"
    class="w-full md:w-80"
    @reset="reset"
    @change="onSelect(event)"
  >
    <template #optiongroup="slotProps">
      <div class="flex items-center">
        <div>{{ slotProps.option.label }}</div>
      </div>
    </template>
  </MultiSelect>
</template>
<script setup>
import { ref } from 'vue'

import { useStore } from 'vuex'
const store = useStore()

const selectedItems = ref([])

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
      { label: 'Entomological Collections (NRM)', value: 'Entomological Collections (NHRS)' },
      { label: 'Invertebrates Collection NRM', value: 'Invertebrates Collection' },
      {
        label: 'Invertebrates Type Specimen Collection NRM',
        value: 'Invertebrates (Type Specimens) '
      },
      { label: 'Fish Collection NRM', value: 'Fish Collection NRM' },

      { label: 'Ornithology Collection NRM', value: 'Ornithology Collection NRM' },
      { label: 'NRM-Mammals', value: 'NRM-Mammals' },
      { label: 'Herpetology Collection NRM', value: 'Herpetology Collection NRM' }
    ]
  },
  {
    label: 'Paleontological collections',
    code: 'event',
    items: [
      { label: 'Palaeobotanical Collections (PB)', value: 'Palaeobotanical Collections (PB)' },
      { label: 'Palaeozoological Collections (PZ)', value: 'Palaeozoological Collections (PZ)' }
    ]
  }
  // {
  //   label: 'Geological collections',
  //   code: 'locality',
  //   items: [
  //     { label: 'Country', value: 'country', key: 'country:' },
  //     { label: 'State/Province', value: 'state', key: 'stateProvince:' },
  //     { label: 'County/Parish', value: 'county', key: 'county:' },
  //     { label: 'Water body', value: 'waterBody', key: 'waterBody:' },
  //     { label: 'Locality', value: 'locality', key: 'locality:' }
  //   ]
  // }
])
function reset() {
  console.log('reset...')
}

function onSelect(event) {
  console.log('onSelect', selectedItems.value)

  if (selectedItems.value && selectedItems.value.length > 0) {
    const names = selectedItems.value.map((obj) => `"${obj.value}"`).join(' ')
    store.commit('setDataResource', names)
  }
}
</script>
<style scoped></style>
