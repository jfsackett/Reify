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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clojure.lang.PersistentVector;

/** Adapts Clojure representation of Pallet. */
@SuppressWarnings("rawtypes")
public class PalletAdapter implements Pallet {

	/** Map of item attributes. */
	private Map attribMap;
	
	/** Available keys into attribMap. */
	private Map<String, Object> keys;

	public PalletAdapter(Map attribMap) {
		this.attribMap = attribMap;
		keys = new HashMap<String, Object>();
		for (Object key : attribMap.keySet()) {
			keys.put(key.toString().substring(1), key);
		}
	}
	
	/** Pallet length. */
	public int getLength() {
		return ((Long) attribMap.get(keys.get("length"))).intValue();
	}
	
	/** Pallet width. */
	public int getWidth() {
		return ((Long) attribMap.get(keys.get("width"))).intValue();
	}
	
	/** Packed items. */
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
