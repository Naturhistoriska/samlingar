<template>
  <div class="card">
    <DataTable
      :value="virtualRecords"
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
          <Button type="button" label="Columns" @click="addColumns" />
        </div>
      </template>
      <Column
        v-for="col of columns"
        :key="col.field"
        :field="col.field"
        :header="col.header"
        stripedRows
      >
        <template #loading>
          <div
            class="flex items-center"
            :style="{ height: '17px', 'flex-grow': '1', overflow: 'hidden' }"
          >
            <Skeleton width="60%" height="1rem" />
          </div>
        </template>
      </Column>
      <!-- <Column field="catalogNumber" header="catalogNumber" style="width: 20%; height: 44px">
        <template #loading>
          <div
            class="flex items-center"
            :style="{ height: '17px', 'flex-grow': '1', overflow: 'hidden' }"
          >
            <Skeleton width="60%" height="1rem" />
          </div>
        </template>
      </Column>
      <Column field="catalogedDate" header="catalogedDate" style="width: 20%; height: 44px">
        <template #loading>
          <div
            class="flex items-center"
            :style="{ height: '17px', 'flex-grow': '1', overflow: 'hidden' }"
          >
            <Skeleton width="40%" height="1rem" />
          </div>
        </template>
      </Column>
      <Column field="collectionName" header="collectionName" style="width: 20%; height: 44px">
        <template #loading>
          <div
            class="flex items-center"
            :style="{ height: '17px', 'flex-grow': '1', overflow: 'hidden' }"
          >
            <Skeleton width="30%" height="1rem" />
          </div>
        </template>
      </Column>
      <Column field="continent" header="continent" style="width: 20%; height: 44px">
        <template #loading>
          <div
            class="flex items-center"
            :style="{ height: '17px', 'flex-grow': '1', overflow: 'hidden' }"
          >
            <Skeleton width="40%" height="1rem" />
          </div>
        </template>
      </Column>
      <Column
        field="higherClassification"
        header="higherClassification"
        style="width: 20%; height: 44px"
      >
        <template #loading>
          <div
            class="flex items-center"
            :style="{ height: '17px', 'flex-grow': '1', overflow: 'hidden' }"
          >
            <Skeleton width="60%" height="1rem" />
          </div>
        </template>
      </Column> -->
    </DataTable>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useStore } from 'vuex'

import Service from '../Service'
const service = new Service()

const store = useStore()

onMounted(() => {
  console.log('onMounted')

  const searchText = store.getters['searchText']
  let value = searchText == null ? '*' : searchText

  service
    .apiFreeTextSearch(value, 0, 10)
    .then((response) => {
      const total = response.response.numFound
      const results = response.response.docs

      store.commit('setResults', results)
      store.commit('setTotalRecords', total)

      console.log('total:', total)
      console.log('results:', results)

      records.value = results
      totalRecords.value = total
      let virtualCount = total > 100000 ? 100000 : total

      console.log('what,...', totalRecords.value, records.value)

      virtualRecords = ref(Array.from({ length: virtualCount }))

      count.value = 10
    })
    .catch()
    .finally(() => {})
})

let records = ref()
let virtualRecords = ref()
// const virtualRecords = ref(Array.from({ length: 100000 }))
const lazyLoading = ref(false)
const loadLazyTimeout = ref()
const totalRecords = ref()
let count = ref(0)
let newSearch = ref(false)

let columns = ref([
  { field: 'collectionName', header: 'Collection Name' },
  { field: 'scientificName', header: 'Scientific Name' },
  { field: 'locality', header: 'Locality' },
  { field: 'catalogNumber', header: 'CatalogNumber' }
])

watch(
  () => store.getters['searchText'],
  (newValue, oldValue) => {
    console.log('watch')
    newSearch.value = true
    reloadData()
  }
)

const totalCount = computed(() => {
  return store.getters['totalRecords']
})

function reloadData() {
  // totalRecords.value = store.getters['totalRecords']
  console.log('reloadData')
  const results = store.getters['results']

  records.value = results
  totalRecords.value = totalCount
  const virtualCount = totalCount > 100000 ? 100000 : totalCount

  virtualRecords = ref(Array.from({ length: virtualCount }))

  count.value = 10
}

const loadRecoudsLazy = (event) => {
  console.log('loadRecoudsLazy')
  console.log(newSearch.value)

  // if (newSearch.value) {
  //   reloadData()
  //   newSearch.value = false
  // }

  !lazyLoading.value && (lazyLoading.value = true)

  if (loadLazyTimeout.value) {
    clearTimeout(loadLazyTimeout.value)
  }

  //simulate remote connection with a timeout
  loadLazyTimeout.value = setTimeout(
    () => {
      // totalRecords.value = store.getters['totalRecords']

      // console.log('totalRecords', totalRecords)
      const virtulCount = totalCount.value > 100000 ? 100000 : totalCount.value

      virtualRecords.value = Array.from({ length: virtulCount })
      let _virtualRecords = [...virtualRecords.value]

      console.log('virtual records 1: ', virtualRecords)

      let { first, last } = event
      console.log(first, last)

      // last = 0 ? 30 : last

      loadData(count.value, 100)
      count.value = count.value + 100
      console.log('count ', count)

      const results = store.getters['results']

      results.forEach(function (item) {
        if (item != undefined) {
          records.value.push(item)
        }
      })

      const loadedRecords = records.value.slice(first, last)

      Array.prototype.splice.apply(_virtualRecords, [...[first, last - first], ...loadedRecords])

      virtualRecords.value = _virtualRecords
      console.log('virtual records : ', virtualRecords)

      lazyLoading.value = false
    },
    Math.random() * 1000 + 250
  )
}

function loadData(start, numPerPage) {
  const searchText = store.getters['searchText']

  console.log('loadData', start)

  let value = searchText == null ? '*' : searchText

  service
    .apiFreeTextSearch(value, start, numPerPage)
    .then((response) => {
      const results = response.response.docs

      store.commit('setResults', results)
    })
    .catch()
    .finally(() => {})
}

function addColumns() {
  console.log('add columns')
}
</script>
