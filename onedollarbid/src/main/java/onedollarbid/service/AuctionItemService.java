package onedollarbid.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

    public Optional<AuctionItem> findById(Long id) {
        return auctionItemRepository.findById(id);
    }

    public AuctionItem save(AuctionItem auctionItem) {
        return auctionItemRepository.save(auctionItem);
    }

    public void deleteById(Long id) {
        auctionItemRepository.deleteById(id);
    }

    public AuctionItem updateAuctionItem(Long id, AuctionItem updatedAuctionItem) {
        return auctionItemRepository.save(updatedAuctionItem);
    }

    public List<AuctionItem> saveAll(List<AuctionItem> auctionItems) {
        return auctionItemRepository.saveAll(auctionItems);
    }

    public List<AuctionItem> findAllWithPagination(int offset, int limit) {
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit);
        Page<AuctionItem> pagedResult = auctionItemRepository.findAll(pageable);
        return pagedResult.getContent();
    }

}
