<template>
  <div class="card">
    <DataView
      :value="images"
      layout="grid"
      paginator
      :rows="30"
      :paginator="true"
      :lazy="true"
      @page="onPage($event)"
      :totalRecords="totalCount"
    >
      <template #grid="slotProps">
        <div class="grid grid-cols-12">
          <div
            v-for="(item, index) in slotProps.items"
            :key="index"
            class="col-span-12 sm:col-span-6 md:col-span-4 xl:col-span-6 p-2"
          >
            <div
              class="flex flex-row justify-between items-start gap-3"
              style="max-width: 350px; min-width: 350px"
            >
              <card style="height: 150px; width: 350px" v-if="item != undefined">
                <template #title>
                  <div class="grid">
                    <div class="col-10" no-gutters>
                      <media-image v-bind:mediaUrl="item" />
                    </div>
                    <div class="col-2" no-gutters>
                      <Button variant="link" @click="view(item)">
                        <small>{{ $t('records.view') }}</small>
                      </Button>
                    </div>
                  </div>
                </template>
                <template #subtitle>
                  <small>{{ item.collectionName }}</small>
                </template>
                <template #content>
                  <div style="font-size: 0.7em">
                    {{ item.catalogNumber }}
                  </div>
                  <div style="font-size: 0.7em">
                    {{ item.locality }} {{ item.country }} {{ item.continent }}
                  </div>
                  <div v-if="item.decimalLatitude != null" style="font-size: 0.7em">
                    Lat: {{ item.decimalLatitude }} Lon: {{ item.decimalLongitude }}
                  </div>
                </template>
              </card>
            </div>
          </div>
        </div>
      </template>
    </DataView>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import Service from '../Service'
import MediaImage from './baseComponents/MediaImage.vue'

const store = useStore()
const router = useRouter()

const service = new Service()

let records = ref(Array.from({ length: 50 }))
let images = ref([])
let dataset = ref()

const searchText = computed(() => {
  let text = store.getters['searchText']
  return text ? text : '*'
})

const scientificName = computed(() => {
  return store.getters['scientificName']
})

const isFuzzySearch = computed(() => {
  return store.getters['isFuzzySearch']
})

const hasCoordinates = computed(() => {
  return store.getters['filterCoordinates']
})

const isInSweden = computed(() => {
  return store.getters['filterInSweden']
})

const isType = computed(() => {
  return store.getters['filterType']
})

const startDate = computed(() => {
  return store.getters['startDate']
})

const endDate = computed(() => {
  return store.getters['endDate']
})

onMounted(async () => {
  console.log('media onMounted')

  await new Promise((res) => setTimeout(res, 500))

  fetchData(0, 30)
})

function buildImages() {
  records.value.forEach((record) => {
    const { associatedMedia, catalogNumber, collectionCode, scientificName } = record

    const botnayColection = 'vp, fungi, mosses, algae'
    const kbo = 'algae, fungi, mosses'
    const paleo = 'pz, pa'
    const zoo = 'ev, et, pi, he'
    const entCode = 'NHRS'

    let medias

    let smallImage = 'tumme'
    if (associatedMedia) {
      if (collectionCode === entCode) {
        associatedMedia.forEach((m) => {
          const imageId = m.split(' ')[0] + '&imgType=thumbs'

          images.value.push(imageId)
        })
      } else {
        if (botnayColection.includes(collectionCode)) {
          if (kbo.includes(collectionCode)) {
            dataset.value = '&dataset=kbo'
          } else {
            smallImage = 'mini'
            dataset.value = '&dataset=fbo'
          }
          medias = associatedMedia
            .filter((media) => !media.includes(smallImage))
            .map((a) => (a = a.match(/(?<=\[).+?(?=\])/g).toString()))
        } else {
          smallImage = 'thumb'
          if (paleo.includes(collectionCode)) {
            dataset.value = '&dataset=pal'
          } else if (zoo.includes(collectionCode)) {
            dataset.value = '&dataset=' + collectionCode
          }
          medias = associatedMedia.filter((media) => !media.includes(smallImage))
        }
        images.value.push(medias)
      }
    }

    console.log('images', images)
  })
}

function fetchData(start, end) {
  const fields = store.getters['fields']
  console.log('fields', fields)

  // const params = new URLSearchParams({
  //   text: searchText.value,
  //   scientificName: scientificName.value,
  //   fuzzySearch: isFuzzySearch.value,
  //   hasImages: true,
  //   isType: isType.value,
  //   isInSweden: isInSweden.value,
  //   hasCoordinates: hasCoordinates.value,
  //   startDate: startDate.value,
  //   endDate: endDate.value
  // })

  const params = new URLSearchParams({
    text: searchText.value,
    hasImages: true
  })

  if (scientificName.value) {
    params.set('scientificName', scientificName.value)
    params.set('fuzzySearch', isFuzzySearch.value)
  }

  if (isType.value) {
    params.set('isType', isType.value)
  }

  if (isInSweden.value) {
    params.set('isInSweden', isInSweden.value)
  }

  if (hasCoordinates.value) {
    params.set('hasCoordinates', hasCoordinates.value)
  }

  if (startDate.value) {
    params.set('startDate', startDate.value)
  }

  if (endDate.value) {
    params.set('endDate', endDate.value)
  }

  fields
    .filter((field) => field.text)
    .forEach((field) => {
      console.log('what...', field.value, field.text)
      params.set(field.value, field.text)
    })

  service
    .apiSearch(params, start, end)
    .then((response) => {
      records.value = response.response.docs
      const total = response.response.numFound
      store.commit('setTotalRecords', total)
    })
    .catch()
    .finally(() => {
      buildImages()
    })
}

const onPage = async (event) => {
  console.log('event')
  const { first, rows } = event
  await new Promise((res) => setTimeout(res, 500))

  fetchData(first, rows)
}

const totalCount = computed(() => {
  return store.getters['totalRecords']
})

function view(data) {
  store.commit('setSelectedRecord', data)
  router.push(`/record/${data.id}`)
}
</script>
<style scoped></style>
