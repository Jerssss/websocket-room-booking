package references;

import java.io.Serializable;

public class Equipment implements Serializable {

    private int equipmentId;
    private String equipmentName;
    private String equipmentDescription;
    private int totalQuantity;
    private int availableQuantity;

    public Equipment(int id, String name, String description, int totalQuantity, int available) {
        this.equipmentId = id;
        this.equipmentName = name;
        this.equipmentDescription = description;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = totalQuantity; // Initially all are available
    }

    // Getters and Setters
    public int getEquipmentId() { return equipmentId; }
    public String getEquipmentName() { return equipmentName; }
    public String getEquipmentDescription() { return equipmentDescription; }
    public int getTotalQuantity() { return totalQuantity; }
    public int getAvailableQuantity() { return availableQuantity; }

    @Override
    public String toString() {
        return "Equipment{" +
                "id=" + equipmentId +
                ", name='" + equipmentName + '\'' +
                ", description='" + equipmentDescription + '\'' +
                ", totalQuantity=" + totalQuantity +
                ", availableQuantity=" + availableQuantity +
                '}';
    }
}
