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
package view;

import java.awt.Color;
import java.awt.Graphics2D;

import model.ElementVisitor;
import model.PackedItem;
import model.Pallet;

/** Visits pallet elements to graphically display them. */
public class GraphicsRenderer implements ElementVisitor {
	/** Graphics context. */
	Graphics2D graphics;
	
	/** Drawing X offset. */
	private int xOffset;
	
	/** Drawing Y offset. */
	private int yOffset;
	
	/** Height of the panel. */
	private int panelHeight;
	
	public GraphicsRenderer(Graphics2D graphics, int xOffset, int yOffset, int panelHeight) {
		this.graphics = graphics;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.panelHeight = panelHeight;
	}

	/** Visit a Pallet. */
	public void visit(Pallet pallet) {
		ShapeNormalizer normalizer = new ShapeNormalizer(panelHeight, yOffset, xOffset, pallet.getLength(), pallet.getWidth());
		graphics.setColor(Color.black);
		graphics.drawRect(normalizer.getULXOffset(), normalizer.getULYOffset(), normalizer.getWidth(), normalizer.getLength());
		// Visit items packed in bin.
		for (PackedItem packedItem : pallet.getPackedItems()) {
			visit(packedItem);
		}
	}

	/** Visit a Packed Item. */
	public void visit(PackedItem packedItem) {
		ShapeNormalizer normalizer = new ShapeNormalizer(panelHeight, yOffset + packedItem.getxOffset(), xOffset + packedItem.getyOffset(), packedItem.getItem().getLength(), packedItem.getItem().getWidth());
		graphics.setColor(new Color(packedItem.getItem().getId()));
		graphics.fillRect(normalizer.getULXOffset(), normalizer.getULYOffset(), normalizer.getWidth(), normalizer.getLength());
        graphics.setColor(Color.black);
        graphics.drawRect(normalizer.getULXOffset(), normalizer.getULYOffset(), normalizer.getWidth(), normalizer.getLength());
	}

	/** Responsible for adjusting coordinates from domain based (lower-left) to display (upper-left) based. */ 
	private static class ShapeNormalizer {
		/** Panel length. */
		private int panelHeight;
		
		/** Lower left X offset. */
		private int xOffset;
		
		/** Lower left Y offset. */
		private int yOffset;
		
		/** Shape Width. */
		private int width;
		
		/** Shape Length. */
		private int length;

		public ShapeNormalizer(int panelHeight, int xOffset, int yOffset, int width, int length) {
			this.panelHeight = panelHeight;
			this.xOffset = xOffset;
			this.yOffset = yOffset;
			this.width = width;
			this.length = length;
		}

		/**
		 * @return the upper left X offset.
		 */
		public int getULXOffset() {
			return xOffset;
		}

		/**
		 * @return the upper left Y offset.
		 */
		public int getULYOffset() {
			return panelHeight - yOffset - length;
		}

		/**
		 * @return the width
		 */
		public int getWidth() {
			return width;
		}

		/**
		 * @return the length
		 */
		public int getLength() {
			return length;
		}
	}
	
}
