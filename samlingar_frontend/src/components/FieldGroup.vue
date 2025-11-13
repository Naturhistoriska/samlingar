<template>
  <div style="min-width: 100%; max-height: 200px; overflow-y: scroll">
    <template v-for="(field, index) in fields" :key="field">
      <input-box v-bind:field="field" v-if="field.label === 'Synonyms'" />
      <autocomplete-box v-bind:field="field" v-else />
    </template>
  </div>
</template>
<script setup>
import { onMounted, ref, watch } from 'vue'
import { useStore } from 'vuex'
import AutocompleteBox from './baseComponents/AutocompleteBox.vue'
import InputBox from './baseComponents/InputBox.vue'

const store = useStore()

const fields = ref([])

watch(
  () => store.getters['fields'],
  (newValue, oldValue) => {
    fields.value = newValue
  }
)

onMounted(() => {
  fields.value = store.getters['fields']
})
</script>
<style scoped></style>
