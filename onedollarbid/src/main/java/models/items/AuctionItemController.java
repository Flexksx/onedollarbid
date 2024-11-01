package models.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
public class AuctionItemController {

    private final AuctionItemService auctionItemService;

    @Autowired
    public AuctionItemController(AuctionItemService auctionItemService) {
        this.auctionItemService = auctionItemService;
    }

    @PostMapping
    public ResponseEntity<AuctionItem> createItem(@RequestBody AuctionItem auctionItem) {
        AuctionItem savedItem = auctionItemService.addAuctionItem(auctionItem);
        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }
}
