<template>
  <Card>
    <template #title>
      <div class="grid">
        <div class="col-8" no-gutters>
          {{ $t('search.search') }}
        </div>
        <div class="col-2" no-gutters>
          <Button variant="text" @click="dialogVisible = !dialogVisible">
            <small>{{ $t('common.help') }}</small>
          </Button>
        </div>
        <div class="col-2" no-gutters>
          <Button variant="text" @click="reset">
            <small>{{ $t('common.reset') }}</small>
          </Button>
        </div>
      </div>
    </template>
    <template #content>
      <Dialog
        id="helpDialog"
        v-model:visible="dialogVisible"
        header="Flex Scroll"
        :style="{ width: '75vw' }"
        maximizable
        modal
        :contentStyle="{ height: '300px' }"
      >
        <Help />
      </Dialog>
      <div class="grid">
        <div class="col-12" no-gutters>
          <search-all style="padding-bottom: 5px" />
        </div>
        <filter-checkbox />

        <Panel
          :class="scientificNamePanelClass"
          :collapsed="scientificNamePanelNotVisible"
          :header="$t('labels.scientificName')"
          toggleable
        >
          <scientific-name />
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
          <event-date />
        </Panel>

        <Panel :class="fieldsPanelClass">
          <filter-fields style="padding-bottom: 5px" />
        </Panel>
      </div>
    </template>
    <template #footer>
      <div class="flex gap-3 mt-1 grid justify-end" style="float: inline-end">
        <Button :label="$t('search.search')" @click="search" id="searchRecordBtn" />
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
const emits = defineEmits(['freeTextSearch', 'resetSerch', 'scientificNameSearch'])

const dialogVisible = ref(false)

const scientificNamePanelNotVisible = computed(() => {
  return store.getters['scientificName'] === null
})

const collectionGroupPanelNotVisible = computed(() => {
  return store.getters['dataResource'] === null
})

const eventDatePanelNotVisible = computed(() => {
  return store.getters['dates'] === null
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
  store.commit('setSearchText', '*')
  store.commit('setScientificName', null)
  store.commit('setSearchMode', 'contains')
  store.commit('setCollectionGroup', null)

  store.commit('setFilterCoordinates', false)
  store.commit('setFilterInSweden', false)
  store.commit('setFilterImage', false)
  store.commit('setFilterType', false)

  store.commit('setSearchParams', null)

  store.commit('setFields', [])
  store.commit('setDataResource', null)
  search()
}

function search() {
  emits('resetSerch')
}
</script>
<style scoped>
.p-panel {
  min-width: 100% !important;
}

.active-panel {
  background-color: #eefaeb;
}
</style>
