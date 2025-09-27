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
    :placeholder="$t('search.filterCollections')"
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
      <small>{{ val.locale[currentLocale] }}</small>
    </li>
  </ul>
</template>
<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import { useStore } from 'vuex'

const store = useStore()
const { t, locale } = useI18n()

const selectedItems = ref([])
const multiSelectRef = ref(null)

const groupedSelections = ref([
  {
    label: t('collectionLabel.botCollection'),
    code: 'bot',
    items: [
      {
        label: t('collectionLabel.algae'),
        locale: { en: 'Algae Collection', sv: 'Alger samling' },
        value: 'Algae',
        code: 'algae'
      },
      {
        label: t('collectionLabel.fungi'),
        locale: { en: 'Fungi Collection', sv: 'Svampar/Lavar samling' },
        value: 'Fungi/Lichens',
        code: 'fungi'
      },
      {
        label: t('collectionLabel.mosses'),
        locale: { en: 'Moss Collection', sv: 'Mossor samling' },
        value: 'Mosses',
        code: 'mosses'
      },
      {
        label: t('collectionLabel.phanerogamic'),
        locale: { en: 'Vascular plant Collections', sv: 'Kärlväxter samling' },
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
        locale: { en: 'Entomological Collections', sv: 'Entomologisk samling' },
        value: 'NRM Entomology Collection Objects',
        code: 'NHRS'
      },
      {
        label: t('collectionLabel.smtpObj'),
        locale: {
          en: 'Swedish Malaise Trap Project (SMTP) Collection Obj',
          sv: 'Swedish Malaise Trap Project (SMTP) Collection Obj'
        },
        value: 'Swedish Malaise Trap Project (SMTP) Collection Obj',
        code: 'SMTP_INV'
      },
      {
        label: t('collectionLabel.smtpList'),
        locale: {
          en: 'Swedish Malaise Trap Project (SMTP) Species Lists',
          sv: 'Swedish Malaise Trap Project (SMTP) Species Lists'
        },
        value: 'Swedish Malaise Trap Project (SMTP) Species Lists',
        code: 'SMTP_SPPLST'
      },
      {
        label: t('collectionLabel.ev'),
        locale: { en: 'Invertebrate Main Collection', sv: 'Evertebrater samling' },
        value: 'Invertebrate main collection',
        code: 'ev'
      },
      {
        label: t('collectionLabel.et'),
        locale: { en: 'Invertebrate Type Specimen Collection"', sv: 'Evertebrater typsamling' },
        value: 'Invertebrate type collection',
        code: 'et'
      },
      {
        label: t('collectionLabel.fish'),
        locale: { en: 'Fish Collection', sv: 'Fisk samling' },
        value: 'Fish',
        code: 'PI'
      },
      {
        label: t('collectionLabel.bird'),
        locale: { en: 'Bird Collection', sv: 'Fågel samling' },
        value: 'Bird',
        code: 'AV'
      },
      {
        label: t('collectionLabel.mammal'),
        locale: { en: 'Mammal Collection', sv: 'Däggdjur samling' },
        value: 'Mammals',
        code: 'MA'
      },
      {
        label: t('collectionLabel.herp'),
        locale: { en: 'Amphibians and reptiles Collections', sv: 'Grod och kräldjur samlingar' },
        value: 'Amphibians and reptiles',
        code: 'HE'
      }
    ]
  },
  {
    label: t('collectionLabel.palCollection'),
    code: 'pal',
    items: [
      {
        label: t('collectionLabel.pz'),
        locale: { en: 'Palaeozoological Collections', sv: 'Paleozoologisk samling' },
        value: 'Paleozoology',
        code: 'pz'
      },
      {
        label: t('collectionLabel.pb'),
        locale: { en: 'Palaeobotanical Collections', sv: 'Paleobotanisk samling' },
        value: 'Paleobotany',
        code: 'pb'
      }
    ]
  },
  {
    label: t('collectionLabel.geoCollection'),
    code: 'geo',
    items: [
      {
        label: t('collectionLabel.nrmlig'),
        locale: { en: 'Isotope Geology Collection', sv: 'Isotopgeologi samling' },
        value: 'NRM Isotope Geology',
        code: 'NRMLIG'
      },
      {
        label: t('collectionLabel.nrmmin'),
        locale: { en: 'Mineralogy Collection', sv: 'Mineralogisk samling' },
        value: 'NRM Mineralogy',
        code: 'NRMMIN'
      },
      {
        label: t('collectionLabel.nrmnod'),
        locale: { en: 'Nodules Collection', sv: 'Noduler samling' },
        value: 'NRM Nodules',
        code: 'NRMNOD'
      }
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

// watch(
//   () => store.getters['locale'],
//   (newValue, oldValue) => {
//     console.log('locale changed... ', newValue)
//     const dataSet = store.getters['dataResource']
//     console.log('dataset', dataSet)

//     selectedItems.value = groupedSelections.value
//       .filter((group) => group.code === dataSet)
//       .map((item) => item.items)[0]
//   }
// )

const currentLocale = computed(() => locale.value)

onMounted(() => {
  const dataSet = store.getters['dataResource']
  console.log('dataset', dataSet)

  selectedItems.value = groupedSelections.value
    .filter((group) => group.code === dataSet)
    .map((item) => item.items)[0]

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
