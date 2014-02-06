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

import java.util.ArrayList;
import java.util.List;

/** This represents a bin to be packed full of items. */
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
	 * This accepts the prior bin space and returns the next available space.
	 * @param priorBinSpace prior bin space or null to start.
	 * @return the net available bin space.
	 */
	public BinSpace findNextSpace(List<BinSpace> priorBinSpaces) {
		BinSpace binSpace;
		
		// Should not be null but handle it.
		if (priorBinSpaces == null) {
			priorBinSpaces = new ArrayList<BinSpace>();
		}
		// Get current map of bin.
		SpaceMap spaceMap = new SpaceMap();
		// Get map of bits (true = filled, false = available).
		boolean[][] fillBits = spaceMap.getFillBits();
		
		int xOffset = 0, yOffset = 0;
		// Set start to next point after prior space.
		if (!priorBinSpaces.isEmpty()) {
			BinSpace priorBinSpace = priorBinSpaces.get(priorBinSpaces.size()-1);
			xOffset = priorBinSpace.getxOffset() + 1;
			yOffset = priorBinSpace.getyOffset();
		}
		
		// Find starting point.
		while(true) {
			// Increase xOffset until it runs out of bin, then increment yOffset & reset xOffset.
			if (xOffset >= length) {
				yOffset++;
				xOffset = 0;
				if (yOffset >= width) {
					// When yOffset runs out of bin, there is no more space.
					return null;
				}
			}
			
			// If point filled, cannot be start of space, skip.
			if (fillBits[yOffset][xOffset]) {
				xOffset++;
				continue;
			}

			// Find length & width of maximum space starting from offsets.
			binSpace = findExtents(fillBits, xOffset, yOffset);
			
			// Check to see if this space is contained by any prior space.
			boolean candidateSubsetPriorSpace = false;
			for (BinSpace priorBinSpace : priorBinSpaces) {
				if (priorBinSpace.contains(binSpace)) {
					candidateSubsetPriorSpace = true;
					break;
				}
			}
			
			// Found new space.
			if (!candidateSubsetPriorSpace) {
				break;
			}
			
			xOffset++;
		}
		
		// Find left wall height.
		int leftWall;
		// If at left wall, use remaining bin edge.
		if (xOffset == 0) {
			leftWall = width - yOffset;
		}
		else {
			// Loop up Item to the left until it ends.
			int xWall = xOffset - 1;
			for (leftWall = yOffset; leftWall < width; leftWall++) {
				if (!fillBits[leftWall][xWall]) { 
					break;
				}
			}
			leftWall = leftWall - yOffset;
		}
		binSpace.setLeftWall(leftWall);
		
		// Find right wall height.
		int rightWall;
		int xExtent = xOffset + binSpace.getLength();
		// If at right wall, use remaining bin edge.
		if (xExtent == length) {
			rightWall = width - yOffset;
		}
		else {
			// Loop up Item to the right until it ends.
			int xWall = xExtent + 1;
			for (rightWall = yOffset; rightWall < width; rightWall++) {
				if (!fillBits[rightWall][xWall]) { 
					break;
				}
			}
			rightWall = rightWall - yOffset;
		}
		binSpace.setRightWall(rightWall);
		
		return binSpace;
	}

	/**
	 * Find length & width of maximum space starting from offsets.
	 * @param fillBits map of bin space.
	 * @param xOffset x coord of lower left corner.
	 * @param yOffset y coord of lower left corner.
	 * @return maximum space starting from coords.
	 */
	private BinSpace findExtents(boolean[][] fillBits, int xOffset, int yOffset) {
		// Find length.
		int xExtent;
		for (xExtent = xOffset; xExtent < length; xExtent++) {
			// Stop when occupied bit encountered.
			if (fillBits[yOffset][xExtent]) { 
				break;
			}
		}
		
		// Find width.
		int yExtent;
		for (yExtent = yOffset; yExtent < width; yExtent++) {
			// Stop when occupied bit encountered.
			if (fillBits[yExtent][xOffset]) { 
				break;
			}
		}
		
		return new BinSpace(xOffset, yOffset, xExtent - xOffset, yExtent - yOffset, 0, 0);
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
	public void addItem(Item item, BinSpace binSpace, boolean leftWall) {
		addItem(item, (leftWall) ? binSpace.getxOffset() : binSpace.getxOffset() + binSpace.getLength() - item.getLength(), binSpace.getyOffset());
	}
	
	/** Build and return the bin's SpaceMap. */
	public SpaceMap getSpaceMap() {
		return new SpaceMap();
	}
	
	/** Visitor accept method. */
	public void accept(BinPackingElementVisitor visitor) {
		visitor.visit(this);
	}

	/** Map to determine available free space. */
	public class SpaceMap implements BinPackingElement {
		/** Map of filled area. */
		private boolean[][] fillBits;
		
		public SpaceMap() {
			fillBits = new boolean[width][length];
			for (PackedItem packedItem : packedItems) {
				for (int y = packedItem.getyOffset(); y < packedItem.getyOffset() + packedItem.getItem().getWidth(); y++) {
					for (int x = packedItem.getxOffset(); x < packedItem.getxOffset() + packedItem.getItem().getLength(); x++) {
						fillBits[y][x] = true;
					}
				}
			}
		}

		/**
		 * Get map of bits (true = filled, false = available).
		 * @return the fillBits
		 */
		public boolean[][] getFillBits() {
			return fillBits;
		}

		/** Visitor accept method. */
		public void accept(BinPackingElementVisitor visitor) {
			visitor.visit(this);
		}
	}
}
