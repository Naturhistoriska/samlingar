<template>
  <Card>
    <template #title>
      <div class="grid">
        <div class="col-8" no-gutters>
          {{ $t('search.search') }}
        </div>

        <div class="col-2" no-gutters>
          <Button variant="text" @click="dialogVisible = !dialogVisible">
            <small>{{ $t('btnLabel.help') }}</small>
          </Button>
        </div>
        <div class="col-2" no-gutters>
          <Button variant="text" @click="reset">
            <small>{{ $t('btnLabel.reset') }}</small>
          </Button>
        </div>
      </div>
    </template>
    <template #content>
      <Dialog
        id="helpDialog"
        :breakpoints="{ '1199px': '75vw', '575px': '90vw' }"
        v-model:visible="dialogVisible"
        :style="{ width: '70rem' }"
        maximizable
        modal
        :contentStyle="{ height: '500px' }"
        class="centered-dialog"
      >
        <template #header>
          <div class="custom-dialog-header">{{ $t('help.searchHelp') }}</div>
        </template>
        <Divider class="custom-divider" />
        <Help />
      </Dialog>
      <div class="grid">
        <div class="col-12" no-gutters>
          <search-all style="padding-bottom: 5px" @freeTextSearch="search" />
        </div>
        <Divider class="custom-divider" />

        <filter-checkbox style="margin-bottom: 1rem" @search="search" />

        <Panel
          :class="scientificNamePanelClass"
          :collapsed="scientificNamePanelNotVisible"
          :header="$t('labels.scientificName')"
          toggleable
        >
          <scientific-name @scientificNameSearch="search" />
        </Panel>

        <Panel
          :class="collectionPanelClass"
          :header="$t('labels.collections')"
          toggleable
          :collapsed="collectionGroupPanelNotVisible"
        >
          <Collections />
        </Panel>

        <Panel
          :class="eventDatePanelClass"
          :header="$t('labels.collectingDate')"
          toggleable
          :collapsed="eventDatePanelNotVisible"
        >
          <event-date @dateSearch="search" />
        </Panel>

        <Panel :class="fieldsPanelClass">
          <filter-fields style="padding-bottom: 5px" />
        </Panel>
      </div>
    </template>
    <template #footer>
      <div class="flex gap-3 mt-1 grid justify-end" style="float: inline-end">
        <Button
          :label="$t('search.search')"
          @click="search"
          id="searchRecordBtn"
          class="my-custom-button"
        />
      </div>
    </template>
  </Card>
</template>
<script setup>
import { computed, ref } from 'vue'
import { useStore } from 'vuex'
import Collections from './Collections.vue'

import EventDate from './EventDate.vue'
import FilterCheckbox from './FilterCheckbox.vue'
import FilterFields from './FilterFields.vue'
import Help from './Help.vue'
import ScientificName from './ScientificName.vue'
import SearchAll from './SearchAll.vue'

const store = useStore()
const emits = defineEmits(['search'])

const dialogVisible = ref(false)
const scientificNamePanelNotVisible = computed(() => {
  return store.getters['scientificName'] === null
})

const collectionGroupPanelNotVisible = computed(() => {
  const selectedCollection = store.getters['selectedCollection']
  return selectedCollection === null
})

const eventDatePanelNotVisible = computed(() => {
  const isDatesEmpty = store.getters['dates'] === null
  const isStartYearEmpty = store.getters['startYear'] === null
  const isEndYearEmpty = store.getters['endYear'] === null

  return isStartYearEmpty && isEndYearEmpty && isDatesEmpty
})

const scientificNamePanelClass = computed(() => {
  return scientificNamePanelNotVisible.value ? '' : 'active-panel'
})
const collectionPanelClass = computed(() => {
  return collectionGroupPanelNotVisible.value ? '' : 'active-panel'
})

const eventDatePanelClass = computed(() => {
  return eventDatePanelNotVisible.value ? '' : 'active-panel'
})

const fieldsPanelClass = computed(() => {
  return store.getters['fields'].length === 0 ? '' : 'active-panel'
})

function reset() {
  store.commit('setSearchText', null)
  store.commit('setFullTextSearchMode', 'contains')
  store.commit('setScientificName', null)
  store.commit('setSearchMode', 'contains')

  store.commit('setCollectionGroup', null)
  store.commit('setSelectedCollection', null)

  store.commit('setFilterCoordinates', false)
  store.commit('setFilterInSweden', false)
  store.commit('setFilterImage', false)
  store.commit('setFilterType', false)
  store.commit('setFilterNordicCountry', false)

  store.commit('setStartDate', null)
  store.commit('setEndDate', null)
  store.commit('setDates', null)

  store.commit('setStartYear', null)
  store.commit('setEndYear', null)
  store.commit('setDateFilter', 'date')

  store.commit('setSearchParams', null)

  store.commit('setFields', [])
  store.commit('setDataResource', null)
  search()
}

function search() {
  emits('search')
}
</script>
<style scoped>
.p-button-text {
  color: #4a4949b6;
  text-decoration: underline;
}

.p-button-text:hover {
  /* color: var(--p-emerald-500) !important; */
  /* color: #fff !important; */
  text-decoration: none !important;
  background: transparent !important;
}
.p-panel {
  min-width: 100% !important;
}

.active-panel {
  background-color: #eefaeb;
}

.custom-divider.p-divider-horizontal::before {
  border-top: 1px solid #144836; /* Change color here */
}
.custom-dialog-header {
  width: 100%;
  text-align: center;
  font-weight: bold;
  font-size: 2rem;
}
</style>
