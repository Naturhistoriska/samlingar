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
      :placeholder="$t('search.addFilters')"
      class="w-full md:w-80"
      @update:modelValue="onSelectionChange"
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
let virtualSelectedItems = ref([])

const groupedSelections = ref([
  {
    label: 'Taxonomy',
    code: 'tx',
    items: [
      { label: 'Phylum', value: 'phylum', key: 'phylum:', code: 'tx', multiple: false },
      { label: 'Class', value: 'clazz', key: 'clazz:', code: 'tx', multiple: false },
      { label: 'Order', value: 'order', key: 'order:', code: 'tx', multiple: false },
      { label: 'Family', value: 'family', key: 'family:', code: 'tx', multiple: false },
      { label: 'Genus', value: 'genus', key: 'genus:', code: 'tx', multiple: false },
      { label: 'Subgenus', value: 'subgenus', key: 'subgenus:', code: 'tx', multiple: false },
      { label: 'Species', value: 'species', key: 'species:', code: 'tx', multiple: false },
      {
        label: 'Common name',
        value: 'vernacularName',
        key: 'vernacularName:',
        code: 'tx',
        multiple: false
      },
      {
        label: 'Synonyms',
        value: 'synonyms',
        key: 'synonyms:',
        code: 'tx',
        multiple: false
      },
      { label: 'Taxon rank', value: 'taxonRank', key: 'taxonRank:', code: 'tx', multiple: false }
    ]
  },
  {
    label: 'Specimen',
    code: 'specimen',
    items: [
      {
        label: 'Catalog number',
        value: 'catalogNumber',
        key: 'catalogNumber:',
        code: 'specimen',
        multiple: false
      },
      {
        label: 'Type status',
        value: 'typeStatus',
        key: 'typeStatus:',
        code: 'specimen',
        multiple: false
      }
    ]
  },
  {
    label: 'Collection event',
    code: 'event',
    items: [
      {
        label: 'Collected by',
        value: 'collectors',
        key: 'collectors:',
        code: 'event',
        multiple: false
      }
      // {
      // label: 'Field number',
      // value: 'fieldNumber',
      // key: 'fieldNumber:',
      // code: 'event',
      // multiple: false
      // }
    ]
  },
  {
    label: 'Locality',
    code: 'locality',
    items: [
      { label: 'Country', value: 'country', key: 'country:', code: 'locality', multiple: false },
      {
        label: 'State/Province',
        value: 'stateProvince',
        key: 'stateProvince:',
        code: 'locality',
        multiple: false
      },
      {
        label: 'County/Parish',
        value: 'county',
        key: 'county:',
        code: 'locality',
        multiple: false
      },

      {
        label: 'Locality',
        value: 'locality',
        key: 'locality:',
        code: 'locality',
        multiple: false
      }
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

function onSelectionChange(newValue) {
  console.log('onSelectionChange', newValue, selectedItems.value)

  if (multiSelectRef.value) {
    multiSelectRef.value.hide() // This closes the panel
  }
  const oldValue = store.getters['fields']
  console.log('oldValue', oldValue)

  // Compare with old value to detect removals
  if (newValue) {
    const removedItems = virtualSelectedItems.value.filter((item) => !newValue.includes(item))
    console.log('Removed:', removedItems)
    removedItems.forEach((obj) => {
      delete obj.text
    })
  } else {
    virtualSelectedItems.value.forEach((obj) => {
      delete obj.text
    })
  }

  if (selectedItems.value && selectedItems.value.length > 0) {
    store.commit('setFields', selectedItems)
  } else {
    store.commit('setFields', [])
  }
  virtualSelectedItems.value = selectedItems.value
}

// function onChange(event) {
// console.log(event.value, selectedItems.value)
// if (multiSelectRef.value) {
// multiSelectRef.value.hide() // This closes the panel
// }
// if (selectedItems.value && selectedItems.value.length > 0) {
// store.commit('setFields', selectedItems)
// } else {
// store.commit('setFields', [])
// }
// }
</script>
<style scoped>
.searchLabel {
  font-size: 10px;
}
</style>
