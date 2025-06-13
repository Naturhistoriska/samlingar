<template>
  <Image
    :src="'https://media-service.nrm.se/images?id=' + mediaUrl"
    alt="Image"
    width="100%"
    preview
  />
</template>
<script setup>
const props = defineProps(['mediaUrl'])
</script>
<style scoped>
/* :v-deep {
  font-size: 16px;
} */
</style>
