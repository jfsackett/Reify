package com.sackett.reify.bp.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JPanel;

import com.sackett.reify.bp.Bin;
import com.sackett.reify.bp.Item;

/** This is responsible for rendering the game state to the graphics context. */
public class BinPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	/** The Bin to be rendered. */
	Bin bin;

	/** The Items to be packed. */
	List<Item> unpackedItems;
	
	/** Status message. */
	String status;

    public BinPanel(Bin bin, List<Item> unpackedItems, String status) {
		super();
		this.bin = bin;
		this.unpackedItems = unpackedItems;
		this.status = status;
	}

    /** Graphically render the bin and packed items. */
	private void doDrawing(Graphics graphics) {
        Graphics2D graphics2d = (Graphics2D) graphics;

    	// Draw shapes to be packed.
        drawUnpackedItems(graphics2d);
        
        // Render packed bin.
    	BPGraphicsRenderer bpGraphicsRenderer = new BPGraphicsRenderer(graphics2d, 5, 20, this.getHeight());
    	
    	bpGraphicsRenderer.visit(bin);
    	
        graphics2d.setColor(Color.black);
        graphics2d.setFont(new Font("Sans Serif", Font.PLAIN, 18));
    	graphics2d.drawString(status, 20, 150);
    } 

	/** Paint the component. */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void drawUnpackedItems(Graphics2D graphics2d) {
    	int xOffset = 20;
    	for (Item item : unpackedItems) {
    		graphics2d.setColor(new Color(item.getId()));
    		graphics2d.fillRect(xOffset, 10, item.getLength() / 4, item.getWidth() / 4);
            graphics2d.setColor(Color.black);
    		graphics2d.drawRect(xOffset, 10, item.getLength() / 4, item.getWidth() / 4);
    		xOffset = xOffset + item.getLength() / 4 + 10;
    	}
    }
    
	public void setBin(Bin bin) {
		this.bin = bin;
	}

	public void setUnpackedItems(List<Item> unpackedItems) {
		this.unpackedItems = unpackedItems;
	}

	public void setStatus(String status) {
		this.status = status;
	}    
	
}
