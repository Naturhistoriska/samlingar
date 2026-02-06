<template>
  <div>
    <!-- Title -->
    <h2 class="card-title">
      {{ $t('common.contactus') }}
    </h2>

    <!-- Content -->
    <div class="card-text">
      {{ $t('contact.text') }}
    </div>

    <div class="section-header">
      <h2>{{ $t('contact.contactInfo') }}</h2>
    </div>

    <div class="card-text">
      {{ $t('contact.departmentBio') }}<br />
      {{ $t('contact.pobox') }}<br />
      {{ $t('contact.poAddress') }}
    </div>

    <br />
    <br />

    <!-- Footer -->
    <div class="footer-grid">
      <div class="footer-label">
        {{ $t('contact.phonelabel') }}
      </div>
      <div class="footer-label">{{ phone }}</div>
    </div>

    <div class="footer-grid">
      <div class="footer-label">
        {{ $t('contact.emailLabel') }}
      </div>
      <div class="footer-value">
        {{ email }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const phone = computed(() => {
  return import.meta.env.VITE_CONTACT_PHONE
})
const email = computed(() => {
  return import.meta.env.VITE_CONTACT_EMAIL
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
