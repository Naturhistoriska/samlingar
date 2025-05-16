<template>
  <div class="grid">
    <InputGroup>
      <InputText
        id="freeTextSearch"
        v-model="value"
        @keydown.enter="onSearch"
        :placeholder="$t('search.searchAllFields')"
        class="w-full"
      />
    </InputGroup>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const emits = defineEmits(['freeTextSearch'])

const value = ref()

// function onChange() {
//   console.log('onChange....', value.value)
// }

onMounted(() => {
  value.value = store.getters['searchText']
})

function onSearch() {
  let searchText = '*'
  if (value.value) {
    searchText = value.value
  }
  console.log('onSearch', searchText)
  store.commit('setSearchText', searchText)
  emits('freeTextSearch', searchText, 0, 10)
}
</script>
<style scoped></style>
