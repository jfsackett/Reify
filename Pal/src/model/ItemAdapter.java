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

import java.util.HashMap;
import java.util.Map;

/** Adapts Clojure representation of Item. */
@SuppressWarnings("rawtypes")
public class ItemAdapter implements Item {

	/** Map of item attributes. */
	protected Map attribMap;
	
	/** Available keys into attribMap. */
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
	
	/** Retrieve item id. */
	public int getId() {
		return ((Long) ((Map) attribMap.get(keys.get("item"))).get(keys.get("id"))).intValue();
	}
	
	/** Retrieve item length. */
	public int getLength() {
		return ((Long) ((Map) attribMap.get(keys.get("item"))).get(keys.get("length"))).intValue();
	}
	
	/** Retrieve item width. */
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
