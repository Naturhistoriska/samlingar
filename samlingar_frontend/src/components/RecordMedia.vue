<template>
  <div class="card">
    <DataView :value="records" :lazy="true" :loading="loading" layout="grid">
      <template #grid="slotProps">
        <div class="grid grid-cols-12">
          <div
            v-for="(item, index) in slotProps.items"
            :key="index"
            class="col-span-12 sm:col-span-6 md:col-span-4 xl:col-span-6 p-2"
          >
            <div class="flex flex-row justify-between items-start gap-3">
              <card style="height: 150px; width: 250px" v-if="item != undefined">
                <template #title>
                  <div class="grid">
                    <div class="col-9" no-gutters>
                      <small>{{ item.record.catalogNumber }}</small>
                    </div>
                    <div class="col-3" no-gutters>
                      <Button variant="link" @click="view(item)">
                        <small>{{ $t('records.view') }}</small>
                      </Button>
                    </div>
                  </div>
                </template>
                <template #subtitle>
                  <small>{{ item.record.scientificName }}</small>
                </template>
                <template #content>
                  <div style="min-width: 100px; max-width: 100px; height: 100px">
                    <Thumbnail v-bind:record="item" v-bind:src="item.src" />
                  </div>
                </template>
              </card>
            </div>
          </div>
        </div>
      </template>
    </DataView>

    <!-- Sentinel for Intersection Observer -->
    <div ref="sentinel" class="sentinel"></div>

    <div v-if="loading" class="loading-indicator">Loading...</div>
    <div v-if="!hasMoreData" class="end-of-data">No more products</div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import Thumbnail from './Thumbnail.vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import Service from '../Service'

import Button from 'primevue/button'

const store = useStore()
const router = useRouter()

const service = new Service()

const records = ref([])
const loading = ref(false)
const page = ref(0)
const rows = 10
const totalRecords = ref(null)
const hasMoreData = ref(true)

// const imageUrl = ref(
//   'https://media-service.nrm.se/images?id=/kryptos/kbo/kryptobase/201609/max/321789.jpg&dataset=kbo'
// )

const sentinel = ref(null)
let observer = null

onMounted(() => {
  fetchData(0, 50) // Initial load
  setupObserver()
})

onBeforeUnmount(() => {
  disconnectObserver()
})

function fetchData() {
  if (loading.value || !hasMoreData.value) return

  loading.value = true

  let params = store.getters['searchParams']
  params.set('hasImage', '*')
  params.set('-collectionCode', 'pz')
  const start = page.value
  const end = 20
  service
    .apiSearch(params, start, end)
    .then((response) => {
      const total = response.facets.count
      const data = response.response.filter(
        (item) => item.collectionCode !== 'pz' && !item.catalogNumber.startsWith('P')
      )

      const allImages = data.flatMap((item) =>
        item.associatedMedia
          .filter((img) => img.includes('thumb') || img.includes('mini') || img.includes('tumme'))
          .map((img) => {
            return {
              src: img,
              record: item // add any metadata from item here
            }
          })
      )

      records.value.push(...allImages)
      totalRecords.value = total

      page.value += 1

      if (records.value.length >= total) {
        hasMoreData.value = false
        disconnectObserver()
      }
    })
    .catch((error) => {
      console.log('error', error)
    })
    .finally(() => {
      loading.value = false
    })
}

const setupObserver = () => {
  if (!sentinel.value) return

  observer = new IntersectionObserver((entries) => {
    if (entries[0].isIntersecting && hasMoreData.value) {
      fetchData()
    }
  })

  observer.observe(sentinel.value)
}

const disconnectObserver = () => {
  if (observer) {
    observer.disconnect()
    observer = null
  }
}
function view(data) {
  store.commit('setSelectedRecord', data.record)
  router.push(`/record/${data.record.id}`)
}
</script>
<style scoped>
/* .p-dataview-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
} */

/* Each record's card */
/* .item-card {
  padding: 1rem;
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 6px;
} */

/* Grid for images inside each item */
/* .image-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 0.5rem;
  margin-top: 1rem;
} */

/* Individual image */
/* .thumb {
  width: 100%;
  height: auto;
  object-fit: cover;
  border-radius: 4px;
  border: 1px solid #ccc;
} */

.loading-indicator,
.end-of-data {
  text-align: center;
  padding: 1rem;
  color: #888;
}

.sentinel {
  height: 1px;
}
</style>
