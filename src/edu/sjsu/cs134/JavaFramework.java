/**
 * Created by jared on 2/9/16.
 */
package edu.sjsu.cs134;

import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.opengl.*;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;

import java.io.*;
import java.nio.ByteBuffer;

public class JavaFramework {
    // Set this to true to force the game to exit.
    private static boolean shouldExit;

    // The previous frame's keyboard state.
    private static boolean kbPrevState[] = new boolean[256];

    // The current frame's keyboard state.
    private static boolean kbState[] = new boolean[256];

    // Position of the sprite.
    private static int[] spritePos = new int[] { 10, 10 };

    // Texture for the sprite.
    private static int spriteTex;

    // Size of the sprite.
    private static int[] spriteSize = new int[2];
    
    // Position of the background.
    private static int[] backgroundPos =new int[] { 640, 480 };
    
    // Texture for the background.
    private static int backgroundTex;
    
    // Size of the background.
    private static int[] backgroundSize = new int[2];

    public static void main(String[] args) {
        GLProfile gl2Profile;

        try {
            // Make sure we have a recent version of OpenGL
            gl2Profile = GLProfile.get(GLProfile.GL2);
        }
        catch (GLException ex) {
            System.out.println("OpenGL max supported version is too low.");
            System.exit(1);
            return;
        }

        //int[] cam = {spritePos[0],spritePos[1]};
        spritePos[0] = 512 - (spriteSize[0]/2);
        spritePos[1] = 384 - (spriteSize[1]/2);
        int[] cam = {spritePos[0] - 640/2,spritePos[1] - 480/2};
        backgroundPos[0] = 0;
        backgroundPos[1] = 0;
        
        // Create the window and OpenGL context.
        GLWindow window = GLWindow.create(new GLCapabilities(gl2Profile));
        window.setSize(640, 480);
        window.setTitle("CS134 Game Design Demo");
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DISPOSE_ON_CLOSE);
        window.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                kbState[keyEvent.getKeyCode()] = true;
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                kbState[keyEvent.getKeyCode()] = false;
            }
        });

        // Setup OpenGL state.
        window.getContext().makeCurrent();
        GL2 gl = window.getGL().getGL2();
        gl.glViewport(0, 0, 640, 480);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glOrtho(0, 640, 480, 0, 0, 100);
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

        
        // Load the texture.
        spriteTex = glTexImageTGAFile(gl, "Mega-Man-transparent.tga", spriteSize);
        backgroundTex = glTexImageTGAFile(gl, "gamemap.tga", backgroundSize);
        // The game loop
        while (!shouldExit) {
            System.arraycopy(kbState, 0, kbPrevState, 0, kbState.length);

            // Actually, this runs the entire OS message pump.
            window.display();
            if (!window.isVisible()) {
                shouldExit = true;
                break;
            }

            // Game logic.
            if (kbState[KeyEvent.VK_ESCAPE]) {
                shouldExit = true;
            }

            if (kbState[KeyEvent.VK_LEFT]) {
                spritePos[0] -= 7;
            }

            if (kbState[KeyEvent.VK_RIGHT]) {
                spritePos[0] += 7;
            }

            if (kbState[KeyEvent.VK_UP]) {
                spritePos[1] -= 7;
            }

            if (kbState[KeyEvent.VK_DOWN]) {
                spritePos[1] += 7;
            }
            // Camera logic.
          
            if (kbState[KeyEvent.VK_A]) {
                cam[0] -= 5;
            }

            if (kbState[KeyEvent.VK_D]) {
                cam[0] += 5;
            }

            if (kbState[KeyEvent.VK_W]) {
                cam[1] -= 5;
            }

            if (kbState[KeyEvent.VK_S]) {
                cam[1] += 5;
            }
            
            //Playermid statement
            if(spritePos[0]-640/2 > cam[0] && cam[0] < spritePos[0]- 640/2 - 10){
            	cam[0]+=4;
            }
            else if(spritePos[0]-640/2 < cam[0] && cam[0] > spritePos[0] - 640/2 +10){
            	cam[0]-=4;
            }
            if(spritePos[1]-480/2 > cam[1] && cam[1] < spritePos[1]- 480/2 - 10){
            	cam[1]+=4;
            }
            else if(spritePos[1]-480/2 < cam[1] && cam[1] > spritePos[1] - 480/2 +10){
            	cam[1]-=4;
            }
            //boundary
            if(spritePos[0] <= backgroundPos[0]){
            	spritePos[0] += backgroundPos[0] - spritePos[0];
            }
            if(spritePos[0] + spriteSize[0] >= backgroundPos[0] + 1024){
            	spritePos[0] -= (spritePos[0]+spriteSize[0]) - (backgroundPos[0] + 1024);
            }
            if(spritePos[1] <= backgroundPos[1]){
            	spritePos[1] += backgroundPos[1] - spritePos[1];
            }
            if(spritePos[1] + spriteSize[1] >= backgroundPos[1] + 768){
            	spritePos[1] -= (spritePos[1]+spriteSize[1]) - (backgroundPos[1] + 768);
            }

            gl.glClearColor(0, 0, 0, 1);
            gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
            //Draw background(s
            
            glDrawSprite(gl, backgroundTex, backgroundPos[0] - cam[0], backgroundPos[1] - cam[1], 1024, 768);
            glDrawSprite(gl, spriteTex, spritePos[0]-cam[0], spritePos[1]-cam[1], spriteSize[0], spriteSize[1]);
            //Draw more background(s)
            // Present to the player.
            // Window.swapBuffers();
        }
        System.exit(0);
    }

    private static void glDrawBackground(GL2 gl, int backgroundTex2, int i, int j, int k, int l) {
		// TODO Auto-generated method stub
		return;
	}

	private static int glTexImageJPGFile(GL2 gl, String string, int[] backgroundSize2) {
		// TODO Auto-generated method stub
		return 0;
	}

	// Load a file into an OpenGL texture and return that texture.
    public static int glTexImageTGAFile(GL2 gl, String filename, int[] out_size) {
        final int BPP = 4;

        DataInputStream file = null;
        try {
            // Open the file.
            file = new DataInputStream(new FileInputStream(filename));
        } catch (FileNotFoundException ex) {
            System.err.format("File: %s -- Could not open for reading.", filename);
            return 0;
        }

        try {
            // Skip first two bytes of data we don't need.
            file.skipBytes(2);

            // Read in the image type.  For our purposes the image type
            // should be either a 2 or a 3.
            int imageTypeCode = file.readByte();
            if (imageTypeCode != 2 && imageTypeCode != 3) {
                file.close();
                System.err.format("File: %s -- Unsupported TGA type: %d", filename, imageTypeCode);
                return 0;
            }

            // Skip 9 bytes of data we don't need.
            file.skipBytes(9);

            int imageWidth = Short.reverseBytes(file.readShort());
            int imageHeight = Short.reverseBytes(file.readShort());
            int bitCount = file.readByte();
            file.skipBytes(1);

            // Allocate space for the image data and read it in.
            byte[] bytes = new byte[imageWidth * imageHeight * BPP];

            // Read in data.
            if (bitCount == 32) {
                for (int it = 0; it < imageWidth * imageHeight; ++it) {
                    bytes[it * BPP + 0] = file.readByte();
                    bytes[it * BPP + 1] = file.readByte();
                    bytes[it * BPP + 2] = file.readByte();
                    bytes[it * BPP + 3] = file.readByte();
                }
            } else {
                for (int it = 0; it < imageWidth * imageHeight; ++it) {
                    bytes[it * BPP + 0] = file.readByte();
                    bytes[it * BPP + 1] = file.readByte();
                    bytes[it * BPP + 2] = file.readByte();
                    bytes[it * BPP + 3] = -1;
                }
            }

            file.close();

            // Load into OpenGL
            int[] texArray = new int[1];
            gl.glGenTextures(1, texArray, 0);
            int tex = texArray[0];
            gl.glBindTexture(GL2.GL_TEXTURE_2D, tex);
            gl.glTexImage2D(
                    GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, imageWidth, imageHeight, 0,
                    GL2.GL_BGRA, GL2.GL_UNSIGNED_BYTE, ByteBuffer.wrap(bytes));
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

            out_size[0] = imageWidth;
            out_size[1] = imageHeight;
            return tex;
        }
        catch (IOException ex) {
            System.err.format("File: %s -- Unexpected end of file.", filename);
            return 0;
        }
    }

    public static void glDrawSprite(GL2 gl, int tex, int x, int y, int w, int h) {
        gl.glBindTexture(GL2.GL_TEXTURE_2D, tex);
        gl.glBegin(GL2.GL_QUADS);
        {
            gl.glColor3ub((byte)-1, (byte)-1, (byte)-1);
            gl.glTexCoord2f(0, 1);
            gl.glVertex2i(x, y);
            gl.glTexCoord2f(1, 1);
            gl.glVertex2i(x + w, y);
            gl.glTexCoord2f(1, 0);
            gl.glVertex2i(x + w, y + h);
            gl.glTexCoord2f(0, 0);
            gl.glVertex2i(x, y + h);
        }
        gl.glEnd();
    }
}
