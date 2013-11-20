package com.sackett.reify.bp.view;

import java.awt.Color;
import java.awt.Graphics2D;

import com.sackett.reify.bp.Bin;
import com.sackett.reify.bp.Bin.SpaceMap;
import com.sackett.reify.bp.BinPackingElementVisitor;
import com.sackett.reify.bp.Item;
import com.sackett.reify.bp.PackedItem;

public class BPGraphicsRenderer implements BinPackingElementVisitor {
	/** Graphics context. */
	Graphics2D graphics;
	
	/** Drawing X offset. */
	private int xOffset;
	
	/** Drawing Y offset. */
	private int yOffset;
	
	/** Height of the panel. */
	private int panelHeight;
	
	public BPGraphicsRenderer(Graphics2D graphics, int xOffset, int yOffset, int panelHeight) {
		this.graphics = graphics;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.panelHeight = panelHeight;
	}

	/** Visit a Bin. */
	public void visit(Bin bin) {
		ShapeNormalizer normalizer = new ShapeNormalizer(panelHeight, yOffset, xOffset, bin.getLength(), bin.getWidth());
		graphics.setColor(Color.black);
		graphics.drawRect(normalizer.getULXOffset(), normalizer.getULYOffset(), normalizer.getWidth(), normalizer.getLength());
		// Visit items packed in bin.
		for (PackedItem packedItem : bin.getPackedItems()) {
			visit(packedItem);
		}
	}

	/** Visit a Packed Item. */
	public void visit(PackedItem packedItem) {
		ShapeNormalizer normalizer = new ShapeNormalizer(panelHeight, yOffset + packedItem.getxOffset(), xOffset + packedItem.getyOffset(), packedItem.getItem().getLength(), packedItem.getItem().getWidth());
		graphics.setColor(new Color(packedItem.getItem().getId()));
		graphics.fillRect(normalizer.getULXOffset(), normalizer.getULYOffset(), normalizer.getWidth(), normalizer.getLength());
	}

	/** Visit an Item. */
	public void visit(Item item) {
//		graphics.setColor(new Color(item.getId()));
//		graphics.fillRect(xOffset, yOffset, item.getWidth(), item.getLength());
	}

	/** Visit a SpaceMap. */
	public void visit(SpaceMap spaceMap) {
		boolean[][] fillBits = spaceMap.getFillBits();
		for (int y = 0; y < fillBits.length; y++) {
			for (int x = 0; x < fillBits[y].length; x++) {
				graphics.setColor((fillBits[y][x]) ? Color.black : Color.white);
				graphics.drawLine(x + xOffset, panelHeight - yOffset - y, x + xOffset, panelHeight - yOffset - y);
			}
		}
	}	
	
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
