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
      <search-all @freeTextSearch="handleFreeTextSearch" style="padding-bottom: 5px" />
      <filter-checkbox @search="handleSearch" />
    </template>
  </Card>
</template>
<script setup>
import SearchAll from './SearchAll.vue'
import FilterCheckbox from './FilterCheckbox.vue'

const emits = defineEmits(['freeTextSearch', 'search'])

function handleFreeTextSearch(value) {
  console.log('handleFreeTextSearch', value)
  emits('freeTextSearch', value, 0, 10)
}

function handleSearch(hasImage, hasCoordinates) {
  emits('search', hasImage, hasCoordinates, 0, 10)
}
</script>
