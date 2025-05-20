import axiosInstance from "../axios/axios";
import { AuctionItem } from "../models/AuctionItem";

export interface AuctionItemCreationPayload {
  name: string;
  startingPrice: number;
}

const AuctionItemService = {
  getAllAuctionItems: async (): Promise<AuctionItem[]> => {
    const response = await axiosInstance.get<AuctionItem[]>("/items");
    return response.data;
  },

  getAuctionItemsPaginated: async (
    offset: number,
    limit: number
  ): Promise<AuctionItem[]> => {
    const response = await axiosInstance.get<AuctionItem[]>("/items", {
      params: { offset, limit },
    });
    return response.data;
  },

  getAuctionItemById: async (id: number): Promise<AuctionItem> => {
    const response = await axiosInstance.get<AuctionItem>(`/items/${id}`);
    return response.data;
  },

  createAuctionItem: async (
    itemData: AuctionItemCreationPayload
  ): Promise<AuctionItem> => {
    const response = await axiosInstance.post<AuctionItem>("/items", itemData);
    return response.data;
  },

  updateAuctionItem: async (
    id: number,
    itemData: Partial<AuctionItem>
  ): Promise<AuctionItem> => {
    const response = await axiosInstance.put<AuctionItem>(
      `/items/${id}`,
      itemData
    );
    return response.data;
  },

  deleteAuctionItem: async (id: number): Promise<void> => {
    await axiosInstance.delete(`/items/${id}`);
  },

  saveAllAuctionItems: async (
    itemsData: AuctionItemCreationPayload[]
  ): Promise<AuctionItem[]> => {
    const response = await axiosInstance.post<AuctionItem[]>(
      "/items/batch",
      itemsData
    );
    return response.data;
  },
};

export default AuctionItemService;
