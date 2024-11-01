package models.items;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import org.springframework.lang.Nullable;

@Entity
public class AuctionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Nullable
    @Column(nullable = true)  // Optional, can be omitted since it's nullable by default
    private String description;

    @Nullable
    @Column(nullable = true)  // Optional, can be omitted since it's nullable by default
    private String category;

    private int startingPrice;

    // No-argument constructor required by JPA
    public AuctionItem() {}

    // Parameterized constructor
    public AuctionItem(String name, String description, String category, int startingPrice) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.startingPrice = startingPrice;
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public String getCategory() {
        return category;
    }

    public int getStartingPrice() {
        return startingPrice;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public void setCategory(@Nullable String category) {
        this.category = category;
    }

    public void setStartingPrice(int startingPrice) {
        this.startingPrice = startingPrice;
    }
}
