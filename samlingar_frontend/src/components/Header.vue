<template>
  <header class="app-header">
    <!-- Logo Section -->
    <div class="logo clickable" @click="openLink">
      <img src="/nrm-logo-liggande-svensk-negativ_copy.png" alt="Logo" class="logo-img" />
    </div>

    <!-- Search Box -->
    <div class="search-box">
      <Search />
    </div>
  </header>
</template>

<script setup>
import Search from './QuickSearch.vue'

function openLink() {
  window.open('https://www.nrm.se', '_blank', 'noopener,noreferrer')
}
</script>

<style scoped>
.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #144836;
  padding: 0.75rem 1rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  position: sticky;
  top: 0;
  z-index: 100;
}

.logo {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.logo-img {
  width: 260px;
  min-height: 7em;
  object-fit: contain;
}

/* Search box styling */
.search-box {
  flex: 1;
  display: flex;
  justify-content: flex-end;
  max-width: 400px;
}

.search-input {
  width: 100%;
  font-size: 1rem; /* ≥16px for mobile */
  padding: 0.5rem 0.75rem;
  border-radius: 6px;
}

.clickable {
  cursor: pointer;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .app-header {
    flex-direction: column;
    align-items: stretch;
    gap: 0.5rem;
  }

  .logo {
    justify-content: center;
  }

  .search-box {
    max-width: 100%;
  }
}
</style>
