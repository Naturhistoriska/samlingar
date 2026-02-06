<template>
  <footer class="footer">
    <div class="footer-content">
      <p class="footer-text">{{ t('footer.nrm') }} – {{ currentYear }}</p>

      <RouterLink
        :to="{ path: '/about', query: { tab: 'contact' } }"
        class="contactlink"
        aria-label="About us"
      >
        <i class="pi pi-envelope" aria-hidden="true"></i>
        <span class="contactlink-text">
          {{ t('common.contactus') }}
        </span>
      </RouterLink>
    </div>
  </footer>
</template>

<script setup>
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()

const currentYear = computed(() => new Date().getFullYear())
</script>

<style scoped>
.footer {
  padding: 1rem;
  width: 100%;
  box-sizing: border-box;
  background: #f6f4f4;
}

.footer-content {
  margin-left: 2em;
  margin-right: 2em;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.footer-text {
  font-size: 1.1rem;
  color: #4a4949;
}

.contactlink {
  display: inline-flex;
  align-items: center;
  gap: 0.4rem;
  font-size: 0.9rem;
  text-decoration: none;
  color: #333;
}

.contactlink:focus-visible {
  outline: 2px solid #005fcc;
  outline-offset: 2px;
}

@media (max-width: 768px) {
  .footer-content {
    flex-direction: column;
    align-items: flex-start;
  }

  .footer-text {
    margin-bottom: 0.5rem;
    font-size: 0.8rem;
  }

  .contactlink {
    font-size: 0.8rem;
  }
}
</style>
