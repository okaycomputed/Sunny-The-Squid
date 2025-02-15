import javax.swing.*;

public class Actions {
    private void updateStatusBar(JLabel statusBar) {

    }

    public void sleep(JLabel sunnySprite, int buttonState) {
        if(buttonState == GamePanel.BUTTON_OFF) {
            Icon sunnySleeping = new ImageIcon("src/assets/sunny-sleeping.GIF");
            sunnySprite.setIcon(sunnySleeping);
        }

        else {
            Icon sunnyIdle = new ImageIcon("src/assets/sunny.GIF");
            sunnySprite.setIcon(sunnyIdle);
        }

    }

    public void eat() {

    }

    public void play() {

    }

    public void bathe() {

    }
}
