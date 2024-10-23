<template>
  <Card>
    <template #title>Search filter</template>
    <template #content>
      <div v-if="displayClearLink">
        <Button link @click="clearFilter">
          {{ $t('search.clearFilter') }}
        </Button>
      </div>
      <Accordion multiple style="background-color: transparent">
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
        <AccordionPanel value="2" style="background: transparent">
          <AccordionHeader style="background: transparent">Taxon</AccordionHeader>
          <AccordionContent style="background: transparent">
            <div
              v-for="c of classs"
              :key="c.label"
              class="flex items-center"
              style="background: transparent"
            >
              <RadioButton
                v-model="selectedClass"
                :inputId="c.label"
                name="dynamic"
                :value="c.label"
                @click="selectYear(c.label)"
              />
              <label :for="c.label" style="padding-left: 6px">
                {{ c.label }} [{{ c.count }}]
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

let selectedCollection = ref()
let selectedClass = ref()

let selectedType = ref()
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

const classs = computed(() => {
  return store.getters['classs']
})

function onclick(value) {
  console.log('onclick', value)
}

function clearFilter() {
  // store.commit('setYear', null)
  store.commit('setSelectedType', null)
  store.commit('setSelectedCollection', null)
  store.commit('setStartRecord', 0)
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
  selectedType.value = value
  store.commit('setSelectedType', value)
  store.commit('setStartRecord', 1)
  store.commit('setNumPerPage', 10)
  displayClearLink.value = true

  emits('searchByType')
}

function selectCollection(value) {
  selectedCollection.value = value
  store.commit('setStartRecord', 0)
  store.commit('setNumPerPage', 10)
  store.commit('setSelectedCollection', value)

  displayClearLink.value = true

  emits('searchByCollection')
}
</script>
<style scoped></style>
