<script setup>
import { ref, computed, watch } from 'vue';
import { useUserStore } from '../../api/stores/userStore';

const userStore = useUserStore();
const searchQuery = ref('');
const selectedUser = ref(null);
const isModalOpen = ref(false);

const newUsername = ref('');

// Filtered users based on search query
const filteredUsers = computed(() => {
  if (!searchQuery.value) return userStore.users;
  
  const query = searchQuery.value.toLowerCase();
  return userStore.users.filter(user => 
    user.username.toLowerCase().includes(query)
  );
});

// Select a user for editing
const selectUser = (user) => {
  selectedUser.value = { ...user };
  newUsername.value = user.username;
  isModalOpen.value = true;
};

// Close the edit modal
const closeModal = () => {
  isModalOpen.value = false;
  selectedUser.value = null;
  newUsername.value = '';
};

// Save user changes
const saveUser = async () => {
  if (!selectedUser.value) return;
  
  try {
    await userStore.updateUser(selectedUser.value.id, {
      username: newUsername.value
    });
    closeModal();
  } catch (error) {
    console.error('Failed to update user:', error);
  }
};

// Delete a user
const deleteUser = async (userId) => {
  if (confirm('Are you sure you want to delete this user? This action cannot be undone.')) {
    try {
      await userStore.deleteUser(userId);
    } catch (error) {
      console.error('Failed to delete user:', error);
    }
  }
};

// Refresh users when component is active
watch(() => userStore.users, () => {
  if (isModalOpen.value && selectedUser.value) {
    const updatedUser = userStore.users.find(u => u.id === selectedUser.value.id);
    if (updatedUser) {
      selectedUser.value = { ...updatedUser };
      newUsername.value = updatedUser.username;
    }
  }
}, { deep: true });
</script>

<template>
  <div class="user-management">
    <h2>User Management</h2>
    
    <!-- Search and Filter -->
    <div class="search-container">
      <input 
        type="text" 
        v-model="searchQuery" 
        placeholder="Search users..." 
        class="search-input"
      />
    </div>
    
    <!-- Users Table -->
    <div class="table-container">
      <table v-if="filteredUsers.length > 0" class="users-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in filteredUsers" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.username }}</td>
            <td class="actions">
              <button @click="selectUser(user)" class="edit-btn">Edit</button>
              <button @click="deleteUser(user.id)" class="delete-btn">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
      
      <div v-else class="no-users">
        <p>No users found matching your search criteria.</p>
      </div>
    </div>
    
    <!-- Edit User Modal -->
    <div v-if="isModalOpen" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <h3>Edit User</h3>
        
        <div class="form-group">
          <label for="username">Username</label>
          <input 
            type="text" 
            id="username" 
            v-model="newUsername" 
            placeholder="Enter username"
          />
        </div>
        
        <div class="modal-actions">
          <button @click="closeModal" class="cancel-btn">Cancel</button>
          <button @click="saveUser" class="save-btn">Save Changes</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.user-management {
  padding: 20px;
}

h2 {
  margin-bottom: 20px;
  text-align: center;
  color: #333;
}

.search-container {
  margin-bottom: 20px;
}

.search-input {
  width: 100%;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.table-container {
  overflow-x: auto;
}

.users-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.users-table th,
.users-table td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.users-table th {
  background-color: #f2f2f2;
  font-weight: bold;
}

.users-table tr:hover {
  background-color: #f9f9f9;
}

.actions {
  display: flex;
  gap: 10px;
}

.edit-btn, .delete-btn {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.edit-btn {
  background-color: #2196F3;
  color: white;
}

.delete-btn {
  background-color: #f44336;
  color: white;
}

.no-users {
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

.form-group input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
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
