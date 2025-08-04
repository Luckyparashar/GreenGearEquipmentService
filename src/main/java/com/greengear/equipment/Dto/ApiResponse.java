package com.greengear.equipment.Dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ApiResponse {
private String message;
private LocalDateTime timeStamp;
public ApiResponse(String message) {
	this.timeStamp=LocalDateTime.now();
	this.message = message;
}

}
