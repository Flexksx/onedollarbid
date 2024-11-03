package onedollarbid.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import onedollarbid.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
