//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2022   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package Interfaces;




import com.mycompany.curriculumdigital.MerkleTree;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

/**
 *
 * @author IPT
 */
public class MerkleGraphics extends javax.swing.JPanel {

    MerkleTree tree;

    
    Object element;
    List<String> proof;

    public boolean containsProof(String data) {
        if (proof == null) {
            return false;
        }
        return proof.contains(data);
    }

    /**
     * Creates new form MerklePanel
     */
    public MerkleGraphics() {
        initComponents();
    }

    public void setMerkle(MerkleTree tree) {
        this.tree = tree;
        repaint();
    }

    public void setProof(Object element, List<String> proof) {
        this.element = element;
        this.proof = proof;
        repaint();
    }
    



    @Override
    public void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        if (tree != null) {
            drawLines(gr);
            //elements + Tree
            int height = tree.getHashTree().size() + 1;
            int sizeY = this.getHeight() / (height + 1);

            List<List<String>> hashTree = tree.getHashTree();
            for (int i = 0; i < hashTree.size(); i++) {

                int blocks = (int) Math.pow(2, i);
                int size = this.getWidth() / blocks;

                for (int j = 0; j < hashTree.get(i).size(); j++) {
                    Color back = new Color(250, 200, 200);
                    if (containsProof(hashTree.get(i).get(j))) {
                        back = new Color(200, 255, 200);
                    }

                    drawCenteredString(gr, hashTree.get(i).get(j),
                            new Rectangle(j * size, sizeY * i, size, sizeY),
                            Color.BLACK, back);
                }
            }
            int blocks = (int) Math.pow(2, hashTree.size() - 1);
            int size = this.getWidth() / blocks;
            for (int j = 0; j < tree.getElements().size(); j++) {
                Color back = new Color(200, 200, 255);
                if (tree.getElements().get(j).equals(element)) {
                    back = new Color(200, 255, 200);
                }

                drawCenteredString(gr, tree.getElements().get(j).toString(),
                        new Rectangle(j * size, sizeY * hashTree.size(), size, sizeY),
                        Color.BLACK, back);
            }

        }
        gr.setColor(Color.WHITE);
        gr.drawString("(c) M@ns0 2022", this.getWidth()-120, this.getHeight()-10);
        gr.setColor(Color.DARK_GRAY);
        gr.drawString("(c) M@ns0 2022", this.getWidth()-122, this.getHeight()-9);

    }

    public void drawLines(Graphics gr) {
        gr.setColor(Color.BLACK);
       List<List<String>> hashTree = tree.getHashTree();
        for (int y = 1; y < hashTree.size(); y++) {
            for (int x = 0; x < hashTree.get(y).size(); x++) {
                Point p1 = getCenter(y - 1, x / 2);
                Point p2 = getCenter(y, x);
                gr.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
        int height = tree.getHashTree().size() + 1;
        int sizeY = this.getHeight() / (height + 1);
        for (int x = 0; x < tree.getElements().size(); x++) {
            Point p1 = getCenter(hashTree.size() - 1, x);
            gr.drawLine(p1.x, p1.y, p1.x, p1.y + sizeY);
        }

    }

    private Point getCenter(int y, int x) {
        int height = tree.getHashTree().size() + 1;
        int sizeY = this.getHeight() / (height + 1);
        int blocks = (int) Math.pow(2, y);
        int size = this.getWidth() / blocks;
        return new Point(x * size + size / 2, sizeY * y + sizeY / 2);
    }

    /**
     * Draw a String centered in the middle of a Rectangle.
     *
     * @param g The Graphics instance.
     * @param text The String to draw.
     * @param rect The Rectangle to centerString the text in.
     * @param lineColor
     * @param backColor
     */
    public void drawCenteredString(Graphics g, String text, Rectangle rect, Color lineColor, Color backColor) {

        Font font = new Font("Courier New", Font.BOLD, 14);
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        g.setFont(font);
        //draw rectangle
        int step = 2;
        g.setColor(backColor);
        g.fillRect(x - step, y - metrics.getHeight(), metrics.stringWidth(text) + 2 * step, metrics.getHeight() * 2);
        g.setColor(lineColor);
        g.drawRect(x - step, y - metrics.getHeight(), metrics.stringWidth(text) + 2 * step, metrics.getHeight() * 2);

        // Draw the String
        g.setColor(Color.BLACK);
        g.drawString(text, x, y);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
