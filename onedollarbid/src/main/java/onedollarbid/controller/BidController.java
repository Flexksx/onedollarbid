package onedollarbid.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import onedollarbid.model.Bid;
import onedollarbid.service.BidService;

@RestController
@RequestMapping("/api/bids")
public class BidController {
    @Autowired
    private BidService bidService;

    @GetMapping
    public List<Bid> getAllBids() {
        return bidService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bid> getBidById(@PathVariable Long id) {
        Optional<Bid> bid = bidService.findById(id);
        if (bid.isPresent()) {
            return new ResponseEntity<>(bid.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Bid> createBid(@RequestBody Bid bid) {
        if (bid == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Bid createdBid = bidService.save(bid);
        return new ResponseEntity<>(createdBid, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBid(@PathVariable Long id) {
        Optional<Bid> bid = bidService.findById(id);
        if (bid.isPresent()) {
            bidService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bid> updateBid(@PathVariable Long id, @RequestBody Bid updatedBid) {
        Optional<Bid> existingBid = bidService.findById(id);
        if (existingBid.isPresent()) {
            updatedBid.setId(id);
            Bid savedBid = bidService.save(updatedBid);
            return new ResponseEntity<>(savedBid, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
