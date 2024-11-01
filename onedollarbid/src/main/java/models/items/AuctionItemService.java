package models.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuctionItemService {

    private final AuctionItemRepository auctionItemRepository;

    @Autowired
    public AuctionItemService(AuctionItemRepository auctionItemRepository) {
        this.auctionItemRepository = auctionItemRepository;
    }

    public AuctionItem addAuctionItem(AuctionItem auctionItem) {
        return auctionItemRepository.save(auctionItem);
    }
}
