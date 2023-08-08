package CIA1;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.*;

public class MovingCarAnimationJOGL implements GLEventListener {
    // Constants for the window and car dimensions
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int CAR_WIDTH = 210;
    private static final int CAR_HEIGHT = 30;
    private static final int WHEEL_RADIUS = 15;
    private static final int FPS_CAP = 60;

    private int x = WINDOW_WIDTH; // Car position on the x-axis

    // Enum for defining colors used in the animation
    private enum Color {
        BODY(1.0f, 0.4f, 0.4f),
        WINDOWS(0.753f, 0.753f, 0.753f),
        TIRES(0.0f, 0.0f, 0.0f),
        HEADLIGHTS(1.0f, 1.0f, 0.0f);

        final float r;
        final float g;
        final float b;

        Color(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }

    public static void main(String[] args) {
        // Create the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(MovingCarAnimationJOGL::createAndShowFrame);
    }

    private static void createAndShowFrame() {
        // Create and set up the main window
        JFrame frame = new JFrame("Moving Car Animation with JOGL");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Create an OpenGL canvas and add the animation listener
        GLCanvas canvas = new GLCanvas();
        MovingCarAnimationJOGL movingCar = new MovingCarAnimationJOGL();
        canvas.addGLEventListener(movingCar);
        frame.add(canvas);
        frame.setVisible(true);

        // Create a timer to control the animation frame rate
        Timer timer = new Timer(1000 / FPS_CAP, e -> canvas.display());
        timer.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        // Initialization method called when the OpenGL context is created
        GL2 gl = drawable.getGL().getGL2();

        // Set up the 2D orthogonal projection
        setupOrthogonalProjection(gl);

        // Set the clear color to white (background color)
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void setupOrthogonalProjection(GL2 gl) {
        // Helper method to set up the orthogonal projection
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, WINDOW_WIDTH, WINDOW_HEIGHT, 0, -1, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        // Method called for each frame to draw the animation
        GL2 gl = drawable.getGL().getGL2();

        // Clear the screen
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        // Draw the moving car
        drawMovingCar(gl);

        // Update the car's position for the next frame
        updateCarPosition();
    }

    private void updateCarPosition() {
        // Helper method to update the car's position
        x -= 2;
        if (x <= -CAR_WIDTH)
            x = WINDOW_WIDTH;
    }

    private void drawMovingCar(GL2 gl) {
        // Helper method to draw the car
        int y = WINDOW_HEIGHT / 2;

        // Draw the main body of the car
        drawCarPart(gl, x, y, CAR_WIDTH, CAR_HEIGHT, Color.BODY);

        // Draw the windows of the car
        gl.glColor3f(Color.WINDOWS.r, Color.WINDOWS.g, Color.WINDOWS.b);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2i(x + 50, y);
        gl.glVertex2i(x + 75, y - CAR_HEIGHT);
        gl.glVertex2i(x + 150, y - CAR_HEIGHT);
        gl.glVertex2i(x + 165, y);
        gl.glEnd();

        // Draw the tires of the car
        drawCircle(gl, x + 55, y + CAR_HEIGHT);
        drawCircle(gl, x + 155, y + CAR_HEIGHT);

        // Draw the headlights of the car
        drawCarPart(gl, x + CAR_WIDTH - 10, y, 10, 10, Color.HEADLIGHTS);
        drawCarPart(gl, x, y, 10, 10, Color.HEADLIGHTS);
    }

    private void drawCarPart(GL2 gl, int x, int y, int width, int height, Color color) {
        // Helper method to draw a rectangular part of the car with the specified color
        gl.glColor3f(color.r, color.g, color.b);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2i(x, y);
        gl.glVertex2i(x + width, y);
        gl.glVertex2i(x + width, y + height);
        gl.glVertex2i(x, y + height);
        gl.glEnd();
    }

    private void drawCircle(GL2 gl, int centerX, int centerY) {
        // Helper method to draw a circle (tire) using OpenGL primitives
        int numSegments = 50;
        gl.glColor3f(Color.TIRES.r, Color.TIRES.g, Color.TIRES.b);
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glVertex2f(centerX, centerY);
        for (int i = 0; i <= numSegments; i++) {
            double angle = 2.0 * Math.PI * i / numSegments;
            double x = centerX + WHEEL_RADIUS * Math.cos(angle);
            double y = centerY + WHEEL_RADIUS * Math.sin(angle);
            gl.glVertex2d(x, y);
        }
        gl.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        // Not used in this class
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // Not used in this class
    }
}
