/*
    Bin Packing System
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
package com.sackett.reify.bp;

/** This represents an item packed in a bin, includes its location. */
public class PackedItem {
	/** Item packed. */
	Item item;
	
	/** Item X offset location. */
	private int xOffset;
	
	/** Item Y offset location. */
	private int yOffset;

	public PackedItem(Item item, int xOffset, int yOffset) {
		this.item = item;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	/**
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * @return the xOffset
	 */
	public int getxOffset() {
		return xOffset;
	}

	/**
	 * @return the yOffset
	 */
	public int getyOffset() {
		return yOffset;
	}
		
	/** Visitor accept method. */
	public void accept(BinPackingElementVisitor visitor) {
		visitor.visit(this);
	}
}
