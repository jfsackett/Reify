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
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sackett.reify.bp.view.BPSurface;

public class BinPacking extends JFrame {
	private static final long serialVersionUID = 1L;
	
	// GUI display constants.
	private static String TITLE = "Palletizer 2D";
	private static int WIDTH = 900;
	private static int HEIGHT = 750;
	
	/** The main bin to be filled. */
	Bin bin;
	
	/** The items to pack in bin. */
    List<Item> items;
    
    /** Main program initializes UI & kicks off bin packing routine. */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	BinPacking binPacking = new BinPacking();
            	binPacking.setVisible(true);
            	
            	binPacking.doPack();
            }
        });
    }
		
	public BinPacking() {
		// Initialize bin & items.
		bin = new Bin(800, 600);
		items = initializeItemsForPacking();
		
		// Initialize UI.
        initUI();
    }
	
	/** Build the list of items to pack. */
	private List<Item> initializeItemsForPacking() {
		List<Item> items = new ArrayList<Item>();
		
		items.add(new Item(200, 400));
		items.add(new Item(100, 500));
		items.add(new Item(300, 200));
		items.add(new Item(500, 300));
		items.add(new Item(300, 200));
		items.add(new Item(300, 300));
		items.add(new Item(300, 100));
		
		return items;
	}
    
	/** Initializes UI. */
    private void initUI() {
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        add(new BPSurface(bin));
        
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);        
    }
    
    /** Execute packing heuristic. */
    private void doPack() {
    	// Space in which to pack item. Also used in loop conditional. 
    	BinSpace binSpace = new BinSpace();
    	// Loop until all items packed or no space sized to hold any items.
    	while (!items.isEmpty() && binSpace != null) {
    		// Clear prior search context.
    		List<BinSpace> binSpaces = new ArrayList<BinSpace>();
	    	// Loop until there are no more available spaces.
	    	while ((binSpace = bin.findNextSpace(binSpaces)) != null) {
	    		binSpaces.add(binSpace);
	    		// Retain best fit item & fitness through search.
	    		Item bestFitItem = null;
	    		int bestFitness = -1;
	    		// Loop through all items to find best fit item.
	    		for (Item item : items) {
	    			int currFitness;
	    			// Better fit?
	    			if ((currFitness = item.fitnessForSpace(binSpace)) > bestFitness) {
	    				// Retain item.
	    				bestFitItem = item;
	    				bestFitness = currFitness;
	    			}
	    		}
	    		
	    		// If no fit items, can't place one, continue to next space.
	    		if (bestFitItem == null) {
	    			continue;
	    		}
	    		
	    		// Add item to bin in available space. Left or right wall decided by conditional.
	    		bin.addItem(bestFitItem, binSpace, binSpace.getLeftWall() >= binSpace.getRightWall());
	    		items.remove(bestFitItem);
	    		break;
	    	}
	    	
    	}
    	
    	System.out.println("Done packing.");
    }
}
