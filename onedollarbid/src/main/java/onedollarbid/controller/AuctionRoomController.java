package onedollarbid.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import onedollarbid.model.AuctionRoom;
import onedollarbid.service.AuctionRoomService;

@RestController
@RequestMapping("/api/rooms")
public class AuctionRoomController {

    @Autowired
    private AuctionRoomService auctionRoomService;

    @GetMapping
    public List<AuctionRoom> getAllAuctionRooms() {
        return auctionRoomService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionRoom> getAuctionRoomById(@PathVariable Long id) {
        Optional<AuctionRoom> auctionRoom = auctionRoomService.findById(id);
        if (auctionRoom.isPresent()) {
            return new ResponseEntity<>(auctionRoom.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<AuctionRoom> createAuctionRoom(@RequestBody AuctionRoom auctionRoom) {
        if (auctionRoom == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AuctionRoom createdAuctionRoom = auctionRoomService.save(auctionRoom);
        return new ResponseEntity<>(createdAuctionRoom, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuctionRoom> updateAuctionRoom(@PathVariable Long id,
            @RequestBody AuctionRoom updatedAuctionRoom) {
        Optional<AuctionRoom> existingAuctionRoom = auctionRoomService.findById(id);
        if (existingAuctionRoom.isPresent()) {
            updatedAuctionRoom.setId(id);
            AuctionRoom savedAuctionRoom = auctionRoomService.save(updatedAuctionRoom);
            return new ResponseEntity<>(savedAuctionRoom, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuctionRoom(@PathVariable Long id) {
        Optional<AuctionRoom> auctionRoom = auctionRoomService.findById(id);
        if (auctionRoom.isPresent()) {
            auctionRoomService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
