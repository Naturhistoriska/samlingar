<template>
  <div class="grid">
    <InputGroup>
      <InputText
        id="freeTextSearch"
        v-model="value"
        @blur="valueChange"
        :placeholder="$t('search.searchAllFields')"
        size="small"
        class="w-full"
      />
      <Button icon="pi pi-times" v-if="showClearBtn" @click="clearInputValue" />
    </InputGroup>
  </div>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const value = ref()

const showClearBtn = computed(() => {
  return value.value
})

onMounted(() => {
  const freeText = store.getters['searchText']
  value.value = freeText == '*' ? null : freeText
})

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
