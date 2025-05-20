import axiosInstance from "../axios/axios";
import { AuctionRoom } from "../models/AuctionRoom";

export interface AuctionRoomCreationPayload {
  itemId: number;
}

const AuctionRoomService = {
  getAllAuctionRooms: async (): Promise<AuctionRoom[]> => {
    const response = await axiosInstance.get<AuctionRoom[]>("/rooms");
    return response.data;
  },

  getAuctionRoomById: async (id: number): Promise<AuctionRoom> => {
    const response = await axiosInstance.get<AuctionRoom>(`/rooms/${id}`);
    return response.data;
  },

  createAuctionRoom: async (
    roomData: AuctionRoomCreationPayload
  ): Promise<AuctionRoom> => {
    const response = await axiosInstance.post<AuctionRoom>("/rooms", roomData);
    return response.data;
  },

  deleteAuctionRoom: async (id: number): Promise<void> => {
    await axiosInstance.delete(`/rooms/${id}`);
  },

  addUserToRoom: async (
    roomId: number,
    userId: number
  ): Promise<AuctionRoom> => {
    const response = await axiosInstance.put<AuctionRoom>(
      `/rooms/${roomId}/addUser/${userId}`
    );
    return response.data;
  },

  
};

export default AuctionRoomService;
