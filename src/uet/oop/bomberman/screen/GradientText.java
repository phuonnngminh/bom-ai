package uet.oop.bomberman.screen;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

public class GradientText {
    private Font font;
    private Color color1;
    private Color color2;
    private Color color3;

    public GradientText(Font font, Color color1, Color color2, Color color3) {
        this.font = font;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
    }

    public void draw(Graphics2D g2d, String text, int x, int y) {
        FontRenderContext frc = g2d.getFontRenderContext();
        GlyphVector gv = font.createGlyphVector(frc, text);
        Shape outline = gv.getOutline(x, y);

        Rectangle bounds = outline.getBounds();
        GradientPaint gradientPaint1 = new GradientPaint(
                bounds.x, bounds.y, color1,
                bounds.x + bounds.width / 2f, bounds.y, color2);
        GradientPaint gradientPaint2 = new GradientPaint(
                bounds.x + bounds.width / 2f, bounds.y, color2,
                bounds.x + bounds.width, bounds.y, color3);

        // Draw first half of the text
        g2d.setPaint(gradientPaint1);
        g2d.fill(outline);

        // Draw second half of the text
        g2d.setPaint(gradientPaint2);
        g2d.fill(outline);
    }
}
