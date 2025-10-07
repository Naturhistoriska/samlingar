<template>
  <div class="grid">
    <div class="col-12" no-gutters>
      <InputGroup>
        <InputText
          id="freeTextSearch"
          v-model="value"
          @blur="valueChange"
          @keydown.enter="handleEnter"
          :placeholder="$t('search.freeTextSearch')"
          size="small"
          class="w-full"
          v-tooltip.bottom="$t('tip.searchAll')"
        />
        <Button
          icon="pi pi-times"
          v-if="showClearBtn"
          @click="clearInputValue"
          style="background-color: #144836 !important; border-color: #1d634a !important"
        />
      </InputGroup>
    </div>
    <div class="col-12" no-gutters>
      <div class="flex flex flex-wrap gap-3" role="radiogroup" aria-label="Search Options">
        <div class="flex items-center">
          <RadioButton
            v-model="searchOptions"
            inputId="optionEquals"
            name="searchOption"
            value="equals"
            size="small"
            class="mt-1"
            :disabled="checkboxDisabled"
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
            name="searchOption"
            value="contains"
            class="mt-1"
            size="small"
            :disabled="checkboxDisabled"
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
            name="searchOption"
            value="startsWith"
            size="small"
            class="mt-1"
            :disabled="checkboxDisabled"
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
import { computed, onMounted, ref, watch } from 'vue'
import { useStore } from 'vuex'

const emits = defineEmits(['freeTextSearch'])

const store = useStore()

const value = ref()

const searchOptions = ref()

const showClearBtn = computed(() => {
  return value.value
})

watch(
  () => store.getters['searchText'],
  (newValue, oldValue) => {
    value.value = newValue == '*' ? null : newValue
    searchOptions.value = store.getters['fullTextSearchMode']
  }
)

const checkboxDisabled = computed(() => {
  return !value.value
})

onMounted(() => {
  const freeText = store.getters['searchText']
  value.value = freeText == '*' ? null : freeText

  setSearchOption()
})

function setSearchOption() {
  const searchMode = store.getters['fullTextSearchMode']
  searchOptions.value = searchMode ? searchMode : 'contains'
}

function change() {
  store.commit('setFullTextSearchMode', searchOptions.value)
  handleEnter()
}

function handleEnter() {
  const searchText = value.value ? value.value : '*'
  store.commit('setSearchText', searchText)
  emits('freeTextSearch', searchText)
}

function valueChange() {
  const searchText = value.value ? value.value.trim() : '*'
  store.commit('setSearchText', searchText)
}

function clearInputValue() {
  value.value = ''
  store.commit('setSearchText', null)
  // store.commit('setFullTextSearchMode', 'contains')
}
</script>
<style scoped></style>
