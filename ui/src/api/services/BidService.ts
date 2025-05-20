import axiosInstance from "../axios/axios";
import { Bid } from "../models/Bid";

export interface BidCreationPayload {
  amount: number;
  userId: number;
  // Potentially itemId if bids are directly associated with items outside a room context via REST
}

const BidService = {
  getAllBids: async (): Promise<Bid[]> => {
    const response = await axiosInstance.get<Bid[]>("/bids");
    return response.data;
  },

  getBidById: async (id: number): Promise<Bid> => {
    const response = await axiosInstance.get<Bid>(`/bids/${id}`);
    return response.data;
  },

  createBid: async (bidData: BidCreationPayload): Promise<Bid> => {
    const response = await axiosInstance.post<Bid>("/bids", bidData);
    return response.data;
  },

  deleteBid: async (id: number): Promise<void> => {
    await axiosInstance.delete(`/bids/${id}`);
  },
};

export default BidService;
