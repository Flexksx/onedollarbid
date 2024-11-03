package onedollarbid.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import onedollarbid.model.AuctionItem;

public interface AuctionItemRepository extends JpaRepository<AuctionItem, Long> {
}
