package com.example.g31_ffs_be.repository;

import com.example.g31_ffs_be.model.ViewProfileHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewProfileHistoryRepository extends JpaRepository<ViewProfileHistory,Integer> {
}
