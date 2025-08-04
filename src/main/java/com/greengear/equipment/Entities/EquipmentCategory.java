package com.greengear.equipment.Entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="equipment_categories")
public class EquipmentCategory extends BaseEntity {
	@Column(name = "category_name",length = 30)
private String categoryName;
	
	@Column(name = "category_desc")
private String categoryDescription;
@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true/* ,fetch = FetchType.EAGER */)
private List<Equipment> equipment=new ArrayList<>();

public EquipmentCategory(String categoryName, String categoryDescription) {
	super();
	this.categoryName = categoryName;
	this.categoryDescription = categoryDescription;
}
public void addEquipment(Equipment equip) {
	equip.setCategory(this);
	this.equipment.add(equip);
	
}
public void deleteEquipment(Equipment equip) {
	equip.setCategory(null);
	this.equipment.remove(equip);
	
}

}
