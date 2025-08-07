package com.greengear.equipment.Controller;

import java.io.IOException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.greengear.equipment.Dto.CustomUserPrincipal;
import com.greengear.equipment.Dto.EquipmentImageDTO;
import com.greengear.equipment.Dto.EquipmentREQDto;
import com.greengear.equipment.Service.EquipmentService;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("ms2/equipment")
@AllArgsConstructor
public class EquipmentController {
	private final EquipmentService equipmentService;

	@GetMapping("/{id}")
	public ResponseEntity<?> getEquipmentDetailsWithReview(@PathVariable Long id) {
		
		return ResponseEntity.ok(equipmentService.getEquipmentDetails(id));
	}
	@GetMapping
	public ResponseEntity<?> getAllEquipment() {
		
		return ResponseEntity.ok(equipmentService.getAllEquipment());
	}
	@GetMapping("/myequipment")
	public ResponseEntity<?> getAllEquipmentByUserId( @AuthenticationPrincipal CustomUserPrincipal details) {
		System.out.println(details.getUsername());
		System.out.println(details.getUserId());
		return ResponseEntity.ok(equipmentService.getAllEquipmentByUserId(details.getUserId()));
	}
	
	@PostMapping(path="/{name}")
	public ResponseEntity<?> addEquipment(@RequestPart EquipmentREQDto dto,@RequestPart MultipartFile image,@PathVariable String name, @AuthenticationPrincipal CustomUserPrincipal details)throws IOException {
		
		System.out.println(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(equipmentService.addEquipment(dto,name,image, details.getUserId()));
				
	}
	@PatchMapping("upload/{id}")
	public ResponseEntity<?> addEquipmentImage(@RequestPart MultipartFile image,@PathVariable Long id)throws IOException  {
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(equipmentService.addEquipmentImage(image,id));
				
	}
	@PatchMapping("/{id}")
	public ResponseEntity<?> toggleEquipmentAvailability(@PathVariable Long id, @AuthenticationPrincipal CustomUserPrincipal details)  {
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(equipmentService.toggleEquipmentAvailability(details.getUserId(),id));
				
	}
	@GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
    	EquipmentImageDTO image = equipmentService.getEquipmentImage(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(image.getImageType())).body(image.getImage());
    }
    @DeleteMapping
    public ResponseEntity<?> deleteEquipment(@PathVariable Long id, @AuthenticationPrincipal CustomUserPrincipal details) {
		
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(equipmentService.deleteEquipment(id,details.getUserId()));
				
}
    @GetMapping("{id}/avl")
public ResponseEntity<?> checkEquipmentAvailability(@PathVariable Long id)  {
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(equipmentService.checkEquipmentAvailability(id));
    }
    @GetMapping("/sort-by-price")
    public ResponseEntity<?> sortEquipmentsByPrice(
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(equipmentService.getEquipmentsSortedByPriceAndCity(order, city, pageable));
    }
}
