<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axiosInstance from '../../api/axios/axios';
import { defineEmits } from 'vue';

const emit = defineEmits(['userJoin']);
const route = useRoute();
const roomId = route.params.id;

const username = ref('');
const userData = ref(null);

// Function to create a new user
const createNewUser = async () => {
  const requestBody = {
    username: username.value,
  };

  try {
    // Attempt to create the new user
    const response = await axiosInstance.post('/users', requestBody);
    userData.value = response.data; 
    emit('userJoin', userData.value.id); 
  } catch (error) {
    // Handle conflict (409) when the username already exists
    console.log('Error status:', error.response.status);
    if (error.response && error.response.status === 409) {
      console.log('User conflict error: Username already exists.');
      console.log("Attempting to find the existing user...");
      const allUsers = await getAllUsers();
      console.log("All users:", allUsers);
      const existingUser = await findUserByUsername(username.value, allUsers);
      console.log("Existing user:", existingUser);
      if (existingUser) {
        emit('userJoin', existingUser.id); 
      } else {
        console.error('User conflict error: User not found.');
      }
    } else {
      console.error('An error occurred while creating the user:', error); 
    }
  }
};

const getAllUsers = async () => {
  try {
    const response = await axiosInstance.get('/users');
    return response.data;
  } catch (error) {
    console.error('Error fetching users:', error);
  }
};

const findUserByUsername = async (username, allUsers) => {
  return allUsers.find((user) => user.username === username);
};

onMounted(() => {
  console.log(`Room ID from URL: ${roomId}`);
});
</script>

<template>
  <form class="join-form" @submit.prevent="createNewUser">
    <div class="join-button">
      <input type="text" class="username-input" v-model="username" placeholder="Enter your username" />
      <button type="submit" class="join-button">Join Room</button>
    </div>
  </form>
</template>

<style scoped>
.div {
  padding: 20px;
  background-color: #f9f9f9;
  display: inline;
}

.username-input {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-bottom: 10px;
}

.join-button {
  padding: 20px;
  background-color: #f9f9f9;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-top: 20px;
}

.join-button button {
  background-color: #007bff;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>
