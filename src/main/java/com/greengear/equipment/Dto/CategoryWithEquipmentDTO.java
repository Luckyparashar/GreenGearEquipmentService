package com.greengear.equipment.Dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryWithEquipmentDTO {
	private Long id;
    private String categoryName;
    private String categoryDescription;
    private List<EquipmentDTO> equipment;
}
