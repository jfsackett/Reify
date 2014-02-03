package com.sackett.reify.bp.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.sackett.reify.bp.Bin;
import com.sackett.reify.bp.BinSpace;
import com.sackett.reify.bp.Item;

/** View of MVC pattern. */
public class BPView extends JPanel {
	private static final long serialVersionUID = 1L;
	
	// Model constants.
	private static int BIN_WIDTH = 750;
	private static int BIN_HEIGHT = 500;
	
	// Initial status message.
	private static String INIT_STATUS = "New: Generate Items     Shuffle: Change Item Order     Palletize: Pack Items";
	
	/** Bin to pack. - Model. */
	Bin bin;
	
	/** Master list of items. - Model. */
	List<Item> masterItems = new ArrayList<Item>();
	
	/** Unpacked items. - Model. */
	List<Item> unpackedItems = new ArrayList<Item>();
	
	/** Game display panel. */
	BinPanel binPanel;
	
	/** New items button. */
	private JButton newButton = new JButton("New");
	
	/** Shuffle items button. */
	private JButton shuffleButton = new JButton("Shuffle");
	
	/** Palletize items button. */
	private JButton packButton = new JButton("Palletize");
	
	/** Constructor. */
	public BPView() {
		setLayout(new BorderLayout());
		
		buildUI();
    }
    
	/** Build the UI layout. */
    private void buildUI() {
		// Initialize bin.
    	bin = new Bin(BIN_WIDTH, BIN_HEIGHT);
		masterItems.clear();
		masterItems.add(new Item(150, 300));
		masterItems.add(new Item(100, 400));
		masterItems.add(new Item(250, 150));
		masterItems.add(new Item(400, 250));
		masterItems.add(new Item(250, 150));
		masterItems.add(new Item(250, 250));
		masterItems.add(new Item(250, 100));
		unpackedItems = new ArrayList<Item>();
		unpackedItems.addAll(masterItems);				
    	
        binPanel = new BinPanel(bin, unpackedItems, INIT_STATUS);
        
        newButton.addActionListener(newItems());
        shuffleButton.addActionListener(shuffleItems());
        packButton.addActionListener(packItems());
                
        newButton.setPreferredSize(new Dimension(100, 30));
        shuffleButton.setPreferredSize(new Dimension(100, 30));
        packButton.setPreferredSize(new Dimension(100, 30));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 15));
        buttonPanel.add(newButton);
        buttonPanel.add(shuffleButton);
        buttonPanel.add(packButton);
        
        add(binPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.PAGE_END);
    }

	/** Build the list of items to pack. */
	private ActionListener newItems() {
		return new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent event) {
        		// Initialize bin.
            	bin = new Bin(BIN_WIDTH, BIN_HEIGHT);
            	binPanel.setBin(bin);
            	
            	masterItems = generateNewItems(BIN_WIDTH * BIN_HEIGHT + 2000);
				
				unpackedItems = new ArrayList<Item>();
				unpackedItems.addAll(masterItems);				
            	binPanel.setUnpackedItems(unpackedItems);
            	binPanel.setStatus(INIT_STATUS);
            	
            	binPanel.repaint();				
            }
		};
	}
	
	private static List<Item> generateNewItems(int maxVolume) {
		List<Item> newItems = new ArrayList<Item>();
		int volume = 0;
		
		while (volume <= maxVolume) {
			Item newItem = generateNewItem();
			newItems.add(newItem);
			volume += newItem.getWidth() * newItem.getLength();
		}

		return newItems;
	}
	
	private static Item generateNewItem() {
		return new Item(50 * generateRandom(1, 8), 50 * generateRandom(1, 8));
	}
        
	private static int generateRandom(int min, int max) {
		return min + (int)(Math.random() * ((max - min) + 1));
	}
        
	/** Shuffle the list of items to pack. */
	private ActionListener shuffleItems() {
		return new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent event) {
        		// Initialize bin.
            	bin = new Bin(BIN_WIDTH, BIN_HEIGHT);
            	binPanel.setBin(bin);

            	// Shuffle the items.
				Collections.shuffle(masterItems);
				// Flip items on their side some of the time.
				for (Item item : masterItems) {
					if (Math.random() > .6) {
						int temp = item.getLength();
						item.setLength(item.getWidth());
						item.setWidth(temp);
					}
				}

				unpackedItems = new ArrayList<Item>();
				unpackedItems.addAll(masterItems);				
            	binPanel.setUnpackedItems(unpackedItems);
            	binPanel.setStatus(INIT_STATUS);
            	
            	binPanel.repaint();
            }
		};
	}
        
	/** Builds action listener for executing packing heuristic. */
	private ActionListener packItems() {
		return new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent event) {
            	bin = new Bin(BIN_WIDTH, BIN_HEIGHT);
            	
               	// Space in which to pack item. Also used in loop conditional. 
            	BinSpace binSpace = new BinSpace();
            	// Loop until all items packed or no space sized to hold any items.
            	while (!unpackedItems.isEmpty() && binSpace != null) {
            		// Clear prior search context.
            		List<BinSpace> binSpaces = new ArrayList<BinSpace>();
        	    	// Loop until there are no more available spaces.
        	    	while ((binSpace = bin.findNextSpace(binSpaces)) != null) {
        	    		binSpaces.add(binSpace);
        	    		// Retain best fit item & fitness through search.
        	    		Item bestFitItem = null;
        	    		int bestFitness = -1;
        	    		// Loop through all items to find best fit item.
        	    		for (Item item : unpackedItems) {
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
        	    		unpackedItems.remove(bestFitItem);
        	    		break;
        	    	}
        	    	
            	}
            	
            	BPSpaceAnalyzer analyzer = new BPSpaceAnalyzer();
            	analyzer.visit(bin);
            	for (Item item : unpackedItems) {
            		analyzer.visit(item);
            	}
            	
            	binPanel.setStatus("Percent Full:  " + String.format("%.2f", analyzer.getPackedSpaceRatio() * 100) + " %");
            	
            	binPanel.setBin(bin);
            	binPanel.repaint();
            }
		};
	}    
    
}
