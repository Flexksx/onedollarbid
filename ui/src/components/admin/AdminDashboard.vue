<script setup>
import { ref, onMounted, computed } from 'vue';
import { useUserStore } from '../../api/stores/userStore';
import { useAuctionItemStore } from '../../api/stores/auctionItemStore';
import { useAuctionRoomStore } from '../../api/stores/auctionRoomStore';
import { useAuthStore } from '../../api/stores/authStore';
import UserManagement from './UserManagement.vue';
import ItemManagement from './ItemManagement.vue';
import RoomManagement from './RoomManagement.vue';
import SystemStats from './SystemStats.vue';

const activeTab = ref('stats');
const userStore = useUserStore();
const itemStore = useAuctionItemStore();
const roomStore = useAuctionRoomStore();
const authStore = useAuthStore();

// Make sure we're authorized - additional security check
const isAdmin = computed(() => authStore.hasRole('ADMIN'));

// Fetch initial data
onMounted(async () => {
  if (isAdmin.value) {
    await Promise.all([
      userStore.fetchAllUsers(),
      itemStore.fetchAllAuctionItems(),
      roomStore.fetchAllAuctionRooms()
    ]);
  }
});

// Tab management
const setActiveTab = (tab) => {
  activeTab.value = tab;
};
</script>

<template>
  <div v-if="isAdmin" class="admin-dashboard">
    <h1>Admin Dashboard</h1>
    
    <!-- Navigation Tabs -->
    <div class="admin-tabs">
      <button 
        @click="setActiveTab('stats')" 
        :class="{ active: activeTab === 'stats' }"
      >
        Dashboard
      </button>
      <button 
        @click="setActiveTab('users')" 
        :class="{ active: activeTab === 'users' }"
      >
        Users
      </button>
      <button 
        @click="setActiveTab('items')" 
        :class="{ active: activeTab === 'items' }"
      >
        Items
      </button>
      <button 
        @click="setActiveTab('rooms')" 
        :class="{ active: activeTab === 'rooms' }"
      >
        Auction Rooms
      </button>
    </div>
    
    <!-- Tab Content -->
    <div class="tab-content">
      <SystemStats v-if="activeTab === 'stats'" />
      <UserManagement v-if="activeTab === 'users'" />
      <ItemManagement v-if="activeTab === 'items'" />
      <RoomManagement v-if="activeTab === 'rooms'" />
    </div>
  </div>
  
  <div v-else class="unauthorized">
    <h2>Unauthorized Access</h2>
    <p>You do not have permission to access the admin dashboard.</p>
    <router-link to="/" class="back-link">Return to Home</router-link>
  </div>
</template>

<style scoped>
.admin-dashboard {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

h1 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.admin-tabs {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
  border-bottom: 1px solid #e0e0e0;
}

.admin-tabs button {
  padding: 12px 24px;
  margin: 0 8px;
  background: none;
  border: none;
  border-bottom: 3px solid transparent;
  cursor: pointer;
  font-size: 1rem;
  color: #555;
  transition: all 0.3s ease;
}

.admin-tabs button:hover {
  color: #2196F3;
}

.admin-tabs button.active {
  color: #2196F3;
  border-bottom: 3px solid #2196F3;
  font-weight: 600;
}

.tab-content {
  background-color: #f9f9f9;
  border-radius: 8px;
  padding: 20px;
  min-height: 500px;
}

.unauthorized {
  text-align: center;
  padding: 50px;
  background-color: #f8d7da;
  border-radius: 8px;
  max-width: 600px;
  margin: 100px auto;
}

.unauthorized h2 {
  color: #721c24;
}

.back-link {
  display: inline-block;
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #28a745;
  color: white;
  text-decoration: none;
  border-radius: 4px;
}

.back-link:hover {
  background-color: #218838;
}
</style>
