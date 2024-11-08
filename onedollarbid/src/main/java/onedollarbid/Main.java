package onedollarbid;

import onedollarbid.model.User;
import onedollarbid.model.AuctionItem;
import onedollarbid.service.AuctionItemService;
import onedollarbid.service.BidService;
import onedollarbid.service.UserService;

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

        Map<String, Double> testAuctionItems = new HashMap<>();
        testAuctionItems.put("BMW X4", 10000.0);
        testAuctionItems.put("Pot plant", 10.0);
        testAuctionItems.put("Gibson Limited Edition", 2000.0);
        testAuctionItems.put("AirPods Max", 500.0);
        testAuctionItems.put("Asus Vivobook", 800.0);
        testAuctionItems.put("Pencil 3000", 5.0);
        testAuctionItems.put("MacBook Pro", 2000.0);
        testAuctionItems.put("Samsung Galaxy S21", 800.0);
        testAuctionItems.put("Apple Watch Series 6", 500.0);
        testAuctionItems.put("Sony WH-1000XM4", 500.0);
        testAuctionItems.put("Nintendo Switch", 500.0);
        testAuctionItems.put("PlayStation 5", 500.0);
        testAuctionItems.put("Xbox Series X", 500.0);
        testAuctionItems.put("Apple AirTag", 20.0);
        testAuctionItems.put("Apple AirPods Pro", 200.0);
        testAuctionItems.put("Apple AirPods", 150.0);
        for (Map.Entry<String, Double> entry : testAuctionItems.entrySet()) {
            AuctionItem auctionItem = new AuctionItem();
            auctionItem.setName(entry.getKey());
            auctionItem.setStartingPrice(entry.getValue());
            AuctionItem createdAuctionItem = auctionItemService.save(auctionItem);
            System.out.println("Created Auction Item: " + createdAuctionItem.getName());
        }
        System.out.println("User deleted successfully");
    }
}
