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

<script setup>
import Foot from './components/Footer.vue'
import Header from './components/Header.vue'
import Navigation from './components/Navigation.vue'
</script>

<style>
/* .p-inputtext {
  background: #000 !important;
  color: #fff !important;
} */
/* .p-fieldset {
  color: #fff !important;
} */
.p-fieldset-legend {
  background: none !important;
}
.p-select {
  background: #000 !important;
  /* color: #fff !important; */
}
/* .p-select-label {
  color: #fff !important;
} */
.p-card {
  /* color: #fff !important; */
  background: transparent !important;
}
.p-menu {
  /* color: #fff !important; */
  background: transparent !important;
}

.p-button-link {
  /* color: #fff !important; */
  background: transparent !important;
  text-decoration: underline;
}
.p-button-link:hover * {
  color: var(--p-emerald-500) !important;
  /* color: #9dabf3 !important; */
  text-decoration: none !important;
  background: transparent !important;
  font-size: 16px;
}
</style>
<style scoped>
.main-container {
  max-width: 100%;
  margin: 0 auto;
  padding: 1rem 2rem 2rem;
  min-height: 400px;
}
</style>
