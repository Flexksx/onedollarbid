<script setup>
import { ref, onMounted } from 'vue'
import { Client } from '@stomp/stompjs'

const bids = ref([])
const connectionStatus = ref('disconnected')
const bidAmount = ref(0)

const stompClient = new Client({
    brokerURL: 'ws://localhost:8080/ws'
});

stompClient.onConnect = (frame) => {
    connectionStatus.value = 'connected';
    setConnected(true);
    console.log('Connected: ' + frame);
};

stompClient.onWebSocketError = (error) => {
    connectionStatus.value = 'error';
    console.error('Error: ' + error + error.message);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

const connect = () => {
    stompClient.activate();
}

const disconnect = () => {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

const setConnected = (connected) => {
    if (connected) {
        stompClient.subscribe('/topic/bids', (bid) => {
            bids.value = JSON.parse(bid.body);
        });
    } else {
        bids.value = [];
    }
}

onMounted(() => {
    connect();
})

const sendBid = () => {
    if (stompClient.connected && bidAmount.value > 0) {
        stompClient.publish({ destination: '/app/bid', body: JSON.stringify({ amount: bidAmount.value }) });
        bidAmount.value = 0;  // Reset input after sending
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
        <div class="bid-input mt-4">
            <input 
                type="number" 
                v-model="bidAmount" 
                placeholder="Enter bid amount" 
                class="border p-2 rounded"
            />
            <button 
                @click="sendBid" 
                class="bg-blue-500 text-white p-2 rounded ml-2"
            >
                Place Bid
            </button>
        </div>
    </div>
</template>
