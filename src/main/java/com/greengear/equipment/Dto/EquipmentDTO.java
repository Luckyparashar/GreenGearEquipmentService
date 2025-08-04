package com.greengear.equipment.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentDTO {

	    private Long id;
	    private String name;
	    private double pricePerDay;
	    private boolean availability;
	    private String location;
	    private String description;

}
