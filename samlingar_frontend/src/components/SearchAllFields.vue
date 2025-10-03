<template>
  <div class="grid">
    <div class="col-12" no-gutters>
      {{ $t('search.freeTextSearch') }}
      <small class="p-text-secondary">[{{ $t('search.searchAll') }}]</small>
    </div>
    <div class="flex flex-col gap-2" style="width: 100%">
      <div class="flex-col gap-2 searchInput">
        <input-with-icon
          @doAction="handleFreeTextSearch"
          v-bind:placehold="$t('search.searchAll')"
          v-bind:size="'small'"
          v-bind:icon="'pi pi-search'"
          v-bind:tip="'tip.searchAll'"
        />
      </div>
    </div>
    <div class="col-12" no-gutters>
      <div class="flex flex flex-wrap gap-3">
        <div class="flex items-center">
          <RadioButton
            v-model="searchOptions"
            inputId="optionEquals"
            name="option1"
            value="equals"
            size="small"
            class="mt-1"
            @value-change="change"
          />
          <label for="optionEquals" class="ml-2">
            <small>{{ $t('search.exact') }}</small>
          </label>
        </div>
        <div class="flex items-center">
          <RadioButton
            v-model="searchOptions"
            inputId="optionContains"
            name="option2"
            value="contains"
            class="mt-1"
            size="small"
            @value-change="change"
          />
          <label for="optionContains" class="ml-2">
            <small>{{ $t('search.contains') }}</small>
          </label>
        </div>
        <div class="flex items-center">
          <RadioButton
            v-model="searchOptions"
            inputId="optionStartWith"
            name="option3"
            value="startsWith"
            size="small"
            class="mt-1"
            @value-change="change"
          />
          <label for="optionStartWith" class="ml-2">
            <small>{{ $t('search.startsWith') }}</small>
          </label>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
import InputWithIcon from './baseComponents/InputWithIcon.vue'

const emits = defineEmits(['freeTextSearch'])

const store = useStore()

let searchOptions = ref('contains')

function change() {
  // console.log('change', searchOptions.value)
}

function handleFreeTextSearch(value) {
  const searchText = value ? value.trim() : '*'
  store.commit('setSearchText', searchText)
  store.commit('setFullTextSearchMode', searchOptions.value)

  store.commit('setScientificName', null)

  store.commit('setFilterCoordinates', false)
  store.commit('setFilterInSweden', false)
  store.commit('setFilterImage', false)
  store.commit('setFilterType', false)

  store.commit('setCollectionGroup', null)
  store.commit('setFields', [])
  store.commit('setDataResource', null)
  store.commit('setDates', null)

  store.commit('setStartYear', null)
  store.commit('setEndYear', null)
  store.commit('setDateFilter', 'date')

  store.commit('setSearchParams', null)

  emits('freeTextSearch', searchText, searchOptions.value)
}
</script>
<style scoped>
.searchInput {
  min-width: 90%;
  text-align: left;
  float: left;
}
</style>
