package onedollarbid.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import onedollarbid.model.Bid;

public interface BidRepository extends JpaRepository<Bid, Long> {

}
