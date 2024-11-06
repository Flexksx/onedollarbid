package onedollarbid.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bids")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private AuctionItem item;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private AuctionRoom room;

    public Bid() {
    }

    public Bid(Double amount, User user, AuctionItem item, AuctionRoom room) {
        this.amount = amount;
        this.user = user;
        this.item = item;
        this.room = room;
    }

    public long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public User getUser() {
        return user;
    }

    public AuctionItem getItem() {
        return item;
    }

    public AuctionRoom getRoom() {
        return room;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setItem(AuctionItem item) {
        this.item = item;
    }

    public void setRoom(AuctionRoom room) {
        this.room = room;
    }

}
