/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.rdf;

import java.io.BufferedReader;
import java.io.Reader;

/**
 *
 * @author ed
 */
public class NQuadReader extends NTripleReader {
    /**
     *
     * @param h
     * @param reader
     */
    public void read(RDFHandler h, Reader reader)
         {

        if (!(reader instanceof BufferedReader)) {
            reader = new BufferedReader(reader);
        }

        this.handler = h;

        in = new IStream(reader);
        readRDF();
    }

    /**
     *
     */
    @Override
    protected void readRDF() {
        Resource subject;
        Resource predicate;
        Node object;
        Resource source;

        while (!in.eof()) {
            while (!in.eof()) {
                inErr = false;

                skipWhiteSpace();
                if (in.eof()) {
                    return;
                }

                subject = readResource();
                if (inErr)
                    break;

                skipWhiteSpace();
                predicate = readResource();
                if (inErr)
                    break;

                skipWhiteSpace();
                object = readNode();
                if (inErr)
                    break;

                skipWhiteSpace();
                source = readResource();
                if (inErr)
                    break;

                skipWhiteSpace();
                if (badEOF())
                    break;

                if (!expect("."))
                    break;

                try {
                    handler.setBase(source.getURI());
                    handler.processTriple(new Triple(subject, predicate, object));
                } catch (Exception e2) {

                }
            }
            if (inErr) {
                errCount++;
                while (!in.eof() && in.readChar() != '\n') {
                }
            }
        }
    }
}
