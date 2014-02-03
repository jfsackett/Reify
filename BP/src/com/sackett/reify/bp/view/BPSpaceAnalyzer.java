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
package com.sackett.reify.bp.view;

import com.sackett.reify.bp.Bin;
import com.sackett.reify.bp.Bin.SpaceMap;
import com.sackett.reify.bp.BinPackingElementVisitor;
import com.sackett.reify.bp.Item;
import com.sackett.reify.bp.PackedItem;

public class BPSpaceAnalyzer implements BinPackingElementVisitor {
	/** Volume of the bin. */
	private int binVolume = 0;
	
	/** Volume of the packed items. */
	private int packedItemsVolume = 0;
	
	/** Volume of the unpacked items. */
	private int unpackedItemsVolume = 0;
	
	/** Visit a Bin. */
	public void visit(Bin bin) {
		binVolume = bin.getLength() * bin.getWidth();
		// Visit items packed in bin.
		for (PackedItem packedItem : bin.getPackedItems()) {
			visit(packedItem);
		}
	}

	/** Visit a Packed Item. */
	public void visit(PackedItem packedItem) {
		packedItemsVolume += packedItem.getItem().getLength() * packedItem.getItem().getWidth();
	}

	/** Visit an Item. */
	public void visit(Item item) {
		unpackedItemsVolume += item.getLength() * item.getWidth();
	}

	/** Visit a SpaceMap. */
	public void visit(SpaceMap spaceMap) {
		// doesn't deal.
	}
	
	/** Returns packed space ratio. */
	public float getPackedSpaceRatio() {
		return (float)packedItemsVolume / (float)binVolume;
	}

	public int getBinVolume() {
		return binVolume;
	}

	public int getPackedItemsVolume() {
		return packedItemsVolume;
	}

	public int getUnpackedItemsVolume() {
		return unpackedItemsVolume;
	}	

}
