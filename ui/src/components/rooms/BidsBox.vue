<script type="module">
  import { Client } from '@stomp/stompjs';

  const client = new Client({
    brokerURL: 'ws://localhost:8080/ws',
    onConnect: () => {
      client.subscribe('/topic', message =>
        console.log(`Received: ${message.body}`)
      );
      client.publish({ destination: '/topic', body: 'First Message' });
    },
  });

  client.activate();
</script>
<script setup>

import { ref, onMounted, onUnmounted } from 'vue'
import { Client } from '@stomp/stompjs'
const bids = ref([])
const connectionStatus = ref('disconnected')
const stompClient = ref(null)
const connectWebSocket = () => {
    stompClient.value = new Client({
        brokerURL: 'ws://localhost:8080/ws',
    })

    stompClient.value.onConnect = () => {
        connectionStatus.value = 'connected'
        console.log('Connected to WebSocket')
        stompClient.value.subscribe('/topic/bids', (message) => {
            const bid = JSON.parse(message.body)
            bids.value = [bid, ...bids.value]
        })
    }
}

const disconnectWebSocket = () => {
    if (stompClient.value) {
        stompClient.value.disconnect(() => {
            connectionStatus.value = 'disconnected'
            console.log('Disconnected from WebSocket')
        })
    }
}

onMounted(() => {
    connectWebSocket()
})

onUnmounted(() => {
    disconnectWebSocket()
})

const sendBid = (amount) => {
    if (stompClient.value && stompClient.value.connected) {
        stompClient.value.send("/app/bid", {}, 
            JSON.stringify({ amount: amount, username: "TestUser" })
        )
    }
}
</script>


<template>
    <div class="bids-box">
        <h3 class="text-xl font-bold mb-4">
            Bids 
            <span 
                class="text-sm ml-2"
                :class="{
                    'text-green-500': connectionStatus === 'connected',
                    'text-red-500': connectionStatus === 'error',
                    'text-yellow-500': connectionStatus === 'disconnected'
                }"
            >
                ({{ connectionStatus }})
            </span>
        </h3>
        <div class="bids-list">
            <div 
                v-for="bid in bids" 
                :key="bid.id" 
                class="bid bg-white shadow-sm rounded-lg p-3 mb-2"
            >
                <p class="font-medium">
                    {{ bid.username }}: 
                    <span class="text-green-600">${{ bid.amount }}</span>
                </p>
            </div>
            <div v-if="bids.length === 0" class="text-gray-500 text-center py-4">
                No bids yet
            </div>
        </div>
    </div>
</template>
