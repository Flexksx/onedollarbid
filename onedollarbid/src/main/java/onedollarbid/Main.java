package onedollarbid;

import onedollarbid.auction_item.AuctionItem;
import onedollarbid.auction_item.AuctionItemService;
import onedollarbid.bid.BidService;
import onedollarbid.user.UserService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private UserService userService;
    @Autowired
    private AuctionItemService auctionItemService;
    @Autowired
    private BidService bidService;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting OneDollarBid application...");
    }

}
