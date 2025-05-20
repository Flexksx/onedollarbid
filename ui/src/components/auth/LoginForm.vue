<script setup>
import { ref, computed } from 'vue';
import { useAuthStore } from '../../api/stores/authStore';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();

const username = ref('');
const password = ref('');
const formError = ref('');

const isLoading = computed(() => authStore.loading);
const storeError = computed(() => authStore.error);

const handleLogin = async () => {
  formError.value = '';
  
  // Form validation
  if (!username.value || !password.value) {
    formError.value = 'Username and password are required';
    return;
  }
  
  const success = await authStore.login({
    username: username.value,
    password: password.value
  });
  
  if (success) {
    // Redirect user after successful login
    router.push('/');
  }
};

// Demo token generation - for testing
const generateTestToken = async (role) => {
  const success = await authStore.generateToken({
    username: username.value || 'test-user',
    role: role
  });
  
  if (success) {
    router.push('/');
  }
};
</script>

<template>
  <div class="login-form">
    <h2>Login</h2>
    
    <div v-if="formError" class="error-message">
      {{ formError }}
    </div>
    
    <div v-if="storeError" class="error-message">
      {{ storeError }}
    </div>
    
    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <label for="username">Username</label>
        <input 
          type="text" 
          id="username" 
          v-model="username" 
          placeholder="Enter your username"
          required
        />
      </div>
      
      <div class="form-group">
        <label for="password">Password</label>
        <input 
          type="password" 
          id="password" 
          v-model="password" 
          placeholder="Enter your password"
          required
        />
      </div>
      
      <button type="submit" class="login-button" :disabled="isLoading">
        {{ isLoading ? 'Logging in...' : 'Login' }}
      </button>
    </form>
    
    <div class="demo-buttons">
      <h3>Demo Token Generation</h3>
      <button @click="generateTestToken('ADMIN')" class="admin-token-button">
        Get ADMIN Token
      </button>
      <button @click="generateTestToken('USER')" class="user-token-button">
        Get USER Token
      </button>
    </div>
  </div>
</template>

<style scoped>
.login-form {
  max-width: 400px;
  margin: 0 auto;
  padding: 2rem;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: #333;
}

.form-group {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.login-button {
  width: 100%;
  padding: 0.75rem;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  margin-top: 1rem;
}

.login-button:disabled {
  background-color: #a0a0a0;
  cursor: not-allowed;
}

.error-message {
  background-color: #ffebee;
  color: #d32f2f;
  padding: 0.75rem;
  border-radius: 4px;
  margin-bottom: 1rem;
}

.demo-buttons {
  margin-top: 2rem;
  border-top: 1px solid #ddd;
  padding-top: 1rem;
}

.demo-buttons h3 {
  font-size: 1rem;
  text-align: center;
  margin-bottom: 1rem;
}

.admin-token-button, .user-token-button {
  width: 100%;
  padding: 0.5rem;
  margin-bottom: 0.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.admin-token-button {
  background-color: #f44336;
  color: white;
}

.user-token-button {
  background-color: #2196F3;
  color: white;
}
</style>
