<template>
  <div class="grid">
    <div class="col-12 search-all-label" no-gutters>
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
      <div class="flex flex flex-wrap gap-3 radio-group">
        <div class="flex items-center radio-item" @keydown.enter="toggleSearchOption('equals')">
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
        <div class="flex items-center radio-item" @keydown.enter="toggleSearchOption('contains')">
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
        <div class="flex items-center radio-item" @keydown.enter="toggleSearchOption('startsWith')">
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

function toggleSearchOption(value) {
  searchOptions.value = value
}

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
  min-width: 60%;
  text-align: left;
  float: left;
}

.search-all-label {
  font-size: 1rem;
  font-weight: 500;
  color: var(--text-color, #333);
  display: flex;
  align-items: center;
  flex-wrap: wrap; /* allows wrapping on small screens */
  gap: 0.25rem; /* adds small space between text and [searchAll] */
}

.search-all-label small {
  font-size: 0.85rem;
  color: var(--text-color-secondary, #6c757d);
}

/* Small label text */
.radio-item small {
  font-size: 0.85rem;
}

/* 📱 Mobile: slightly larger and with better spacing */
@media (max-width: 768px) {
  .search-all-label {
    font-size: 0.8rem;
    line-height: 1.4;
    flex-direction: column; /* stack main text and small text */
    align-items: flex-start;
    gap: 0.1rem;
  }

  .search-all-label small {
    font-size: 0.6rem;
  }

  .searchInput {
    min-width: 100%;
    text-align: left;
    float: left;
  }

  .radio-group {
    gap: 0.5rem; /* slightly smaller spacing */
  }

  .radio-item {
    min-width: 100%; /* stack vertically on small screens */
    justify-content: flex-start;
  }

  .radio-item small {
    font-size: 0.6rem; /* slightly larger for readability */
  }
}
</style>
