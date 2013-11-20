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

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sackett.reify.bp.view.BPSurface;

public class BinPacking extends JFrame {
	private static final long serialVersionUID = 1L;

	public BinPacking() {
        initUI();
    }
    
    private void initUI() {
        setTitle("Bin Packing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Bin bin = new Bin(500, 400);
        bin.addItem(new Item(100, 50), 0, 0);
        bin.addItem(new Item(80, 60), 10, 50);
        
        add(new BPSurface(bin));
        
        setSize(600, 500);
        setLocationRelativeTo(null);        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	BinPacking binPacking = new BinPacking();
            	binPacking.setVisible(true);
            }
        });
    }
}
