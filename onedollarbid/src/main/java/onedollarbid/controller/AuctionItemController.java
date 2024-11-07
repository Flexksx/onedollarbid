package onedollarbid.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;

import onedollarbid.model.AuctionItem;
import onedollarbid.service.AuctionItemService;

@RestController
@RequestMapping("/api/items")
public class AuctionItemController {
    @Autowired
    private AuctionItemService auctionItemService;

    @GetMapping("/{id}")
    public ResponseEntity<AuctionItem> getAuctionItemById(@PathVariable Long id) {
        Optional<AuctionItem> auctionItem = auctionItemService.findById(id);
        if (auctionItem.isPresent()) {
            return new ResponseEntity<>(auctionItem.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<AuctionItem>> getAllAuctionItems(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "8") int limit) {
        if (offset < 0 || limit < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<AuctionItem> auctionItems = auctionItemService.findAllWithPagination(offset, limit);
        return new ResponseEntity<>(auctionItems, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AuctionItem> createAuctionItem(@RequestBody AuctionItem auctionItem) {
        if (auctionItem == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AuctionItem createdAuctionItem = auctionItemService.save(auctionItem);
        return new ResponseEntity<>(createdAuctionItem, HttpStatus.CREATED);
    }

    @PostMapping("/json")
    public ResponseEntity<List<AuctionItem>> createAuctionItemsFromJson(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            List<AuctionItem> auctionItems = parseJsonFile(file);
            List<AuctionItem> createdAuctionItems = auctionItemService.saveAll(auctionItems);
            return new ResponseEntity<>(createdAuctionItems, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<AuctionItem> parseJsonFile(MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(file.getInputStream(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, AuctionItem.class));
    }

    @PostMapping("/csv")
    public ResponseEntity<List<AuctionItem>> createAuctionItems(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            List<AuctionItem> auctionItems = parseCsvFile(file);
            List<AuctionItem> createdAuctionItems = auctionItemService.saveAll(auctionItems);
            return new ResponseEntity<>(createdAuctionItems, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<AuctionItem> parseCsvFile(MultipartFile file) throws Exception {
        List<AuctionItem> auctionItems = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                AuctionItem item = new AuctionItem();
                item.setName(nextLine[0]);
                item.setStartingPrice(Double.parseDouble(nextLine[1]));
                auctionItems.add(item);
            }
        } catch (Exception e) {
            throw new Exception("Error parsing CSV file: " + e.getMessage());
        }
        return auctionItems;
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuctionItem> updateAuctionItem(@PathVariable Long id,
            @RequestBody AuctionItem updatedAuctionItem) {
        Optional<AuctionItem> existingAuctionItem = auctionItemService.findById(id);
        if (!existingAuctionItem.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            updatedAuctionItem.setId(id);
            AuctionItem savedAuctionItem = auctionItemService.save(updatedAuctionItem);
            return new ResponseEntity<>(savedAuctionItem, HttpStatus.OK);
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
