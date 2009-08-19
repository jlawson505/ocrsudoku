package org.hackatron.ocrsudoku;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.os.SystemClock;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.opengles.GL10;

import org.hackatron.ocrsudoku.graphics.Grid;
import org.hackatron.ocrsudoku.graphics.LabelMaker;
import org.hackatron.ocrsudoku.graphics.Lines;
import org.hackatron.ocrsudoku.graphics.NumericSprite;
import org.hackatron.ocrsudoku.graphics.Projector;
import org.hackatron.ocrsudoku.graphics.Quad;
import org.hackatron.ocrsudoku.graphics.Triangle;

class HUDView implements GLSurfaceView.Renderer{
	
	private int _width;
    private int _height;
    private Context _context;
    private int frames;
    private int _msPerFrame;
    private final static int SAMPLE_PERIOD_FRAMES = 12;
    private final static float SAMPLE_FACTOR = 1.0f / SAMPLE_PERIOD_FRAMES;
    private long _startTime;
    private LabelMaker _labelMaker;
    private Paint _labelPaint;
    private int _labelMsPF;
    private Projector _projector;
    private NumericSprite _numericSprite;
    private Lines _gridLines;
    
    private int[] _sudukoArray = new int[] {
			1,2,3,4,5,6,7,8,9,
			2,3,4,5,6,7,8,9,1,
			3,4,5,6,7,8,9,1,2,
			4,5,6,7,8,9,1,2,3,
			5,6,7,8,9,1,2,3,4,
			6,7,8,9,1,2,3,4,5,
			7,8,9,1,2,3,4,5,6,
			8,9,1,2,3,4,5,6,7,
			9,1,2,3,4,5,6,7,8};
	    
    public HUDView(Context context) {
        _context = context;
        _projector = new Projector();
       
        _labelPaint = new Paint();
        _labelPaint.setTextSize(20);
        _labelPaint.setAntiAlias(true);
        _labelPaint.setARGB(0xff, 0x00, 0x00, 0x00);
    }
    
    public int[] getConfigSpec() {
        // We don't need a depth buffer, and don't care about our
        // color depth.
        int[] configSpec = {
                EGL10.EGL_DEPTH_SIZE, 0,
                EGL10.EGL_NONE
        };
        return configSpec;
    }

    @Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        /*
         * By default, OpenGL enables features that improve quality
         * but reduce performance. One might want to tweak that
         * especially on software renderer.
         */
        gl.glDisable(GL10.GL_DITHER);

        /*
         * Some one-time OpenGL initialization can be made here
         * probably based on features of this particular context
         */
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                GL10.GL_FASTEST);

        gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        
        if (_labelMaker != null) {
            _labelMaker.shutdown(gl);
        } else {
            _labelMaker = new LabelMaker(true, 256, 64);
        }
        _labelMaker.initialize(gl);
        _labelMaker.beginAdding(gl);
        _labelMsPF = _labelMaker.add(gl, "ms/f", _labelPaint);
        _labelMaker.endAdding(gl);
        
        if (_numericSprite != null) {
            _numericSprite.shutdown(gl);
        } else {
            _numericSprite = new NumericSprite();
        }
        _numericSprite.initialize(gl, _labelPaint);
        
        _gridLines = new Lines();
    }

	@Override
	public void onDrawFrame(GL10 gl) {
        /*
         * By default, OpenGL enables features that improve quality
         * but reduce performance. One might want to tweak that
         * especially on software renderer.
         */
        gl.glDisable(GL10.GL_DITHER);
        
        /*
         * Usually, the first thing one might want to do is to clear
         * the screen. The most efficient way of doing this is to use
         * glClear().
         */
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        /*
         * Now we're ready to draw some 3D objects
         */
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glOrthof(0.0f, _width, 0.0f, _height, 0.0f, 10.0f);
       
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);
        gl.glLineWidth(1.0f);      
        
        float msPFX = _width - _labelMaker.getWidth(_labelMsPF) - 1;
        
        _projector.getCurrentModelView(gl);
        _labelMaker.beginDrawing(gl, _width, _height);
        _labelMaker.draw(gl, msPFX, 0, _labelMsPF);
        _labelMaker.endDrawing(gl);

        drawSuduko(gl);
        drawMsPF(gl, msPFX);
      }
	
	  private void drawSuduko(GL10 gl) {
		  
		  float width = _width / 1.5f;
		  float height = _height / 1.5f;
		  
		  float length = 0.0f;
		  if(width > height)
		  {
			  length = height;			 
		  }
		  else
		  {
			  length = width;			
		  }
		  
		  float xOffset = (_width - length) / 2.0f;
		  float yOffset = (_height - length) / 2.0f;			  
		  
		  // TODO: Check if rendering numbers in groups are faster...
		  for(int x = 0; x < 9; x++) {
			  for(int y = 0; y < 9; y++) {
				  int value = _sudukoArray[y*9 + x];
				  
				  float xPosition = xOffset + (float)x / 9.0f * length;
				  float yPosition = yOffset + length - ((float)y / 9.0f * length);
				  
				  _numericSprite.setValue(value);				  
				  float numWidth = _numericSprite.width();
				  
				  _numericSprite.draw(gl, 
						  xPosition + (numWidth / 2.0f), 
						  yPosition, 
						  _width, _height);
			  }
		  }
	  }

    private void drawMsPF(GL10 gl, float rightMargin) {
        long time = SystemClock.uptimeMillis();
        if (_startTime == 0) {
            _startTime = time;
        }
        if (frames++ == SAMPLE_PERIOD_FRAMES) {
            frames = 0;
            long delta = time - _startTime;
            _startTime = time;
            _msPerFrame = (int) (delta * SAMPLE_FACTOR);
        }
        if (_msPerFrame > 0) {
            _numericSprite.setValue(_msPerFrame);
            float numWidth = _numericSprite.width();
            float x = rightMargin - numWidth - 10;
            _numericSprite.draw(gl, x, 0, _width, _height);
        }
    }

    @Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
        _width = width;
        _height = height;
        gl.glViewport(0, 0, width, height);
        _projector.setCurrentView(0, 0, width, height);

        /*
        * Set our projection matrix. This doesn't have to be done
        * each time we draw, but usually a new projection needs to
        * be set when the viewport is resized.
        */
        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
        _projector.getCurrentProjection(gl);
    }
}