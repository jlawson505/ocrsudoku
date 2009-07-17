package org.hackatron.ocrsudoku;

import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;

public class OCR {
	private int _width;
	private int _height;

	private ArrayList<Neuron> _neuronList;

	public OCR(int width, int height) {
		_width = width;
		_height = height;
		_neuronList = new ArrayList<Neuron>();
	}

	public boolean doLearn(Bitmap inputBitmap, char character) {
		int[][] input = createArray(inputBitmap);
		return doLearn(input, character);
	}

	// Learns a character.
	// Gets the pixel values and the character to be learned as input.
	// Returns true if character already learned.
	public boolean doLearn(int[][] input, char character) {
		for (Neuron neuron : _neuronList) {
			// Finds if character already is in the list.
			if (neuron.getCharacter() == character) {
				// Learn
				neuron.doLearn(input, 1);
				return true;
			}
		}

		// Creates a new neuron if character not already in list.
		Neuron neuron = new Neuron(character, _width, _height);
		neuron.doLearn(input, 1);

		// Adds the neuron to the list.
		_neuronList.add(neuron);

		return false;
	}

	public char getBestMatch(Bitmap inputBitmap) {
		int[][] input = createArray(inputBitmap);
		return getBestMatch(input);
	}

	// Finds the best matching neuron.
	// Gets the pixel values as input.
	// Returns a string of the best match
	public char getBestMatch(int[][] input) {

		if (_neuronList.size() == 0) {
			return ' ';
		}

		Neuron bestNeuron = _neuronList.get(0);
		float bestValue = bestNeuron.getOutput(input);

		// For each neuron in the list the best match is found to the input
		// pixel values
		for (int index = 1; index < _neuronList.size(); index++) {
			Neuron currentNeuron = _neuronList.get(index);
			float currentValue = currentNeuron.getOutput(input);

			if (currentValue > bestValue) {
				bestNeuron = currentNeuron;
				bestValue = currentValue;
			}
		}

		return bestNeuron.getCharacter();
	}

	private int[][] createArray(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		// Check if rescaling of bitmap is required
		if (width != _width || height != _height) {

			// Calculate the scale
			float scaleWidth = ((float) _width) / width;
			float scaleHeight = ((float) _height) / height;

			// Create a matrix for the manipulation
			Matrix matrix = new Matrix();
			// Resize the bit map
			matrix.postScale(scaleWidth, scaleHeight);

			// Recreate the new Bitmap
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
					true);
		}

		int[][] input = new int[_width][_height];
		for (int row = 0; row < _height; row++) {
			for (int column = 0; column < _width; column++) {
				int value = Color.blue(bitmap.getPixel(column, row));

				if (value == 255) {
					input[column][row] = 0;
				} else {
					input[column][row] = 1;
				}
			}
		}
		return input;
	}

}