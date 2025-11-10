<template>
  <div class="grid">
    <div class="col-12" no-gutters>
      <p class="m-0 textStyle">{{ $t('startPage.text1') }}</p>
      <br />
      <p class="m-0 textStyle">{{ $t('startPage.text2') }}</p>
      <br />
      <p class="m-0 textStyle">{{ $t('startPage.text3') }}</p>
      <br />
      <p class="m-0 textStyle">{{ $t('startPage.text4') }}</p>
    </div>
  </div>
</template>
<script setup></script>
<style scoped>
.textStyle {
  padding-right: 1em;
  font-size: 1.1rem;
}

@media (max-width: 768px) {
  .textStyle {
    font-size: 0.6rem; /* 14px for mobile */
  }
}
</style>
