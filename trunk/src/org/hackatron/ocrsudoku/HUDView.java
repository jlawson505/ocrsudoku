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
import org.hackatron.ocrsudoku.graphics.NumericSprite;
import org.hackatron.ocrsudoku.graphics.Projector;
import org.hackatron.ocrsudoku.graphics.Quad;
import org.hackatron.ocrsudoku.graphics.Triangle;

class HUDView implements GLSurfaceView.Renderer{
	
	private int _width;
    private int _height;
    private Context _context;
    private Quad _quad;
    private Grid _grid;
    private int _textureID;
    private int frames;
    private int _msPerFrame;
    private final static int SAMPLE_PERIOD_FRAMES = 12;
    private final static float SAMPLE_FACTOR = 1.0f / SAMPLE_PERIOD_FRAMES;
    private long _startTime;
    private LabelMaker _labelMaker;
    private Paint _labelPaint;
    private int _labelA;
    private int _labelB;
    private int _labelC;
    private int _labelMsPF;
    private Projector _projector;
    private NumericSprite _numericSprite;
    private float[] mScratch = new float[8];
	    
    public HUDView(Context context) {
        _context = context;
        _quad = new Quad();
        _grid = new Grid(64,64);
        _projector = new Projector();
       
        _labelPaint = new Paint();
        _labelPaint.setTextSize(32);
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

        gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_TEXTURE_2D);

        /*
         * Create our texture. This has to be done each time the
         * surface is created.
         */

        int[] textures = new int[1];
        gl.glGenTextures(1, textures, 0);

        _textureID = textures[0];
        gl.glBindTexture(GL10.GL_TEXTURE_2D, _textureID);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);

        InputStream is = _context.getResources().openRawResource(R.drawable.character3);
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch(IOException e) {
                // Ignore.
            }
        }

        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

        if (_labelMaker != null) {
            _labelMaker.shutdown(gl);
        } else {
            _labelMaker = new LabelMaker(true, 256, 64);
        }
        _labelMaker.initialize(gl);
        _labelMaker.beginAdding(gl);
        _labelA = _labelMaker.add(gl, "A", _labelPaint);
        _labelB = _labelMaker.add(gl, "B", _labelPaint);
        _labelC = _labelMaker.add(gl, "C", _labelPaint);
        _labelMsPF = _labelMaker.add(gl, "ms/f", _labelPaint);
        _labelMaker.endAdding(gl);

        if (_numericSprite != null) {
            _numericSprite.shutdown(gl);
        } else {
            _numericSprite = new NumericSprite();
        }
        _numericSprite.initialize(gl, _labelPaint);
    }

	@Override
	public void onDrawFrame(GL10 gl) {
        /*
         * By default, OpenGL enables features that improve quality
         * but reduce performance. One might want to tweak that
         * especially on software renderer.
         */
        gl.glDisable(GL10.GL_DITHER);
        //gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);

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

        GLU.gluLookAt(gl, 0.0f, 0.0f, -5.0f,
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        
        gl.glDisable(GL10.GL_TEXTURE_2D);

        gl.glActiveTexture(GL10.GL_TEXTURE0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, _textureID);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.090f * ((int) time);

        gl.glRotatef(angle, 0.0f, 1.0f, 0.0f);
        float quadWidth = 1.5f;
        
        gl.glScalef(0.5f, 0.5f, 0.5f);
        gl.glTranslatef(-quadWidth * 4,-quadWidth * 4, 0.0f);
        
        for(int x=0; x<9; x++)
        {
        	gl.glTranslatef(quadWidth,0.0f, 0.0f);
        	for(int y=0; y<9; y++)
            {
        		gl.glTranslatef(0.0f,quadWidth, 0.0f);
        		_quad.draw(gl);
            }
        	gl.glTranslatef(0.0f,-quadWidth*9, 0.0f);
        }

        

//        _projector.getCurrentModelView(gl);
//        _labelMaker.beginDrawing(gl, _width, _height);
//        drawLabel(gl, 0, _labelA);
//        drawLabel(gl, 1, _labelB);
//        drawLabel(gl, 2, _labelC);
//        float msPFX = _width - _labelMaker.getWidth(_labelMsPF) - 1;
//        _labelMaker.draw(gl, msPFX, 0, _labelMsPF);
//        _labelMaker.endDrawing(gl);

//        drawMsPF(gl, msPFX);
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
            float x = rightMargin - numWidth;
            _numericSprite.draw(gl, x, 0, _width, _height);
        }
    }

	private void drawLabel(GL10 gl, int triangleVertex, int labelId) {
        float x = _quad.getX(triangleVertex);
        float y = _quad.getY(triangleVertex);
        
        mScratch[0] = x;
        mScratch[1] = y;
        mScratch[2] = 0.0f;
        mScratch[3] = 1.0f;
      
        _projector.project(mScratch, 0, mScratch, 4);
        
        float sx = mScratch[4];
        float sy = mScratch[5];
        float height = _labelMaker.getHeight(labelId);
        float width = _labelMaker.getWidth(labelId);
        float tx = sx - width * 0.5f;
        float ty = sy - height * 0.5f;
       
        _labelMaker.draw(gl, tx, ty, labelId);
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