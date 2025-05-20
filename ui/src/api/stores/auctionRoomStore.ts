import { defineStore } from "pinia";
import AuctionRoomService, {
  AuctionRoomCreationPayload,
} from "../services/AuctionRoomService";
import { AuctionRoom } from "../models/AuctionRoom";

interface AuctionRoomState {
  rooms: AuctionRoom[];
  currentRoom: AuctionRoom | null;
  loading: boolean;
  error: string | null;
}

export const useAuctionRoomStore = defineStore("auctionRoom", {
  state: (): AuctionRoomState => ({
    rooms: [],
    currentRoom: null,
    loading: false,
    error: null,
  }),
  actions: {
    async fetchAllAuctionRooms() {
      this.loading = true;
      this.error = null;
      try {
        this.rooms = await AuctionRoomService.getAllAuctionRooms();
      } catch (e: any) {
        this.error = e.message || "Failed to fetch auction rooms";
      } finally {
        this.loading = false;
      }
    },
    async fetchAuctionRoomById(id: number) {
      this.loading = true;
      this.error = null;
      try {
        this.currentRoom = await AuctionRoomService.getAuctionRoomById(id);
      } catch (e: any) {
        this.error = e.message || `Failed to fetch auction room ${id}`;
        this.currentRoom = null;
      } finally {
        this.loading = false;
      }
    },
    async createAuctionRoom(roomData: AuctionRoomCreationPayload) {
      this.loading = true;
      this.error = null;
      try {
        const newRoom = await AuctionRoomService.createAuctionRoom(roomData);
        this.rooms.push(newRoom);
        this.currentRoom = newRoom; // Optionally set as current
      } catch (e: any) {
        this.error = e.message || "Failed to create auction room";
      } finally {
        this.loading = false;
      }
    },
    async deleteAuctionRoom(id: number) {
      this.loading = true;
      this.error = null;
      try {
        await AuctionRoomService.deleteAuctionRoom(id);
        this.rooms = this.rooms.filter((room) => room.id !== id);
        if (this.currentRoom?.id === id) {
          this.currentRoom = null;
        }
      } catch (e: any) {
        this.error = e.message || `Failed to delete auction room ${id}`;
      } finally {
        this.loading = false;
      }
    },
    async addUserToRoom(roomId: number, userId: number) {
      this.loading = true;
      this.error = null;
      try {
        const updatedRoom = await AuctionRoomService.addUserToRoom(
          roomId,
          userId
        );
        const index = this.rooms.findIndex((room) => room.id === roomId);
        if (index !== -1) {
          this.rooms[index] = updatedRoom;
        }
        if (this.currentRoom?.id === roomId) {
          this.currentRoom = updatedRoom;
        }
      } catch (e: any) {
        this.error = e.message || `Failed to add user to room ${roomId}`;
      } finally {
        this.loading = false;
      }
    },
    // Example for removeUserFromRoom if you implement it in the service
    // async removeUserFromRoom(roomId: number, userId: number) {
    //   this.loading = true;
    //   this.error = null;
    //   try {
    //     const updatedRoom = await AuctionRoomService.removeUserFromRoom(roomId, userId);
    //     const index = this.rooms.findIndex(room => room.id === roomId);
    //     if (index !== -1) {
    //       this.rooms[index] = updatedRoom;
    //     }
    //     if (this.currentRoom?.id === roomId) {
    //       this.currentRoom = updatedRoom;
    //     }
    //   } catch (e: any) {
    //     this.error = e.message || `Failed to remove user from room ${roomId}`;
    //   } finally {
    //     this.loading = false;
    //   }
    // },
  },
});
