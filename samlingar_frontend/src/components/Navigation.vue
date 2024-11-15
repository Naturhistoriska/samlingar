<template>
  <div class="card">
    <div class="grid navbg">
      <div class="col-8" style="margin-left: 1em">
        <Tabs v-model:value="value">
          <TabList>
            <Tab value="0" to="/" as="router-link" @click="onClick" class="navbg">
              <i class="pi pi-home navbg"></i> {{ $t('nav.home') }}
            </Tab>
            <Tab value="1" to="/about" as="router-link" class="navbg">
              {{ $t('nav.about') }}
            </Tab>
            <Tab value="2" to="/contact" as="router-link" class="navbg">
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

.p-tab {
  border-style: none !important;
}

.p-tab:hover {
  background: #0b583d !important;
}

.p-tabmenu .p-tabmenu-tablist {
  border-style: none !important;
  border: 0px;
}

.p-tabmenu-tablist {
  border-style: none !important;
}

.p-tabmenu > .p-tabmenu-tablist {
  border-style: none !important;
}

.pi {
  background-color: transparent !important;
}

.pi:hover {
  background: #0b583d !important;
}
</style>
