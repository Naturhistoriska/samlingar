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

<style scoped>
.main-container {
  max-width: 100%;
  margin: 0 auto;
  padding: 5.5rem 6rem 3rem;
  min-height: 400px;
}
</style>
