<template>
  <Card>
    <template #title>
      <div class="grid">
        <div class="col-8" no-gutters>
          {{ $t('search.search') }}
        </div>
        <div class="col-2" no-gutters>
          <Button variant="link">
            <small>{{ $t('common.help') }}</small>
          </Button>
        </div>
        <div class="col-2" no-gutters>
          <Button variant="link">
            <small>{{ $t('common.reset') }}</small>
          </Button>
        </div>
      </div>
    </template>
    <template #content>
      <div class="grid">
        <div class="col-12" no-gutters>
          <search-all @search="handleSearch" style="padding-bottom: 5px" />
        </div>
        <filter-checkbox @search="handleSearch" />

        <Panel>
          <filter-fields @search="handleSearch" style="padding-bottom: 5px" />
        </Panel>

        <Panel :header="$t('labels.scientificName')" toggleable>
          <scientific-name @search="handleSearch" />
        </Panel>
        <Panel :header="$t('labels.collectingDate')" toggleable>
          <event-date @search="handleSearch" />
        </Panel>
      </div>
    </template>
    <template #footer>
      <div class="flex gap-3 mt-1 grid justify-end" style="float: inline-end">
        <Button :label="$t('search.search')" @click="search()" />
      </div>
    </template>
  </Card>
</template>
<script setup>
import EventDate from './EventDate.vue'
import FilterCheckbox from './FilterCheckbox.vue'
import FilterFields from './FilterFields.vue'
import ScientificName from './ScientificName.vue'
import SearchAll from './SearchAll.vue'

const emits = defineEmits(['freeTextSearch', 'search', 'scientificNameSearch'])

function search() {
  emits('search', 0, 50, true)
}

// function doSearch(key, value) {
//   console.log('doSearch')
//   emits('search', 0, 50)
// }

function handleSearch() {
  // emits('search', 0, 50)
}
</script>
<style scoped>
.p-panel {
  min-width: 100% !important;
}
</style>
