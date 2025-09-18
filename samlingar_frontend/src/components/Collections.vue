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
  <ul>
    <li v-for="val in selectedItems" :key="val">
      <small>{{ val.label }}</small>
    </li>
  </ul>
</template>
<script setup>
import { onMounted, ref, watch } from 'vue'
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
      { label: t('collectionLabel.algae'), value: 'Algae', code: 'algae' },
      { label: t('collectionLabel.fungi'), value: 'Fungi/Lichens', code: 'fungi' },
      { label: t('collectionLabel.mosses'), value: 'Mosses', code: 'mosses' },
      {
        label: t('collectionLabel.phanerogamic'),
        value: 'Vascular Plants',
        code: 'vp'
      }
    ]
  },
  {
    label: t('collectionLabel.zooCollection'),
    code: 'zoo',
    items: [
      {
        label: t('collectionLabel.ent'),
        value: 'NRM Entomology Collection Objects',
        code: 'NHRS'
      },
      {
        label: t('collectionLabel.smtpObj'),
        value: 'Swedish Malaise Trap Project (SMTP) Collection Obj',
        code: 'SMTP_INV'
      },
      {
        label: t('collectionLabel.smtpList'),
        value: 'Swedish Malaise Trap Project (SMTP) Species Lists',
        code: 'SMTP_SPPLST'
      },
      {
        label: t('collectionLabel.ev'),
        value: 'Invertebrate main collection',
        code: 'ev'
      },
      {
        label: t('collectionLabel.et'),
        value: 'Invertebrate type collection',
        code: 'et'
      },
      { label: t('collectionLabel.fish'), value: 'Fish', code: 'PI' },

      { label: t('collectionLabel.bird'), value: 'Bird', code: 'AV' },
      { label: t('collectionLabel.mammal'), value: 'Mammals', code: 'MA' },
      { label: t('collectionLabel.herp'), value: 'Amphibians and reptiles', code: 'HE' }
    ]
  },
  {
    label: t('collectionLabel.palCollection'),
    code: 'pal',
    items: [
      {
        label: t('collectionLabel.pz'),
        value: 'Paleozoology',
        code: 'pz'
      },
      {
        label: t('collectionLabel.pb'),
        value: 'Paleobotany',
        code: 'pb'
      }
    ]
  },
  {
    label: t('collectionLabel.geoCollection'),
    code: 'geo',
    items: [
      { label: t('collectionLabel.nrmlig'), value: 'NRM Isotope Geology', code: 'NRMLIG' },
      { label: t('collectionLabel.nrmmin'), value: 'NRM Mineralogy', code: 'NRMMIN' },
      { label: t('collectionLabel.nrmnod'), value: 'NRM Nodules', code: 'NRMNOD' }
    ]
  }
])

watch(
  () => store.getters['selectedCollection'],
  (newValue, oldValue) => {
    if (store.getters['selectedCollection'] === null) {
      selectedItems.value = null
    }
  }
)

onMounted(() => {
  const dataSet = store.getters['dataResource']
  console.log('dataset', dataSet)

  selectedItems.value = groupedSelections.value
    .filter((group) => group.code === dataSet)
    .map((item) => item.items)[0]

  console.log('selectedItems', selectedItems)
  if (selectedItems.value && selectedItems.value.length > 0) {
    const value = selectedItems.value.map((obj) => `'${obj.code}'`).join(' ')
    const codes = `(${value})`
    store.commit('setSelectedCollection', codes)
  } else {
    store.commit('setSelectedCollection', null)
  }
})

function onSelect(event) {
  console.log('onSelect...')
  if (multiSelectRef.value) {
    multiSelectRef.value.hide() // This closes the panel
  }

  store.commit('setDataResource', null)
  if (selectedItems.value && selectedItems.value.length > 0) {
    const value = selectedItems.value.map((obj) => `'${obj.code}'`).join(' ')
    const codes = `(${value})`
    console.log('names...', codes)
    store.commit('setSelectedCollection', codes)
  } else {
    store.commit('setSelectedCollection', null)
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
