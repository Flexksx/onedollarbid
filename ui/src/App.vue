<script setup>
import router from './router/index';  // Import router instance
import Navigation from './components/common/Navigation.vue';
import { useAuthStore } from './api/stores/authStore';
import { onMounted } from 'vue';

const authStore = useAuthStore();

onMounted(() => {
  // Initialize auth state on app load
  authStore.init();
});
</script>

<template>
  <div>
    <Navigation />
    <div class="app-content">
      <router-view />
    </div>
  </div>
</template>

<style>
body {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  margin: 0;
  padding: 0;
}

.app-content {
  padding: 2rem;
}
</style>