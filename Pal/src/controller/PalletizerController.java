package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import model.PalletizerModel;
import view.PalletizerView;

public class PalletizerController implements PropertyChangeListener {

	private PalletizerModel model;
	
	private PalletizerView view;

	public PalletizerController(PalletizerModel model, PalletizerView view) {
		this.model = model;
		this.view = view;
		
		// Tie view events to callbacks.
		initialize();
	}
	
	/** Initialize callbacks from view events. */
	private void initialize() {
		// Add button listeners.
		view.getNewButton().addActionListener(buildNewActionListener());
		view.getPackButton().addActionListener(buildPalletizeActionListener());
		
		// Register this controller as model event listener.
		model.addPropertyChangeListener(this);
	}
	
	/** Builds action listener for new button. */
	private ActionListener buildNewActionListener() {
		return new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent event) {
            	model.newItems();
             	view.getPalletPanel().repaint();
            }
		};
	}
	
	/** Builds action listener for palletize button. */
	private ActionListener buildPalletizeActionListener() {
		return new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent event) {
            	model.packItems();
             	view.getPalletPanel().repaint();
            }
		};
	}
	
	/** Handles property change events from model. */
	public void propertyChange(PropertyChangeEvent event) {
        String propertyName = event.getPropertyName();

//        if (propertyName.equalsIgnoreCase(SquaresPuzzleModel.COMPLEXITY)) {
//    		view.getCustomTextField().setEnabled(model.getComplexity() == Complexity.CUSTOM);
//        	// Change to Custom complexity? 
//    		if ((Complexity)event.getNewValue() == Complexity.CUSTOM) {
//    			// Default puzzle.
//    			model.setPuzzle(SquaresPuzzleModel.GOAL_PUZZLE);
//    			view.getCustomTextField().setText(SquaresPuzzleModel.GOAL_PUZZLE);
//    			view.getCustomTextField().requestFocus();
//    			view.getCustomTextField().selectAll();
//    		}
//        }
//        else if(propertyName.equalsIgnoreCase(SquaresPuzzleModel.PUZZLE)) {
//        	view.getGamePanel().setPuzzle(model.getPuzzle());
//        }
//    	view.getSearchButton().setEnabled(model.isValidPuzzle() && model.getSearchMethod() != null);
//        
//    	view.getGamePanel().repaint();
//        view.revalidate();
    }
}
