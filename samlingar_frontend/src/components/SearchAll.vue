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

onMounted(() => {
  value.value = store.getters['searchText']
})

function onSearch() {
  const searchText = value.value ? value.value : '*'
  store.commit('setSearchText', searchText)
}
</script>
<style scoped></style>
