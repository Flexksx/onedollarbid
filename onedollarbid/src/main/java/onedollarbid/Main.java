package onedollarbid;

import onedollarbid.model.User;
import onedollarbid.model.AuctionItem;
import onedollarbid.service.AuctionItemService;
import onedollarbid.service.BidService;
import onedollarbid.service.UserService;
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

        String[] testUserNames = { "john_doe", "jill_doe", "jack_doe", "jane_doe", "joe_doe", "jim_doe" };
        for (String username : testUserNames) {
            User user = new User();
            user.setUsername(username);
            user.setBalance(100.0);
            User createdUser = userService.save(user);
            System.out.println("Created User: " + createdUser.getUsername());
        }

        String[] auctionItemsNames = { "BMW X4", "Pot plant", "Gibson Limited Edition", "AirPods Max", "Asus Vivobook",
                "Pencil 3000" };
        Double[] auctionItemsPrices = { 10000.0, 10.0, 2000.0, 500.0, 800.0, 5.0 };
        for (int i = 0; i < auctionItemsNames.length; i++) {
            AuctionItem auctionItem = new AuctionItem();
            auctionItem.setName(auctionItemsNames[i]);
            auctionItem.setStartingPrice(auctionItemsPrices[i]);
            AuctionItem createdAuctionItem = auctionItemService.save(auctionItem);
            System.out.println("Created Auction Item: " + createdAuctionItem.getName());
        }

        System.out.println("User deleted successfully");
    }
}
