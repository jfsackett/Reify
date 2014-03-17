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
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.PalletizerModel;
import view.PalletizerView;

/** Main Palletizer application controller. */
public class PalletizerController {

	/** Palletizer state model. */
	private PalletizerModel model;
	
	/** Palletizer view. */
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
		view.getShuffleButton().addActionListener(buildShuffleActionListener());
		view.getPackButton().addActionListener(buildPalletizeActionListener());
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
	
	/** Builds action listener for shuffle button. */
	private ActionListener buildShuffleActionListener() {
		return new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent event) {
            	model.shuffleItems();
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
	
}
