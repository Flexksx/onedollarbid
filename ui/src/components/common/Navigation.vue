<script setup>
import { computed } from 'vue';
import { useAuthStore } from '../../api/stores/authStore';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();

const isAuthenticated = computed(() => authStore.isAuthenticated);
const isAdmin = computed(() => authStore.hasRole('ADMIN'));

const logout = () => {
  authStore.logout();
  router.push('/login');
};
</script>

<template>
  <nav class="main-nav">
    <div class="brand">
      <router-link to="/">OneDollarBid</router-link>
    </div>
    
    <div class="nav-links">
      <router-link to="/" class="nav-link">Home</router-link>
      
      <template v-if="isAuthenticated">
        <router-link v-if="isAdmin" to="/admin" class="nav-link admin-link">Admin Dashboard</router-link>
        <a @click="logout" class="nav-link logout-link">Logout</a>
      </template>
      
      <template v-else>
        <router-link to="/login" class="nav-link">Login</router-link>
        <router-link to="/register" class="nav-link">Register</router-link>
      </template>
    </div>
  </nav>
</template>

<style scoped>
.main-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  background-color: #333;
  color: white;
}

.brand a {
  color: white;
  font-size: 1.5rem;
  font-weight: bold;
  text-decoration: none;
}

.nav-links {
  display: flex;
  gap: 1.5rem;
}

.nav-link {
  color: white;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.2s;
  cursor: pointer;
}

.nav-link:hover {
  color: #4CAF50;
}

.admin-link {
  color: #FFC107;
}

.logout-link {
  color: #FF5252;
}
</style>
