<template>
  <MultiSelect
    v-model="selectedItems"
    ref="multiSelectRef"
    :options="localizedGroups"
    optionLabel="localizedName"
    optionGroupLabel="label"
    optionGroupChildren="items"
    :maxSelectedLabels="3"
    filter
    showClear
    size="small"
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

const emits = defineEmits(['collectionsSearch'])

const store = useStore()
const { t, locale } = useI18n()

const selectedItems = ref([])
const multiSelectRef = ref(null)

const groupedSelections = ref([
  {
    label: {
      en: 'Botanical and mycological collections',
      sv: 'Botaniska och mykologiska samlingar'
    },
    code: 'bot',
    items: [
      {
        label: { en: 'Algae Collection', sv: 'Alger samling' },
        locale: { en: 'Algae Collection', sv: 'Alger samling' },
        value: 'Algae',
        code: 'algae'
      },
      {
        label: { en: 'Fungi Collection', sv: 'Svampar/Lavar samling' },
        locale: { en: 'Fungi Collection', sv: 'Svampar/Lavar samling' },
        value: 'Fungi/Lichens',
        code: 'fungi'
      },
      {
        label: { en: 'Moss Collection', sv: 'Mossor samling' },
        locale: { en: 'Moss Collection', sv: 'Mossor samling' },
        value: 'Mosses',
        code: 'mosses'
      },
      {
        label: { en: 'Vascular plant Collections', sv: 'Kärlväxter samling' },
        locale: { en: 'Vascular plant Collections', sv: 'Kärlväxter samling' },
        value: 'Vascular Plants',
        code: 'vp'
      }
    ]
  },
  {
    label: {
      en: 'Zoological collections',
      sv: 'Zoologiska samlingar'
    },
    code: 'zoo',
    items: [
      {
        label: { en: 'Entomological Collections', sv: 'Entomologisk samling' },
        locale: { en: 'Entomological Collections', sv: 'Entomologisk samling' },
        value: 'NRM Entomology Collection Objects',
        code: 'NHRS'
      },
      {
        label: {
          en: 'Swedish Malaise Trap Project (SMTP) Collection Obj',
          sv: 'Swedish Malaise Trap Project (SMTP) Collection Obj'
        },
        locale: {
          en: 'Swedish Malaise Trap Project (SMTP) Collection Obj',
          sv: 'Swedish Malaise Trap Project (SMTP) Collection Obj'
        },
        value: 'Swedish Malaise Trap Project (SMTP) Collection Obj',
        code: 'SMTP_INV'
      },
      {
        label: {
          en: 'Swedish Malaise Trap Project (SMTP) Species Lists',
          sv: 'Swedish Malaise Trap Project (SMTP) Species Lists'
        },
        locale: {
          en: 'Swedish Malaise Trap Project (SMTP) Species Lists',
          sv: 'Swedish Malaise Trap Project (SMTP) Species Lists'
        },
        value: 'Swedish Malaise Trap Project (SMTP) Species Lists',
        code: 'SMTP_SPPLST'
      },
      {
        label: { en: 'Invertebrate Main Collection', sv: 'Evertebrater samling' },
        locale: { en: 'Invertebrate Main Collection', sv: 'Evertebrater samling' },
        value: 'Invertebrate main collection',
        code: 'ev'
      },
      {
        label: { en: 'Invertebrate Type Specimen Collection"', sv: 'Evertebrater typsamling' },
        locale: { en: 'Invertebrate Type Specimen Collection"', sv: 'Evertebrater typsamling' },
        value: 'Invertebrate type collection',
        code: 'et'
      },
      {
        label: { en: 'Fish Collection', sv: 'Fisk samling' },
        locale: { en: 'Fish Collection', sv: 'Fisk samling' },
        value: 'Fish',
        code: 'PI'
      },
      {
        label: { en: 'Bird Collection', sv: 'Fågel samling' },
        locale: { en: 'Bird Collection', sv: 'Fågel samling' },
        value: 'Bird',
        code: 'AV'
      },
      {
        label: { en: 'Mammal Collection', sv: 'Däggdjur samling' },
        locale: { en: 'Mammal Collection', sv: 'Däggdjur samling' },
        value: 'Mammals',
        code: 'MA'
      },
      {
        label: { en: 'Amphibians and reptiles Collections', sv: 'Grod och kräldjur samlingar' },
        locale: { en: 'Amphibians and reptiles Collections', sv: 'Grod och kräldjur samlingar' },
        value: 'Amphibians and reptiles',
        code: 'HE'
      }
    ]
  },
  {
    label: {
      en: 'Paleontological collections',
      sv: 'Paleontologiska samlingar'
    },
    code: 'pal',
    items: [
      {
        label: { en: 'Palaeozoological Collections', sv: 'Paleozoologisk samling' },
        locale: { en: 'Palaeozoological Collections', sv: 'Paleozoologisk samling' },
        value: 'Paleozoology',
        code: 'pz'
      },
      {
        label: { en: 'Palaeobotanical Collections', sv: 'Paleobotanisk samling' },
        locale: { en: 'Palaeobotanical Collections', sv: 'Paleobotanisk samling' },
        value: 'Paleobotany',
        code: 'pb'
      }
    ]
  },
  {
    label: {
      en: 'Geological collections',
      sv: 'Geologiska samlingar'
    },
    code: 'geo',
    items: [
      {
        label: { en: 'Isotope Geology Collection', sv: 'Isotopgeologi samling' },
        locale: { en: 'Isotope Geology Collection', sv: 'Isotopgeologi samling' },
        value: 'NRM Isotope Geology',
        code: 'NRMLIG'
      },
      {
        label: { en: 'Mineralogy Collection', sv: 'Mineralogisk samling' },
        locale: { en: 'Mineralogy Collection', sv: 'Mineralogisk samling' },
        value: 'NRM Mineralogy',
        code: 'NRMMIN'
      },
      {
        label: { en: 'Nodules Collection', sv: 'Noduler samling' },
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

const localizedGroups = computed(() =>
  groupedSelections.value.map((group) => ({
    label: group.label[locale.value] || group.label['en'],
    code: group.code,
    items: group.items.map((item) => ({
      ...item,
      localizedName: item.label[locale.value] || item.label.en
    }))
  }))
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

  if (dataSet) {
    selectedItems.value = groupedSelections.value
      .filter((group) => group.code === dataSet)
      .map((item) => item.items)[0]
    if (selectedItems.value && selectedItems.value.length > 0) {
      const value = selectedItems.value.map((obj) => `'${obj.code}'`).join(' ')
      const codes = `(${value})`
      store.commit('setSelectedCollection', codes)
    }
  } else if (store.getters['chartCode'] !== null) {
    const code = store.getters['chartCode']
    let parentGroup
    for (const group of groupedSelections.value) {
      const match = group.items.find((item) => item.code === code)
      if (match) {
        parentGroup = group
        selectedItems.value.push(match)
        break
      }
    }
    store.commit('setSelectedCollection', `('${code}')`)
  } else {
    store.commit('setSelectedCollection', null)
  }
})

function onSelect(event) {
  if (multiSelectRef.value) {
    multiSelectRef.value.hide() // This closes the panel
  }

  store.commit('setDataResource', null)
  if (selectedItems.value && selectedItems.value.length > 0) {
    const value = selectedItems.value.map((obj) => `'${obj.code}'`).join(' ')
    const codes = `(${value})`
    store.commit('setSelectedCollection', codes)
  } else {
    store.commit('setSelectedCollection', null)
  }
  emits('collectionsSearch')
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
