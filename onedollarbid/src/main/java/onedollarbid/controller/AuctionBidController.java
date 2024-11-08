package onedollarbid.controller;

import onedollarbid.model.Bid;
import onedollarbid.service.BidService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuctionBidController {

    @Autowired
    private final BidService bidService;

    public AuctionBidController(BidService bidService) {
        this.bidService = bidService;
    }

    // WebSocket endpoint to receive bids
    @MessageMapping("/bid")
    @SendTo("/topic/bids")
    public List<Bid> receiveBid(Bid bid) {
        bidService.save(bid);
        return bidService.findAll();
    }

    // HTTP endpoint to display bids
    @GetMapping("/bids")
    public String showBids(Model model) {
        model.addAttribute("bids", bidService.findAll()); // Retrieve all bids to display
        return "bids";
    }
}
