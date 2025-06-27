<template>
  <div class="grid" id="filters">
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
    <Divider type="solid" />
    <field-group @search="handleSearch" />
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
import Service from '../Service'
import FieldGroup from './FieldGroup.vue'

const store = useStore()
const service = new Service()

const emits = defineEmits(['search'])

// let hide = ref(false)

const selectedItems = ref([])

const groupedSelections = ref([
  {
    label: 'Taxonomy',
    code: 'tx',
    items: [
      // { label: 'Kingdom', value: 'kingdom', key: 'kingdom:' },
      // { label: 'Phylum', value: 'phylum', key: 'phylum:' },
      { label: 'Class', value: 'clazz', key: 'clazz:' },
      // { label: 'Order', value: 'order', key: 'order:' },
      { label: 'Family', value: 'family', key: 'family:' },
      { label: 'Genus', value: 'genus', key: 'genus:' },
      { label: 'Subgenus', value: 'subgenus', key: 'subgenus:' },
      { label: 'Species', value: 'species', key: 'species:' },
      { label: 'Vernacular name', value: 'vernacularName', key: 'vernacularName:' },
      { label: 'Synonyms', value: 'synonyms', key: 'synonyms:' },
      { label: 'Taxon rank', value: 'taxonRank', key: 'taxonRank:' }
    ]
  },
  {
    label: 'Specimen',
    code: 'specimen',
    items: [
      { label: 'Catalog number', value: 'catalogNumber', key: 'catalogNumber:' },
      { label: 'Type status', value: 'typeStatus', key: 'typeStatus:' },
      { label: 'Collection code', value: 'collectionCode', key: 'collectionCode:' },
      { label: 'Collection name', value: 'collectionName', key: 'collectionName:' }
    ]
  },
  {
    label: 'Collection event',
    code: 'event',
    items: [
      { label: 'Collected by', value: 'collector', key: 'collector:' },
      { label: 'Field number', value: 'fieldNumber', key: 'fieldNumber:' }
    ]
  },
  {
    label: 'Locality',
    code: 'locality',
    items: [
      { label: 'Country', value: 'country', key: 'country:' },
      { label: 'State/Province', value: 'state', key: 'stateProvince:' },
      { label: 'County/Parish', value: 'county', key: 'county:' },
      { label: 'Water body', value: 'waterBody', key: 'waterBody:' },
      { label: 'Locality', value: 'locality', key: 'locality:' }
    ]
  }
])

function reset() {
  console.log('reset...')
}

function onSelect(event) {
  console.log('onSelect', selectedItems.value)
  if (selectedItems.value && selectedItems.value.length > 0) {
    store.commit('setFields', selectedItems)

    const item = selectedItems.value.slice(-1)

    if (item) {
      console.log('here...', item[0].value)
    }

    // hide.value = true
  }
}

function handleSearch() {
  emits('search')
}
</script>
<style scoped>
.searchLabel {
  font-size: 10px;
}
</style>
