package com.huuloc.bookstore.bbook.repository;

import com.huuloc.bookstore.bbook.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
