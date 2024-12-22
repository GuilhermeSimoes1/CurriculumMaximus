//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     Biosystems & Integrative Sciences Institute                         ::
//::     Faculty of Sciences University of Lisboa                            ::
//::     http://www.fc.ul.pt/en/unidade/bioisi                               ::
//::                                                                         ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2016   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

/**
 *
 * @author antoniomanso
 */
public class ImageUtils {
    /**
     * converte uma BufferedImage para um array de bytes
     * @param image imagem
     * @return array de bytes da imagem
     * @throws IOException 
     */
    public static byte[] imageToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        baos.flush();
        return  baos.toByteArray();
    }
    /**
     * converte um array de bytes para uma imagem
     * @param data dados da imagem
     * @return imagem
     * @throws IOException 
     */
    public static BufferedImage byteArrayToImage(byte[] data) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(data));
    }

     public static void saveImage(BufferedImage image, String fileName) throws IOException {
       ImageIO.write(image, "jpg", new File(fileName));
    }
    public static void saveImage(byte[] data, String fileName) throws IOException {
        Files.write(Paths.get(fileName), data);
    }
    
    public static BufferedImage loadImage(String fileName) throws IOException {
        return ImageIO.read(new File(fileName));
    }
    
      /**
     * redimensiona uma imagem
     *
     * @param srcImg imagem original
     * @param w largura
     * @param h altura
     * @return imagem redimensioanda
     */
    public static Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }


}
