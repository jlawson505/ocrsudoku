package org.hackatron.ocrsudoku;

import java.util.Random;

public class Neuron {
	private int _width;
	private int _height;

	// The character that the neuron has learned.
	public char _character;

	// Initializes the neuron weights and inputs
	private float[][] _weights;

	// Learning rate.
	private float _eta;

	// Creates an random method.
	private Random _rand;

	// Neuron constructor.
	// Sets the learning rate and initializes the neuron.
	public Neuron(char character, int width, int height) {

		_character = character;
		_width = width;
		_height = height;

		_eta = 0.20f;
		_rand = new Random(System.nanoTime());

		_weights = new float[width][height];

		initNeuron();
	}

	// Sets the weights to random floats with values 0 - 1
	private void initNeuron() {
		for (int row = 0; row < _height; row++) {
			for (int column = 0; column < _width; column++) {
				_weights[column][row] = ((float) _rand.nextFloat());
			}
		}
	}

	// Gets the character
	public char getCharacter() {
		return _character;
	}

	// Calculates the output from the neuron
	// Multiplies each weight with input and returns the sum
	public float getOutput(int[][] input) {
		float output = 0;
		for (int row = 0; row < _height; row++) {
			for (int column = 0; column < _width; column++) {
				output += input[column][row] * _weights[column][row];
			}
		}
		return output;
	}

	// Learns the neuron
	// Inputs error. If 1, positive learning and if -1, negative learning.
	public void doLearn(int[][] input, int error) {
		for (int row = 0; row < _height; row++) {
			for (int column = 0; column < _width; column++) {
				// Calculates the new weights.
				_weights[column][row] += input[column][row] * _eta * error;
			}
		}
	}
}