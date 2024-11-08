package onedollarbid.model;

import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class AuctionRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @ElementCollection
    @CollectionTable(name = "room_users", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "user_id")
    private Set<Long> userIds;

    public AuctionRoom() {
    }

    public AuctionRoom(Long itemId) {
        this.itemId = itemId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Set<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(Set<Long> userIds) {
        this.userIds = userIds;
    }

    public void addUserId(Long userId) {
        this.userIds.add(userId);
    }

    public void removeUserId(Long userId) {
        this.userIds.remove(userId);
    }
}
