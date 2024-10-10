<template>
  <div class="card" style="min-width: 920px; align-items: center">
    <Fieldset>
      <template #legend>
        <div class="flex items-center pl-2">
          <span class="font-bold p-2">{{ $t('search.advanceSearch') }}</span>
        </div>
      </template>
      <div class="flex flex-col gap-2 selectGroup">
        <label class="searchLabel">
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
          <Button icon="pi pi-times" v-if="showClearScentificName" @click="clearScientificName" />
        </InputGroup>
      </div>
      <div class="card flex justify-center selectGroup">
        <label class="searchLabel">{{ $t('search.speciesGroup') }}</label>
        <Select
          v-model="selectedGroup"
          id="speciesGroup"
          :highlightOnSelect="true"
          :options="speciesGroup"
          :placeholder="$t('search.selectSpeciesGroup')"
          class="w-full md:w-56"
          checkmark
          showClear
          style="margin-left: 7px"
        />
      </div>
      <div class="card flex justify-center selectGroup">
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
      </div>

      <div class="flex flex-col gap-2 selectGroup">
        <label class="searchLabel">
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
          <Button icon="pi pi-times" v-if="showClearCatalogNumber" @click="clearCatalogNumber" />
        </InputGroup>
      </div>
      <div class="flex flex-col gap-2 selectGroup">
        <label for="beginDate" class="searchLabel">
          {{ $t('search.beginDateLabel') }}
        </label>
        <InputGroup>
          <InputText
            id="beginDate"
            v-model="beginDate"
            :placeholder="$t('search.beginDate')"
            @input="onInputBeginDate"
            class="w-11 md:w-56"
          />

          <Button icon="pi pi-times" v-if="showClearBeginDate" @click="clearBeginDate" />
        </InputGroup>
      </div>
      <div class="flex flex-col gap-2 selectGroup">
        <label for="endDate" class="searchLabel">
          {{ $t('search.endDateLabel') }}
        </label>
        <InputGroup>
          <InputText
            id="endDate"
            v-model="endDate"
            :placeholder="$t('search.endDate')"
            @input="onInputEndDate"
            class="w-11 md:w-56"
          />
          <Button icon="pi pi-times" v-if="showClearEndDate" @click="clearEndDate" />
        </InputGroup>
      </div>
      <div class="flex flex-col gap-2 selectGroup">
        <label for="endDate" class="searchLabel">
          {{ $t('search.typeStatus') }}
        </label>
        <Checkbox v-model="isType" :binary="true" />
      </div>
      <div
        id="btnDiv"
        class="flex flex-col gap-2"
        style="min-width: 50%; text-align: right; float: right"
      >
        <Button :label="$t('search.search')" @click="search" />
        <Button :label="$t('common.clearAll')" @click="clearAll" />
      </div>
    </Fieldset>
  </div>
</template>
<script setup>
import { computed, ref, watch } from 'vue'
import { useStore } from 'vuex'
import Fieldset from 'primevue/fieldset'

const store = useStore()
const emits = defineEmits(['advanceSearch'])

let beginDate = ref()
let catalogNumber = ref()
let endDate = ref()
let isType = ref(false)
let scientificName = ref()
let selectedGroup = ref()
let selectedDataset = ref()

let showClearScentificName = ref(false)
let showClearCatalogNumber = ref(false)
let showClearBeginDate = ref(false)
let showClearEndDate = ref(false)

const speciesGroup = computed(() => {
  return import.meta.env.VITE_SUPPORTED_COLLECTIONS.split(',')
})
const datasetName = computed(() => {
  return import.meta.env.VITE_SUPPORTED_DATASET.split(':')
})

// watch(
//   () => scientificName.value,
//   (newValue, oldValue) => {
//     scientificName.value = newValue
//   }
// )

function onInputScientificName() {
  showClearScentificName =
    scientificName === undefined || scientificName.value === '' ? false : true
}

function onInputCatalogNumber() {
  showClearCatalogNumber = catalogNumber === undefined || catalogNumber.value === '' ? false : true
}

function onInputBeginDate() {
  showClearBeginDate = beginDate === undefined || beginDate.value === '' ? false : true
}

function onInputEndDate() {
  showClearEndDate = endDate === undefined || endDate.value === '' ? false : true
}

function clearCatalogNumber() {
  catalogNumber.value = ''
  showClearCatalogNumber = false
}

function clearScientificName() {
  scientificName.value = ''
  showClearScentificName = false
}

function clearBeginDate() {
  beginDate.value = ''
  showClearBeginDate = false
}

function clearEndDate() {
  endDate.value = ''
  showClearEndDate = false
}

function search() {
  store.commit('setCatalogNumber', catalogNumber)
  store.commit('setSpeciesGroup', selectedGroup)
  store.commit('setDataset', selectedDataset)
  store.commit('setStartDate', beginDate)
  store.commit('setEndDate', endDate)
  store.commit('setScientificName', scientificName)
  store.commit('setIsType', isType)

  emits('advanceSearch')
}

function clearAll() {
  console.log('clear all....')

  catalogNumber.value = ''
  scientificName.value = ''
  beginDate.value = ''
  endDate.value = ''

  showClearBeginDate = false
  showClearCatalogNumber = false
  showClearEndDate = false
  showClearScentificName = false

  if (selectedDataset !== undefined) {
    selectedDataset.value = ''
    selectedDataset = undefined
  }

  if (selectedGroup !== undefined) {
    selectedGroup.value = ''
    selectedGroup = undefined
  }
}
</script>
<style scoped>
.selectGroup {
  max-width: 770px;
  padding-bottom: 1em;
}

.searchLabel {
  padding-right: 2em;
  text-align: right;
  min-width: 150px;
}
</style>
