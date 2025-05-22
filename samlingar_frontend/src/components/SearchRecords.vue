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
          <search-all @freeTextSearch="handleFreeTextSearch" style="padding-bottom: 5px" />
        </div>
        <filter-checkbox @search="handleSearch" />
        <Panel :header="$t('labels.scientificName')" toggleable>
          <scientific-name @search="handleSearch" />
        </Panel>
      </div>
    </template>
  </Card>
</template>
<script setup>
import SearchAll from './SearchAll.vue'
import ScientificName from './ScientificName.vue'
import FilterCheckbox from './FilterCheckbox.vue'

const emits = defineEmits(['freeTextSearch', 'search'])

function handleFreeTextSearch(value) {
  console.log('handleFreeTextSearch', value)
  emits('freeTextSearch', value, 0, 50)
}

function handleSearch(hasImage, hasCoordinates) {
  emits('search', hasImage, hasCoordinates, 0, 50)
}
</script>
<style scoped>
.p-panel {
  min-width: 100% !important;
}
/* .p-panel-title {
  font-weight: 100 !important;
  font-size: 6px !important;
} */
</style>
