<template>
  <Card>
    <template #title>
      <div class="grid">
        <div class="col-9" no-gutters>
          {{ $t('search.search') }}
        </div>
        <div class="col-1" no-gutters>
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
      <search-all @freeTextSearch="handleFreeTextSearch" />
    </template>
  </Card>
</template>
<script setup>
import SearchAll from './SearchAll.vue'

const emits = defineEmits(['freeTextSearch'])

function handleFreeTextSearch(value) {
  console.log('handleFreeTextSearch', value)
  emits('freeTextSearch', value, 0, 10)
}
</script>
