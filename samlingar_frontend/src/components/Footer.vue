<template>
  <div class="footer">
    <div class="footer-content">
      <span class="grey--text text--darken-3">
        {{ $t('footer.nrm') }} - {{ new Date().getFullYear() }}
      </span>
      <Button
        icon="pi pi-envelope"
        :label="$t('contact.contactUs')"
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
.contactlink {
  cursor: pointer;
  text-decoration: none;
  /* color: #fff; */
}
/* a {
  cursor: pointer;
}

.footer {
  max-height: 50px;
  margin-top: 6.9rem;
  width: 100%;
  position: relative;
  margin-bottom: 2.85rem;
} */

/* .footer::before {
  content: '';
  display: block;
  position: absolute;
  height: 1px;
  width: 100%;
  top: 50%;
  background-color: rgba(255, 255, 255, 0.1);
  z-index: -1;
} */

.footer .footer-content {
  display: flex;
  width: 100%;
  justify-content: space-between;
  padding: 0 6rem;
  /* align-items: center;

  max-width: 1448px;
  margin: 0 auto; */
}

/* .footer .footer-content .license,
.socials {
  display: flex;
  padding: 7.5px;
  align-items: center;
  gap: 0.857rem;
  border-radius: 48px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  background-color: #1e2226;
  text-decoration: none;
  color: #fff;
  transition: color 0.3s;
} */
/*
.footer .footer-content .license:hover {
  color: #41b883df;
} */
/*
.footer .footer-content .socials a {
  display: flex;
  color: #fff;
  text-decoration: none;
}

.footer .footer-content .socials a i {
  transition: color 0.3s;
}

.footer .footer-content .socials a:hover i {
  color: #41b883df;
} */
/*
.footer .footer-content .footer-logo {
  display: flex;
  width: 44px;
  height: 44px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
  border-radius: 48px;
  background-color: #1e2226;
  border: 1px solid rgba(255, 255, 255, 0.1);
  transition: all 0.3s;
}

.footer .footer-content .footer-logo img {
  width: 17.711px;
  height: 20px;
} */

/* .footer .footer-content .footer-logo:hover {
  background-color: #2b3035;
} */
</style>
