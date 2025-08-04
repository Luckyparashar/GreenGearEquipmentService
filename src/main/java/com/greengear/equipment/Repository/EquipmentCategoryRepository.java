package com.greengear.equipment.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greengear.equipment.Entities.EquipmentCategory;


public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, Long> {
	
	boolean existsByCategoryName(String categoryName);
	//to fetch complete details(=restaurant + food items)
		@Query("select e from EquipmentCategory e left join fetch e.equipment where e.categoryName=:categoryName")
		EquipmentCategory fetchCompleteDetails(String categoryName);
		Optional<EquipmentCategory> findByCategoryName(String categoryName);
}
