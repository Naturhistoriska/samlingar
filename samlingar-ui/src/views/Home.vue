<template>
  <div id="container" class="container container--fluid">
    <Results />
  </div>
</template>

<script setup>
import Results from '@/components/Results.vue'
// import Main from '@/components/Main.vue'
</script>
<style scoped>
#container {
  position: relative;
  background: transparent;
  /* overflow: auto; */
  width: 100%;
  height: 100%; /* To make sure height is ok in Safari*/
}

#infoi {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  z-index: 0;
}
</style>
