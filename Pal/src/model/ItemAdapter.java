package model;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class ItemAdapter implements Item {

	/** Map of item attributes. */
	protected Map attribMap;
	
	protected Map<String, Object> keys;

	public ItemAdapter(Map attribMap) {
		this.attribMap = attribMap;
		keys = new HashMap<String, Object>();
		for (Object key : attribMap.keySet()) {
			keys.put(key.toString().substring(1), key);
		}
		for (Object key : ((Map) attribMap.get(keys.get("item"))).keySet()) {
			keys.put(key.toString().substring(1), key);
		}
	}
	
	public int getId() {
		return ((Long) ((Map) attribMap.get(keys.get("item"))).get(keys.get("id"))).intValue();
	}
	
	public int getLength() {
		return ((Long) ((Map) attribMap.get(keys.get("item"))).get(keys.get("length"))).intValue();
	}
	
	public int getWidth() {
		return ((Long) ((Map) attribMap.get(keys.get("item"))).get(keys.get("width"))).intValue();
	}
	
	/**
	 * @return Item's hash code.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getId();
		return result;
	}

	/**
	 * @param Item to compare
	 * @return equality comparison result.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (getId() != other.getId())
			return false;
		return true;
	}

	/**
	 * @return string representation of Item.
	 */
	@Override
	public String toString() {
		return "Item [id=" + getId() + ", length=" + getLength() + ", width=" + getWidth()	+ "]";
	}
}
