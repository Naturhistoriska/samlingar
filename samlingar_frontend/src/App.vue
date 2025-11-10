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
/* GLOBAL STYLES — DO NOT USE scoped */
body .p-galleria-mask {
  background-color: black !important; /* or any color you want */
}

/* Optional: Remove or customize the modal shadow */
body .p-galleria {
  box-shadow: none !important;
  background-color: transparent !important;
}

/* Dialog/Sidebar/Generic overlay mask */
body .p-overlay-mask,
body .p-overlay-mask-enter {
  background-color: black !important;
}

.p-tooltip {
  max-width: 300px !important;
  white-space: normal !important;
  background-color: #144836 !important; /* Dark background */
  color: #fff !important; /* White text */
  border-radius: 6px !important;
  padding: 12px !important;
  font-size: 14px !important;
}

.p-fieldset-legend {
  background: none !important;
}
.p-select {
  background: #000 !important;
  /* color: #fff !important; */
}

.p-card {
  /* color: #fff !important; */
  background: transparent !important;
}

.p-button-link {
  /* color: #fff !important; */
  background: transparent !important;
  text-decoration: underline;
}
.p-button-link:hover * {
  /* color: var(--p-emerald-500) !important; */
  /* color: #9dabf3 !important; */
  color: #4b4949 !important;
  text-decoration: none !important;
  background: transparent !important;
  font-size: 16px;
}

/* .p-tabview-nav .p-tabview-nav-item.p-highlight .p-tabview-nav-link {
  background-color: #007bff !important;
  color: white !important;
}

.p-tab-active {
  background-color: #007bff;
  color: white;
  font-weight: bold;
}

.p-link {
  color: #007bff !important;
  font-weight: 500;
}

.p-link:hover {
  color: #0056b3 !important;
  text-decoration: underline;
} */

.my-custom-button {
  background-color: #144836 !important;
  border: none !important;
  color: white !important;
}

.my-custom-button:hover {
  background-color: #43a047;
}
</style>
<style scoped>
.main-container {
  max-width: 100%;
  margin: 0 auto;
  padding: 1rem 2rem 2rem;
  display: flex;
  flex-direction: column;
  min-height: 100vh; /* full viewport height */
}
</style>
