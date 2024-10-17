<template>
  <Card>
    <template #title>
      <h2>{{ $t('contact.contactUs') }}</h2>
    </template>
    <template #content>
      <div>{{ $t('contact.text') }}</div>
      <div>
        <h2>{{ $t('contact.contactInfo') }}</h2>
      </div>
      <div>
        {{ $t('contact.departmentBio') }}<br />
        {{ $t('contact.pobox') }}<br />
        {{ $t('contact.poAddress') }}
      </div>
      <br />
      <br />
    </template>
    <template #footer>
      <div class="grid footerDiv">
        <div class="col-2 footerlabel">
          {{ $t('contact.phonelabel') }}
        </div>
        <div class="col-10">
          {{ phone }}
        </div>
      </div>
      <div class="grid footerDiv">
        <div class="col-2 footerlabel">
          {{ $t('contact.emailLabel') }}
        </div>
        <div class="col-10">
          {{ phone }}
        </div>
      </div>
    </template>
  </Card>
</template>
<script setup>
import { computed } from 'vue'

const phone = computed(() => {
  return import.meta.env.VITE_CONTACT_PHONE
})
const email = computed(() => {
  return import.meta.env.VITE_CONTACT_email
})
</script>
<style scoped>
.footerDiv {
  width: 500px;
}

.footerlabel {
  font-weight: bold;
}
</style>
