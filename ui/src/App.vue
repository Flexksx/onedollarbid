<script setup>
import router from './router/index';  // Import router instance
</script>

<template>
  <div>
    <router-view />
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
</style>