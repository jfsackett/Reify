package model;

import java.util.Map;

@SuppressWarnings("rawtypes")
public class PackedItemAdapter extends ItemAdapter implements PackedItem {

	public PackedItemAdapter(Map attribMap) {
		super(attribMap);
	}
	
	public Item getItem() {
		return (Item) this;
	}

	public int getxOffset() {
		return ((Integer) attribMap.get(keys.get("x")));
	}
	
	public int getyOffset() {
		return ((Integer) attribMap.get(keys.get("y")));
	}

	/** Visitor accept method. */
	public void accept(ElementVisitor visitor) {
		visitor.visit(this);
	}
	
	/**
	 * @return PackedItem's hash code.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getId();
		return result;
	}

	/**
	 * @param PackedItem to compare
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
	 * @return string representation of PackedItem.
	 */
	@Override
	public String toString() {
		return "PackedItem [id=" + getId() + ", length=" + getLength() + ", width=" + getWidth()	+ "]";
	}
}
