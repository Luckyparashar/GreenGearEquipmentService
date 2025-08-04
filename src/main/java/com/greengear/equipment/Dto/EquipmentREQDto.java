package com.greengear.equipment.Dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EquipmentREQDto {

	    private Long id;
	    private String name;
	    private double pricePerDay;
	    private boolean availability;
	    private String location;
	    private String description;
	    private Long userId;
	     
}
