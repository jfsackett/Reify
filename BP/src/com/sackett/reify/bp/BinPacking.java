/*
    Bin Packing System
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
package com.sackett.reify.bp;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sackett.reify.bp.view.BPView;

/** Main application frame. */
public class BinPacking extends JFrame {
	private static final long serialVersionUID = 1L;
	
	// GUI display constants.
	private static String TITLE = "Palletizer 2D";
	private static int WIDTH = 800;
	private static int HEIGHT = 750;
	
    /** Main program initializes UI & kicks off bin packing UI. */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	BinPacking binPacking = new BinPacking();
            	binPacking.setVisible(true);
            }
        });
    }
		
	public BinPacking() {		
		// Initialize UI.
        initUI();
    }
	
	/** Initializes UI. */
    private void initUI() {
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		add(new BPView());
       
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);        
    }
}
