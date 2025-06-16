<template>
  <div class="grid">
    <InputGroup>
      <InputText
        id="freeTextSearch"
        v-model="value"
        @input="onSearch"
        :placeholder="$t('search.searchAllFields')"
        size="small"
        class="w-full"
      />
      <Button icon="pi pi-times" v-if="showClearBtn" @click="clearInputValue" />
    </InputGroup>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const emits = defineEmits(['search'])

const value = ref()
let showClearBtn = ref(false)

onMounted(() => {
  const freeText = store.getters['searchText']
  value.value = freeText == '*' ? null : freeText
})

function onSearch() {
  showClearBtn = value.value

  const searchText = value.value ? value.value : '*'
  store.commit('setSearchText', searchText)
  emits('search')
}

function clearInputValue() {
  showClearBtn = false

  value.value = ''
  store.commit('searchText', null)
  emits('search')
}
</script>
<style scoped></style>
