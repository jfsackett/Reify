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
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.sackett.reify.nn.NeuralNetwork.ClassifyOutput;

/**
 * This executes an artificial neural network, using eight the simulated annealing or backpropagation metheuristic.
 * Parameter suggestions:
 * 	sa pallet.csv 2 10 7 true -1.0 1.0 10000 .008 .002 0.5 0.2 1.0
 *  sa xor.csv 2 1 3 false -1.0 1.0 10000 .008 .002 0.5 0.2 1.0
 *  sa iris.csv 4 3 5 false -1.0 1.0 10000 .008 .002 0.5 0.2 1.0
 *  bp xor.csv 2 1 3 false -1.0 1.0 10000 1.0
 *  bp iris.csv 4 3 5 false -1.0 1.0 10000 0.1
 * @author Joseph Sackett
 */
public class MainNN {
	/** Neural network. */
	private NeuralNetwork neuralNetwork;
	
	/** Input data. */
	private double[][] trainInputs;
	
	/** Output data. */
	private double[][] trainOutputs;

	/** Maximum number of training epochs. */
	private int maxEpochs;

	/** Starting temperature for simulated annealing. */
	private double startTemp;
	
	/** Edning temperature for simulated annealing. */ 
	private double endTemp;
	
	/** Probability of updating a weight. */
	private double updateProb;
	
	/** Factor by which a chosen weight can be updated. */
	private double weightFactor;
	
	/** Weight factor change each epoch. */
	private double weightFactorChange;
	
	/** 4 decimal display. */
	private static DecimalFormat decFormat = new DecimalFormat("#.####");
	
	/** 1 decimal display format. */
	private static DecimalFormat pctFormat = new DecimalFormat("#.#");
	
	static {
		decFormat.setRoundingMode(RoundingMode.HALF_UP);
		pctFormat.setRoundingMode(RoundingMode.HALF_UP);
	}

	/** Main program for neural network test. */
	public static void main(String[] args) {
		if (args.length < 4) {
			printUsage();
			System.exit(1);
		}
		
		// Can use either simulated annealing or backpropagation.
		boolean saBpFlag = true;;
		if (args[0].equalsIgnoreCase("sa")) {
			// Use simulated annealing.
			saBpFlag = true;
		}
		else if (args[0].equalsIgnoreCase("bp")) {
			// Use backpropagation.
			saBpFlag = false;
		}
		else {
			printUsage();
			System.exit(1);
		}
		
		// Get input filename from command line.
		String inputFileName = args[1];
		// Get number of input nodes from command line.
		int numInputNodes = Integer.parseInt(args[2]);
		// Get number of output nodes from command line.
		int numOutputNodes = Integer.parseInt(args[3]);

		// Load data file into input & output arrays.
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
		
		// Parse the optional command line parameters, else use initialization defauls below.
		boolean palletData = false;
		int numHiddenNodes = 3;
		double minWeight = -1.0;
		double maxWeight = 1.0;
		int maxEpochs = 10000;
		double startTemp = .008;
		double endTemp = .002;
		double updateProb = 0.5;
		double weightFactor = 0.2;
		double weightFactorChange = 0.9998;
		double eta = .1;
		double momentum = 0.0;
		MainNN mainNN;
		if (saBpFlag) {
			switch(args.length) {
			case 14:
				weightFactorChange = Double.parseDouble(args[13]);
			case 13:
				weightFactor = Double.parseDouble(args[12]);
			case 12:
				updateProb = Double.parseDouble(args[11]);
			case 11:
				endTemp = Double.parseDouble(args[10]);
			case 10:
				startTemp = Double.parseDouble(args[9]);
			case 9:
				maxEpochs = Integer.parseInt(args[8]);
			case 8:
				maxWeight = Double.parseDouble(args[7]);
			case 7:
				minWeight = Double.parseDouble(args[6]);
			case 6:
				palletData = Boolean.parseBoolean(args[5]);
			case 5:
				numHiddenNodes = Integer.parseInt(args[4]);
				break;
			default:
				printUsage();
				System.exit(1);
			}
			// Construct neural network and mainNN metaheuristic execution object.
			mainNN = new MainNN(trainInputs.toArray(new double[trainInputs.size()][]), trainOutputs.toArray(new double[trainOutputs.size()][]), maxEpochs, startTemp, endTemp, updateProb, weightFactor, weightFactorChange, 
								buildNeuralNetwork(numInputNodes, numOutputNodes, numHiddenNodes, minWeight, maxWeight, true, palletData));
		}
		else {
			switch(args.length) {
			case 11:
				momentum = Double.parseDouble(args[10]);
			case 10:
				eta = Double.parseDouble(args[9]);
			case 9:
				maxEpochs = Integer.parseInt(args[8]);
			case 8:
				maxWeight = Double.parseDouble(args[7]);
			case 7:
				minWeight = Double.parseDouble(args[6]);
			case 6:
				palletData = Boolean.parseBoolean(args[5]);
			case 5:
				numHiddenNodes = Integer.parseInt(args[4]);
				break;
			default:
				printUsage();
				System.exit(1);
			}
			// Construct neural network and mainNN metaheuristic execution object.
			mainNN = new MainNN(trainInputs.toArray(new double[trainInputs.size()][]), trainOutputs.toArray(new double[trainOutputs.size()][]), maxEpochs, eta, momentum,
								buildNeuralNetwork(numInputNodes, numOutputNodes, numHiddenNodes, minWeight, maxWeight, true, palletData));
		}
		
		// Execute mainNN metaheuristic, based on above configuration.
		mainNN.run(saBpFlag);
	}
	
	/** Display command line syntax. */
	private static void printUsage() {
		System.out.println("Usage for simulated annealing:");
		System.out.println("java com.sackett.reify.nn.MainNN sa {input filename} {num input nodes} {num output nodes} [num hidden nodes] [pallet output flag] [min init weight] [max init weight] [max num epochs] [init temp] [start temp] [end temp] [update prob] [weight factor] [weight factor change]");
		System.out.println("Usage for backpropagation:");
		System.out.println("java com.sackett.reify.nn.MainNN bp {input filename} {num input nodes} {num output nodes} [num hidden nodes] [pallet output flag] [min init weight] [max init weight] [max num epochs] [eta]");
	}

	/**
	 * Constructor for simulated annealing neural network definition.
	 */
	public MainNN(double[][] trainInputs, double[][] trainOutputs, int maxEpochs, double startTemp, double endTemp, double updateProb, double weightFactor, double weightFactorChange, NeuralNetwork neuralNetwork) {
		this.trainInputs = trainInputs;
		this.trainOutputs = trainOutputs;
		this.maxEpochs = maxEpochs;
		this.startTemp = startTemp;
		this.endTemp = endTemp;
		this.updateProb = updateProb;
		this.weightFactor = weightFactor;
		this.weightFactorChange = weightFactorChange;
		this.neuralNetwork = neuralNetwork;
	}
		
	/**
	 * Constructor for backpropagation neural network definition.
	 */
	public MainNN(double[][] trainInputs, double[][] trainOutputs, int maxEpochs, double eta, double momentum, NeuralNetwork neuralNetwork) {
		this.trainInputs = trainInputs;
		this.trainOutputs = trainOutputs;
		this.maxEpochs = maxEpochs;
		this.neuralNetwork = neuralNetwork;
		this.neuralNetwork.setEta(eta);
		this.neuralNetwork.setMomentum(momentum);		
	}
	
	/** Executes simulated annealing or backpropagation metaheuristic. */
	private void run(boolean saBpFlag) {
		if (saBpFlag) {
			sa();
		}
		else {
			bp();
		}
	}
	
	/** Execute simulated annealing metaheuristic. */
	private void sa() {
		// Previous neural network.
		NeuralNetwork prevNeuralNetwork = null;
		// Previous epoch average error.
		double prevAvgRMSE = Double.MAX_VALUE;
		// Previous epoch maximum error.
		double prevMaxRMSE = Double.MAX_VALUE;
		// Previous epoch accuracy.
		double prevAccuracy = Double.MAX_VALUE;
		// Maximum accuracy over all epocks.
		double maxAccuracy = 0.0;
		// Minimum average RMSE over all epochs.
		double minAveRMSE = Double.MAX_VALUE;

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
				ClassifyOutput classifyOutput = neuralNetwork.classify(trainInputs[ixTrain], trainOutputs[ixTrain]);
				sumRMSE = sumRMSE + classifyOutput.getRmsError();
				maxRMSE = Math.max(maxRMSE, classifyOutput.getRmsError());
				sumClassError = sumClassError + classifyOutput.getClassError();
			}
			
			double avgRMSE = sumRMSE / trainInputs.length;
			double accuracy = 1 - sumClassError / trainInputs.length;
			System.out.println("Epoch " + epoch + ": maxRMSE: " + decFormat.format(maxRMSE) + ", aveRMSE: " + decFormat.format(avgRMSE) 
								+ ", Acc: " + pctFormat.format(accuracy * 100) + '%');
			// Regress to previous neural network if error is higher.
			if (avgRMSE > prevAvgRMSE && !moveUphill(prevAvgRMSE, avgRMSE, prevMaxRMSE, maxRMSE, ((double)maxEpochs - (double)epoch)/(double)maxEpochs * (startTemp - endTemp) + endTemp)) {
				neuralNetwork = prevNeuralNetwork;
				avgRMSE = prevAvgRMSE;
				maxRMSE = prevMaxRMSE;
				accuracy = prevAccuracy;
			}
			
			// Save previous neural network.
			try {
				prevNeuralNetwork = neuralNetwork.clone();
			}
			catch (CloneNotSupportedException ex) {
				ex.printStackTrace();
			}
			
			// Save previous error for next comparison.
			prevAvgRMSE = avgRMSE;
			prevMaxRMSE = maxRMSE;
			prevAccuracy = accuracy;
			
			if (epoch+1 < maxEpochs) {
				// Update neural network neighborhood.
				neuralNetwork.updateNeighborhood(updateProb, weightFactor);
			}
			
			// Terminate if classification error is zero.
			if (sumClassError == 0.0) {
				System.out.println("Success");
				break;
			}
			
			// Change weight factor.
			weightFactor *= weightFactorChange;
			
			// Save globally best accuracy & avgRMSE for report.
			if (maxAccuracy < accuracy) {
				maxAccuracy = accuracy;
			}
			if (minAveRMSE > avgRMSE) {
				minAveRMSE = avgRMSE;
			}
		}
		System.out.println("minAveRMSE: " + decFormat.format(minAveRMSE) + ", maxAccuracy: " + pctFormat.format(maxAccuracy * 100) + '%');
	}
	
	/** Calculate simulated annealing chance of moving uphill, check random probability and return flag indicating direction. */ 
	private static boolean moveUphill(double prevAvgRMSE, double avgRMSE, double prevMaxRMSE, double maxRMSE, double temp) {
		double adjPrevAvgRMSE = prevAvgRMSE + prevMaxRMSE / 100;
		double adjAvgRMSE = avgRMSE + maxRMSE / 100;
		double prob = Math.exp((adjPrevAvgRMSE - adjAvgRMSE) / temp);
		double random = Math.random();
		System.out.println(((prob > random) ? "UP" : "  ") + "  prob- " + decFormat.format(prob) + "  random- " + decFormat.format(random) + "  prevAvgRMSE- " + decFormat.format(adjPrevAvgRMSE) + "  avgRMSE- " + decFormat.format(adjAvgRMSE) + "  temp- " + temp);
		return prob > random ;
	}
	
	/** Execute backpropagation metaheuristic. */
	private void bp() {
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
			
			System.out.println("Epoch " + epoch + ": maxRMSE: " + decFormat.format(maxRMSE) + ", aveRMSE: " + decFormat.format(sumRMSE / trainInputs.length) + ", Acc: " + pctFormat.format((1 - sumClassError / trainInputs.length) * 100) + '%');
			
			// Terminate if classification error is zero.
			if (sumClassError == 0.0) {
				System.out.println("Success");
				break;
			}
		}
	}

	/** Build regular neural network or more advanced palletizing network with factored output nodes, based on flag. */
	private static NeuralNetwork buildNeuralNetwork(int numInputNodes, int numOutputNodes, int numHiddenNodes, double minWeight, double maxWeight, boolean biasNodes, boolean palletData) {
		if (palletData) {
			return buildPalletNeuralNetwork(numInputNodes, numOutputNodes, numHiddenNodes, minWeight, maxWeight, biasNodes);
		}
		else {
			return buildStandardNeuralNetwork(numInputNodes, numOutputNodes, numHiddenNodes, minWeight, maxWeight, biasNodes);
		}
	}
	
	/** Build standard neural network. */
	private static NeuralNetwork buildStandardNeuralNetwork(int numInputNodes, int numOutputNodes, int numHiddenNodes, double minWeight, double maxWeight, boolean biasNodes) {
		NeuralNetwork neuralNetwork = new NeuralNetwork();
		
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
		outputNodes.add(new OutputNode(true));
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
				Napse napse = new Napse(inputNode, hiddenNode, getRandomWeight(minWeight, maxWeight));
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
				Napse napse = new Napse(hiddenNode, outputNode, getRandomWeight(minWeight, maxWeight));
				// Add to both of its ends.
				hiddenNode.getOutputNapses().add(napse);
				outputNode.getInputNapses().add(napse);
			}
		}
		
		// Add input, hidden and output layers to neural network.
		neuralNetwork.setInputNodes(inputNodes);
		neuralNetwork.setHiddenNodes(hiddenNodes);
		neuralNetwork.setOutputNodes(outputNodes);
		
		return neuralNetwork;
	}

	/** Build advanced palletizing network with factored output nodes. */
	private static NeuralNetwork buildPalletNeuralNetwork(int numInputNodes, int numOutputNodes, int numHiddenNodes, double minWeight, double maxWeight, boolean biasNodes) {
		NeuralNetwork neuralNetwork = new NeuralNetwork();
		
		// Build node layers. Capacity includes room for optional bias node.
		List<InputNode> inputNodes = new ArrayList<InputNode>(numInputNodes + 1);
		List<HiddenNode> hiddenNodes = new ArrayList<HiddenNode>(numHiddenNodes + 1);
		List<OutputNode> outputNodes = new ArrayList<OutputNode>(numOutputNodes + 1);
		// Intermediate hidden layers between first hidden later and output nodes.
		List<HiddenNode> hiddenNodesA = new ArrayList<HiddenNode>(numHiddenNodes + 1);
		List<HiddenNode> hiddenNodesB = new ArrayList<HiddenNode>(numHiddenNodes + 1);
		
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
		
		// Build intermediate A hidden nodes (at 1..numHiddenNodes). Includes optionally used bias node at index 0.
		hiddenNodesA.add(new HiddenNode(1.0, true));
		for (int ix = 1 ; ix <= numHiddenNodes ; ix++) {
			hiddenNodesA.add(new HiddenNode());
		}
		
		// Build intermediate B hidden nodes (at 1..numHiddenNodes). Includes optionally used bias node at index 0.
		hiddenNodesB.add(new HiddenNode(1.0, true));
		for (int ix = 1 ; ix <= numHiddenNodes ; ix++) {
			hiddenNodesB.add(new HiddenNode());
		}
		
		// Build output nodes (at 1..numOutputNodes). Includes unused node at index 0 for consistency.
		outputNodes.add(new OutputNode(true));
		for (int ix = 1 ; ix <= numOutputNodes ; ix++) {
			outputNodes.add(new FactoredOutputNode((ix < 6) ? 9.0 : 4.0, false));
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
				Napse napse = new Napse(inputNode, hiddenNode, getRandomWeight(minWeight, maxWeight));
				// Add to both of its ends.
				inputNode.getOutputNapses().add(napse);
				hiddenNode.getInputNapses().add(napse);
			}
		}
		
		// Add synapse from hidden to hiddenA & hiddenB layers.
		for (HiddenNode hiddenNode : hiddenNodes) {
			// If not using bias nodes, skip adding synapses to bias node.
			if (!biasNodes && hiddenNode.isBias()) {
				continue;
			}
			for (HiddenNode hiddenNodeA : hiddenNodesA) {
				// Skip adding synapses to bias node.
				if (hiddenNodeA.isBias()) {
					continue;
				}
				// Create synapse.
				Napse napse = new Napse(hiddenNode, hiddenNodeA, getRandomWeight(minWeight, maxWeight));
				// Add to both of its ends.
				hiddenNode.getOutputNapses().add(napse);
				hiddenNodeA.getInputNapses().add(napse);
			}
			for (HiddenNode hiddenNodeB : hiddenNodesB) {
				// Skip adding synapses to bias node.
				if (hiddenNodeB.isBias()) {
					continue;
				}
				// Create synapse.
				Napse napse = new Napse(hiddenNode, hiddenNodeB, getRandomWeight(minWeight, maxWeight));
				// Add to both of its ends.
				hiddenNode.getOutputNapses().add(napse);
				hiddenNodeB.getInputNapses().add(napse);
			}
		}
		
		// Add synapse between hiddenA & first 5 nodes of output layers.
		for (HiddenNode hiddenNodeA : hiddenNodesA) {
			// If not using bias nodes, skip adding synapses to bias node.
			if (!biasNodes && hiddenNodeA.isBias()) {
				continue;
			}
			int ix = 0;
			for (OutputNode outputNode : outputNodes) {
				// Skip adding synapses to bias node.
				if (outputNode.isBias()) {
					continue;
				}
				// Create synapse.
				Napse napse = new Napse(hiddenNodeA, outputNode, getRandomWeight(minWeight, maxWeight));
				// Add to both of its ends.
				hiddenNodeA.getOutputNapses().add(napse);
				outputNode.getInputNapses().add(napse);
				
				if (++ix >= 5) {
					break;
				}
			}
		}
		
		// Add synapse between hiddenB & last 5 nodes of output layers.
		for (HiddenNode hiddenNodeB : hiddenNodesB) {
			// If not using bias nodes, skip adding synapses to bias node.
			if (!biasNodes && hiddenNodeB.isBias()) {
				continue;
			}
			int ix = 0;
			for (OutputNode outputNode : outputNodes) {
				// Skip adding synapses to bias node.
				if (outputNode.isBias() || ++ix <= 5) {
					continue;
				}
				// Create synapse.
				Napse napse = new Napse(hiddenNodeB, outputNode, getRandomWeight(minWeight, maxWeight));
				// Add to both of its ends.
				hiddenNodeB.getOutputNapses().add(napse);
				outputNode.getInputNapses().add(napse);				
			}			
		}

		// Collect different layers of hidden nodes together.
		List<HiddenNode> allHiddenNodes = new ArrayList<HiddenNode>();
		allHiddenNodes.addAll(hiddenNodes);
		allHiddenNodes.addAll(hiddenNodesA);
		allHiddenNodes.addAll(hiddenNodesB);
		
		// Add input, hidden and output layers to neural network.
		neuralNetwork.setInputNodes(inputNodes);
		neuralNetwork.setHiddenNodes(allHiddenNodes);
		neuralNetwork.setOutputNodes(outputNodes);
		
		return neuralNetwork;
	}

	/**
	 * Generate random weight between minWeight & maxWeight.
	 * @return random weight
	 */
	private static double getRandomWeight(double minWeight, double maxWeight) {
		return (maxWeight - minWeight) * Math.random() + minWeight;
	}
}
