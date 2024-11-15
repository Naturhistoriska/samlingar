<template>
  <div class="card" style="min-width: 920px; align-items: center">
    <Fieldset style="background: transparent">
      <template #legend>
        <div
          class="flex items-center pl-2"
          style="background: transparent; border: 0px transparent"
        >
          <span class="font-bold p-2" style="font-size: 18px">{{
            $t('search.advanceSearch')
          }}</span>
        </div>
      </template>

      <div class="grid" id="scientificNameDiv">
        <div class="col-8">
          <div class="flex flex-col gap-2 selectGroup">
            <label class="searchLabel" for="searchScientificName">
              {{ $t('search.scientificName') }}
            </label>
            <InputGroup>
              <InputText
                id="searchScientificName"
                v-model="scientificName"
                @input="onInputScientificName"
                :placeholder="$t('search.searchScientificName')"
                class="w-11 md:w-56"
              />
              <Button
                icon="pi pi-times"
                v-if="showClearScentificName"
                @click="clearScientificName"
              />
            </InputGroup>
          </div>
        </div>
        <div class="col-4">
          <div class="flex flex flex-wrap justify-center gap-3" style="padding-top: 6px">
            <div class="flex items-center vertical-align-middle">
              <RadioButton
                v-model="taxonOptions"
                inputId="taxonOption1"
                name="option1"
                value="exact"
                class="mt-1"
              />
              <label for="taxonOption1" class="ml-2">{{ $t('search.exact') }}</label>
            </div>
            <div class="flex items-center">
              <RadioButton
                v-model="taxonOptions"
                inputId="taxonOption2"
                name="option2"
                value="contains"
                class="mt-1"
              />
              <label for="taxonOption2" class="ml-2">{{ $t('search.contains') }}</label>
            </div>
            <div class="flex items-center">
              <RadioButton
                v-model="taxonOptions"
                inputId="taxonOption3"
                name="option3"
                value="startsWith"
                class="mt-1"
              />
              <label for="taxonOption3" class="ml-2">{{ $t('search.startsWith') }}</label>
            </div>
          </div>
        </div>
      </div>

      <div class="grid" id="catalogNumberDiv">
        <div class="col-8">
          <div class="flex flex-col gap-2 selectGroup">
            <label class="searchLabel" for="catalogNumber">
              {{ $t('search.catalogNumber') }}
            </label>
            <InputGroup>
              <InputText
                id="catalogNumber"
                v-model="catalogNumber"
                @input="onInputCatalogNumber"
                :placeholder="$t('search.searchCatalogNumber')"
                class="w-11 md:w-56"
              />
              <Button
                icon="pi pi-times"
                v-if="showClearCatalogNumber"
                @click="clearCatalogNumber"
              />
            </InputGroup>
          </div>
        </div>
        <div class="col-4">
          <div class="flex flex flex-wrap justify-center gap-3" style="padding-top: 6px">
            <div class="flex items-center vertical-align-middle">
              <RadioButton
                v-model="catlogNumOptions"
                inputId="catOption1"
                name="option1"
                value="exact"
                class="mt-1"
              />
              <label for="catOption1" class="ml-2">{{ $t('search.exact') }}</label>
            </div>
            <div class="flex items-center">
              <RadioButton
                v-model="catlogNumOptions"
                inputId="catOption2"
                name="option2"
                value="contains"
                class="mt-1"
              />
              <label for="catOption2" class="ml-2">{{ $t('search.contains') }}</label>
            </div>
            <div class="flex items-center">
              <RadioButton
                v-model="catlogNumOptions"
                inputId="catOption3"
                name="option3"
                value="startsWith"
                class="mt-1"
              />
              <label for="catOption3" class="ml-2">{{ $t('search.startsWith') }}</label>
            </div>
          </div>
        </div>
      </div>

      <div class="grid" id="synonymDiv">
        <div class="col-8">
          <div class="flex flex-col gap-2 selectGroup">
            <label class="searchLabel" for="synonym">
              {{ $t('search.synonym') }}
            </label>
            <InputGroup>
              <InputText
                id="synonym"
                v-model="synonym"
                @input="onInputSynonym"
                :placeholder="$t('search.searchSynonym')"
                class="w-11 md:w-56"
              />
              <Button icon="pi pi-times" v-if="showClearSynonym" @click="clearSynonym" />
            </InputGroup>
          </div>
        </div>
        <div class="col-4">
          <div class="flex flex flex-wrap justify-center gap-3" style="padding-top: 6px">
            <div class="flex items-center vertical-align-middle">
              <RadioButton
                v-model="synonymOptions"
                inputId="synOption1"
                name="option1"
                value="exact"
                class="mt-1"
              />
              <label for="synOption1" class="ml-2">{{ $t('search.exact') }}</label>
            </div>
            <div class="flex items-center">
              <RadioButton
                v-model="synonymOptions"
                inputId="synOption2"
                name="option2"
                value="contains"
                class="mt-1"
              />
              <label for="synOption2" class="ml-2">{{ $t('search.contains') }}</label>
            </div>
            <div class="flex items-center">
              <RadioButton
                v-model="synonymOptions"
                inputId="synOption3"
                name="option3"
                value="startsWith"
                class="mt-1"
              />
              <label for="synOption3" class="ml-2">{{ $t('search.startsWith') }}</label>
            </div>
          </div>
        </div>
      </div>

      <!-- <div class="card flex justify-center selectGroup">
        <label class="searchLabel">{{ $t('search.speciesGroup') }}</label>
        <Select
          v-model="selectedGroup"
          inputId="speciesGroup"
          :highlightOnSelect="true"
          :options="speciesGroup"
          :placeholder="$t('search.selectSpeciesGroup')"
          class="w-full md:w-56"
          checkmark
          showClear
          style="margin-left: 7px"
        />
      </div> -->
      <!-- <div class="card flex justify-center selectGroup">
        <label class="searchLabel">
          {{ $t('search.datasetName') }}
        </label>
        <Select
          id="datasetName"
          v-model="selectedDataset"
          :options="datasetName"
          :placeholder="$t('search.selectDatasetName')"
          checkmark
          :highlightOnSelect="true"
          class="w-full md:w-56"
          showClear
          style="margin-left: 7px"
        />
      </div> -->
      <div class="card flex justify-center selectGroup">
        <label class="searchLabel">
          {{ $t('search.datasetName') }}
        </label>
        <Select
          id="groupDataset"
          v-model="selectedGroupDataset"
          :options="groupDataset"
          optionLabel="label"
          optionGroupLabel="label"
          optionGroupChildren="items"
          :placeholder="$t('search.selectDatasetName')"
          checkmark
          :highlightOnSelect="true"
          class="w-full md:w-56"
          showClear
          style="margin-left: 7px"
        />
      </div>
      <div class="grid" id="dates">
        <div class="col-10">
          <div class="grid">
            <div class="col-6">
              <div class="flex flex-col gap-2 selectGroup">
                <label class="searchLabel">
                  {{ $t('search.beginDateLabel') }}
                </label>
                <DatePicker
                  v-model="beginDate"
                  dateFormat="yy-m-dd"
                  showIcon
                  fluid
                  inputId="startDate"
                  :placeholder="$t('search.beginDateLabel')"
                />
              </div>
            </div>
            <div class="col-6">
              <div class="flex flex-col gap-2 selectGroup">
                <label class="searchLabel">
                  {{ $t('search.endDateLabel') }}
                </label>
                <DatePicker
                  v-model="endDate"
                  dateFormat="yy-m-dd"
                  showIcon
                  fluid
                  inputId="endDate"
                  :placeholder="$t('search.endDateLabel')"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="card flex justify-center selectGroup">
        <label class="searchLabel">
          {{ $t('search.typeStatus') }}
        </label>
        <MultiSelect
          id="typestatus"
          v-model="selectedTypes"
          display="chip"
          :options="types"
          optionLabel="label"
          optionValue="label"
          filter
          placeholder="Select type status"
          :maxSelectedLabels="3"
          class="w-full md:w-56"
          key="label"
          :selectAll="selectAll"
          optionDisabled="disable"
          @selectall-change="onSelectAllChange($event)"
          @change="onChange($event)"
        >
          <template #header>
            <div class="font-medium p-3">Available Typestatus</div>
          </template>
          <template #filtericon>
            <i class="pi pi-filter" />
          </template>
        </MultiSelect>
      </div>

      <div
        id="btnDiv"
        class="flex flex-col gap-2"
        style="min-width: 20%; text-align: right; float: right; margin-top: 5em"
      >
        <Button :label="$t('search.search')" @click="search" :loading="loading" />
        <Button :label="$t('common.clearAll')" @click="clearAll" />
      </div>
    </Fieldset>
  </div>
</template>
<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useStore } from 'vuex'
import DatePicker from 'primevue/datepicker'
import Fieldset from 'primevue/fieldset'
// import TypeStatus from './TypeStatus.vue'
import MultiSelect from 'primevue/multiselect'
import moment from 'moment'

import Service from '../Service'
const service = new Service()

const store = useStore()
const emits = defineEmits(['advanceSearch'])

let beginDate = ref()
let catalogNumber = ref()
let endDate = ref()
let scientificName = ref()
let synonym = ref()

// let selectedGroup = ref()
// let selectedDataset = ref()
let selectedGroupDataset = ref()

let showClearScentificName = ref(false)
let showClearCatalogNumber = ref(false)
let showClearSynonym = ref(false)

let loading = ref(false)
let catlogNumOptions = ref()
let taxonOptions = ref()
let synonymOptions = ref()

let selectedTypes = ref()
let types = ref([])
let selectAll = ref(false)
let disableSelect = ref(false)

// const speciesGroup = computed(() => {
//   return import.meta.env.VITE_SUPPORTED_COLLECTIONS.split(',')
// })
// const datasetName = computed(() => {
//   return import.meta.env.VITE_SUPPORTED_DATASET.split(':')
// })

const groupDataset = ref([
  {
    label: 'Zoological Collection',
    items: [
      { label: 'Amphibians and reptiles', value: 'herps' },
      { label: 'Bird', value: 'va' },
      { label: 'Fish', value: 'fish' },
      { label: 'Invertebrate main collection', value: 'ev' },
      { label: 'Invertebrate type collection', value: 'et' },
      { label: 'Mammals', value: 'ma' },
      { label: 'NRM Entomology Collection Objects', value: '163840' },
      { label: 'Swedish Malaise Trap Project (SMTP) Collection Obj', value: '262144' },
      { label: 'Swedish Malaise Trap Project (SMTP) Species Lists', value: '655361' }
    ]
  },
  {
    label: 'Botanical and mycological collections',
    items: [
      { label: 'Algae', value: 'algae' },
      { label: 'Fungi/Lichens', value: 'fungi' },
      { label: 'Mosses', value: 'mosses' },
      { label: 'Vascular Plants', value: 'vp' }
    ]
  },
  {
    label: 'Palaeontological collection',
    items: [
      { label: 'Paleozoology', value: 'pz' },
      { label: 'Paleobotany', value: 'pb' }
    ]
  },
  {
    label: 'Geological collection',
    items: [
      { label: 'NRM Isotope Geology', value: '753664' },
      { label: 'NRM Mineralogy', value: '557057' },
      { label: 'NRM Nodules', value: '786432' }
    ]
  }
])

// watch(
//   () => scientificName.value,
//   (newValue, oldValue) => {
//     scientificName.value = newValue
//   }
// )

onMounted(() => {
  console.log('advance search mounted')

  service
    .apiSearchTypeStatus()
    .then((response) => {
      const facets = response.facets.typeStatus
      if (facets) {
        types.value = facets.buckets.map((a) => ({ label: a.val, count: a.count, disable: false }))
      }
    })
    .catch()
    .finally(() => {})
})

function onInputScientificName() {
  showClearScentificName = scientificName.value
  // showClearScentificName =
  //   scientificName === undefined || scientificName.value === '' ? false : true
}

function onInputCatalogNumber() {
  showClearCatalogNumber = catalogNumber.value
  // showClearCatalogNumber = catalogNumber === undefined || catalogNumber.value === '' ? false : true
}

function onInputSynonym() {
  showClearSynonym = synonym.value
}

function clearCatalogNumber() {
  catalogNumber.value = ''
  showClearCatalogNumber = false
  catlogNumOptions.value = null
}

function clearScientificName() {
  scientificName.value = ''
  showClearScentificName = false
  taxonOptions.value = null
}

function clearSynonym() {
  synonym.value = ''
  showClearSynonym = false
  synonymOptions.value = null
}

function search() {
  loading.value = true

  if (scientificName.value) {
    const option = taxonOptions.value
    let searchTaxon
    if (option === 'exact') {
      searchTaxon = `%2Btx:"${scientificName.value}"`
    } else if (option === 'startsWith') {
      searchTaxon = `%2B(tx:${scientificName.value}* tx:${scientificName.value})`
    } else {
      // '%2B(text:' + value.value + '*' + ' text:"' + value.value + '")'
      searchTaxon = `%2B(tx:*${scientificName.value}* tx:"${scientificName.value}")`
    }
    if (searchTaxon) {
      store.commit('setScientificName', searchTaxon)
    }
  } else {
    store.commit('setScientificName', null)
  }

  if (catalogNumber.value) {
    const option = catlogNumOptions.value
    let searchCatalogNumber
    if (option === 'exact') {
      searchCatalogNumber = `%2Bcn:"${catalogNumber.value}"`
    } else if (option === 'startsWith') {
      searchCatalogNumber = `%2B(cn:${catalogNumber.value}* cn:${catalogNumber.value})`
    } else {
      // '%2B(text:' + value.value + '*' + ' text:"' + value.value + '")'
      searchCatalogNumber = `%2B(cn:*${catalogNumber.value}* cn:${catalogNumber.value})`
    }
    if (searchCatalogNumber) {
      store.commit('setCatalogNumber', searchCatalogNumber)
    }
    console.log('catgalogNumber', searchCatalogNumber)
  } else {
    store.commit('setCatalogNumber', null)
  }

  if (synonym.value) {
    const option = synonymOptions.value
    let searchSynonym
    if (option === 'exact') {
      searchSynonym = `%2Bsynonym:"${synonym.value}"`
    } else if (option === 'startsWith') {
      searchSynonym = `%2B(synonym:${synonym.value}* synonym:${synonym.value})`
    } else {
      // '%2B(text:' + value.value + '*' + ' text:"' + value.value + '")'
      searchSynonym = `%2B(synonym:*${synonym.value}* synonym:"${synonym.value}")`
    }
    if (searchSynonym) {
      store.commit('setSynonym', searchSynonym)
    }
  } else {
    store.commit('setSynonym', null)
  }

  if (selectedGroupDataset.value) {
    console.log('selectedGroupDataset', selectedGroupDataset.value.value)
    const collectionCode = `%2BcollectionId:${selectedGroupDataset.value.value}`
    store.commit('setSelectedDataset', collectionCode)
  } else {
    store.commit('setSelectedDataset', null)
  }

  // startDate:[2019-11-12T00:00:00Z TO 2022-11-16T00:00:00Z]
  if (beginDate.value && endDate.value) {
    const startDate = moment(beginDate.value).format('yyyy-MM-DDT00:00:00')
    const finishDate = moment(endDate.value).format('yyyy-MM-DDT00:00:00')
    const dateSearch = `%2BstartDate:[${startDate}Z TO ${finishDate}Z]`
    store.commit('setDateRange', dateSearch)
  } else if (beginDate.value) {
    const startDate = moment(beginDate.value).format('yyyy-MM-DDT00:00:00')
    const finishDate = '*'
    const dateSearch = `%2BstartDate:[${startDate}Z TO ${finishDate}]`
    store.commit('setDateRange', dateSearch)
  } else if (endDate.value) {
    const startDate = '*'
    const finishDate = moment(endDate.value).format('yyyy-MM-DDT00:00:00')
    const dateSearch = `%2BstartDate:[${startDate} TO ${finishDate}Z]`
    store.commit('setDateRange', dateSearch)
  } else {
    store.commit('setDateRange', null)
  }

  if (selectedTypes.value) {
    let searchTypes
    if (selectAll.value) {
      searchTypes = '%2BisType:*'
    } else {
      const typestatus = selectedTypes.value.join(' ')
      searchTypes = `%2BtypeStatus:(${typestatus})`
    }
    store.commit('setSelectedTypes', searchTypes)
  } else {
    store.commit('setSelectedTypes', null)
  }

  emits('advanceSearch')

  setTimeout(() => {
    loading.value = false
  }, 2000)
}

function onChange(event) {
  selectAll.value = event.value !== null ? event.value.length === types.value.length : false
  // selectedTypes.value = event.checked ? types.value.map((type) => type.label) : []
  if (event.value) {
    if (event.value.length === 3) {
      types.value.forEach((type) => {
        type.disable = true
      })
      disableSelect = true
    } else {
      if (disableSelect) {
        types.value.forEach((type) => {
          type.disable = false
        })
        disableSelect = false
      }
    }
  }
  // store.commit('setSelectedTypes', types.value)
}

function onSelectAllChange(event) {
  console.log('event 1', event.value)
  selectedTypes.value = event.checked ? types.value.map((type) => type.label) : []
  selectAll.value = event.checked

  if (disableSelect) {
    disableSelect = false
    types.value.forEach((type) => {
      type.disable = false
    })
  }
  // if (selectAll.value) {
  //   store.commit('setIsType', true)
  // }
}

function clearAll() {
  console.log('clear all....')

  catalogNumber.value = ''
  scientificName.value = ''
  synonym.value = ''
  beginDate.value = ''
  endDate.value = ''

  showClearCatalogNumber = false
  showClearScentificName = false

  // if (selectedDataset !== undefined) {
  //   selectedDataset = undefined
  // }

  // if (selectedGroup !== undefined) {
  //   selectedGroup = undefined
  // }

  if (selectedGroupDataset.value) {
    selectedGroupDataset.value = ''
  }

  if (selectedTypes.value) {
    selectedTypes.value = ''
  }
  // store.commit('setSelectedTypes', null)
}
</script>
<style scoped>
.selectGroup {
  max-width: 770px;
  padding-bottom: 1rem;
}

.searchLabel {
  padding-right: 2rem;
  padding-top: 0.3rem;
  text-align: right;
  min-width: 150px;
}

.p-multiselect {
  width: 18rem;
  background: #000 !important;
}

.p-multiselect-label:not(.p-placeholder) {
  padding-top: 0.25rem;
  padding-bottom: 0.25rem;
  color: #fff !important;
}
.p-select-label:not(.p-placeholder) {
  background: #000 !important;
  color: #fff !important;
}

/* .multiselect-custom .country-item-value {
  padding: 0.25rem 0.5rem;
  border-radius: 3px;
  display: inline-flex;
  margin-right: 0.5rem;
  background-color: var(--primary-color);
  color: var(--primary-color-text);
}

.multiselect-custom .country-item-value img.flag {
  width: 17px;
} */
.p-button-text {
  color: #000 !important;
}

@media screen and (max-width: 640px) {
  .p-multiselect {
    width: 100%;
  }
}
</style>
