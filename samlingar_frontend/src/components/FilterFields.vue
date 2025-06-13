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
    >
      <template #optiongroup="slotProps">
        <div class="flex items-center">
          <div>{{ slotProps.option.label }}</div>
        </div>
      </template>
    </MultiSelect>
    <div style="min-height: 20px">&nbsp</div>
    <input-box v-bind:field="field" v-bind:placehold="label" />
  </div>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'
import { useStore } from 'vuex'
import InputBox from './baseComponents/InputBox.vue'

const store = useStore()

const selectedItems = ref()

const groupedSelections = ref([
  {
    label: 'Taxonomy',
    code: 'tx',
    items: [
      { label: 'Kingdom', value: 'kingdom' },
      { label: 'Phylum', value: 'phylum' },
      { label: 'Class', value: 'class' },
      { label: 'Order', value: 'order' },
      { label: 'Family', value: 'family' },
      { label: 'Genus', value: 'genus' },
      { label: 'Subgenus', value: 'subgenus' },
      { label: 'Species', value: 'species' },
      { label: 'Vernacular name', value: 'vernacularName' },
      { label: 'Taxon rank', value: 'taxonRank' }
    ]
  },
  {
    label: 'Specimen',
    code: 'specimen',
    items: [
      { label: 'Institution code', value: 'instCode' },
      { label: 'Catalog number', value: 'catalogNumber' },
      { label: 'Type status', value: 'typeStatus' },
      { label: 'Collection code', value: 'collectionCode' }
    ]
  },
  {
    label: 'Collection event',
    code: 'event',
    items: [
      { label: 'Date collected', value: 'collectingDate' },
      { label: 'Colleced by', value: 'collector' },
      { label: 'Field number', value: 'fieldNumber' }
    ]
  },
  {
    label: 'Locality',
    code: 'locality',
    items: [
      { label: 'Country', value: 'country' },
      { label: 'State/Province', value: 'state' },
      { label: 'County/Parish', value: 'county' },
      { label: 'Water body', value: 'waterBody' },
      { label: 'Locality', value: 'locality' }
    ]
  }
])

const label = ref('search.searchAll')
const field = ref('setKindom')

const emits = defineEmits(['search'])
</script>
<style scoped>
.searchLabel {
  font-size: 10px;
}
</style>
