package onedollarbid.auction_room;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuctionRoomService {
    @Autowired
    private AuctionRoomRepository auctionRoomRepository;

    public List<AuctionRoom> findAll() {
        return auctionRoomRepository.findAll();
    }

    public Optional<AuctionRoom> findById(long id) {
        return auctionRoomRepository.findById(id);
    }

    public AuctionRoom save(AuctionRoom auctionRoom) {
        return auctionRoomRepository.save(auctionRoom);
    }

    public void deleteById(long id) {
        auctionRoomRepository.deleteById(id);
    }

}
