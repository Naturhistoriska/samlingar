<template>
  <div class="card navbg">
    <div class="grid">
      <div class="col-8">
        <Tabs v-model:value="value">
          <TabList>
            <Tab value="0" to="/" as="router-link" @click="onClick">
              <i class="pi pi-home"></i> {{ $t('nav.home') }}
            </Tab>
            <Tab value="1" to="/about" as="router-link">
              {{ $t('nav.about') }}
            </Tab>
            <Tab value="2" to="/contact" as="router-link">
              {{ $t('nav.contactUs') }}
            </Tab>
          </TabList>
        </Tabs>
      </div>
      <div class="col text-right pr-4">
        <LocaleSwitcher />
      </div>
    </div>
  </div>
</template>
<script>
import LocaleSwitcher from './LocaleSwitcher.vue'
import { mapMutations } from 'vuex'

export default {
  components: {
    LocaleSwitcher
  },
  data() {
    return {
      value: '0'
    }
  },
  watch: {
    $route(to) {
      const { name } = to
      // this.routeName = name

      switch (name) {
        case 'Home':
          this.value = '0'
          break
        case 'About':
          this.value = '1'
          break
        case 'Contact':
          this.value = '2'
          break
        default:
          this.value = '0'
      }
    }
  },
  methods: {
    ...mapMutations(['setIsAdvanceSearch', 'setShowResults']),
    onClick() {
      this.setShowResults(false)
      this.setIsAdvanceSearch(false)
    }
  }
}
</script>

<style scoped>
a {
  cursor: pointer;
  text-decoration: none;
  color: #fff;
}

a:hover {
  color: rgb(166, 158, 158) !important;
}

.navbg {
  background-color: #052b1e;
}

.p-tablist-tab-list {
  border-style: none !important;
  border-radius: 0;
}

.p-tabs {
  flex-direction: row !important;
  border-color: transparent !important;
}

.p-tabmenu .p-tabmenu-tablist {
  /* flex-direction: row !important;
  border-color: transparent !important; */
  border-style: none !important;
  border: 0px;
}

.p-tabmenu-tablist {
  /* flex-direction: row !important;
  border-color: transparent !important; */
  border-style: none !important;
}

.p-tabmenu > .p-tabmenu-tablist {
  border-style: none !important;
}

/* .p-tabs {
  flex-direction: row !important;
  border-color: transparent !important;
}

.tabcss {
  text-decoration: none;
  border: none;
} */
/* --p-tabs-tab-border-color:  */
/* .p-tabs .p-tab .p-content {
  border-color: #3f3f4630;
} */
/* .p-tabs-tab-border-color {
  border-color: #3f3f4630;
  outline: 0;
} */
</style>
