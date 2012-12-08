package edu.colorado.trackers.shoppinglist;

public class ShoppingListItem {
	
	private Integer id;
	private String name;
	private Double price;
	private Integer quantity;
	private Boolean isCrossed = false;
	
	public ShoppingListItem(Integer id, String name, Double price, Integer quantity) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}
	
	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Double getPrice() {
		return price;
	}

	public Integer getQuantity() {
		return quantity;
	}
	
	public Boolean isCrossed() {
		return isCrossed;
	}
	
	public void toggleCrossed() {
		isCrossed = !isCrossed;
	}
	
	public String toString() {
		return String.format("(%d) %s $%.2f", quantity, name, price);
	}

}

