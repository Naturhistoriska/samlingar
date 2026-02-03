<template>
  <div class="grid" id="filters">
    <MultiSelect
      v-model="selectedItems"
      ref="multiSelectRef"
      id="fieldSelect"
      :options="localizedGroups"
      optionGroupLabel="label"
      optionGroupChildren="items"
      optionLabel="label"
      filter
      showClear
      size="small"
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
import { computed, onMounted, ref, watch } from 'vue'
import { useStore } from 'vuex'
import { useI18n } from 'vue-i18n'
import FieldGroup from './FieldGroup.vue'

const store = useStore()

const { t, locale } = useI18n()

const selectedItems = ref([])
const multiSelectRef = ref(null)
let virtualSelectedItems = ref([])

const groupedSelections = ref([
  {
    label: {
      en: 'Taxonomy',
      sv: 'Taxonomi'
    },
    code: 'tx',
    items: [
      {
        label: 'Phylum',
        locale: { en: 'Phylum', sv: 'Stam' },
        value: 'phylum',
        key: 'phylum:',
        code: 'tx',
        multiple: false
      },
      {
        label: 'Class',
        locale: { en: 'Class', sv: 'Klass' },
        value: 'clazz',
        key: 'clazz:',
        code: 'tx',
        multiple: false
      },
      {
        label: 'Order',
        locale: { en: 'Order', sv: 'Ordning' },
        value: 'order',
        key: 'order:',
        code: 'tx',
        multiple: false
      },
      {
        label: 'Family',
        locale: { en: 'Family', sv: 'Familj' },
        value: 'family',
        key: 'family:',
        code: 'tx',
        multiple: false
      },
      {
        label: 'Genus',
        locale: { en: 'Genus', sv: 'Släkte' },
        value: 'genus',
        key: 'genus:',
        code: 'tx',
        multiple: false
      },
      {
        label: 'Subgenus',
        locale: { en: 'Subgenus', sv: 'Undersläkte' },
        value: 'subgenus',
        key: 'subgenus:',
        code: 'tx',
        multiple: false
      },
      {
        label: 'Species',
        locale: { en: 'Species', sv: 'Art' },
        value: 'species',
        key: 'species:',
        code: 'tx',
        multiple: false
      },
      {
        label: 'Common name',
        locale: { en: 'Common name', sv: 'Svenskt namn' },
        value: 'vernacularName',
        key: 'vernacularName:',
        code: 'tx',
        multiple: false
      },
      {
        label: 'Synonyms',
        locale: { en: 'Synonyms', sv: 'Synonymer' },
        value: 'synonyms',
        key: 'synonyms:',
        code: 'tx',
        multiple: false
      },
      {
        label: 'Taxon rank',
        locale: { en: 'Taxon rank', sv: 'Taxonrang' },
        value: 'taxonRank',
        key: 'taxonRank:',
        code: 'tx',
        multiple: false
      }
    ]
  },
  {
    label: {
      en: 'Specimen',
      sv: 'Föremål'
    },
    code: 'specimen',
    items: [
      {
        label: 'Catalog number',
        locale: { en: 'Catalog number', sv: 'Katalognummer' },
        value: 'catalogNumber',
        key: 'catalogNumber:',
        code: 'specimen',
        multiple: false
      },
      {
        label: 'Type status',
        locale: { en: 'Type status', sv: 'Typstatus' },
        value: 'typeStatus',
        key: 'typeStatus:',
        code: 'specimen',
        multiple: false
      }
    ]
  },
  {
    label: {
      en: 'Collecting event',
      sv: 'Insamlingstillfälle'
    },
    code: 'event',
    items: [
      {
        label: 'Collected by',
        locale: { en: 'Collected by', sv: 'Insamlare' },
        value: 'collectors',
        key: 'collectors:',
        code: 'event',
        multiple: false
      }
    ]
  },
  {
    label: {
      en: 'Locality',
      sv: 'Lokal'
    },
    code: 'locality',
    items: [
      {
        label: 'Country',
        locale: { en: 'Country', sv: 'Land' },
        value: 'country',
        key: 'country:',
        code: 'locality',
        multiple: false
      },
      {
        label: 'State/Province',
        locale: { en: 'State/Province', sv: 'Provins' },
        value: 'stateProvince',
        key: 'stateProvince:',
        code: 'locality',
        multiple: false
      },
      {
        label: 'County/Parish',
        locale: { en: 'County/Parish', sv: 'Län' },
        value: 'county',
        key: 'county:',
        code: 'locality',
        multiple: false
      },

      {
        label: 'Locality',
        locale: { en: 'Locality', sv: 'Lokal' },
        value: 'locality',
        key: 'locality:',
        code: 'locality',
        multiple: false
      }
    ]
  },
  {
    label: {
      en: 'Botanical and mycological collections',
      sv: 'Botaniska och mykologiska samlingar'
    },
    code: 'bot',
    items: [
      {
        label: 'Herbarium',
        locale: { en: 'Herbarium', sv: 'Herbarium' },
        value: 'herbarium',
        key: 'herbarium:',
        code: 'bot',
        multiple: false
      }
    ]
  },

  {
    label: {
      en: 'Paleontological collection',
      sv: 'Paleontologisk samling'
    },
    code: 'pal',
    items: [
      {
        label: 'Minimum age',
        locale: { en: 'Minimum age', sv: 'Minsta ålder' },
        value: 'earliestAgeOrLowestStage',
        key: 'earliestAgeOrLowestStage:',
        code: 'pal',
        multiple: false
      },
      {
        label: 'Maximum age',
        locale: { en: 'Maximum age', sv: 'Högsta ålder' },
        value: 'latestAgeOrHighestStage',
        key: 'latestAgeOrHighestStage:',
        code: 'pal',
        multiple: false
      },
      {
        label: 'Minimum period',
        locale: { en: 'Minimum period', sv: 'Minsta period' },
        value: 'earliestPeriodOrLowestSystem',
        key: 'earliestPeriodOrLowestSystem:',
        code: 'pal',
        multiple: false
      },
      {
        label: 'Maximum period',
        locale: { en: 'Maximum period', sv: 'Högsta period' },
        value: 'latestPeriodOrHighestSystem',
        key: 'latestPeriodOrHighestSystem:',
        code: 'pal',
        multiple: false
      },

      {
        label: 'Minimum epoch',
        locale: { en: 'Minimum epoch', sv: 'Minsta epok' },
        value: 'earliestEpochOrLowestSeries',
        key: 'earliestEpochOrLowestSeries:',
        code: 'pal',
        multiple: false
      },
      {
        label: 'Maximum epoch',
        locale: { en: 'Maximum epoch', sv: 'Högsta epok' },
        value: 'latestEpochOrHighestSeries',
        key: 'latestEpochOrHighestSeries:',
        code: 'pal',
        multiple: false
      }
    ]
  }
])

const displayDivid = computed(() => {
  return selectedItems.value && selectedItems.value.length > 0
})

// const currentLocale = computed(() => locale.value)

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

watch(locale, (newValue) => {
  selectedItems.value = selectedItems.value.map((item) => ({
    ...item,
    label: item.locale[locale.value] || item.locale.en
  }))
})

onMounted(() => {
  const fields = store.getters['fields']
  if (fields) {
    fields.forEach((field) => {
      const code = field.code

      const group = localizedGroups.value.filter((element) => element.code === code)
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
