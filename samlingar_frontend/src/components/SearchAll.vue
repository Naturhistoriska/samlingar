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

// const emits = defineEmits(['search'])

const value = ref()
// let showClearBtn = ref(false)

const showClearBtn = computed(() => {
  return value.value
})

onMounted(() => {
  const freeText = store.getters['searchText']
  value.value = freeText == '*' ? null : freeText
})

function onInput() {
  console.log('onInput')
  showClearBtn = value.value
  // const searchText = value.value ? value.value : '*'
  // store.commit('setSearchText', searchText)
}

function valueChange() {
  console.log('valueChange', value.value)

  const searchText = value.value ? value.value : '*'
  store.commit('setSearchText', searchText)
}

function clearInputValue() {
  // showClearBtn = false

  value.value = ''
  store.commit('searchText', null)
}
</script>
<style scoped></style>
