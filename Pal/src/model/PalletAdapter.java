package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clojure.lang.PersistentVector;

@SuppressWarnings("rawtypes")
public class PalletAdapter implements Pallet {

	/** Map of item attributes. */
	private Map attribMap;
	
	private Map<String, Object> keys;

	public PalletAdapter(Map attribMap) {
		this.attribMap = attribMap;
		keys = new HashMap<String, Object>();
		for (Object key : attribMap.keySet()) {
			keys.put(key.toString().substring(1), key);
		}
	}
	
	public int getLength() {
		return ((Long) attribMap.get(keys.get("length"))).intValue();
	}
	
	public int getWidth() {
		return ((Long) attribMap.get(keys.get("width"))).intValue();
	}
	
/*
{:length 750, :width 500, :items [{:y 0, :x 0, :item {:id 128, :length 200, :width 150}}]}
 */
	public List<PackedItem> getPackedItems() {
		List<PackedItem> items = new ArrayList<PackedItem>();
		for (Object packedItem : ((PersistentVector) attribMap.get(keys.get("items")))) {
			items.add(new PackedItemAdapter((Map) packedItem));
		}

		return items;
	}
	
	/** Visitor accept method. */
	public void accept(ElementVisitor visitor) {
		visitor.visit(this);
	}
	
	/**
	 * @return string representation of Item.
	 */
	@Override
	public String toString() {
		return "Pallet [length=" + getLength() + ", width=" + getWidth()	+ "]";
	}
}
