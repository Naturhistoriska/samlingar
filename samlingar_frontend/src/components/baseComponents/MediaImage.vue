<template>
  <div>
    <Image
      :src="'https://media-service.nrm.se/images?id=' + mediaUrl + dataset"
      alt="Image"
      width="100%"
      preview
    />
  </div>
</template>
<script setup>
const props = defineProps(['mediaUrl', 'dataset'])
</script>
<style scoped>
/* :v-deep {
  font-size: 16px;
} */
</style>
