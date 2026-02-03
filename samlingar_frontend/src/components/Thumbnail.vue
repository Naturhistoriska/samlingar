<template>
  <Image
    :src="`https://media-service.nrm.se/images?id=${imageId}${dataset}`"
    alt="Image"
    width="60"
    preview
  />
</template>

<script setup>
import { computed, ref } from 'vue'
import Image from 'primevue/image'

const props = defineProps(['record', 'src'])

const botCollection = ref('algae, fungi, mosses', 'vp')
const zooCollection = ref('ev, et')
const kboCollection = ref('algae, fungi, mosses')
const palCollection = ref('pz, pb')

const dataset = computed(() => {
  const { collectionCode } = props.record.record
  console.log('record', props.record)

  console.log('collectionCode', collectionCode)

  if (kboCollection.value.includes(collectionCode)) {
    return '&dataset=kbo'
  } else if (palCollection.value.includes(collectionCode)) {
    return '&dataset=pal'
  } else if (zooCollection.value.includes(collectionCode)) {
    return `&dataset=${collectionCode}`
  } else if (collectionCode === 'HE') {
    return '&dataset=herps'
  } else if (collectionCode === 'PI') {
    return '&dataset=fish'
  } else if (collectionCode === 'vp') {
    return '&dataset=fbo'
  }
  return '&imgType=thumbs'
})

const imageId = computed(() => {
  const { collectionCode } = props.record.record
  if (botCollection.value.includes(collectionCode)) {
    return props.src.match(/(?<=\[).+?(?=\])/g).toString()
  }
  return props.src
})

// function openImage() {
//   console.log('openImage')

//   store.commit('setSelectedResult', props.result)

//   store.commit('setOpenGalleria', true)
// }

// const thumbnailsStyle = computed(() => {
//   const collectionId = props.collectionId
//   if (collectionId === 'mosses') {
//     return "width: 60px"
//   } else if (collectionId === "vp") {
//     return
//   }

//   return props.associatedMedias
// })
</script>
<style scoped></style>
