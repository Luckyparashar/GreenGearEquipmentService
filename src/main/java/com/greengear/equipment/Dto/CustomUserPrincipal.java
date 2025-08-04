package com.greengear.equipment.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CustomUserPrincipal {
private String username;
private Long userId;
	public CustomUserPrincipal(String username, Long userId) {
this.userId=userId;
this.username=username;

	}

}
