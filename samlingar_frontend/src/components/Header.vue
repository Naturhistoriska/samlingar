<template>
  <header class="app-header">
    <!-- Logo / Home link -->
    <a
      href="https://www.nrm.se"
      target="_blank"
      rel="noopener noreferrer"
      class="logo"
      aria-label="Open NRM website in a new tab"
    >
      <img
        src="/nrm-logo-liggande-svensk-negativ_copy.png"
        alt="Naturhistoriska riksmuseet"
        class="logo-img"
      />
    </a>

    <!-- Search -->
    <div class="search-box">
      <!-- Ensure Search component has an accessible label internally -->
      <Search />
    </div>
  </header>
</template>

<script setup>
import Search from './QuickSearch.vue'
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

/* Logo link */
.logo {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  text-decoration: none;
}

/* Visible focus state */
.logo:focus-visible {
  outline: 3px solid #ffffff;
  outline-offset: 4px;
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
