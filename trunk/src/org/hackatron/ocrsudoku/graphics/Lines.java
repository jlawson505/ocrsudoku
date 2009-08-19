package org.hackatron.ocrsudoku.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Lines {
	
	private final static int VERTS = 2;

    private FloatBuffer _vertexBuffer;
    private ShortBuffer _indexBuffer;
        
    // Buffers to be passed to gl*Pointer() functions
    // must be direct, i.e., they must be placed on the
    // native heap where the garbage collector cannot
    // move them.
    //
    // Buffers with multi-byte datatypes (e.g., short, int, float)
    // must have their byte order set to native order
    
    public Lines() {
    }
    
    public void initialize(float x0, float y0, float x1, float y1)
    {	    	
        float[] coords = new float[] { x0, y0, x1, y1 };
    	_vertexBuffer = FloatBuffer.wrap(coords);
          
        short[] indices = new short[] { 0, 1 };
        _indexBuffer = ShortBuffer.wrap(indices);

        _vertexBuffer.position(0);
        _indexBuffer.position(0);
    }

    public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CCW);
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, _vertexBuffer);
        gl.glDrawElements(GL10.GL_LINES, VERTS, GL10.GL_UNSIGNED_SHORT, _indexBuffer);
    }
}