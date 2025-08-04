package com.greengear.equipment.Service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.greengear.equipment.Dto.CategoryWithEquipmentDTO;
import com.greengear.equipment.Entities.EquipmentCategory;
import com.greengear.equipment.Exception.ResourceNotFoundException;
import com.greengear.equipment.Repository.EquipmentCategoryRepository;

import lombok.AllArgsConstructor;
@Service
@Transactional
@AllArgsConstructor
public class EquipmentCategoryServiceImpl implements EquipmentCategoryService {
public final EquipmentCategoryRepository categoryRepository;
public final ModelMapper mapper;
	@Override
	public CategoryWithEquipmentDTO getCategoryWithEquipment(String category) {
		// validate if restaurant name - distinct
				if (!categoryRepository.existsByCategoryName(category))
					throw new ResourceNotFoundException
					(" Category Name not found");

		EquipmentCategory details = categoryRepository.fetchCompleteDetails(category);
		return mapper.map(details, CategoryWithEquipmentDTO.class);
	}

}
