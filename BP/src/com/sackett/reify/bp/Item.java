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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** 
 * This represents an item to be packed in a bin. 
 * @author Joseph Sackett
 */
public class Item {
	/** List of available Ids. */
	private static List<Integer> ids;
	
	static {
		Integer[] idArray = {0x8B0000, 0xA52A2A, 0xB22222, 0xDC143C, 0xFF0000, 0xFF6347, 0xFF7F50, 0xCD5C5C, 0xF08080, 0xE9967A, 0xFA8072, 0xFFA07A, 0xFF4500, 0xFF8C00, 0xFFA500, 0xFFD700, 0xB8860B, 0xDAA520, 0xEEE8AA, 0xBDB76B, 0xF0E68C, 0x808000, 0xFFFF00, 0x9ACD32, 0x556B2F, 0x6B8E23, 0x7CFC00, 0x7FFF00, 0xADFF2F, 0x006400, 0x008000, 0x228B22, 0x00FF00, 0x32CD32, 0x90EE90, 0x98FB98, 0x8FBC8F, 0x00FA9A, 0x00FF7F, 0x2E8B57, 0x66CDAA, 0x3CB371, 0x20B2AA, 0x2F4F4F, 0x008080, 0x008B8B, 0x00FFFF, 0x00FFFF, 0xE0FFFF, 0x00CED1, 0x40E0D0, 0x48D1CC, 0xAFEEEE, 0x7FFFD4, 0xB0E0E6, 0x5F9EA0, 0x4682B4, 0x6495ED, 0x00BFFF, 0x1E90FF, 0xADD8E6, 0x87CEEB, 0x87CEFA, 0x191970, 0x000080, 0x00008B, 0x0000CD, 0x0000FF, 0x4169E1, 0x8A2BE2, 0x4B0082, 0x483D8B, 0x6A5ACD, 0x7B68EE, 0x9370DB, 0x8B008B, 0x9400D3, 0x9932CC, 0xBA55D3, 0x800080, 0xD8BFD8, 0xDDA0DD, 0xEE82EE, 0xFF00FF, 0xDA70D6, 0xC71585, 0xDB7093, 0xFF1493, 0xFF69B4, 0xFFB6C1, 0xFFC0CB};
		ids = new ArrayList<Integer>(Arrays.asList(idArray));
		Collections.shuffle(ids);
	}
	
	/** Item Id. */
	private int id;
	
	/** Item length. */
	private int length;
	
	/** Item width. */
	private int width;

	public Item(int length, int width) {
		id = (ids.isEmpty()) ? 0 : ids.remove(0);
		this.length = length;
		this.width = width;
	}
	
	/**
	 * Calculate this item's fitness for input space.
	 * @param binSpace space for which fitness calculated.
	 * @return fitness [-1 - 4].
	 */
	public int fitnessForSpace(BinSpace binSpace) {
		int wall;
		// Fit to taller wall.
		if (binSpace.getLeftWall() >= binSpace.getRightWall()) {
			wall = binSpace.getLeftWall();
		}
		else {
			wall = binSpace.getRightWall();
		}
		
		if (binSpace.getLength() == length && wall == width) {
			// Item's length & width fit perfectly.
			return 4;
		}
		else if (binSpace.getLength() == length && wall < width && binSpace.getWidth() >= width) {
			// Item's length fits perfectly; wider than wall width but less than full width of space.
			return 3;
		}
		else if (binSpace.getLength() == length && wall > width) {
			// Item's length fits perfectly; narrower than wall width.
			return 2;
		}
		else if (binSpace.getLength() > length && wall == width) {
			// Item's length shorter than space; width fits perfectly. 
			return 1;
		}
		else if (binSpace.getLength() > length && binSpace.getWidth() >= width) {
			// Item's length shorter than space; width less than full width of space. 
			return 0;
		}

		// Does not fit.
		return -1;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
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
		
	/** Visitor accept method. */
	public void accept(BinPackingElementVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * @return Item's hash code.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		if (id != other.id)
			return false;
		return true;
	}

	/**
	 * @return string representation of Item.
	 */
	@Override
	public String toString() {
		return "Item [id=" + id + ", length=" + length + ", width=" + width
				+ "]";
	}
	
}
