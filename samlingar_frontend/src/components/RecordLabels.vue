<template>
  <div class="card">
    <!-- <DataTable
      :value="records"
      stripedRows
      resizableColumns
      columnResizeMode="fit"
      scrollable
      scrollHeight="400px"
      tableStyle="min-width: 50rem"
      :virtualScrollerOptions="{
        lazy: true,
        onLazyLoad: loadRecoudsLazy,
        itemSize: 44,
        delay: 200,
        showLoader: true,
        loading: lazyLoading,
        numToleratedItems: 10
      }"
    >
      <template #header>
        <div class="flex flex-wrap align-items-center justify-content-between gap-2">
          <span class="text-xl text-900 font-bold">
            {{ $t('results.searchResults') }} [{{ $t('results.num_results', totalCount) }}]
          </span>
        </div>
      </template>

      <Column v-for="col of columns" :key="col.field" :field="col.field" :header="col.header">
        <template #loading>
          <div
            class="flex items-center"
            :style="{ height: '17px', 'flex-grow': '1', overflow: 'hidden' }"
          >
            <Skeleton width="60%" height="1rem" />
          </div>
        </template>
      </Column>
    </DataTable> -->
  </div>
</template>
<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

let records = ref(Array.from({ length: 100000 }))

const totalCount = computed(() => {
  return store.getters['totalRecords']
})

const loadRecoudsLazy = (event) => {}
</script>
<style scoped></style>
