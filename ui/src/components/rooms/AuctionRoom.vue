<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from '../../axios/axios';
import JoinButton from './JoinButton.vue';
import BidsBox from './BidsBox.vue';
const route = useRoute();
const roomId = route.params.id; // Access the roomId from the URL

const username = ref('');

// Fetch user data (you can adjust this based on your app’s logic)
const getUserData = async (userId) => {
  try {
    const response = await axios.get(`/users/${userId}`);
    return response.data;
  } catch (error) {
    console.error(error);
  }
};



const getRoomData = async () => {
  try {
    const response = await axios.get(`/rooms/${roomId}`);
    return response.data;
  } catch (error) {
    console.error(error);
  }
};

const getRoomItemData = async (roomId) => {
  try {
    const roomResponse = await axios.get(`/rooms/${roomId}`);
    const itemId = roomResponse.data.itemId;
    const response = await axios.get(`/items/${itemId}`);
    return response.data;
  } catch (error) {
    console.error(error);
  }
}

let productNameLabel = ref('Loading...');
let startingPriceLabel = ref(0);

const roomItemData = getRoomItemData(roomId);
const roomData = getRoomData();

roomItemData.then((data) => {
  console.log(data);
  productNameLabel.value = data.name;
  startingPriceLabel.value = data.startingPrice;
});

if (username.value) {
  const userData = getUserData(userId);
  userData.then((data) => {
    console.log(data);
    userId.value = data.id;
  });
}

const addUserToRoom = async (userId) => {
  try {
    const response = await axios.put(`/rooms/${roomId}/addUser/${userId}`);
    console.log('User added to room:', response.data);
  } catch (error) {
    console.error(error);
  }
};

// Handle updated username from JoinButton
const handleUsernameUpdated = (newUserId) => {
  console.log('Username updated:', newUserId);
  addUserToRoom(newUserId);
  const fetchedUserData = getUserData(newUserId);
  fetchedUserData.then((data) => {
    console.log(data);
    username.value = data.username;
  });
};

onMounted(() => {
  console.log(`Room ID from URL: ${roomId}`);
});
</script>

<template>
  <div class="auction-room">
    <h2>{{ productNameLabel }}</h2>
    <p>Starting Price: ${{ startingPriceLabel }}</p>
    <p v-if="username">Welcome, {{ username }}!</p> 
    <p v-else>Please enter your username to join the auction.</p>    
    <JoinButton @userJoin="handleUsernameUpdated" />
  </div>
  <BidsBox />
</template>

<style scoped>
.auction-room {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  max-width: 600px;
  margin: 0 auto;
}

button {
  background-color: #4CAF50;
  color: white;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
  border-radius: 8px;
}

button:hover {
  opacity: 0.8;
}

h2, h3, h4 {
  margin-bottom: 1rem;
}

ul {
  list-style-type: none;
  padding-left: 0;
}
</style>
