package onedollarbid.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import onedollarbid.model.AuctionRoom;

public interface AuctionRoomRepository extends JpaRepository<AuctionRoom, Long> {
}
