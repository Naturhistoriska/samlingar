<template>
  <MultiSelect
    v-model="selectedItems"
    ref="multiSelectRef"
    id="collectionSelect"
    :options="localizedGroups"
    optionLabel="label"
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
    id: 'bot',
    code: 'bot',
    items: [
      {
        // label: { en: 'Algae Collection', sv: 'Alger samling' },
        label: t('collectionLabel.algae'),
        id: 'algae',
        locale: { en: 'Algae Collection', sv: 'Algsamling' },
        value: { en: 'Algae Collection', sv: 'Alger samling' },
        code: 'algae'
      },
      {
        label: t('collectionLabel.fungi'),
        id: 'fungi',
        locale: { en: 'Fungi Collection', sv: 'Svampsamling' },
        value: 'Fungi Collection',
        code: 'fungi'
      },
      {
        label: t('collectionLabel.mosses'),
        id: 'mosses',
        locale: { en: 'Moss Collection', sv: 'Mossamling' },
        value: 'Mosses',
        code: 'mosses'
      },
      {
        label: t('collectionLabel.phanerogamic'),
        id: 'vascularPlants',
        locale: { en: 'Vascular plant Collections', sv: 'Kärlväxtsamling' },
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
    id: 'zoo',
    code: 'zoo',
    items: [
      {
        label: t('collectionLabel.ent'),
        locale: { en: 'Entomological Collections', sv: 'Entomologiska samlingar' },
        value: 'NRM Entomology Collection Objects',
        code: 'NHRS'
      },
      {
        label: t('collectionLabel.smtpObj'),
        locale: {
          en: 'Swedish Malaise Trap Project (SMTP) Collection Obj',
          sv: 'Svenska Malaisefälleprojektet (SMTP) Samlingsobjekt'
        },
        value: 'Swedish Malaise Trap Project (SMTP) Collection Obj',
        code: 'SMTP_INV'
      },
      {
        label: t('collectionLabel.smtpList'),
        locale: {
          en: 'Swedish Malaise Trap Project (SMTP) Species Lists',
          sv: 'Svenska Malaisefälleprojektet (SMTP) Artlistor'
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
        locale: { en: 'Fish Collection', sv: 'Fisksamling' },
        value: 'Fish',
        code: 'PI'
      },
      {
        label: t('collectionLabel.bird'),
        locale: { en: 'Bird Collection', sv: 'Fågelsamling' },
        value: 'Bird',
        code: 'AV'
      },
      {
        label: t('collectionLabel.mammal'),
        locale: { en: 'Mammal Collection', sv: 'Däggdjurssamling' },
        value: 'Mammals',
        code: 'MA'
      },
      {
        label: t('collectionLabel.herp'),
        locale: { en: 'Amphibians and reptiles Collections', sv: 'Amfibie och reptilsamling' },
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
    id: 'pal',
    code: 'pal',
    items: [
      {
        label: t('collectionLabel.pz'),
        locale: { en: 'Palaeozoological Collections', sv: 'Paleozoologiska samlingar' },
        value: 'Paleozoology',
        code: 'pz'
      },
      {
        label: t('collectionLabel.pb'),
        locale: { en: 'Palaeobotanical Collections', sv: 'Paleobotaniska samlingar' },
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
    id: 'geo',
    code: 'geo',
    items: [
      {
        label: t('collectionLabel.nrmlig'),
        locale: { en: 'Isotope Geology Collection', sv: 'Isotopgeologisk samling' },
        value: 'NRM Isotope Geology',
        code: 'NRMLIG'
      },
      {
        label: t('collectionLabel.nrmmin'),
        locale: { en: 'Mineralogy Collection', sv: 'Mineralsamling' },
        value: 'NRM Mineralogy',
        code: 'NRMMIN'
      },
      {
        label: t('collectionLabel.nrmnod'),
        locale: { en: 'Nodules Collection', sv: 'Nodulsamling' },
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

watch(locale, (newValue) => {
  selectedItems.value = selectedItems.value.map((item) => ({
    ...item,
    label: item.locale[locale.value] || item.locale.en
  }))
})

const localizedGroups = computed(() =>
  groupedSelections.value.map((group) => ({
    label: group.label[locale.value] || group.label['en'],
    code: group.code,
    items: group.items.map((item) => ({
      ...item,
      label: item.locale[locale.value] || item.locale.en
    }))
  }))
)

const currentLocale = computed(() => locale.value)

onMounted(() => {
  const dataSet = store.getters['dataResource']

  if (dataSet) {
    selectedItems.value = localizedGroups.value
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
    for (const group of localizedGroups.value) {
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
