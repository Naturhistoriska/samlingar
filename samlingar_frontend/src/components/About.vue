<template>
  <div>
    <!-- Title -->
    <h1 class="card-title">
      {{ $t('about.aboutSamlingar') }}
    </h1>

    <!-- Content -->
    <div class="card-text">
      {{ $t('about.text1') }}
    </div>

    <div class="section-header">
      <h2>APIs</h2>
    </div>

    <div class="card-text">
      {{ $t('about.apiText') }}
    </div>
    <div class="card-text">
      {{ $t('about.apiDocumention') }}: <a :href="swaggar" target="_blank">{{ swaggar }} </a>.
    </div>

    <div class="section-header">
      <h2>Version</h2>
    </div>

    <div class="card-text">
      <span>Version {{ appVersion }}</span>
    </div>
    <p>
      {{ $t('about.github') }}
      <a href="https://github.com/Naturhistoriska/Samlingar" target="_blank"> Github </a>
    </p>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const appVersion = __APP_VERSION__

const swaggar = computed(() => {
  return import.meta.env.VITE_SAMLINGAR_SWAGGAR
})
</script>
<style scoped>
/* Card Title */
.card-title {
  font-size: 1.3rem;
  margin-bottom: 1rem;
}

/* Card text / content */
.card-text {
  font-size: 1rem;
  line-height: 1.5;
  margin-bottom: 1rem;
}

/* Section headers inside card */
.section-header h2 {
  font-size: 1.1rem;
  margin-top: 2em;
  margin-bottom: 0.5em;
}

/* Footer grid for contact info */
.footer-grid {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 0.5rem;
  gap: 0.25rem;
}

.footer-label {
  flex: 0 0 auto; /* shrink to fit content */
  font-weight: bold;
  margin-right: 0.5rem;
}

.footer-value {
  flex: 1 1 auto; /* take remaining space */
}

/* 📱 Mobile adjustments */
@media (max-width: 768px) {
  .card-title {
    font-size: 1rem;
  }

  .card-text {
    font-size: 0.8rem;
  }

  .section-header h2 {
    font-size: 1rem;
  }

  .footer-grid {
    flex-direction: column; /* stack label + value */
    gap: 0.25rem;
  }

  .footer-label,
  .footer-value {
    flex: 1 1 100%;
    font-size: 0.8rem;
  }
}
</style>
