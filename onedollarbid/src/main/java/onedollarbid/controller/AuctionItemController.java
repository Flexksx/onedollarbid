package onedollarbid.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import onedollarbid.model.AuctionItem;
import onedollarbid.service.AuctionItemService;

@RestController
@RequestMapping("/api/auction-items")
public class AuctionItemController {
    @Autowired
    private AuctionItemService auctionItemService;

    @GetMapping
    public List<AuctionItem> getAllAuctionItems() {
        return auctionItemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionItem> getAuctionItemById(@PathVariable Long id) {
        Optional<AuctionItem> auctionItem = auctionItemService.findById(id);
        if (auctionItem.isPresent()) {
            return new ResponseEntity<>(auctionItem.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<AuctionItem> createAuctionItem(@RequestBody AuctionItem auctionItem) {
        if (auctionItem == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AuctionItem createdAuctionItem = auctionItemService.save(auctionItem);
        return new ResponseEntity<>(createdAuctionItem, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuctionItem> updateAuctionItem(@PathVariable Long id,
            @RequestBody AuctionItem updatedAuctionItem) {
        Optional<AuctionItem> existingAuctionItem = auctionItemService.findById(id);
        if (existingAuctionItem.isPresent()) {
            updatedAuctionItem.setId(id);
            AuctionItem savedAuctionItem = auctionItemService.updateAuctionItem(id, updatedAuctionItem);
            return new ResponseEntity<>(savedAuctionItem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AuctionItem> deleteAuctionItem(@PathVariable Long id) {
        Optional<AuctionItem> auctionItem = auctionItemService.findById(id);
        if (auctionItem.isPresent()) {
            auctionItemService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
