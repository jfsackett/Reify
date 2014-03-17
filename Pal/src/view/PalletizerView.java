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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import model.PalletizerModel;

/** View responsible for rendering Palletizer elements to the graphics context. */
public class PalletizerView extends JPanel {
	private static final long serialVersionUID = 1L;
	
	// Initial status message.
	private static String INIT_STATUS = "New: Generate Items     Shuffle: Change Item Order     Palletize: Pack Items";
	
	/** Palletizer state model. */
	private PalletizerModel model;
	
	/** Game display panel. */
	PalletPanel palletPanel;
	
	/** New items button. */
	private JButton newButton = new JButton("New");
	
	/** Shuffle items button. */
	private JButton shuffleButton = new JButton("Shuffle");
	
	/** Palletize items button. */
	private JButton packButton = new JButton("Palletize");
	
	/** Constructor. */
	public PalletizerView(PalletizerModel model) {
		this.model = model;
		
		setLayout(new BorderLayout());
		
		buildUI();
    }
    
	/** Build the UI layout. */
    private void buildUI() {
		// Init pallet display panel.
    	palletPanel = new PalletPanel(model, INIT_STATUS);
        
        // Set button sizes.
        newButton.setPreferredSize(new Dimension(100, 30));
        shuffleButton.setPreferredSize(new Dimension(100, 30));
        packButton.setPreferredSize(new Dimension(100, 30));
        
        // Create button panel.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 15));
        buttonPanel.add(newButton);
        buttonPanel.add(shuffleButton);
        buttonPanel.add(packButton);
        
        // Add panels to view panel.
        add(palletPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.PAGE_END);
    }

	public PalletPanel getPalletPanel() {
		return palletPanel;
	}

	public JButton getNewButton() {
		return newButton;
	}

	public JButton getShuffleButton() {
		return shuffleButton;
	}

	public JButton getPackButton() {
		return packButton;
	}

}
