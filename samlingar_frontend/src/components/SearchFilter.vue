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
              :key="collection.val"
              class="flex items-center"
              style="background: transparent"
            >
              <RadioButton
                v-model="selectedCollection"
                :inputId="collection.val"
                name="dynamic"
                :key="collection.val"
                :value="collection.val"
                @click="selectCollection(collection.val)"
              />
              <label :for="collection.val" style="padding-left: 6px">
                {{ collection.val }} [{{ collection.count }}]
              </label>
            </div>
          </AccordionContent>
        </AccordionPanel>
        <AccordionPanel value="1" style="background: transparent">
          <AccordionHeader style="background: transparent">Type status</AccordionHeader>
          <AccordionContent style="background: transparent">
            <div
              v-for="typeStatus of typeStatus"
              :key="typeStatus.val"
              class="flex items-center"
              style="background: transparent"
            >
              <RadioButton
                v-model="selectedType"
                :inputId="typeStatus.val"
                name="dynamic"
                :value="typeStatus.val"
                @click="selectType(typeStatus.val)"
              />
              <label :for="typeStatus.val" style="padding-left: 6px">
                {{ typeStatus.val }} [{{ typeStatus.count }}]
              </label>
            </div>
          </AccordionContent>
        </AccordionPanel>
        <AccordionPanel value="2" style="background: transparent">
          <AccordionHeader style="background: transparent">Taxon</AccordionHeader>
          <AccordionContent style="background: transparent">
            <div
              v-for="family of familys"
              :key="family.val"
              class="flex items-center"
              style="background: transparent"
            >
              <RadioButton
                v-model="selectedFamily"
                :inputId="family.val"
                name="dynamic"
                :value="family.val"
                @click="selectFamily(family.val)"
              />
              <label :for="family.val" style="padding-left: 6px">
                {{ family.val }} [{{ family.count }}]
              </label>
            </div>
          </AccordionContent>
        </AccordionPanel>
      </Accordion>
    </template>
    <template #footer>
      <div class="flex gap-2 mt-1" style="float: left">
        <Button text class="w-full">
          <small>
            {{ $t('startPage.specimensWithCoordinates') }}
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            {{ coordinatesCount }}
          </small>
        </Button>
      </div>
      <div class="flex gap-2 mt-1" style="float: left">
        <Button text class="w-full">
          <small>
            {{ $t('startPage.specimensFromSweden') }}
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            {{ inSwedenCount }}
          </small>
        </Button>
      </div>
      <div class="flex gap-2 mt-1" style="float: left">
        <Button text class="w-full">
          <small>
            {{ $t('startPage.specimensWithImages') }}
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            {{ imageCount }}
          </small>
        </Button>
      </div>
      <div class="flex gap-2 mt-1" style="float: left">
        <Button text class="w-full">
          <small>
            {{ $t('startPage.specimensWithType') }}
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            {{ isTypeCount }}
          </small>
        </Button>
      </div>
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
let selectedFamily = ref()

let selectedType = ref()
let displayClearLink = ref(false)

const store = useStore()

const emits = defineEmits(['search', 'filterSearch'])

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
    console.log('selectedType', selectedType)
  }
)

const coordinatesCount = computed(() => {
  return store.getters['hasCoordinatesCount']
})

const imageCount = computed(() => {
  return store.getters['imageCount']
})

const isTypeCount = computed(() => {
  return store.getters['isTypeCount']
})

const inSwedenCount = computed(() => {
  return store.getters['inSwedenCount']
})

const collections = computed(() => {
  const c = store.getters['collectons']
  console.log('collections', c)
  return store.getters['collectons']
})

const typeStatus = computed(() => {
  return store.getters['typeStatus']
})

const familys = computed(() => {
  return store.getters['family']
})

function onclick(value) {
  console.log('onclick', value)
}

function clearFilter() {
  // store.commit('setYear', null)
  store.commit('setSelectedType', null)
  store.commit('setSelectedCollection', null)
  store.commit('setSelectedFamily', null)
  store.commit('setStartRecord', 0)
  store.commit('setNumPerPage', 10)
  displayClearLink.value = false
  emits('search')
}

function selectType(value) {
  console.log('selectType', value)
  selectedType.value = value
  store.commit('setSelectedType', value)
  store.commit('setStartRecord', 0)
  store.commit('setNumPerPage', 10)
  displayClearLink.value = true

  emits('filterSearch', 'filterByType')
}

function selectCollection(value) {
  selectedCollection.value = value
  store.commit('setStartRecord', 0)
  store.commit('setNumPerPage', 10)
  store.commit('setSelectedCollection', value)

  displayClearLink.value = true

  emits('filterSearch', 'filterByCollection')
}

function selectFamily(value) {
  console.log('selectFamily')

  selectFamily.value = value
  store.commit('setStartRecord', 0)
  store.commit('setNumPerPage', 10)
  store.commit('setSelectedFamily', value)

  displayClearLink.value = true

  emits('filterSearch', 'filterByFamily')
}
</script>
<style scoped></style>
