package com.greengear.equipment.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greengear.equipment.Entities.Equipment;
import java.util.List;



public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
	Optional<Equipment> findById(Long id);
	List<Equipment> findByOwnerId(Long ownerId);

}
