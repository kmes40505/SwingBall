package com.leochen.swingball;

import com.leochen.swingball.units.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

import android.util.Log;

public class DrawObj {
    private final static String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            // The matrix must be included as a modifier of gl_Position.
            // Note that the uMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}";

    private final static String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

	private static int mProgram;

	//memory space only for one triangle
	final static ByteBuffer bb = ByteBuffer.allocateDirect(36);//3 axis * 3 vertices * 4
	//2 bytes per short
	final static ByteBuffer dlbb = ByteBuffer.allocateDirect(18);//3 axis * 3 vertices * 2

	final static int circVtxNum = 30;

	//2 is for x and y. z is always 0
	final static float circCoord[] = new float[circVtxNum * 2];

	public static void init() {
		mProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(mProgram,
			Board.loadShader(
				GLES20.GL_VERTEX_SHADER,
				vertexShaderCode)
			);
		GLES20.glAttachShader(mProgram,
			Board.loadShader(
				GLES20.GL_FRAGMENT_SHADER,
				fragmentShaderCode)
			);
		GLES20.glLinkProgram(mProgram);

		//init circ coor templates for performance use
		double rdis = Math.PI * 2 / circVtxNum;
		for (int count = 0; count < circVtxNum; count++) {
			circCoord[count * 2] = (float) Math.cos(rdis * count);
			circCoord[count * 2 + 1] = (float) Math.sin(rdis * count);
		}

	}

	public static void circle(int xInt, int yInt, int radInt, float[] mMVPMatrix, float color[]){
		int width = EnvVar.getMainWidth();
		int height = EnvVar.getMainHeight();
		if (yInt + radInt < 0 || yInt - radInt > height || xInt + radInt < 0 || xInt - radInt > width)
			return;

		float r = (float) radInt / height;
		float x = (float) (xInt * 2 - width) / height;
		float y = (float) (yInt * 2 - height) / height;

		for (int idx = 0; idx < circCoord.length; idx += 2) {
			drawTriangle(new float[]{
				x, y, 0,
				x + r * circCoord[idx], y + r * circCoord[idx + 1], 0,
				x + r * circCoord[(idx + 2) % circCoord.length], y + r * circCoord[(idx + 3) % circCoord.length], 0
			}, color, mMVPMatrix);
		}
	}

	public static void circle(Instance target, float[] mMVPMatrix, float color[]) {
		circle(target.getx(), target.gety(), target.getxSize(), mMVPMatrix, color);
	}

	public static void rectangle(Instance target, float[] mMVPMatrix) {
		int width = EnvVar.getMainWidth();
		int height = EnvVar.getMainHeight();
		int x = target.getDrawX();
		int xS = target.getxSize();
		int y = target.getDrawY();
		int yS = target.getySize();
		if (x > width || x + xS < 0 || y > height || y + yS < 0)
			return;

		float lx = (float) (target.getDrawX() * 2 - width) / height;
		float ty = (float) (target.getDrawY() * 2 - height) / height;
		float rx = lx + (float) target.getxSize() * 2 / height;
		float by = ty + (float) target.getySize() * 2 / height;
		float color[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };

		drawTriangle(new float[] {
			lx, ty, 0,
			lx, by, 0,
			rx, by, 0
		}, color, mMVPMatrix);

		drawTriangle(new float[] {
			lx, ty, 0,
			rx, by, 0,
			rx, ty, 0
		}, color, mMVPMatrix);

	}


	private static void drawTriangle(float coords[], float color[], float[] mMVPMatrix){
		if (coords.length != 9) {
			Log.e("DrawObj", "drawTriangle coords only allow length 9");
			System.exit(1);
		}

		FloatBuffer vertexBuffer;
		ShortBuffer drawListBuffer;
		int mPositionHandle;
		int mColorHandle;
		int mMVPMatrixHandle;

		short drawOrder[] = new short[9];
		for (int idx = 0; idx < drawOrder.length; idx++)
			drawOrder[idx] = (short)idx;

		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(coords);
		vertexBuffer.position(0);

		dlbb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlbb.asShortBuffer();
		drawListBuffer.put(drawOrder);
		drawListBuffer.position(0);

		GLES20.glUseProgram(mProgram);
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(
			mPositionHandle,
			3,
			GLES20.GL_FLOAT,
			true,
			//4 bytes per vertex
			3*4,
			vertexBuffer);
		mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
		GLES20.glUniform4fv(mColorHandle, 1, color, 0);
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		Board.checkGlError("glGetUniformLocation");
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
		Board.checkGlError("glUniformMatrix4fv");

		GLES20.glDrawElements(
			GLES20.GL_TRIANGLES,
			drawOrder.length,
			GLES20.GL_UNSIGNED_SHORT,
			drawListBuffer);

		GLES20.glDisableVertexAttribArray(mPositionHandle);

	}

}