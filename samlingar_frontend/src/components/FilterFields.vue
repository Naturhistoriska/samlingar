<template>
  <div class="grid" id="filters">
    <MultiSelect
      v-model="selectedItems"
      ref="multiSelectRef"
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
      @change="onChange(event)"
    >
      <template #optiongroup="slotProps">
        <div class="flex items-center">
          <div>{{ slotProps.option.label }}</div>
        </div>
      </template>
    </MultiSelect>
    <Divider type="solid" v-if="displayDivid" />
    <field-group />
  </div>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'
import { useStore } from 'vuex'
import FieldGroup from './FieldGroup.vue'

const store = useStore()

const selectedItems = ref([])
const multiSelectRef = ref(null)

const groupedSelections = ref([
  {
    label: 'Taxonomy',
    code: 'tx',
    items: [
      { label: 'Kingdom', value: 'kingdom', key: 'kingdom:', code: 'tx' },
      { label: 'Phylum', value: 'phylum', key: 'phylum:', code: 'tx' },
      { label: 'Class', value: 'class', key: 'class:', code: 'tx' },
      { label: 'Order', value: 'order', key: 'order:', code: 'tx' },
      { label: 'Family', value: 'family', key: 'family:', code: 'tx' },
      { label: 'Genus', value: 'genus', key: 'genus:', code: 'tx' },
      { label: 'Subgenus', value: 'subgenus', key: 'subgenus:', code: 'tx' },
      { label: 'Species', value: 'species', key: 'species:', code: 'tx' },
      { label: 'Vernacular name', value: 'vernacularName', key: 'vernacularName:', code: 'tx' },
      { label: 'Synonyms', value: 'synonyms', key: 'synonyms:', code: 'tx' },
      { label: 'Taxon rank', value: 'taxonRank', key: 'taxonRank:', code: 'tx' }
    ]
  },
  {
    label: 'Specimen',
    code: 'specimen',
    items: [
      { label: 'Catalog number', value: 'catalogNumber', key: 'catalogNumber:', code: 'specimen' },
      { label: 'Type status', value: 'typeStatus', key: 'typeStatus:', code: 'specimen' },
      {
        label: 'Collection code',
        value: 'collectionCode',
        key: 'collectionCode:',
        code: 'specimen'
      },
      {
        label: 'Collection name',
        value: 'collectionName',
        key: 'collectionName:',
        code: 'specimen'
      },
      {
        label: 'Data resource name',
        value: 'dataResourceName',
        key: 'dataResourceName:',
        code: 'specimen'
      }
    ]
  },
  {
    label: 'Collection event',
    code: 'event',
    items: [
      { label: 'Collected by', value: 'collector', key: 'collector:', code: 'event' },
      { label: 'Field number', value: 'fieldNumber', key: 'fieldNumber:', code: 'event' }
    ]
  },
  {
    label: 'Locality',
    code: 'locality',
    items: [
      { label: 'Country', value: 'country', key: 'country:', code: 'locality' },
      { label: 'State/Province', value: 'state', key: 'stateProvince:', code: 'locality' },
      { label: 'County/Parish', value: 'county', key: 'county:', code: 'locality' },
      { label: 'Water body', value: 'waterBody', key: 'waterBody:', code: 'locality' },
      { label: 'Locality', value: 'locality', key: 'locality:', code: 'locality' }
    ]
  }
])

const displayDivid = computed(() => {
  return selectedItems.value && selectedItems.value.length > 0
})

onMounted(() => {
  const fields = store.getters['fields']
  if (fields) {
    fields.forEach((field) => {
      const code = field.code

      const group = groupedSelections.value.filter((element) => element.code === code)

      const items = group[0].items

      const item = items.filter((item) => item.key === field.key)

      selectedItems.value.push(item[0])
    })
  }
})

function onChange(event) {
  if (multiSelectRef.value) {
    multiSelectRef.value.hide() // This closes the panel
  }
  if (selectedItems.value && selectedItems.value.length > 0) {
    store.commit('setFields', selectedItems)
  } else {
    store.commit('setFields', [])
  }
}
</script>
<style scoped>
.searchLabel {
  font-size: 10px;
}
</style>
