<template>
  <div class="grid">
    <InputGroup>
      <InputText
        id="freeTextSearch"
        v-model="value"
        @blur="valueChange"
        @keydown.enter="handleEnter"
        :placeholder="$t('search.searchAllFields')"
        size="small"
        class="w-full"
      />
      <Button icon="pi pi-times" v-if="showClearBtn" @click="clearInputValue" />
    </InputGroup>
  </div>
</template>
<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useStore } from 'vuex'

const emits = defineEmits(['freeTextSearch'])

const store = useStore()

const value = ref()

const showClearBtn = computed(() => {
  return value.value
})

watch(
  () => store.getters['setSearchText'],
  (newValue, oldValue) => {
    value.value = newValue == '*' ? null : freeText
  }
)

onMounted(() => {
  const freeText = store.getters['searchText']
  value.value = freeText == '*' ? null : freeText
})

function handleEnter() {
  const searchText = value.value ? value.value : '*'
  store.commit('setSearchText', searchText)
  emits('freeTextSearch', searchText)
}

function valueChange() {
  const searchText = value.value ? value.value : '*'
  store.commit('setSearchText', searchText)
}

function clearInputValue() {
  value.value = ''
  store.commit('setSearchText', null)
}
</script>
<style scoped></style>
