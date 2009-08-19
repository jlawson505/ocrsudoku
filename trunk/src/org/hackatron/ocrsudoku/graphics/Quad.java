package org.hackatron.ocrsudoku.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Quad {
	
	private final static int VERTS = 4;

    private FloatBuffer _vertexBuffer;
    private FloatBuffer _texBuffer;
    private FloatBuffer _colorBuffer;
    private ShortBuffer _indexBuffer;
    
    // A quad centered on the origin.
    private final static float[] _coords = {
            // X, Y, Z
            -0.5f, -0.5f, 0,
            -0.5f,  0.5f, 0,
             0.5f, -0.5f, 0,
             0.5f,  0.5f, 0,
    	};
	    
    // Buffers to be passed to gl*Pointer() functions
    // must be direct, i.e., they must be placed on the
    // native heap where the garbage collector cannot
    // move them.
    //
    // Buffers with multi-byte datatypes (e.g., short, int, float)
    // must have their byte order set to native order
    
    public Quad() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(VERTS * 3 * 4);
        vbb.order(ByteOrder.nativeOrder());
        _vertexBuffer = vbb.asFloatBuffer();
        
        ByteBuffer cbb = ByteBuffer.allocateDirect(VERTS * 4 * 4);
        cbb.order(ByteOrder.nativeOrder());
        _colorBuffer = cbb.asFloatBuffer();

        ByteBuffer tbb = ByteBuffer.allocateDirect(VERTS * 2 * 4);
        tbb.order(ByteOrder.nativeOrder());
        _texBuffer = tbb.asFloatBuffer();

        ByteBuffer ibb = ByteBuffer.allocateDirect(VERTS * 2);
        ibb.order(ByteOrder.nativeOrder());
        _indexBuffer = ibb.asShortBuffer();

        for (int i = 0; i < VERTS; i++) {
            for(int j = 0; j < 3; j++) {
                _vertexBuffer.put(_coords[i*3+j]);
            }
        }
        
        for (int i = 0; i < VERTS; i++) {
            for(int j = 0; j < 3; j++) {
            	if(j == 0) { 
            		_colorBuffer.put(1.0f); 
        		}
            	else { 
            		_colorBuffer.put(_coords[i*3+(j-1)] + 0.5f);            	
        		}
            }
        }

        for (int i = 0; i < VERTS; i++) {
            for(int j = 0; j < 2; j++) {
                _texBuffer.put(_coords[i*3+j] + 0.5f);
            }
        }

        for(int i = 0; i < VERTS; i++) {
            _indexBuffer.put((short) i);
        }

        _colorBuffer.position(0);
        _vertexBuffer.position(0);
        _texBuffer.position(0);
        _indexBuffer.position(0);
    }

    public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CCW);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _vertexBuffer);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, _colorBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, _texBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, VERTS, GL10.GL_UNSIGNED_SHORT, _indexBuffer);
    }
}
