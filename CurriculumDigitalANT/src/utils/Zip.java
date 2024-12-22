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
//::                                                               (c)2020   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author doctmanso
 */
public class Zip {

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::        ZIP /  UNZIP                                :::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * Comprime um array de bytes utilizando o algoritmo GZIP
     *
     * @param data dados originais
     * @return dados comprimidos
     * @throws IOException
     */
    public static byte[] compress(byte[] data) throws IOException {
        //array de bytes em memória
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        // adaptador GZIP para comprimir bytes
        GZIPOutputStream zout = new GZIPOutputStream(bout);
        //escrever os dados no GZIP
        zout.write(data, 0, data.length);
        //terminar a escrita de dados
        zout.finish();
        //devolver os dados comprimidos
        return bout.toByteArray();
    }

    /**
     * Expande um array de dados comprimidos pelo algoritmo GZIP
     *
     * @param data dados comprimidos
     * @return dados originais
     * @throws IOException
     */
    public static byte[] expand(byte[] data) throws IOException {
        //Stream com Array de bytes em memória
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        //Adaptador GZIP para descomprimir a stream
        GZIPInputStream zin = new GZIPInputStream(bin);
        //Array de bytes expandidos
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        //buffer de leitura
        byte[] buffer = new byte[1024];
        int len = 0;
        //ler os dados da stream GZIP
        while ((len = zin.read(buffer)) > 0) {
            //escrever os dados na Stream expandida
            bout.write(buffer, 0, len);
        }
        //retornar os bytes originais
        return bout.toByteArray();
    }

}
