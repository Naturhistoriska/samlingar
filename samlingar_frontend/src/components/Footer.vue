<template>
  <div class="footer">
    <div class="footer-content">
      <span class="footer-text"> {{ $t('footer.nrm') }} - {{ new Date().getFullYear() }} </span>
      <Button
        icon="pi pi-envelope"
        :label="$t('common.contactus')"
        @click="onContackLinkclick()"
        class="contactlink"
        link
      />
    </div>
  </div>
</template>
<script>
export default {
  methods: {
    onContackLinkclick() {
      const pushUrl = '/contact'
      this.$router.push(pushUrl)
    }
  }
}
</script>
<style scoped>
.footer {
  /* background-color: #f5f5f5;    */
  padding: 1rem;
  width: 100%;
  box-sizing: border-box;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap; /* allow wrapping on mobile */
  gap: 0.5rem;
}

.footer-text {
  font-size: 0.9rem;
  color: #4a4949b6;
}

.contactlink {
  cursor: pointer;
  font-size: 0.9rem;
  text-decoration: none;
  color: #4a4949b6;
}

/* .footer .footer-content {
  display: flex;
  width: 100%;
  justify-content: space-between;
  padding: 0 3em;
} */

@media (max-width: 768px) {
  .footer-content {
    flex-direction: column; /* stack items */
    align-items: flex-start;
  }

  .footer-text {
    margin-bottom: 0.5rem; /* spacing between text and button */
    font-size: 0.8em;
  }

  .contactlink {
    font-size: 0.8rem;
    color: #4a4949b6;
  }
}
</style>
