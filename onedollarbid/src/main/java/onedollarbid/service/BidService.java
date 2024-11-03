package onedollarbid.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import onedollarbid.model.Bid;
import onedollarbid.repository.BidRepository;

@Service
public class BidService {
    @Autowired
    private BidRepository bidRepository;

    public List<Bid> findAll() {
        return bidRepository.findAll();
    }

    public Optional<Bid> findById(long id) {
        return bidRepository.findById(id);
    }

    public Bid save(Bid bid) {
        return bidRepository.save(bid);
    }

    public void deleteById(long id) {
        bidRepository.deleteById(id);
    }

}
