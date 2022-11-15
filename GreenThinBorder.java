import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

public class GreenThinBorder implements Border{
    //既存の線では太すぎたので上と左のみの太さ１の緑線を作成
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Insets insets = getBorderInsets(c);
      
        g.setColor(Color.green);
        g.fillRect(x, y, insets.left, height);
        g.fillRect(x, y, width, insets.top);
        g.fillRect(x + width - insets.right, y, insets.right, height);
        g.fillRect(x, y + height - insets.bottom, width, insets.bottom);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(1, 1, 0, 0);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
    
}
