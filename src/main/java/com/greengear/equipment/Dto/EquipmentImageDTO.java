package com.greengear.equipment.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EquipmentImageDTO {
private String imageType;
private byte[] image;
}
