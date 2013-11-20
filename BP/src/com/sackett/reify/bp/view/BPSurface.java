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

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.sackett.reify.bp.Bin;

/**
 * This class is responsible for rendering Bin Packing elements to the graphics context.
 * @author Joseph Sackett
 */
public class BPSurface extends JPanel {
	private static final long serialVersionUID = 1L;
	
	/** The Bin to be rendered. */
	Bin bin;

    public BPSurface(Bin bin) {
		super();
		this.bin = bin;
	}

    /** Graphically render the bin and packed items. */
	private void doDrawing(Graphics graphics) {
        Graphics2D graphics2d = (Graphics2D) graphics;
    	BPGraphicsRenderer bpGraphicsRenderer = new BPGraphicsRenderer(graphics2d, 50, 50, this.getHeight());

    	bpGraphicsRenderer.visit(bin);
    } 

	/** Paint the component. */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }    
}
