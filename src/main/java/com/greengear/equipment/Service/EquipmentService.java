package com.greengear.equipment.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.greengear.equipment.Dto.ApiResponse;
import com.greengear.equipment.Dto.EquipmentDTO;
import com.greengear.equipment.Dto.EquipmentImageDTO;
import com.greengear.equipment.Dto.EquipmentREQDto;

public interface EquipmentService {

	EquipmentDTO getEquipmentDetails(Long id);

	ApiResponse addEquipment(EquipmentREQDto dto, String name,MultipartFile image, Long userId)throws IOException;

	ApiResponse addEquipmentImage(MultipartFile image, Long id)throws IOException ;

	EquipmentImageDTO getEquipmentImage(Long id);

	ApiResponse deleteEquipment(Long id, Long userId);

	ApiResponse toggleEquipmentAvailability(Long userId, Long id);

	boolean checkEquipmentAvailability(Long id);

	List<EquipmentDTO> getAllEquipment();

	List<EquipmentDTO> getAllEquipmentByUserId(Long userId);

}
