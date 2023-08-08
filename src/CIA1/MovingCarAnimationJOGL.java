import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class MovingCarAnimationJOGL implements GLEventListener {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int CAR_WIDTH = 210;
    private static final int CAR_HEIGHT = 30;
    private static final int WHEEL_RADIUS = 15;
    private static final int FPS_CAP = 60;

    private int x = 0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MovingCarAnimationJOGL::createAndShowFrame);
    }

    // Method to create and show the JFrame
    private static void createAndShowFrame() {
        JFrame frame = new JFrame("Moving Car Animation with JOGL");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        GLCanvas canvas = new GLCanvas();
        MovingCarAnimationJOGL movingCar = new MovingCarAnimationJOGL();
        canvas.addGLEventListener(movingCar);
        frame.add(canvas);
        frame.setVisible(true);

        movingCar.startAnimation(canvas);
    }

    // Method to start the animation
    private void startAnimation(GLCanvas canvas) {
        FPSAnimator animator = new FPSAnimator(canvas, FPS_CAP);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // Set up orthogonal projection and clear color
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, WINDOW_WIDTH, WINDOW_HEIGHT, 0, -1, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // Clear the screen
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        // Draw the moving car
        drawMovingCar(gl);

        // Update the car's position
        x += 10;
        if (x >= WINDOW_WIDTH)
            x = 0;
    }

    // Method to draw the moving car
    private void drawMovingCar(GL2 gl) {
        int y = WINDOW_HEIGHT / 2; // Y-coordinate of the car

        // Set color of car as red
        gl.glColor3f(1.0f, 0.0f, 0.0f);

        // Draw lines for the bonnet and body of car
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex2i(x, y);
        gl.glVertex2i(x + CAR_WIDTH, y);

        gl.glVertex2i(x + 50, y);
        gl.glVertex2i(x + 75, y - CAR_HEIGHT);

        gl.glVertex2i(x + 75, y - CAR_HEIGHT);
        gl.glVertex2i(x + 150, y - CAR_HEIGHT);

        gl.glVertex2i(x + 150, y - CAR_HEIGHT);
        gl.glVertex2i(x + 165, y);

        gl.glVertex2i(x, y);
        gl.glVertex2i(x, y + CAR_HEIGHT);

        gl.glVertex2i(x + CAR_WIDTH, y);
        gl.glVertex2i(x + CAR_WIDTH, y + CAR_HEIGHT);
        gl.glEnd();

        // Draw left wheel of car
        drawCircle(gl, x + 65, y + CAR_HEIGHT);

        // Draw right wheel of car
        drawCircle(gl, x + 145, y + CAR_HEIGHT);
    }

    // Method to draw a circle at the given position
    private void drawCircle(GL2 gl, int centerX, int centerY) {
        int numSegments = 50;
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glVertex2f(centerX, centerY);
        for (int i = 0; i <= numSegments; i++) {
            double angle = 2.0 * Math.PI * i / numSegments;
            double x = centerX + MovingCarAnimationJOGL.WHEEL_RADIUS * Math.cos(angle);
            double y = centerY + MovingCarAnimationJOGL.WHEEL_RADIUS * Math.sin(angle);
            gl.glVertex2d(x, y);
        }
        gl.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        // Empty, not used in this example
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // Empty, not used in this example
    }
}
