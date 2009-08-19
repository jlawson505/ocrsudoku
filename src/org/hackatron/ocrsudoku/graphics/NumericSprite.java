package org.hackatron.ocrsudoku.graphics;

import javax.microedition.khronos.opengles.GL10;


import android.graphics.Paint;

public class NumericSprite {
	
    private LabelMaker _labelMaker;
    private String _text;
    private int[] _width = new int[10];
    private int[] _labelId = new int[10];
	private final static String _strike = "0123456789";
	    
    public NumericSprite() {
        _text = "";
        _labelMaker = null;
    }

    public void initialize(GL10 gl, Paint paint) {
        int height = roundUpPower2((int) paint.getFontSpacing());
        final float interDigitGaps = 9 * 1.0f;
        int width = roundUpPower2((int) (interDigitGaps + paint.measureText(_strike)));
        
        _labelMaker = new LabelMaker(true, width, height);
        _labelMaker.initialize(gl);
        _labelMaker.beginAdding(gl);
        
        for (int i = 0; i < 10; i++) {
            String digit = _strike.substring(i, i+1);
            _labelId[i] = _labelMaker.add(gl, digit, paint);
            _width[i] = (int) Math.ceil(_labelMaker.getWidth(i));
        }
        _labelMaker.endAdding(gl);
    }

    public void shutdown(GL10 gl) {
        _labelMaker.shutdown(gl);
        _labelMaker = null;
    }

    /**
     * Find the smallest power of two >= the input value.
     * (Doesn't work for negative numbers.)
     */
    private int roundUpPower2(int x) {
        x = x - 1;
        x = x | (x >> 1);
        x = x | (x >> 2);
        x = x | (x >> 4);
        x = x | (x >> 8);
        x = x | (x >>16);
        return x + 1;
    }

    public void setValue(int value) {
        _text = format(value);
    }

    public void draw(GL10 gl, float x, float y, float viewWidth, float viewHeight) {
        int length = _text.length();
        _labelMaker.beginDrawing(gl, viewWidth, viewHeight);
        for(int i = 0; i < length; i++) {
            char c = _text.charAt(i);
            int digit = c - '0';
            _labelMaker.draw(gl, x, y, _labelId[digit]);
            x += _width[digit];
        }
        _labelMaker.endDrawing(gl);
    }

    public float width() {
        float width = 0.0f;
        int length = _text.length();
        for(int i = 0; i < length; i++) {
            char c = _text.charAt(i);
            width += _width[c - '0'];
        }
        return width;
    }

    private String format(int value) {
        return Integer.toString(value);
    }

  
}
