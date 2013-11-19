/*
    Bin Packing System
    Copyright (C) 2013  Sackett Inc.

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

import java.util.ArrayList;
import java.util.List;

/** 
 * This represents a bin to be packed full of items. 
 * @author Joseph Sackett
 */
public class Bin implements BinPackingElement {
	/** Item length. */
	private int length;
	
	/** Item width. */
	private int width;
	
	/** Items packed in bin. */
	private List<PackedItem> packedItems = new ArrayList<PackedItem>();

	public Bin(int length, int width) {
		this.length = length;
		this.width = width;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the packedItems
	 */
	public List<PackedItem> getPackedItems() {
		return packedItems;
	}

	/** Add an item to this bin at specific location. */
	public void addItem(Item item, int xOffset, int yOffset) {
		packedItems.add(new PackedItem(item, xOffset, yOffset));
	}

	/** Add an item to this bin in the bin space. */
	public void addItem(Item item, BinSpace binSpace) {
		addItem(item, binSpace.getxOffset(), binSpace.getRightWall());
	}
	
	/** Visitor accept method. */
	public void accept(BinPackingElementVisitor visitor) {
		visitor.visit(this);
	}
}
