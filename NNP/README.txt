========================================================================
    Adaptive Neural Network
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
=========================================================================

This provides an easy explanation of how to build and execute the programs distributed here.
These are the programs:
MainNN - Artificial Neural Network builder, trainer & executor.
BinPacking - Bin packing program with graphical display.

Build instructions:
Make sure you have a JDK environment in your path, execute:
build_all.bat

Note that this is distributed with precompiled versions compatible with JDK 1.7 so the build step may be skipped.

Neural Network execution instructions:
Make sure you have java in your path. NOTE: This should be the same as you compiled with above.
Execute any of the scripts below or open one and see how to do it manually.
run_pallet_sa.bat   - simulated annealing palletizing dataset
run_xor_sa.bat      - simulated annealing xor dataset
run_iris_sa.bat     - simulated annealing iris dataset
run_xor_bp.bat      - backpropagation xor dataset
run_iris_bp.bat     - backpropagation iris dataset

Usage to manually execute simulated annealing:
java com.sackett.reify.nn.MainNN sa {input filename} {num input nodes} {num output nodes} [num hidden nodes] [pallet output flag] [min init weight] [max init weight] [max num epochs] [init temp] [start temp] [end temp] [update prob] [weight factor] [weight factor change]

Usage to manually execute backpropagation:
java com.sackett.reify.nn.MainNN bp {input filename} {num input nodes} {num output nodes} [num hidden nodes] [pallet output flag] [min init weight] [max init weight] [max num epochs] [eta]


Bin Packing execution instructions:
Make sure you have java in your path. NOTE: This should be the same as you compiled with above.
Execute the scripts below or open one and see how to do it manually.
run_bin_packing.bat

Usage to manually execute bin packing:
java com.sackett.reify.bp.BinPacking
