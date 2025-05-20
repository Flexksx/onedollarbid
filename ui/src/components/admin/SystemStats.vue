<script setup>
import { computed } from 'vue';
import { useUserStore } from '../../api/stores/userStore';
import { useAuctionItemStore } from '../../api/stores/auctionItemStore';
import { useAuctionRoomStore } from '../../api/stores/auctionRoomStore';

const userStore = useUserStore();
const itemStore = useAuctionItemStore();
const roomStore = useAuctionRoomStore();

const userCount = computed(() => userStore.users.length);
const itemCount = computed(() => itemStore.items.length);
const roomCount = computed(() => roomStore.rooms.length);

// Calculate active auctions (rooms with non-sold items)
const activeAuctions = computed(() => {
  let count = 0;
  for (const room of roomStore.rooms) {
    const item = itemStore.items.find(item => item.id === room.itemId);
    if (item && !item.sold) {
      count++;
    }
  }
  return count;
});

// Calculate total value of all items
const totalItemValue = computed(() => {
  return itemStore.items.reduce((sum, item) => {
    return sum + (item.startingPrice || 0);
  }, 0);
});
</script>

<template>
  <div class="system-stats">
    <h2>System Overview</h2>
    
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon user-icon">👥</div>
        <div class="stat-details">
          <h3>Total Users</h3>
          <div class="stat-number">{{ userCount }}</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon item-icon">📦</div>
        <div class="stat-details">
          <h3>Total Items</h3>
          <div class="stat-number">{{ itemCount }}</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon room-icon">🏠</div>
        <div class="stat-details">
          <h3>Total Rooms</h3>
          <div class="stat-number">{{ roomCount }}</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon auction-icon">🔨</div>
        <div class="stat-details">
          <h3>Active Auctions</h3>
          <div class="stat-number">{{ activeAuctions }}</div>
        </div>
      </div>
    </div>
    
    <div class="additional-stats">
      <div class="value-stat">
        <h3>Total Item Value</h3>
        <div class="value">${{ totalItemValue.toFixed(2) }}</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.system-stats {
  padding: 20px;
}

h2 {
  margin-bottom: 30px;
  text-align: center;
  color: #333;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

@media (min-width: 768px) {
  .stats-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}

.stat-card {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  font-size: 2.5rem;
  margin-right: 15px;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}

.user-icon {
  background-color: #e3f2fd;
  color: #2196F3;
}

.item-icon {
  background-color: #e8f5e9;
  color: #4CAF50;
}

.room-icon {
  background-color: #fff3e0;
  color: #FF9800;
}

.auction-icon {
  background-color: #fce4ec;
  color: #E91E63;
}

.stat-details {
  flex: 1;
}

.stat-details h3 {
  margin: 0;
  font-size: 1rem;
  color: #555;
}

.stat-number {
  font-size: 1.8rem;
  font-weight: bold;
  color: #333;
}

.additional-stats {
  margin-top: 30px;
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.value-stat h3 {
  margin: 0 0 10px;
  font-size: 1.2rem;
  color: #555;
}

.value {
  font-size: 2rem;
  font-weight: bold;
  color: #4CAF50;
}
</style>
