<script setup>
import Foot from './components/Footer.vue'
import Header from './components/Header.vue'
import Navigation from './components/Navigation.vue'
</script>

<template>
  <div class="main-wrapper">
    <Header />
    <Navigation />
    <div class="main-container">
      <router-view />
    </div>
    <Foot />
  </div>
</template>
<style>
.p-card {
  color: #fff !important;
}
.p-button-text {
  color: #fff !important;
}
.p-button-link {
  color: #fff !important;
  /* text-decoration: underline; */
}
.texthover {
  color: #2f57f7 !important;
  text-decoration: underline;
}
</style>
<style scoped>
.main-container {
  max-width: 100%;
  margin: 0 auto;
  padding: 5.5rem 6rem 3rem;
  min-height: 400px;
}
</style>
