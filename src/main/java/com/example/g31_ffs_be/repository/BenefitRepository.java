package com.example.g31_ffs_be.repository;

import com.example.g31_ffs_be.model.Benefit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BenefitRepository extends JpaRepository<Benefit,Integer> {
}