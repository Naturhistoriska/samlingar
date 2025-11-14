<template>
  <div style="min-width: 100%; max-height: 200px; overflow-y: auto">
    <div v-for="(field, index) in fields" :key="index" class="field-line">
      <input-box v-if="field.label === 'Synonyms'" :field="field" class="field-line" />
      <autocomplete-box v-else :field="field" class="full-input" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useStore } from 'vuex'
import AutocompleteBox from './baseComponents/AutocompleteBox.vue'
import InputBox from './baseComponents/InputBox.vue'

const store = useStore()
const fields = ref([])

onMounted(() => {
  fields.value = store.getters['fields'] || []
})

watch(
  () => store.getters['fields'],
  (newFields) => {
    fields.value = newFields || []
  }
)
</script>

<style scoped>
.field-line {
  display: flex;
  flex-direction: row; /* arrange components horizontally */
  align-items: flex-start; /* align to top/left */
  width: 100%;
  /* margin-bottom: 8px; */
  box-sizing: border-box;
}

/* smaller width for input-box */
.small-input {
  width: 50%; /* adjust width as needed */
}

/* full width for autocomplete-box */
.full-input {
  width: 100%;
}
</style>
