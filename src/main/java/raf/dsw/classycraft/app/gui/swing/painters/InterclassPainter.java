package raf.dsw.classycraft.app.gui.swing.painters;

import raf.dsw.classycraft.app.model.modelImpl.DiagramElement;
import raf.dsw.classycraft.app.model.modelImpl.classes.Enum;
import raf.dsw.classycraft.app.model.modelImpl.classes.Interclass;
import raf.dsw.classycraft.app.model.modelImpl.classes.Interfejs;
import raf.dsw.classycraft.app.model.modelImpl.classes.Klasa;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

public abstract class InterclassPainter extends ElementPainter{
    public InterclassPainter(DiagramElement element) {
        super(element);
    }

    @Override
    public void draw(Graphics g) {

        Interclass interclass = (Interclass) super.getElement();

        char type;
        if (interclass instanceof Klasa)
            type = 'C';
        else if (interclass instanceof Enum) {
            type = 'E';
        } else if (interclass instanceof Interfejs) {
            type = 'I';
        } else return;


        List<String> contents = interclass.getContentStrings();


        Graphics2D g2D = (Graphics2D)g;
        FontMetrics fontMetrics = g2D.getFontMetrics();
        g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        double startX = interclass.getX();
        double startY = interclass.getY();

        // ukupna duzina
        int totalHeight = (contents.size() + 2) * (fontMetrics.getHeight());

        // w i h okvira
        int width;
        if (contents.isEmpty())
            width=fontMetrics.stringWidth(interclass.getName()) + 20;
        else
            width=calculateMaxWidth(fontMetrics, contents) + 20;

        ((Interclass) super.getElement()).setWidth(width);

        int height = totalHeight + 10;
        ((Interclass) super.getElement()).setHeight(height);

        //crtanje
        this.setShape(new Rectangle2D.Double(startX, startY-10, width, height));
        g2D.setStroke(new BasicStroke(interclass.getStrokeWidth()));
        g2D.setColor(interclass.getColourInside());
        g2D.fill(getShape());
        g2D.setColor(interclass.getColourOutline());
        g2D.draw(getShape());

        drawStringCentered(g2D, "[" + type + "]", startX, startY + 2, width,fontMetrics);
        drawStringCentered(g2D, interclass.getName(), startX, startY+ fontMetrics.getHeight(), width,fontMetrics);

        g2D.drawLine((int) startX, (int) (startY+5+fontMetrics.getHeight()), (int) (startX+width), (int) (startY+5+fontMetrics.getHeight()));

        // metode i atributi
        for (int i = 0; i < contents.size(); i++) {
            drawStringCentered(g2D, contents.get(i), startX, startY + 2 + (fontMetrics.getHeight()) * (i+2), width, fontMetrics);
        }

    }





    @Override
    public boolean elementAt(int x, int y) {
        return getShape().contains(x, y);
    }

    public void drawStringCentered(Graphics2D g2d, String text, double x, double y, int width, FontMetrics fontMetrics) {
        g2d.drawString(text, (float) x + (width-fontMetrics.stringWidth(text)) / 2, (float) y);
    }


    public int calculateMaxWidth(FontMetrics fontMetrics, List<String> contents) {
        int maxWidth = 0;
        for (String s : contents) {
            int stringWidth = fontMetrics.stringWidth(s);
            if (stringWidth > maxWidth) {
                maxWidth = stringWidth;
            }
        }
        return maxWidth;
    }
}
