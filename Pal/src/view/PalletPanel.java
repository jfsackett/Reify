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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import model.Item;
import model.PalletizerModel;

/** This is responsible for rendering the game state to the graphics context. */
public class PalletPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	/** The model to display. */
	PalletizerModel model;

	/** Status message. */
	String status;

    public PalletPanel(PalletizerModel model, String status) {
		super();
		this.model = model;
		this.status = status;
	}

    /** Graphically render the bin and packed items. */
	private void doDrawing(Graphics graphics) {
        Graphics2D graphics2d = (Graphics2D) graphics;

    	// Draw shapes to be packed.
        drawUnpackedItems(graphics2d);
        
        // Render packed bin.
    	GraphicsRenderer graphicsRenderer = new GraphicsRenderer(graphics2d, 5, 20, this.getHeight());
    	
    	graphicsRenderer.visit(model.getPallet());
    	
        graphics2d.setColor(Color.black);
        graphics2d.setFont(new Font("Sans Serif", Font.PLAIN, 18));
    	graphics2d.drawString(status, 20, 120);
    } 

	/** Paint the component. */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void drawUnpackedItems(Graphics2D graphics2d) {
    	int xOffset = 20;
    	for (Item item : model.getUnpackedItems()) {
    		graphics2d.setColor(new Color(item.getId()));
    		graphics2d.fillRect(xOffset, 10, item.getLength() / 4, item.getWidth() / 4);
            graphics2d.setColor(Color.black);
    		graphics2d.drawRect(xOffset, 10, item.getLength() / 4, item.getWidth() / 4);
    		xOffset = xOffset + item.getLength() / 4 + 10;
    	}
    }

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
    	
}
