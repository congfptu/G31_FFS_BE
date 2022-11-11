package com.example.g31_ffs_be.repository;

import com.example.g31_ffs_be.model.Fee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeRepository extends JpaRepository<Fee,Integer> {
    Fee findByName(String name);
}
