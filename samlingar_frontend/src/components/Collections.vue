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
    :pt="{
      item: { class: 'text-sm' }, // Individual item
      itemGroup: { class: 'text-xs font-bold' } // Group label
    }"
  >
    <template #optiongroup="slotProps">
      <div class="flex items-center">
        {{ slotProps.option.label }}
      </div>
    </template>
  </MultiSelect>
  <ul v-if="itemSelected">
    <li v-for="val in selectedItems" :key="val">
      <small>{{ val.label }}</small>
    </li>
  </ul>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { useStore } from 'vuex'

const store = useStore()
const { t } = useI18n()

const selectedItems = ref([])
const multiSelectRef = ref(null)

const groupedSelections = ref([
  {
    label: t('collectionLabel.botCollection'),
    code: 'bot',
    items: [
      { label: t('collectionLabel.algae'), value: 'Algae' },
      { label: t('collectionLabel.fungi'), value: 'Fungi/Lichens' },
      { label: t('collectionLabel.mosses'), value: 'Mosses' },
      {
        label: t('collectionLabel.phanerogamic'),
        value: 'Vascular Plants'
      }
    ]
  },
  {
    label: t('collectionLabel.zooCollection'),
    code: 'zoo',
    items: [
      {
        label: t('collectionLabel.ent'),
        value: 'NRM Entomology Collection Objects'
      },
      {
        label: t('collectionLabel.smtpObj'),
        value: 'Swedish Malaise Trap Project (SMTP) Collection Obj'
      },
      {
        label: t('collectionLabel.smtpList'),
        value: 'Swedish Malaise Trap Project (SMTP) Species Lists'
      },
      {
        label: t('collectionLabel.ev'),
        value: 'Invertebrate main collection'
      },
      {
        label: t('collectionLabel.et'),
        value: 'Invertebrate type collection'
      },
      { label: t('collectionLabel.fish'), value: 'Fish' },

      { label: t('collectionLabel.bird'), value: 'Bird' },
      { label: t('collectionLabel.mammal'), value: 'Mammals' },
      { label: t('collectionLabel.herp'), value: 'Amphibians and reptiles' }
    ]
  },
  {
    label: t('collectionLabel.palCollection'),
    code: 'paleo',
    items: [
      {
        label: t('collectionLabel.pz'),
        value: 'Paleozoology'
      },
      {
        label: t('collectionLabel.pb'),
        value: 'Paleobotany'
      }
    ]
  },
  {
    label: t('collectionLabel.geoCollection'),
    code: 'geo',
    items: [
      { label: t('collectionLabel.nrmlig'), value: 'NRM Isotope Geology' },
      { label: t('collectionLabel.nrmmin'), value: 'NRM Mineralogy' },
      { label: t('collectionLabel.nrmnod'), value: 'NRM Nodules' }
    ]
  }
])

const itemSelected = computed(() => {
  return store.getters['collectionGroup'] !== null
})

onMounted(() => {
  const group = store.getters['collectionGroup']
  const dataSet = store.getters['dataResource']

  selectedItems.value = groupedSelections.value
    .filter((group) => group.code === dataSet)
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
<style scoped>
.p-multiselect-items .p-multiselect-item-group {
  font-size: 0.75rem;
  /* font-weight: bold; */
}

/* Style the group items */
.p-multiselect-items .p-multiselect-item {
  font-size: 0.5rem !important;
}
</style>
