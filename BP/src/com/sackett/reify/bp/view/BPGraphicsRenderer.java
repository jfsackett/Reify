package com.sackett.reify.bp.view;

import java.awt.Graphics2D;

import com.sackett.reify.bp.Bin;
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
	
	public BPGraphicsRenderer(Graphics2D graphics, int xOffset, int yOffset, int yHeight) {
		this.graphics = graphics;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	/** Visit a Bin. */
	public void visit(Bin bin) {
		ShapeNormalizer normalizer = new ShapeNormalizer(600, 500, yOffset, xOffset, bin.getLength(), bin.getWidth());
		graphics.drawRect(normalizer.getULXOffset(), normalizer.getULYOffset(), normalizer.getWidth(), normalizer.getLength());
		// Visit items packed in bin.
		for (PackedItem packedItem : bin.getPackedItems()) {
			visit(packedItem);
		}
	}

	/** Visit a Packed Item. */
	public void visit(PackedItem packedItem) {
		ShapeNormalizer normalizer = new ShapeNormalizer(600, 500, yOffset + packedItem.getxOffset(), xOffset + packedItem.getyOffset(), packedItem.getItem().getLength(), packedItem.getItem().getWidth());
		graphics.drawRect(normalizer.getULXOffset(), normalizer.getULYOffset(), normalizer.getWidth(), normalizer.getLength());
	}

	/** Visit an Item. */
	public void visit(Item item) {
		graphics.drawRect(xOffset, yOffset, item.getWidth(), item.getLength());
	}
	
	
	private static class ShapeNormalizer {
		/** Panel width. */
		private int panelWidth;
		
		/** Panel length. */
		private int panelHeight;
		
		/** Lower left X offset. */
		private int xOffset;
		
		/** Lower left Y offeet. */
		private int yOffset;
		
		/** Shape Width. */
		private int width;
		
		/** Shape Length. */
		private int length;

		public ShapeNormalizer(int panelWidth, int panelHeight, int xOffset, int yOffset, int width, int length) {
			this.panelWidth = panelWidth;
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
