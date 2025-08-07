package com.greengear.equipment.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.greengear.equipment.Entities.Equipment;



public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
	Optional<Equipment> findById(Long id);
	List<Equipment> findByOwnerId(Long ownerId);
	Page<Equipment> findByLocationOrderByPricePerDayAsc(String location, Pageable pageable);
	Page<Equipment> findByLocationOrderByPricePerDayDesc(String location, Pageable pageable);
}
