package com.greengear.equipment.Dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class BookingDTO {

	 private Long id;
	   
	    private Long equipmentId;
	    private LocalDate startDate;
	    private LocalDate endDate;
	    private String status;
	    
}
