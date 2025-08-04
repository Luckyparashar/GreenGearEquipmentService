package com.greengear.equipment.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greengear.equipment.Service.EquipmentCategoryService;

import lombok.AllArgsConstructor;



@RestController
@RequestMapping("ms2/category")
@AllArgsConstructor
public class EquipmentCategoryController {
	private final EquipmentCategoryService equipmentCategoryService;

	@GetMapping("/{name}")
	public ResponseEntity<?> getMethodName(@PathVariable String name) {
		
		return ResponseEntity.ok(equipmentCategoryService.getCategoryWithEquipment(name));
	}
	
	
	
	
	
}
