import { createRouter, createWebHistory } from 'vue-router';
import Items from '../components/items/Items.vue';
import AuctionRoom from '../components/rooms/AuctionRoom.vue';

const routes = [
    {
        path: '/',
        name: 'Items',
        component: Items,
    },
    {
        path: '/rooms/:id',
        name: 'AuctionRoom',
        component: AuctionRoom,
        props: true,
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;

