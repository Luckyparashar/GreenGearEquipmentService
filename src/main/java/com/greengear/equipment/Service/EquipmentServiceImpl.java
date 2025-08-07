package com.greengear.equipment.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.greengear.equipment.Dto.ApiResponse;
import com.greengear.equipment.Dto.EquipmentDTO;
import com.greengear.equipment.Dto.EquipmentImageDTO;
import com.greengear.equipment.Dto.EquipmentREQDto;
import com.greengear.equipment.Entities.Equipment;
import com.greengear.equipment.Entities.EquipmentCategory;
import com.greengear.equipment.Exception.ApiException;
import com.greengear.equipment.Exception.InvalidInputException;
import com.greengear.equipment.Exception.ResourceNotFoundException;
import com.greengear.equipment.Repository.EquipmentCategoryRepository;
import com.greengear.equipment.Repository.EquipmentRepository;

import lombok.AllArgsConstructor;
@Service
@Transactional
@AllArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {
private final EquipmentRepository equipmentRepository;
private final ModelMapper modelMapper;
private final EquipmentCategoryRepository categoryRepository;


   public EquipmentDTO getEquipmentDetails(Long equipmentId) {
    Equipment equipment = equipmentRepository.findById(equipmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Not found"));


    return modelMapper.map(equipment, EquipmentDTO.class);
}

	@Override
	public ApiResponse addEquipment(EquipmentREQDto dto, String name ,MultipartFile image,Long userId)throws IOException {
		 EquipmentCategory category = categoryRepository.findByCategoryName(name)
			        .orElseThrow(() -> new ApiException("Invalid Category.!!!!!!"));
		    Equipment equipment = modelMapper.map(dto, Equipment.class);
		    equipment.setOwnerId(userId);

		    if (image != null && !image.isEmpty()) {
		        equipment.setImage(image.getBytes());
		        equipment.setImageType(image.getContentType());
		    }

		   
		    category.addEquipment(equipment);

		    return new ApiResponse("Equipment added...");
		

	}
	@Override
	public ApiResponse addEquipmentImage(MultipartFile image, Long id) throws IOException {
		Equipment equipment = equipmentRepository.findById(id).orElseThrow(()->new InvalidInputException("equipment not exist..!!!!"));
		equipment.setImage(image.getBytes());
		equipment.setImageType(image.getContentType());
		return new ApiResponse("uploded successfully...");
	}
	@Override
	public EquipmentImageDTO getEquipmentImage(Long id) {
		Equipment equipment = equipmentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("equipment not exist..!!!!"));
		return new EquipmentImageDTO(equipment.getImageType(), equipment.getImage());
	}
	@Override
	public ApiResponse deleteEquipment(Long id, Long ownerId) {
		Equipment equipment = equipmentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("equipment not exist..!!!!"));
		if(equipment.getOwnerId()!=ownerId)
			throw new ApiException("Invalid Operation.!!!!!!");
		equipmentRepository.delete(equipment);
		return new ApiResponse("deleted successfully...");
	}
	@Override
	public ApiResponse toggleEquipmentAvailability(Long userId, Long id) {
		Equipment equipment = equipmentRepository.findById(id).orElseThrow(()->new InvalidInputException("equipment not exist..!!!!"));
		if(equipment.isAvailability())
			equipment.setAvailability(false);
		else
			equipment.setAvailability(true);
		return new ApiResponse(" successfull...");
	}
	@Override
	public boolean checkEquipmentAvailability(Long id) {
		Equipment equipment = equipmentRepository.findById(id).orElseThrow(()->new InvalidInputException("equipment not exist..!!!!"));
		return equipment.isAvailability();
	}
	@Override
	public List<EquipmentDTO> getAllEquipment() {
		List<Equipment> equipements = equipmentRepository.findAll();
		if(equipements.isEmpty())
			throw new ResourceNotFoundException("No equipement founds  !!!");
		
		return equipements.stream().map(equip->modelMapper.map(equip, EquipmentDTO.class)).toList();
	}
	@Override
	public List<EquipmentDTO> getAllEquipmentByUserId(Long OwnerId) {
		List<Equipment> equipements = equipmentRepository.findByOwnerId(OwnerId);
		if(equipements.isEmpty())
			throw new ResourceNotFoundException("No equipement founds  !!!");
		
		return equipements.stream().map(equip->modelMapper.map(equip, EquipmentDTO.class)).toList();
	}
	@Override
	 public Page<EquipmentDTO> getEquipmentsSortedByPriceAndCity(String order,String city, Pageable pageable) {
	        if ("desc".equalsIgnoreCase(order)) {
	           Page<Equipment> allByOrderByPricePerDayDesc = equipmentRepository.findByLocationOrderByPricePerDayDesc(city,pageable);
	           List<EquipmentDTO> dtoList = allByOrderByPricePerDayDesc
	                   .stream()
	                   .map(equipment -> modelMapper.map(equipment, EquipmentDTO.class))
	                   .collect(Collectors.toList());
	           return new PageImpl<>(dtoList, pageable, allByOrderByPricePerDayDesc.getTotalElements());
	        } else {
	            Page<Equipment> allByOrderByPricePerDayAsc = equipmentRepository.findByLocationOrderByPricePerDayAsc(city,pageable);
	            List<EquipmentDTO> dtoList2 = allByOrderByPricePerDayAsc
		                   .stream()
		                   .map(equipment -> modelMapper.map(equipment, EquipmentDTO.class))
		                   .collect(Collectors.toList());
	          return  new PageImpl<>(dtoList2, pageable, allByOrderByPricePerDayAsc.getTotalElements());
	        }
	    }

}
