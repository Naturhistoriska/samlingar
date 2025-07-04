<template>
  <Card>
    <template #title>
      <div class="grid">
        <div class="col-8" no-gutters>
          {{ $t('search.search') }}
        </div>
        <div class="col-2" no-gutters>
          <Button variant="text" @click="help">
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

        <Panel :header="$t('labels.scientificName')" toggleable>
          <scientific-name />
        </Panel>

        <Panel :header="$t('labels.collections')" toggleable>
          <Collections />
        </Panel>

        <Panel :header="$t('labels.collectingDate')" toggleable>
          <event-date />
        </Panel>

        <Panel>
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
import { ref } from 'vue'
import { useStore } from 'vuex'
import Collections from './Collections.vue'
import EventDate from './EventDate.vue'
import FilterCheckbox from './FilterCheckbox.vue'
import FilterFields from './FilterFields.vue'
import Help from './Help.vue'
import ScientificName from './ScientificName.vue'
import SearchAll from './SearchAll.vue'

const store = useStore()
const emits = defineEmits(['freeTextSearch', 'search', 'scientificNameSearch'])

const dialogVisible = ref(false)

function help() {
  dialogVisible.value = true
}
function reset() {
  console.log('reset')

  store.commit('setSearchText', '*')
  store.commit('setScientificName', null)
  store.commit('setCollectionGroup', null)

  store.commit('setFilterCoordinates', false)
  store.commit('setFilterInSweden', false)
  store.commit('setFilterImage', false)
  store.commit('setFilterType', false)

  store.commit[('setFields', [])]
  store.commit('setDataResource', null)
  search()
}

function search() {
  emits('search', 0, 50, true)
}
</script>
<style scoped>
.p-panel {
  min-width: 100% !important;
}
</style>
