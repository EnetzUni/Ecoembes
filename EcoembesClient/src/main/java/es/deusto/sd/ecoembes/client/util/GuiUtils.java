package es.deusto.sd.ecoembes.client.util;

import java.awt.Color;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public final class GuiUtils {
    public static void switchFrames(JFrame currentFrame, JFrame nextFrame) {
        // 1. Open the new frame
        if (nextFrame != null) {
            nextFrame.setVisible(true);
        }

        // 2. Close the old frame
        if (currentFrame != null) {
            currentFrame.dispose();
        }
    }

    public static JFrame setupFrame() {
        JFrame frame = new JFrame("Ecoembes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- 1. ICON SETUP ---
        URL iconURL = GuiUtils.class.getResource("/EcoembesLogo1080x1080.png");
        if (iconURL != null) {
            frame.setIconImage(new ImageIcon(iconURL).getImage());
        } else {
            ImageIcon fileIcon = new ImageIcon("EcoembesClient/src/resources/images/EcoembesLogo1080x1080.png");
            frame.setIconImage(fileIcon.getImage());
        }

        // --- WINDOW SETTINGS ---
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.WHITE);

        return frame;
    }
}