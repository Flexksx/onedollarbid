<script setup>
import { ref, onMounted, watch } from 'vue'
import { Client } from '@stomp/stompjs'
import axiosInstance from '../../axios/axios';
import { useRoute } from 'vue-router';
const props = defineProps({
    userId: {
        type: String,
        required: true
    }
})
const route = useRoute();
const roomId = route.params.id;
const userId = ref(props.userId)
watch(() => props.userId, (newVal) => {
    userId.value = newVal
    console.log('userId changed to: ' + newVal);

})
const bids = ref([])
const connectionStatus = ref('disconnected')
const activeUsers = ref([])
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
const getUsernameById = async (userId) => {
    const response = await axiosInstance.get(`/users/${userId}`);
    return response.data.username;
}

const setConnected = (connected) => {
    if (connected) {
        stompClient.subscribe('/topic/bids', (bid) => {
            bids.value = JSON.parse(bid.body);
            bids.value.forEach(async (bid) => {
                bid.username = await getUsernameById(bid.userId);
            });
        });

        stompClient.subscribe('/topic/userJoined', async (message) => {
            const userId = message.body;
            const username = await getUsernameById(userId);
            activeUsers.value.push(username);
            bids.value.push({ username, amount: 'connected' });
        });

        stompClient.subscribe('/topic/userLeft', async (message) => {
            const userId = message.body;
            const username = await getUsernameById(userId);
            activeUsers.value = activeUsers.value.filter(user => user !== username);
            bids.value.push({ username, amount: 'disconnected' });
        });
    } else {
        bids.value = [];
    }
};


const fetchUserData = async (userId) => {
    const response = await axiosInstance.get(`/users/${userId}`);
    return response.data;
}


const fetchRoomData = async () => {
    const response = await axiosInstance.get(`/rooms/${roomId}`);
    return response.data;
}

const getParticipatingUsers = async (roomId) => {
    const roomData = await fetchRoomData();
    const userIds = roomData.userIds;
    const usersNames = [];
    for (const userId of userIds) {
        const user = await fetchUserData(userId);
        usersNames.push(user.username);
    }
    return usersNames;
}


let usernames = ref([]);
getParticipatingUsers(roomId).then((data) => {
    console.log(data);

    usernames.value = data;
});





onMounted(() => {
    connect();
})

const sendBid = () => {
    if (stompClient.connected && bidAmount.value > 0) {
        const requestBody = {
            userId: userId.value,
            amount: bidAmount.value
        }
        console.log(requestBody);
        const requestBodyJson = JSON.stringify(requestBody);
        console.log(requestBodyJson);


        stompClient.publish({ destination: '/app/bid', body: JSON.stringify(requestBody) });
        bidAmount.value = 0;
    }
}
</script>

<template>
    <div class="bids-box">
        <div class="bid-input mt-4">
            <input type="number" v-model="bidAmount" placeholder="Enter bid amount" class="border p-2 rounded" />
            <button @click="sendBid" class="bg-blue-500 text-white p-2 rounded ml-2">
                Place Bid
            </button>
        </div>
        <h3 class="text-xl font-bold mb-4">
            Bids
            <span class="text-sm ml-2" :class="{
                'text-green-500': connectionStatus === 'connected',
                'text-red-500': connectionStatus === 'error',
                'text-yellow-500': connectionStatus === 'disconnected'
            }">
                ({{ connectionStatus }})
            </span>
        </h3>
        <h4>
            Participating users:
            <span v-for="username in activeUsers" :key="username">
                {{ username }},
            </span>
        </h4>
        <div class="bids-list">
            <div v-for="bid in bids.slice().reverse()" :key="bid.id" class="bid bg-white shadow-sm rounded-lg p-3 mb-2">
                <p class="font-medium">
                    {{ bid.username }} has
                    <span v-if="bid.amount === 'connected'" class="text-blue-600">connected</span>
                    <span v-else-if="bid.amount === 'disconnected'" class="text-red-600">disconnected</span>
                    <span v-else class="text-green-600">${{ bid.amount }}</span>
                </p>

            </div>
            <div v-if="bids.length === 0" class="text-gray-500 text-center py-4">
                No bids yet
            </div>
        </div>
    </div>
</template>

<style scoped>
.bids-box {
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

h2,
h3,
h4 {
    margin-bottom: 1rem;
}

ul {
    list-style-type: none;
}

.bids-list {
    max-height: 300px;
    overflow-y: auto;
}

.bid {
    border-left: 4px solid #4CAF50;
}

.bid-input {
    display: flex;
    align-items: center;
}
</style>