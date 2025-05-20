<script setup>
import { ref, computed } from 'vue';
import { useAuthStore } from '../../api/stores/authStore';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();

const username = ref('');
const password = ref('');
const confirmPassword = ref('');
const role = ref('USER');
const formError = ref('');

const isLoading = computed(() => authStore.loading);
const storeError = computed(() => authStore.error);

const handleRegister = async () => {
  formError.value = '';
  
  // Form validation
  if (!username.value || !password.value) {
    formError.value = 'Username and password are required';
    return;
  }
  
  if (password.value !== confirmPassword.value) {
    formError.value = 'Passwords do not match';
    return;
  }
  
  const success = await authStore.register({
    username: username.value,
    password: password.value,
    role: role.value
  });
  
  if (success) {
    // Login with the newly created credentials
    await authStore.login({
      username: username.value,
      password: password.value
    });
    
    // Redirect user after successful registration and login
    router.push('/');
  }
};
</script>

<template>
  <div class="register-form">
    <h2>Register</h2>
    
    <div v-if="formError" class="error-message">
      {{ formError }}
    </div>
    
    <div v-if="storeError" class="error-message">
      {{ storeError }}
    </div>
    
    <form @submit.prevent="handleRegister">
      <div class="form-group">
        <label for="username">Username</label>
        <input 
          type="text" 
          id="username" 
          v-model="username" 
          placeholder="Choose a username"
          required
        />
      </div>
      
      <div class="form-group">
        <label for="password">Password</label>
        <input 
          type="password" 
          id="password" 
          v-model="password" 
          placeholder="Choose a password"
          required
        />
      </div>
      
      <div class="form-group">
        <label for="confirm-password">Confirm Password</label>
        <input 
          type="password" 
          id="confirm-password" 
          v-model="confirmPassword" 
          placeholder="Confirm your password"
          required
        />
      </div>
      
      <div class="form-group">
        <label for="role">Role</label>
        <select id="role" v-model="role">
          <option value="USER">User</option>
          <option value="ADMIN">Admin</option>
        </select>
      </div>
      
      <button type="submit" class="register-button" :disabled="isLoading">
        {{ isLoading ? 'Registering...' : 'Register' }}
      </button>
    </form>
    
    <div class="login-link">
      Already have an account? <router-link to="/login">Login</router-link>
    </div>
  </div>
</template>

<style scoped>
.register-form {
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

input, select {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.register-button {
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

.register-button:disabled {
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

.login-link {
  text-align: center;
  margin-top: 1rem;
}

a {
  color: #4CAF50;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}
</style>
