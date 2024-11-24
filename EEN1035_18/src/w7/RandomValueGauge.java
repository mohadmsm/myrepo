package w7;


import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class RandomValueGauge {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RandomValueGauge::new);
    }

    public RandomValueGauge() {
        JFrame frame = new JFrame("Random Value Gauge");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        GaugeCanvas canvas = new GaugeCanvas();
        frame.add(canvas, BorderLayout.CENTER);

        // Timer to generate random values and update the gauge
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    int randomValue = new Random().nextInt(101); // Random value between 0 and 100
                    canvas.setValue(randomValue);
                });
            }
        }, 0, 1000); // Update every 1 second

        frame.setVisible(true);
    }

    static class GaugeCanvas extends JPanel {
        private int value = 0;

        public void setValue(int value) {
            this.value = value;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            int width = getWidth();
            int height = getHeight();
            int radius = Math.min(width, height) / 3;
            int centerX = width / 2;
            int centerY = height / 2;

            // Draw the outer circle
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);

            // Draw the gauge background
            g2d.setColor(Color.WHITE);
            g2d.fillOval(centerX - radius + 10, centerY - radius + 10, 2 * radius - 20, 2 * radius - 20);

            // Draw the gauge arc
            int angle = (int) (value * 2.7); // Map value (0-100) to angle (0-270)
            g2d.setColor(Color.GREEN);
            g2d.fillArc(centerX - radius + 10, centerY - radius + 10, 2 * radius - 20, 2 * radius - 20, 135, -angle);

            // Draw the value text
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.drawString("Value: " + value, centerX - 40, centerY + radius + 20);
        }
    }
}
