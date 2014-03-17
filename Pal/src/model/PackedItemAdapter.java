/*
    Palletizer
    Copyright (C) 2014  Sackett Inc.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package model;

import java.util.Map;

/** Adapts Clojure representation of Packed Item. */
@SuppressWarnings("rawtypes")
public class PackedItemAdapter extends ItemAdapter implements PackedItem {

	public PackedItemAdapter(Map attribMap) {
		super(attribMap);
	}
	
	/** Retrieve packed item. */
	public Item getItem() {
		return (Item) this;
	}

	/** Retrieve x offset of packed location. */
	public int getxOffset() {
		return ((Long) attribMap.get(keys.get("x"))).intValue();
	}
	
	/** Retrieve y offset of packed location. */
	public int getyOffset() {
		return ((Long) attribMap.get(keys.get("y"))).intValue();
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
