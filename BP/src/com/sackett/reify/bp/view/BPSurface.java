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
	BPGraphicsRenderer bpGraphicsRenderer;
    
    public BPSurface(Bin bin) {
		super();
		this.bin = bin;
	}

	private void doDrawing(Graphics graphics) {
        Graphics2D graphics2d = (Graphics2D) graphics;
    	BPGraphicsRenderer bpGraphicsRenderer = new BPGraphicsRenderer(graphics2d, 50, 50, 400);

    	bpGraphicsRenderer.visit(bin);
    } 

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }    
}
