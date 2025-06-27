<template>
  <div style="min-width: 100%; max-height: 200px; overflow-y: scroll">
    <template v-for="(field, index) in fields" :key="field">
      <!-- <input-box v-bind:field="field" @search="handleSearch" /> -->
      <autocomplete-box v-bind:multiple="true" v-bind:field="field" @search="handleSearch" />
    </template>
  </div>
</template>
<script setup>
import { ref, watch } from 'vue'
import { useStore } from 'vuex'
import AutocompleteBox from './baseComponents/AutocompleteBox.vue'
import InputBox from './baseComponents/InputBox.vue'
const emits = defineEmits(['search'])

const store = useStore()

const fields = ref([])

watch(
  () => store.getters['fields'],
  (newValue, oldValue) => {
    console.log('value...', newValue)
    fields.value = newValue
  }
)

function handleSearch() {
  emits('search')
}
</script>
<style scoped></style>
