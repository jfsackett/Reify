/*
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
*/
package com.sackett.reify.nn;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sackett.reify.nn.NeuralNetwork.ClassifyOutput;

/**
 * This executes an artificial neural network. 
 * @author Joseph Sackett
 */
public class MainNN {
	/** Neural network. */
	private NeuralNetwork neuralNetwork;
	
	/** Maximum number of training epochs. */
	private int maxEpochs;
	
	/** Weight adjustment coefficient. */
	private double eta;
	
	/** Weight adjustment factor to prior update. */
	private double momentum;
	
	/** Minimum initial synapse weight. */
	private double minWeight;
	
	/** Maximum initial synapse weight. */
	private double maxWeight;
	
	/** Input data. */
	private double[][] trainInputs;
	
	/** Output data. */
	private double[][] trainOutputs;


	/** Main program for neural network test. */
	public static void main(String[] args) {
		//TODO Process command line parameters.
		String inputFileName = null;
		int numInputNodes = 0;
		int numOutputNodes = 0;
		int numHiddenNodes = 10;
		int maxEpochs = 10000;
		double eta = 1.0;
		double momentum = 0.0;
		double minWeight = -1.0;
		double maxWeight = 1.0;
		
		switch(args.length) {
		case 9:
			maxWeight = Double.parseDouble(args[8]);
		case 8:
			minWeight = Double.parseDouble(args[7]);
		case 7:
			momentum = Double.parseDouble(args[6]);
		case 6:
			eta = Double.parseDouble(args[5]);
		case 5:
			maxEpochs = Integer.parseInt(args[4]);
		case 4:
			numHiddenNodes = Integer.parseInt(args[3]);
		case 3:
			inputFileName = args[0];
			numInputNodes = Integer.parseInt(args[1]);
			numOutputNodes = Integer.parseInt(args[2]);
			break;
		default:
			System.out.println("Command line error.");
			System.exit(1);
		}
		
		// Load data file into input & output arrays.
//		double[][] trainInputs = new double[][]{new double[]{0.0, 0.0}, new double[]{1.0, 0.0}, new double[]{0.0, 1.0}, new double[]{1.0, 1.0}};
//		double[][] trainOutputs = new double[][]{new double[]{0.0}, new double[]{1.0}, new double[]{1.0}, new double[]{0.0}};
		List<double[]> trainInputs = new ArrayList<double[]>();
		List<double[]> trainOutputs = new ArrayList<double[]>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(inputFileName));
			String row;
			String[] cells;
			while ((row = reader.readLine()) != null) {
				cells = row.split(",");
				double[] inputValues = new double[numInputNodes];
				trainInputs.add(inputValues);
				double[] outputValues = new double[numOutputNodes];
				trainOutputs.add(outputValues);
				for (int ix = 0 ; ix < cells.length ; ix++) {
					// Place value in input or output array.
					if (ix < numInputNodes) {
						inputValues[ix] = Double.parseDouble(cells[ix]);
					}
					else {
						outputValues[ix - numInputNodes] = Double.parseDouble(cells[ix]);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try { reader.close(); } catch (IOException e) {}
			}
		}
		
		boolean biasNodes = true;
		MainNN mainNN = new MainNN(trainInputs.toArray(new double[trainInputs.size()][]), trainOutputs.toArray(new double[trainOutputs.size()][]), numInputNodes, numHiddenNodes, numOutputNodes, biasNodes, maxEpochs, eta, momentum, minWeight, maxWeight);
		mainNN.run();
	}

	/**
	 * Constructor takes neural network definition.
	 */
	private MainNN(double[][] trainInputs, double[][] trainOutputs, int numInputNodes, int numHiddenNodes, int numOutputNodes, boolean biasNodes, 
					int maxEpochs, double eta, double momentum, double minWeight, double maxWeight) {
		this.trainInputs = trainInputs;
		this.trainOutputs = trainOutputs;
		this.maxEpochs = maxEpochs;
		this.eta = eta;
		this.momentum = momentum;
		this.minWeight = minWeight;
		this.maxWeight = maxWeight;
		
		neuralNetwork = buildNeuralNetwork(numInputNodes, numHiddenNodes, numOutputNodes, biasNodes);
	}
	
	private void run() {
		// Loop through epochs.
		for (int epoch = 0 ; epoch < maxEpochs ; epoch++) {
			// Sum root mean square error. 
			double sumRMSE = 0.0;
			// Maximum root mean square error. 
			double maxRMSE = 0.0;
			// Sum classification errors.
			double sumClassError = 0.0;
			
			// Loop through training instances.
			for (int ixTrain = 0 ; ixTrain < trainInputs.length ; ixTrain++) {
				// Set neural network inputs.
				neuralNetwork.backpropagate(trainInputs[ixTrain], trainOutputs[ixTrain]);
				ClassifyOutput classifyOutput = neuralNetwork.classify(trainInputs[ixTrain], trainOutputs[ixTrain]);
				sumRMSE = sumRMSE + classifyOutput.getRmsError();
				maxRMSE = Math.max(maxRMSE, classifyOutput.getRmsError());
				sumClassError = sumClassError + classifyOutput.getClassError();
			}
			
			System.out.println("Epoch " + epoch + ": maxRMSE: " + maxRMSE + ", aveRMSE: " + sumRMSE / trainInputs.length + ", Acc: " + ((1 - sumClassError / trainInputs.length) * 100) + '%');
			
			// Terminate if classification error is zero.
			if (sumClassError == 0.0) {
				System.out.println("Success");
				break;
			}
		}
	}

	private NeuralNetwork buildNeuralNetwork(int numInputNodes, int numHiddenNodes, int numOutputNodes, boolean biasNodes) {
		NeuralNetwork neuralNetwork = new NeuralNetwork(eta, momentum);
		
		// Build node layers. Capacity includes room for optional bias node.
		List<InputNode> inputNodes = new ArrayList<InputNode>(numInputNodes + 1);
		List<HiddenNode> hiddenNodes = new ArrayList<HiddenNode>(numHiddenNodes + 1);
		List<OutputNode> outputNodes = new ArrayList<OutputNode>(numOutputNodes + 1);
		
		// Build input nodes (at 1..numInputNodes). Includes optionally used bias node at index 0.
		inputNodes.add(new InputNode(1.0, true));
		for (int ix = 1 ; ix <= numInputNodes ; ix++) {
			inputNodes.add(new InputNode());
		}
		
		// Build hidden nodes (at 1..numHiddenNodes). Includes optionally used bias node at index 0.
		hiddenNodes.add(new HiddenNode(1.0, true));
		for (int ix = 1 ; ix <= numHiddenNodes ; ix++) {
			hiddenNodes.add(new HiddenNode());
		}
		
		// Build input nodes (at 1..numOutputNodes). Includes unused node at index 0 for consistency.
		outputNodes.add(new OutputNode(0.0, true));
		for (int ix = 1 ; ix <= numOutputNodes ; ix++) {
			outputNodes.add(new OutputNode());
		}
		
		// Add synapse between input & hidden layers.
		for (InputNode inputNode : inputNodes) {
			// If not using bias nodes, skip adding synapses to bias node.
			if (!biasNodes && inputNode.isBias()) {
				continue;
			}
			for (HiddenNode hiddenNode : hiddenNodes) {
				// If not using bias nodes, skip adding synapses to bias node.
				if (hiddenNode.isBias()) {
					continue;
				}
				// Create synapse.
				Napse napse = new Napse(inputNode, hiddenNode, getRandomWeight());
				// Add to both of its ends.
				inputNode.getOutputNapses().add(napse);
				hiddenNode.getInputNapses().add(napse);
			}
		}
		
		// Add synapse between hidden & output layers.
		for (HiddenNode hiddenNode : hiddenNodes) {
			// If not using bias nodes, skip adding synapses to bias node.
			if (!biasNodes && hiddenNode.isBias()) {
				continue;
			}
			for (OutputNode outputNode : outputNodes) {
				// Skip adding synapses to bias node.
				if (outputNode.isBias()) {
					continue;
				}
				// Create synapse.
				Napse napse = new Napse(hiddenNode, outputNode, getRandomWeight());
				// Add to both of its ends.
				hiddenNode.getOutputNapses().add(napse);
				outputNode.getInputNapses().add(napse);
			}
		}
		
		// Add input and output layers to neural network.
		neuralNetwork.setInputNodes(inputNodes);
		neuralNetwork.setOutputNodes(outputNodes);
		
		return neuralNetwork;
	}

	private double getRandomWeight() {
		return (maxWeight - minWeight) * Math.random() + minWeight;
	}
}
