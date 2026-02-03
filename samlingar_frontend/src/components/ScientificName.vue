<template>
  <div class="grid" id="scientificNameDiv">
    <div class="col-12" no-gutters>
      <InputGroup>
        <InputText
          id="searchScientificName"
          v-model="scientificName"
          @input="onInputScientificName"
          @keydown.enter="handleEnter"
          @blur="handleBlur"
          :placeholder="$t('search.filterScientificname')"
          size="small"
          class="w-full"
        />
        <Button
          icon="pi pi-times"
          v-if="showClearScentificName"
          @click="clearScientificName"
          style="background-color: #144836 !important; border-color: #1d634a !important"
        />
      </InputGroup>
    </div>

    <div class="col-12">
      <div
        class="flex flex flex-wrap gap-3"
        style="margin-top: -9px"
        role="radiogroup"
        aria-label="Search Options"
      >
        <div class="flex items-center">
          <RadioButton
            v-model="searchOptions"
            inputId="searchOption1"
            name="searchScientificOptions"
            value="equals"
            size="small"
            class="mt-1"
            :disabled="checkboxDisabled"
            @value-change="change"
          />
          <label for="searchOption1" class="ml-2">
            <small>{{ $t('search.exact') }}</small>
          </label>
        </div>
        <div class="flex items-center">
          <RadioButton
            v-model="searchOptions"
            inputId="searchOptions2"
            name="searchScientificOptions"
            value="contains"
            class="mt-1"
            size="small"
            :disabled="checkboxDisabled"
            @value-change="change"
          />
          <label for="searchOptions2" class="ml-2">
            <small>{{ $t('search.contains') }}</small>
          </label>
        </div>
        <div class="flex items-center">
          <RadioButton
            v-model="searchOptions"
            inputId="searchOptions3"
            name="searchScientificOptions"
            value="startsWith"
            size="small"
            class="mt-1"
            :disabled="checkboxDisabled"
            @value-change="change"
          />
          <label for="searchOptions3" class="ml-2">
            <small>{{ $t('search.startsWith') }}</small>
          </label>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useStore } from 'vuex'

import Button from 'primevue/button'

const store = useStore()

const emits = defineEmits(['scientificNameSearch'])

const scientificName = ref()
let searchOptions = ref()
let showClearScentificName = ref(false)

watch(
  () => store.getters['scientificName'],
  (newValue, oldValue) => {
    scientificName.value = newValue
    setSearchOption()
  }
)

watch(
  () => store.getters['searchMode'],
  (newValue, oldValue) => {
    setSearchOption()
  }
)

const checkboxDisabled = computed(() => {
  return !scientificName.value
})

onMounted(() => {
  scientificName.value = store.getters['scientificName']
  setSearchOption()
})

function setSearchOption() {
  const searchMode = store.getters['searchMode']
  searchOptions.value = searchMode ? searchMode : 'contains'
  if (scientificName.value) {
    showClearScentificName = true
  }
}

function change() {
  handleEnter()
}

function handleBlur() {
  store.commit('setScientificName', scientificName.value)
  store.commit('setSearchMode', searchOptions.value)
}

function onInputScientificName() {
  showClearScentificName = scientificName.value
}

function clearScientificName() {
  scientificName.value = ''
  searchOptions.value = null
  search(null, null)
}

function handleEnter() {
  search(scientificName.value, searchOptions.value)
}

function search(scientificName, searchMode) {
  store.commit('setScientificName', scientificName)
  store.commit('setSearchMode', searchMode)
  emits('scientificNameSearch')
}
</script>
<style scoped>
.selectGroup {
  max-width: 770px;
  padding-bottom: 1rem;
}

/* .searchLabel {
  padding-right: 2rem;
  padding-top: 0.3rem;
  text-align: right;
  min-width: 150px;
} */
</style>
