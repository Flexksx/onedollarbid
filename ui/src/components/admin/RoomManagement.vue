<script setup>
import { ref, computed, onMounted } from 'vue';
import { useAuctionRoomStore } from '../../api/stores/auctionRoomStore';
import { useAuctionItemStore } from '../../api/stores/auctionItemStore';
import { useUserStore } from '../../api/stores/userStore';

const roomStore = useAuctionRoomStore();
const itemStore = useAuctionItemStore();
const userStore = useUserStore();

const searchQuery = ref('');
const isAddModalOpen = ref(false);
const selectedItemId = ref(null);

// Fetch all available items for the dropdown
onMounted(async () => {
  if (itemStore.items.length === 0) {
    await itemStore.fetchAllAuctionItems();
  }
  if (userStore.users.length === 0) {
    await userStore.fetchAllUsers();
  }
});

// Get available items (not sold and not already in a room)
const availableItems = computed(() => {
  const roomItemIds = roomStore.rooms.map(room => room.itemId);
  return itemStore.items.filter(item => 
    !item.sold && !roomItemIds.includes(item.id)
  );
});

// Filtered rooms based on search query
const filteredRooms = computed(() => {
  if (!searchQuery.value) return roomStore.rooms;
  
  const query = searchQuery.value.toLowerCase();
  return roomStore.rooms.filter(room => {
    const item = getItemForRoom(room);
    return item && item.name.toLowerCase().includes(query);
  });
});

// Get item details for a room
const getItemForRoom = (room) => {
  return itemStore.items.find(item => item.id === room.itemId);
};

// Get user details for a room
const getUsersForRoom = (room) => {
  if (!room.userIds || room.userIds.length === 0) return 'No users';
  
  return room.userIds.map(userId => {
    const user = userStore.users.find(u => u.id === userId);
    return user ? user.username : `User #${userId}`;
  }).join(', ');
};

// Open add modal
const openAddModal = () => {
  selectedItemId.value = availableItems.value.length > 0 ? availableItems.value[0].id : null;
  isAddModalOpen.value = true;
};

// Close add modal
const closeAddModal = () => {
  isAddModalOpen.value = false;
  selectedItemId.value = null;
};

// Create a new auction room
const createRoom = async () => {
  if (!selectedItemId.value) {
    alert('Please select an item for the auction room.');
    return;
  }
  
  try {
    await roomStore.createAuctionRoom({
      itemId: parseInt(selectedItemId.value)
    });
    closeAddModal();
  } catch (error) {
    console.error('Failed to create auction room:', error);
  }
};

// Delete a room
const deleteRoom = async (roomId) => {
  if (confirm('Are you sure you want to delete this auction room? This action cannot be undone.')) {
    try {
      await roomStore.deleteAuctionRoom(roomId);
    } catch (error) {
      console.error('Failed to delete auction room:', error);
    }
  }
};
</script>

<template>
  <div class="room-management">
    <h2>Auction Room Management</h2>
    
    <!-- Add Button and Search -->
    <div class="controls">
      <button @click="openAddModal" class="add-btn" :disabled="availableItems.length === 0">
        Create New Auction Room
      </button>
      <input 
        type="text" 
        v-model="searchQuery" 
        placeholder="Search rooms by item name..." 
        class="search-input"
      />
    </div>
    
    <div v-if="availableItems.length === 0" class="info-message">
      <p>No available items for new auction rooms. All items are either sold or already in a room.</p>
    </div>
    
    <!-- Rooms Table -->
    <div class="table-container">
      <table v-if="filteredRooms.length > 0" class="rooms-table">
        <thead>
          <tr>
            <th>Room ID</th>
            <th>Item</th>
            <th>Starting Price</th>
            <th>Participants</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="room in filteredRooms" :key="room.id">
            <td>{{ room.id }}</td>
            <td>{{ getItemForRoom(room)?.name || 'Unknown Item' }}</td>
            <td>${{ getItemForRoom(room)?.startingPrice || '0.00' }}</td>
            <td>{{ getUsersForRoom(room) }}</td>
            <td class="actions">
              <button @click="deleteRoom(room.id)" class="delete-btn">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
      
      <div v-else class="no-rooms">
        <p>No auction rooms found matching your search criteria.</p>
      </div>
    </div>
    
    <!-- Add Room Modal -->
    <div v-if="isAddModalOpen" class="modal-overlay" @click.self="closeAddModal">
      <div class="modal-content">
        <h3>Create New Auction Room</h3>
        
        <div class="form-group">
          <label for="itemSelect">Select Item</label>
          <select id="itemSelect" v-model="selectedItemId">
            <option v-for="item in availableItems" :key="item.id" :value="item.id">
              {{ item.name }} - ${{ item.startingPrice }}
            </option>
          </select>
        </div>
        
        <div class="modal-actions">
          <button @click="closeAddModal" class="cancel-btn">Cancel</button>
          <button @click="createRoom" class="save-btn">Create Room</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.room-management {
  padding: 20px;
}

h2 {
  margin-bottom: 20px;
  text-align: center;
  color: #333;
}

.controls {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.add-btn {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
}

.add-btn:disabled {
  background-color: #a0a0a0;
  cursor: not-allowed;
}

.search-input {
  width: 60%;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.info-message {
  background-color: #e3f2fd;
  color: #0d47a1;
  padding: 10px 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.table-container {
  overflow-x: auto;
}

.rooms-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.rooms-table th,
.rooms-table td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.rooms-table th {
  background-color: #f2f2f2;
  font-weight: bold;
}

.rooms-table tr:hover {
  background-color: #f9f9f9;
}

.actions {
  display: flex;
  gap: 10px;
}

.delete-btn {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  background-color: #f44336;
  color: white;
}

.no-rooms {
  text-align: center;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  max-width: 90%;
}

.modal-content h3 {
  margin-top: 0;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
}

.form-group select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.cancel-btn, .save-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.cancel-btn {
  background-color: #f2f2f2;
  color: #333;
}

.save-btn {
  background-color: #4CAF50;
  color: white;
}
</style>
