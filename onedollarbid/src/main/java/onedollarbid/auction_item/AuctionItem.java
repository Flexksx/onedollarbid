package onedollarbid.auction_item;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class AuctionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double startingPrice;

    @Column(nullable = true)
    private Double soldPrice;

    @Column(nullable = true)
    private Boolean sold = false;

    public AuctionItem() {
    }

    public AuctionItem(String name, String description, Double startingPrice) {
        this.name = name;
        this.startingPrice = startingPrice;
        this.sold = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(Double startingPrice) {
        this.startingPrice = startingPrice;
    }

    public Double getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(Double soldPrice) {
        this.soldPrice = soldPrice;
    }

    public Boolean getSold() {
        return sold;
    }

    public void setSold(Boolean sold) {
        this.sold = sold;
    }

}
