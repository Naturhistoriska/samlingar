<template>
  <v-container fluid>
    <v-row>
      <v-col class="col-lg-6 col-md-12 pl-md-10 pr-md-12">
        <h1>{{ $t('common.contactus') }}</h1>
        <p class="title font-weight-light text-darken-3">{{ $t('common.contactus') }}</p>
        <p>
          <v-icon>mdi-email</v-icon>
          <a class="ml-1 title text-decoration-none" :href="mailTo">{{ contactMail }}</a>
        </p>
        <p></p>
      </v-col>
    </v-row>
  </v-container>
</template>
<script>
const contact = import.meta.env.VITE_CONTACT
export default {
  name: 'Contact',
  computed: {
    contactMail: function () {
      return `${contact}`
    },
    mailTo: function () {
      return `mailto: ${contact}`
    }
  }
}
</script>
