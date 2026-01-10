package es.deusto.sd.ecoembes.client.util;

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
}