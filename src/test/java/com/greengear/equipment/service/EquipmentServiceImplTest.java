package com.greengear.equipment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import com.greengear.equipment.Dto.ApiResponse;
import com.greengear.equipment.Dto.EquipmentDTO;
import com.greengear.equipment.Dto.EquipmentIdDTO;
import com.greengear.equipment.Dto.EquipmentImageDTO;
import com.greengear.equipment.Dto.EquipmentREQDto;
import com.greengear.equipment.Entities.Equipment;
import com.greengear.equipment.Entities.EquipmentCategory;
import com.greengear.equipment.Exception.ApiException;
import com.greengear.equipment.Exception.ResourceNotFoundException;
import com.greengear.equipment.Repository.EquipmentCategoryRepository;
import com.greengear.equipment.Repository.EquipmentRepository;
import com.greengear.equipment.Service.EquipmentServiceImpl;

class EquipmentServiceImplTest {

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private EquipmentCategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EquipmentServiceImpl equipmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ Positive test for getEquipmentDetails
    @Test
    void testGetEquipmentDetails() {
        Equipment equipment = new Equipment();
        equipment.setId(1L);

        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(1L);

        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        when(modelMapper.map(equipment, EquipmentDTO.class)).thenReturn(dto);

        EquipmentDTO result = equipmentService.getEquipmentDetails(1L);

        assertEquals(1L, result.getId());
    }

    // ❌ Negative: Equipment not found
    @Test
    void testGetEquipmentDetailsNotFound() {
        when(equipmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> equipmentService.getEquipmentDetails(99L));
    }

    // ✅ Positive addEquipment
    @Test
    void testAddEquipment() throws IOException {
        EquipmentREQDto reqDto = new EquipmentREQDto();
        Equipment equipment = new Equipment();
        MultipartFile file = mock(MultipartFile.class);

        EquipmentCategory category = new EquipmentCategory();
        when(categoryRepository.findByCategoryName("Tractor")).thenReturn(Optional.of(category));
        when(modelMapper.map(reqDto, Equipment.class)).thenReturn(equipment);
        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenReturn("image".getBytes());
        when(file.getContentType()).thenReturn("image/png");

        ApiResponse response = equipmentService.addEquipment(reqDto, "Tractor", file, 100L);

        assertEquals("Equipment added...", response.getMessage());
        assertEquals(100L, equipment.getOwnerId());
    }

    // ❌ Negative: Category not found
    @Test
    void testAddEquipmentCategoryNotFound() {
        EquipmentREQDto reqDto = new EquipmentREQDto();
        when(categoryRepository.findByCategoryName("Invalid")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> equipmentService.addEquipment(reqDto, "Invalid", null, 100L));
    }

    // ✅ Add Equipment Image
    @Test
    void testAddEquipmentImage() throws IOException {
        Equipment equipment = new Equipment();
        MultipartFile file = mock(MultipartFile.class);

        when(file.getBytes()).thenReturn("img".getBytes());
        when(file.getContentType()).thenReturn("image/jpeg");
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));

        ApiResponse response = equipmentService.addEquipmentImage(file, 1L);

        assertEquals("uploded successfully...", response.getMessage());
    }

    // ❌ Negative: Equipment not found for image
    @Test
    void testAddEquipmentImageNotFound() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(equipmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> equipmentService.addEquipmentImage(file, 1L));
    }

    // ✅ Get Equipment Image
    @Test
    void testGetEquipmentImage() {
        Equipment equipment = new Equipment();
        equipment.setImage("abc".getBytes());
        equipment.setImageType("image/png");

        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));

        EquipmentImageDTO result = equipmentService.getEquipmentImage(1L);

        assertEquals("image/png", result.getImageType());
    }

    // ❌ Negative: No image found
    @Test
    void testGetEquipmentImageNotFound() {
        Equipment equipment = new Equipment();
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));

        assertThrows(ResourceNotFoundException.class, () -> equipmentService.getEquipmentImage(1L));
    }

    // ✅ Delete Equipment
    @Test
    void testDeleteEquipment() {
        Equipment equipment = new Equipment();
        equipment.setId(1L);
        equipment.setOwnerId(100L);

        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));

        ApiResponse response = equipmentService.deleteEquipment(1L, 100L);

        assertEquals("deleted successfully...", response.getMessage());
    }

    // ❌ Negative: Unauthorized owner delete
    @Test
    void testDeleteEquipmentUnauthorized() {
        Equipment equipment = new Equipment();
        equipment.setOwnerId(200L);

        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));

        assertThrows(ApiException.class, () -> equipmentService.deleteEquipment(1L, 100L));
    }

    // ✅ Toggle Availability
    @Test
    void testToggleEquipmentAvailability() {
        Equipment equipment = new Equipment();
        equipment.setAvailability(true);

        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));

        ApiResponse response = equipmentService.toggleEquipmentAvailability(100L, 1L);

        assertFalse(equipment.isAvailability());
        assertEquals(" successfull...", response.getMessage());
    }

    // ❌ Negative: Equipment not found for toggle
    @Test
    void testToggleAvailabilityNotFound() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> equipmentService.toggleEquipmentAvailability(100L, 1L));
    }

    // ✅ Update Equipment
    @Test
    void testUpdateEquipment() {
        Equipment equipment = new Equipment();
        equipment.setOwnerId(100L);

        EquipmentREQDto dto = new EquipmentREQDto();
        dto.setName("NewName");
        dto.setPricePerDay(500.0);

        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));

        ApiResponse response = equipmentService.updateEquipment(100L, 1L, dto);

        assertEquals("successfull updated...", response.getMessage());
        assertEquals("NewName", equipment.getName());
    }

    // ❌ Negative: Wrong owner updating
    @Test
    void testUpdateEquipmentUnauthorized() {
        Equipment equipment = new Equipment();
        equipment.setOwnerId(200L);

        EquipmentREQDto dto = new EquipmentREQDto();
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));

        assertThrows(ApiException.class, () -> equipmentService.updateEquipment(100L, 1L, dto));
    }

    // ✅ Get All Equipment
    @Test
    void testGetAllEquipment() {
        Equipment equipment = new Equipment();
        EquipmentDTO dto = new EquipmentDTO();

        when(equipmentRepository.findAll()).thenReturn(List.of(equipment));
        when(modelMapper.map(equipment, EquipmentDTO.class)).thenReturn(dto);

        List<EquipmentDTO> result = equipmentService.getAllEquipment();
        assertEquals(1, result.size());
    }

    // ✅ Get Equipment By UserId
    @Test
    void testGetAllEquipmentByUserId() {
        Equipment equipment = new Equipment();
        EquipmentDTO dto = new EquipmentDTO();

        when(equipmentRepository.findByOwnerId(100L)).thenReturn(List.of(equipment));
        when(modelMapper.map(equipment, EquipmentDTO.class)).thenReturn(dto);

        List<EquipmentDTO> result = equipmentService.getAllEquipmentByUserId(100L);
        assertEquals(1, result.size());
    }

    // ✅ Get All Equipment Id
    @Test
    void testGetAllEquipmentId() {
        Equipment equipment = new Equipment();
        EquipmentIdDTO dto = new EquipmentIdDTO();

        when(equipmentRepository.findByOwnerId(100L)).thenReturn(List.of(equipment));
        when(modelMapper.map(equipment, EquipmentIdDTO.class)).thenReturn(dto);

        List<EquipmentIdDTO> result = equipmentService.getAllEquipmentId(100L);
        assertEquals(1, result.size());
    }
}
