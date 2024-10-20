<template>
  <Card>
    <template #title>Search filter</template>
    <template #content>
      <div v-if="displayClearLink">
        <Button link @click="clearFilter">
          {{ $t('search.clearFilter') }}
        </Button>
      </div>
      <Accordion :value="['0']" multiple style="background-color: transparent">
        <AccordionPanel value="0" style="background-color: transparent">
          <AccordionHeader style="background-color: transparent">
            {{ $t('search.searchByCollection') }}
          </AccordionHeader>
          <AccordionContent style="background: transparent">
            <div
              v-for="collection of collections"
              :key="collection.label"
              class="flex items-center"
              style="background: transparent"
            >
              <RadioButton
                v-model="selectedCollection"
                :inputId="collection.label"
                name="dynamic"
                :key="collection.label"
                :value="collection.label"
                @click="selectCollection(collection.label)"
              />
              <label :for="collection.label" style="padding-left: 6px">
                {{ $t(collection.i18nCode) }} [{{ collection.count }}]
              </label>
            </div>
          </AccordionContent>
        </AccordionPanel>
        <AccordionPanel value="1" style="background: transparent">
          <AccordionHeader style="background: transparent">Type status</AccordionHeader>
          <AccordionContent style="background: transparent">
            <div
              v-for="typeStatus of typeStatus"
              :key="typeStatus.label"
              class="flex items-center"
              style="background: transparent"
            >
              <RadioButton
                v-model="selectedType"
                :inputId="typeStatus.label"
                name="dynamic"
                :value="typeStatus.label"
                @click="selectType(typeStatus.label)"
              />
              <label :for="typeStatus.label" style="padding-left: 6px">
                {{ typeStatus.label }} [{{ typeStatus.count }}]
              </label>
            </div>
          </AccordionContent>
        </AccordionPanel>
        <!-- <AccordionPanel value="1" style="background: transparent">
          <AccordionHeader style="background: transparent">Occurrences (year)</AccordionHeader>
          <AccordionContent style="background: transparent">
            <div
              v-for="year of years"
              :key="year.label"
              class="flex items-center"
              style="background: transparent"
            >
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
        </AccordionPanel> -->
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
// let selectedYear = ref()

let selectedType = ref('')
let displayClearLink = ref(false)

const store = useStore()

const emits = defineEmits(['search', 'searchByCollection', 'searchByType'])

watch(
  () => store.getters['selectedCollection'],
  () => {
    selectedCollection.value = store.getters['selectedCollection']
  }
)

watch(
  () => store.getters['selectedType'],
  () => {
    selectedType.value = store.getters['selectedType']
  }
)

// watch(
//   () => store.getters['year'],
//   () => {
//     selectedYear.value = store.getters['year']
//   }
// )

const collections = computed(() => {
  return store.getters['collectons']
})

const typeStatus = computed(() => {
  return store.getters['typeStatus']
})

// const years = computed(() => {
//   return store.getters['occurrenceYears']
// })

function clearFilter() {
  // store.commit('setYear', null)
  store.commit('setSelectedType', null)
  store.commit('setSelectedCollection', null)
  store.commit('setStartRecord', 1)
  store.commit('setNumPerPage', 10)
  displayClearLink.value = false
  emits('search')
}

// function selectYear(value) {
//   this.selectedYear = value
//   store.commit('setYear', value)
//   store.commit('setStartRecord', 1)
//   store.commit('setNumPerPage', 10)
//   displayClearLink.value = true

//   emits('searchByYear')
// }

function selectType(value) {
  console.log('selectType...', value)
  this.selectedType = value
  store.commit('setSelectedType', value)
  store.commit('setStartRecord', 1)
  store.commit('setNumPerPage', 10)
  displayClearLink.value = true

  emits('searchByType')
}

function selectCollection(value) {
  this.selectedCollection = value
  store.commit('setSelectedCollection', value)
  store.commit('setStartRecord', 1)
  store.commit('setNumPerPage', 10)
  displayClearLink.value = true

  emits('searchByCollection')
}
</script>
<style scoped>
.accordionBg {
  background: red;
  background-color: transparent;
}
</style>
