package model;


public interface PackedItem extends Item, PalletElement {

	Item getItem();

	int getxOffset();
	
	int getyOffset();
	
}
