package model;

import java.util.List;

public interface Pallet extends PalletElement {

	int getLength();
	
	int getWidth();
	
	List<PackedItem> getPackedItems();
	
	@Override
	String toString();
}
