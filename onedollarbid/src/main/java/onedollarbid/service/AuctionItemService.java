package onedollarbid.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import onedollarbid.model.AuctionItem;
import onedollarbid.repository.AuctionItemRepository;

@Service
public class AuctionItemService {
    @Autowired
    private AuctionItemRepository auctionItemRepository;

    public List<AuctionItem> findAll() {
        return auctionItemRepository.findAll();
    }

    public Optional<AuctionItem> findById(long id) {
        return auctionItemRepository.findById(id);
    }

    public AuctionItem save(AuctionItem auctionItem) {
        return auctionItemRepository.save(auctionItem);
    }

    public void deleteById(long id) {
        auctionItemRepository.deleteById(id);
    }

}
