<template>
  <Card>
    <template #title>{{ $t('search.searchFilter') }}</template>
    <template #content>
      <div v-if="displayClearLink">
        <Button link @click="clearFilter">
          {{ $t('search.clearFilter') }}
        </Button>
      </div>
      <Accordion multiple class="p-accordion-header-variant-a">
        <AccordionPanel value="0" style="font-size: 12px">
          <AccordionHeader style="background: transparent">
            {{ $t('search.searchByCollection') }}
          </AccordionHeader>
          <AccordionContent style="background: transparent" :unstyled="true">
            <div
              v-for="collection of collections"
              :key="collection.val"
              class="flex items-center"
              style="background: transparent; padding: 0.1rem"
            >
              <RadioButton
                v-model="selectedCollection"
                :inputId="collection.val"
                name="dynamic"
                :key="collection.val"
                :value="collection.val"
                @click="selectCollection(collection.val)"
              />
              <label
                :for="collection.val"
                style="padding-left: 6px; padding-bottom: 2px; color: #fff"
              >
                {{ collection.val }} [{{ collection.count }}]
              </label>
            </div>
          </AccordionContent>
        </AccordionPanel>
        <AccordionPanel value="1" style="background: transparent; font-size: 12px">
          <AccordionHeader style="background: transparent">Type status</AccordionHeader>
          <AccordionContent style="background: transparent" :unstyled="true">
            <div
              v-for="typeStatus of typeStatus"
              :key="typeStatus.val"
              class="flex items-center"
              style="background: transparent; padding: 0.1rem"
            >
              <RadioButton
                v-model="selectedType"
                :inputId="typeStatus.val"
                name="dynamic"
                :value="typeStatus.val"
                @click="selectType(typeStatus.val)"
              />
              <label
                :for="typeStatus.val"
                style="padding-left: 6px; padding-bottom: 2px; color: #fff"
              >
                {{ typeStatus.val }} [{{ typeStatus.count }}]
              </label>
            </div></AccordionContent
          >
        </AccordionPanel>
        <AccordionPanel value="2" style="background: transparent; font-size: 12px">
          <AccordionHeader style="background: transparent">Taxon</AccordionHeader>
          <AccordionContent style="background: transparent" :unstyled="true">
            <div
              v-for="family of familys"
              :key="family.val"
              class="flex items-center"
              style="background: transparent; padding: 0.1rem"
            >
              <RadioButton
                v-model="selectedFamily"
                :inputId="family.val"
                name="dynamic"
                :value="family.val"
                @click="selectFamily(family.val)"
              />
              <label :for="family.val" style="padding-left: 6px; padding-bottom: 2px; color: #fff">
                {{ family.val }} [{{ family.count }}]
              </label>
            </div>
          </AccordionContent>
        </AccordionPanel>
      </Accordion>
    </template>
    <template #footer>
      <div class="grid divLink" v-if="coordinatesCount > 0">
        <div
          class="col-10"
          @click="searchAllCoordinates"
          no-gutters
          style="float: left; text-align: left"
        >
          <div class="grid">
            <div class="col-9" no-gutters style="float: left; text-align: left">
              <Button text :disabled="coordinatesFiltered">
                <small>
                  {{ $t('startPage.specimensWithCoordinates') }}
                </small>
              </Button>
            </div>
            <div class="col-3" no-gutters style="float: left; text-align: left">
              <Button text :disabled="coordinatesFiltered">
                <small>{{ coordinatesCount }}</small>
              </Button>
            </div>
          </div>
        </div>

        <div class="col-2" style="float: left; text-align: left" v-if="coordinatesFiltered">
          <Button
            text
            icon="pi pi-times-circle"
            aria-label="Remove"
            title="Remove"
            @click="removeCoordinatesFilter"
          />
        </div>
      </div>
      <div class="grid divLink" v-if="inSwedenCount > 0">
        <div class="col-10" @click="searchAllCollectedInSweden">
          <div class="grid">
            <div class="col-9" no-gutters style="float: left; text-align: left">
              <Button text :disabled="inSwedenFiltered">
                <small>
                  {{ $t('startPage.specimensFromSweden') }}
                </small>
              </Button>
            </div>
            <div class="col-3" style="float: left; text-align: left">
              <Button text :disabled="inSwedenFiltered">
                <small>{{ inSwedenCount }}</small>
              </Button>
            </div>
          </div>
        </div>
        <div class="col-2" style="float: left; text-align: left" v-if="inSwedenFiltered">
          <Button
            text
            icon="pi pi-times-circle"
            aria-label="Remove"
            title="Remove"
            @click="removeCollectedInSwedenFilter"
          />
        </div>
      </div>
      <div class="grid divLink" v-if="imageCount > 0">
        <div class="col-10" @click="searchAllHasImages">
          <div class="grid">
            <div class="col-9" no-gutters style="float: left; text-align: left">
              <Button text :disabled="imageFiltered">
                <small>
                  {{ $t('startPage.specimensWithImages') }}
                </small>
              </Button>
            </div>
            <div class="col-3" style="float: left; text-align: left">
              <Button text :disabled="imageFiltered">
                <small>{{ imageCount }}</small>
              </Button>
            </div>
          </div>
        </div>
        <div class="col-2" style="float: left; text-align: left" v-if="imageFiltered">
          <Button
            text
            icon="pi pi-times-circle"
            aria-label="Remove"
            title="Remove"
            @click="removeImageFilter"
          />
        </div>
      </div>
      <div class="grid divLink" v-if="isTypeCount > 0">
        <div
          class="col-10"
          no-gutters
          style="float: left; text-align: left"
          @click="searchAllHasType"
        >
          <div class="grid">
            <div class="col-9" no-gutters style="float: left; text-align: left">
              <Button text :disabled="typeFiltered">
                <small>
                  {{ $t('startPage.specimensWithType') }}
                </small>
              </Button>
            </div>
            <div class="col-3" style="float: left; text-align: left">
              <Button text :disabled="typeFiltered">
                <small>{{ isTypeCount }}</small>
              </Button>
            </div>
          </div>
        </div>
        <div class="col-2" style="float: left; text-align: left" v-if="typeFiltered">
          <Button
            text
            icon="pi pi-times-circle"
            aria-label="Remove"
            title="Remove"
            @click="removeTypeSpecimensFilter"
          />
        </div>
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

// let coordinatesFiltered = ref(false)

const store = useStore()

// const emits = defineEmits(['conditionalSearch', 'filterSearch', 'search'])
const emits = defineEmits([
  'collectionSearch',
  'familySearch',
  'typeSearch',
  'search',
  'filterSearch'
])

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
//   () => store.getters['filterCoordinates'],
//   () => {
//     console.log('filterCoordinates changed...')
//     coordinatesFiltered.value = store.getters['filterCoordinates']
//   }
// )

const imageFiltered = computed(() => {
  return store.getters['filterImage']
})

const typeFiltered = computed(() => {
  return store.getters['filterType']
})

const inSwedenFiltered = computed(() => {
  return store.getters['filterInSweden']
})

const coordinatesFiltered = computed(() => {
  console.log('coordinatesFiltered', store.getters['filterCoordinates'])
  return store.getters['filterCoordinates']
})

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
  return store.getters['collectons']
})

const typeStatus = computed(() => {
  return store.getters['typeStatus']
})

const familys = computed(() => {
  return store.getters['family']
})

function selectCollection(value) {
  selectedCollection.value = value
  store.commit('setStartRecord', 0)
  store.commit('setNumPerPage', 10)
  store.commit('setSelectedCollection', value)
  store.commit('setShowDetail', false)
  store.commit('setShowResults', true)

  displayClearLink.value = true

  emits('collectionSearch')
}

function selectType(value) {
  selectedType.value = value
  store.commit('setSelectedType', value)
  store.commit('setStartRecord', 0)
  store.commit('setNumPerPage', 10)
  store.commit('setShowDetail', false)
  store.commit('setShowResults', true)
  displayClearLink.value = true

  emits('typeSearch')
}

function selectFamily(value) {
  selectedFamily.value = value
  store.commit('setSelectedFamily', value)
  store.commit('setStartRecord', 0)
  store.commit('setNumPerPage', 10)
  store.commit('setShowDetail', false)
  store.commit('setShowResults', true)
  displayClearLink.value = true

  emits('familySearch')
}

function clearFilter() {
  store.commit('setSelectedType', null)
  store.commit('setSelectedCollection', null)
  store.commit('setSelectedFamily', null)
  store.commit('setStartRecord', 0)
  store.commit('setNumPerPage', 10)
  displayClearLink.value = false
  emits('search')
}

function searchAllCoordinates() {
  console.log('searchAllCoordinates')
  store.commit('setStartRecord', 0)
  store.commit('setNumPerPage', 10)
  store.commit('setFilterCoordinates', true)
  emits('filterSearch')
}

function searchAllCollectedInSweden() {
  console.log('searchAllCollectedInSweden')
  store.commit('setStartRecord', 0)
  store.commit('setNumPerPage', 10)
  store.commit('setFilterInSweden', true)
  emits('filterSearch')
}

function searchAllHasImages() {
  store.commit('setStartRecord', 0)
  store.commit('setNumPerPage', 10)
  store.commit('setFilterImage', true)
  emits('filterSearch')
}

function searchAllHasType() {
  store.commit('setStartRecord', 0)
  store.commit('setNumPerPage', 10)
  store.commit('setFilterType', true)
  emits('filterSearch')
}

function removeTypeSpecimensFilter() {
  console.log('removeTypeSpecimensFilter')
  store.commit('setStartRecord', 0)
  store.commit('setNumPerPage', 10)
  store.commit('setFilterType', false)
  emits('filterSearch')
}

function removeCollectedInSwedenFilter() {
  store.commit('setStartRecord', 0)
  store.commit('setNumPerPage', 10)
  store.commit('setFilterInSweden', false)
  emits('filterSearch')
}

function removeCoordinatesFilter() {
  console.log('removeCoordinatesFilter')
  store.commit('setStartRecord', 0)
  store.commit('setNumPerPage', 10)
  store.commit('setFilterCoordinates', false)
  emits('filterSearch')
}

function removeImageFilter() {
  store.commit('setStartRecord', 0)
  store.commit('setNumPerPage', 10)
  store.commit('setFilterImage', false)
  emits('filterSearch')
}
</script>
<style scoped>
.p-button-text {
  /* color: #fff !important; */
  text-decoration: underline;
  font-size: 1rem;
}
.p-button-text:hover {
  color: var(--p-emerald-500) !important;
  text-decoration: none !important;
  background: transparent !important;
}
.p-accordionpanel:not(.p-active).p-accordionpanel > .p-accordionheader {
  background: var(--p-accordion-header-active-background);
  color: #cbcad1;
  font-size: 1rem;
}
.divLink {
  min-width: 100% !important;
  cursor: pointer !important;
}
.divLink:hover {
  background: #0e3a12;
  color: var(--p-emerald-500) !important;
}
/* .p-accordionpanel:not(.p-disabled).p-accordionpanel-hover > .p-accordionheader { */
/* background: var(--p-accordion-header-active-background); */
/* color: #e70808; */
/* } */

/* .p-accordionpanel:not(.p-disabled).p-accordionpanel-active > .p-accordionheader {
  background: var(--p-accordion-header-active-background);
  color: #fff;
} */

/* .p-button-text {
  color: #0dff5c;
} */

/* .p-accordion-header-variant-a .p-accordion-header-link {
  border: solid 3px red;
  background-color: red;
} */
/* .p-accordioncontent .p-accordioncontent-content { */
/* background-color: red; */
/* } */

/* .p-accordion .p-accordion-header .p-accordion-header-link {
  padding: 1.25rem;
  border: 0 none;
  color: rgba(144, 34, 34, 0.87);
  background: #1e1e1e;
  font-weight: 400;
  border-radius: 4px;
  transition: none;
} */

/* .p-accordioncontent {
  background-color: #9c6d0f !important;
} */
/* .p-accordionheader {
  /* background-color: #9c6d0f !important;
   */
/* color: #e5e50e;
  background-color: transparent;
} */
/* .p-accordioncontent-content {
  background-color: #ffaa00 !important;
} */

/* .p-accordion .p-accordion-header:not(.p-highlight):not(.p-disabled):hover .p-accordion-header-link {
  background: rgba(255, 255, 255, 0.04);
  border-color: red;
  color: rgba(255, 255, 255, 0.87);
} */
/* .p-accordioncontent-content {
  background-color: red;
} */

/* .p-accordion {
  background-color: red important;
} */
/* .p-accordion .p-accordioncontent .p-accordioncontent-content {
  background: red !important;
} */
/* #p-accordioncontent {
  background: blue !important;
} */
</style>
