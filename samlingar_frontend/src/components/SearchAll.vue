<template>
  <div class="grid">
    <InputGroup>
      <InputText
        id="freeTextSearch"
        v-model="value"
        @keydown.enter="onSearch"
        :placeholder="$t('search.searchAllFields')"
        size="small"
        class="w-full"
      />
    </InputGroup>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const emits = defineEmits(['search'])

const value = ref()

onMounted(() => {
  const freeText = store.getters['searchText']
  value.value = freeText == '*' ? null : freeText
})

function onSearch() {
  const searchText = value.value ? value.value : '*'
  store.commit('setSearchText', searchText)
  emits('search')
}
</script>
<style scoped></style>
