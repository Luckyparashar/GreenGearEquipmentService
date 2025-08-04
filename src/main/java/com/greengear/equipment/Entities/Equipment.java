package com.greengear.equipment.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name="equipments")
public class Equipment extends BaseEntity {
	@Column(nullable = false)
	 private String name;
	 @Lob
	 private byte[] image;
	 @Column(nullable = false)
	 private double pricePerDay;
	 @Column(nullable = false)
	 private boolean availability;
	 @Column(nullable = false)
	 private String location;
	 private String description;
	 @Column(nullable = false,name = "owner_id")
	 
	 private Long ownerId;
	 @Column(name="image_type")
	 private String imageType;
	
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "category_id")
     private EquipmentCategory category;
     
	  
	 
		public Equipment(String name, double pricePerDay, boolean availability, String location, Long ownerId,
				EquipmentCategory category) {
			super();
			this.name = name;
			this.pricePerDay = pricePerDay;
			this.availability = availability;
			this.location = location;
			this.ownerId = ownerId;
			this.category = category;
		}
		
}
