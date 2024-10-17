<template>
  <Card>
    <template #title>Search filter</template>
    <template #content>
      <div>
        <Button link @click="clearFilter">
          {{ $t('search.clearFilter') }}
        </Button>
      </div>
      <Accordion value="0">
        <AccordionPanel value="0">
          <AccordionHeader> {{ $t('search.searchByCollection') }} </AccordionHeader>
          <AccordionContent>
            <div
              v-for="collection of collections"
              :key="collection.label"
              class="flex items-center"
            >
              <RadioButton
                v-model="selectedCollection"
                :inputId="collection.label"
                name="dynamic"
                :key="collection.label"
                :value="collection.label"
                @click="selectCollection(collection.label)"
              />
              <label :for="collection.name" style="padding-left: 6px">
                {{ $t(collection.i18nCode) }} [{{ collection.count }}]
              </label>
            </div>
          </AccordionContent>
        </AccordionPanel>
        <AccordionPanel value="1">
          <AccordionHeader>Occurrences (year)</AccordionHeader>
          <AccordionContent>
            <div v-for="year of years" :key="year.label" class="flex items-center">
              <RadioButton
                v-model="selectedYear"
                :inputId="year.label"
                name="dynamic"
                :value="year.label"
                @click="selectYear(year.label)"
              />
              <label :for="year.label" style="padding-left: 6px">
                {{ year.label }} [{{ year.count }}]
              </label>
            </div>
          </AccordionContent>
        </AccordionPanel>
      </Accordion>
    </template>
  </Card>
</template>
<script setup>
import { ref, computed, watch } from 'vue'
import { useStore } from 'vuex'

import Accordion from 'primevue/accordion'
import AccordionPanel from 'primevue/accordionpanel'
import AccordionHeader from 'primevue/accordionheader'
import AccordionContent from 'primevue/accordioncontent'

let selectedCollection = ref('')
let selectedYear = ref()

const store = useStore()

const emits = defineEmits(['search', 'searchByCollection', 'searchByYear'])

watch(
  () => store.getters['selectedCollection'],
  () => {
    selectedCollection.value = store.getters['selectedCollection']
  }
)

watch(
  () => store.getters['year'],
  () => {
    selectedYear.value = store.getters['year']
  }
)

const collections = computed(() => {
  return store.getters['collectons']
})

const years = computed(() => {
  return store.getters['occurrenceYears']
})

function clearFilter() {
  store.commit('setYear', null)
  store.commit('setSelectedCollection', null)
  store.commit('setStartRecord', 1)
  store.commit('setNumPerPage', 10)
  emits('search')
}

function selectYear(value) {
  this.selectedYear = value
  store.commit('setYear', value)
  store.commit('setStartRecord', 1)
  store.commit('setNumPerPage', 10)
  emits('searchByYear')
}

function selectCollection(value) {
  this.selectedCollection = value
  store.commit('setSelectedCollection', value)
  store.commit('setStartRecord', 1)
  store.commit('setNumPerPage', 10)
  emits('searchByCollection')
}
</script>
<style scoped></style>
