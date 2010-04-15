/*
 * This file is part of TrOWL.
 *
 * TrOWL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TrOWL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with TrOWL.  If not, see <http://www.gnu.org/licenses/>. 
 *
 * Copyright 2010 University of Aberdeen
 */

package eu.trowl.rdf;

import eu.trowl.TrOWLException;

import java.net.URL;
import java.io.*;
import java.net.URI;

/** N-Triple Reader
 *
 * @author  Brian McBride, Jeremy Carroll, Dave Banks
 * @version  Release=$Name:  $ Date=$Date: 2009/04/24 12:52:47 $
 */
public class NTripleReader {
    /**
     *
     */
    protected IStream in = null;
    /**
     *
     */
    protected boolean inErr = false;
    /**
     *
     */
    protected int errCount = 0;
    /**
     *
     */
    protected static final int sbLength = 200;

    /**
     *
     */
    protected URI base;
    /**
     *
     */
    protected RDFHandler handler;

    /**
     *
     */
    public NTripleReader() {
    }

    /**
     *
     * @param h
     * @param in
     * @param base
     */
    public void read(RDFHandler h, InputStream in, URI base)
         {
        // N-Triples must be in ASCII, we permit UTF-8.
        read(h, new InputStreamReader(in), base);
    }
    
    /**
     *
     * @param h
     * @param reader
     * @param base
     */
    public void read(RDFHandler h, Reader reader, URI base)
         {

        if (!(reader instanceof BufferedReader)) {
            reader = new BufferedReader(reader);
        }

        this.handler = h;
        this.base = base;
        in = new IStream(reader);
        readRDF();
    }

    /**
     *
     * @param h
     * @param url
     */
    public void read(RDFHandler h, URI uri)  {
        try {
            read(
                h,
                new InputStreamReader(uri.toURL().openStream()),
                uri);
        } catch (Exception e) {
        }
    }

    /**
     *
     */
    protected void readRDF() {
        Resource subject;
        Resource predicate;
        Node object;

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
                if (badEOF())
                    break;

                if (!expect("."))
                    break;

                try {
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

    /**
     *
     * @return
     */
    public Resource readResource()  {
        char inChar = in.readChar();
        if (badEOF())
            return null;

        if (inChar == '_') { // anon resource
            if (!expect(":"))
                return null;
            String name = readName();
            if (name == null) {
                syntaxError("expected bNode label");
                return null;
            }
            return Resource.fromBNode(name);
        } else if (inChar == '<') { // uri
            String uri = readURI();
            if (uri == null) {
                inErr = true;
                return null;
            }
            inChar = in.readChar();
            if (inChar != '>') {
                syntaxError("expected '>'");
                return null;
            }
            return Resource.fromURI(uri);
        } else {
            syntaxError("unexpected input in Resource: " + String.valueOf(inChar));
            return null;
        }
    }

    /**
     *
     * @return
     */
    public Node readNode()  {
        skipWhiteSpace();
        Node n;
        char nextc = in.nextChar();
        switch (nextc) {
            case '"' :
                return readLiteral(false);
            case 'x' :
                return readLiteral(true);
            case '<' :
            case '_' :
                return readResource();
            default :
                syntaxError("unexpected input in Node: " + String.valueOf(nextc));
                return null;
        }
    }

    /**
     *
     * @param err
     */
    protected void syntaxError(String err) {
        System.err.println(err);
//        System.err.println("current line: " + in.buf.toString());
        inErr = true;
    }

    /**
     *
     * @param wellFormed
     * @return
     */
    protected Literal readLiteral(boolean wellFormed)  {

        StringBuffer lit = new StringBuffer(sbLength);

       /* if (wellFormed) {
            deprecated("Use ^^rdf:XMLLiteral not xml\"literals\", .");

            if (!expect("xml"))
                return null;
        }*/

        if (!expect("\"")) {
            System.err.println("first quote");
            return null;

        }
            

        while (true) {
            char inChar = in.readChar();
            if (badEOF())
                return null;
            if (inChar == '\\') {
                char c = in.readChar();
                if (in.eof()) {
                    inErr = true;
                    return null;
                }
                if (c == 'n') {
                    inChar = '\n';
                } else if (c == 'r') {
                    inChar = '\r';
                } else if (c == 't') {
                    inChar = '\t';
                } else if (c == '\\' || c == '"') {
                    inChar = c;
                } else if (c == 'u') {
                    inChar = readUnicode4Escape();
                    if (inErr)
                        return null;
                } else {
                    syntaxError("illegal escape sequence '" + c + "'");
                    return null;
                }
            } else if (inChar == '"') {
                String lang;
                if ('@' == in.nextChar()) {
                    expect("@");
                   lang = readLang();
                } else if ('-' == in.nextChar()) {
                    expect("-");
                    deprecated("Language tags should be introduced with @ not -.");
                    lang = readLang();
                } else {
                    lang = "";
                }
                if (wellFormed) {
                    Literal.create(
                        lit.toString(),
//                        "",
                        wellFormed);
                } else if ('^' == in.nextChar()) {
                    String datatypeURI = null;
                    if (!expect("^^<")) {
                        syntaxError("ill-formed datatype");
                        return null;
                    }
                    datatypeURI = readURI();
                    if (datatypeURI == null || !expect(">"))
                        return null;
					if ( lang.length() > 0 )
					   deprecated("Language tags are not permitted on typed literals.");

                    return Literal.createTyped (
                        lit.toString(),
                        datatypeURI);
                } else {
                    return Literal.create(lit.toString(), lang);
                }
            }
            lit = lit.append(inChar);
        }
    }

    private char readUnicode4Escape() {
        char buf[] =
            new char[] {
                in.readChar(),
                in.readChar(),
                in.readChar(),
                in.readChar()};
        if (badEOF()) {
            return 0;
        }
        try {
            return (char) Integer.parseInt(new String(buf), 16);
        } catch (NumberFormatException e) {
            syntaxError("bad unicode escape sequence");
            return 0;
        }
    }
    private void deprecated(String s) {
        
    }

    private String readLang() {
        StringBuffer lang = new StringBuffer(15);


        while (true) {
            char inChar = in.nextChar();
            if (Character.isWhitespace(inChar) || inChar == '.' || inChar == '^')
                return lang.toString();
            lang = lang.append(in.readChar());
        }
    }

    /**
     *
     * @return
     */
    protected boolean badEOF() {
        if (in.eof()) {
            syntaxError("premature end of file");
        }
        return inErr;
    }
    
    /**
     *
     * @return
     */
    protected String readURI() {
        StringBuffer uri = new StringBuffer(sbLength);

        while (in.nextChar() != '>') {
            char inChar = in.readChar();

            if (inChar == '\\') {
                expect("u");
                inChar = readUnicode4Escape();
            }
            if (badEOF()) {
                return null;
            }
            uri = uri.append(inChar);
        }
        return uri.toString();
    }

    /**
     *
     * @return
     */
    protected String readName() {
        StringBuffer name = new StringBuffer(sbLength);

        while (!Character.isWhitespace(in.nextChar())) {
            name = name.append(in.readChar());
            if (badEOF())
                return null;
        }
        return name.toString();
    }
    /**
     *
     * @param str
     * @return
     */
    protected boolean expect(String str) {
        StringBuffer b = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            char want = str.charAt(i);

            if (badEOF())
                return false;

            char inChar = in.readChar();
            b.append(inChar);
            if (inChar != want) {
                //System.err.println("N-triple reader error");
                syntaxError("expected \"" + str + "\" found \"" + b.toString() + "\"");
                return false;
            }
        }
        return true;
    }
    /**
     *
     */
    protected void skipWhiteSpace() {
        while (Character.isWhitespace(in.nextChar()) || in.nextChar() == '#') {
            char inChar = in.readChar();
            if (in.eof()) {
                return;
            }
            if (inChar == '#') {
                while (inChar != '\n') {
                    inChar = in.readChar();
                    if (in.eof()) {
                        return;
                    }
                }
            }
        }
    }


    /**
     *
     * @param sort
     * @param msg
     * @param linepos
     * @param charpos
     * @return
     */
    protected String syntaxErrorMessage(
        String sort,
        String msg,
        int linepos,
        int charpos) {
        return base
            + sort
            + " at line "
            + linepos
            + " position "
            + charpos
            + ": "
            + msg;
    }

}

class IStream {

    // simple input stream handler

    Reader in;
    char[] thisChar = new char[1];
    boolean eof;
    int charpos = 1;
    int linepos = 1;
//    StringBuilder buf = new StringBuilder();

    protected IStream(Reader in) {
        try {
            this.in = in;
            eof = (in.read(thisChar, 0, 1) == -1);
        } catch (IOException e) {
            throw new TrOWLException(e);
        }
    }

    protected char readChar() {
        try {
            if (eof)
                return '\000';
            char rv = thisChar[0];
            eof = (in.read(thisChar, 0, 1) == -1);
            if (rv == '\n') {
                linepos++;
                charpos = 0;
 //               buf.delete (0, buf.length());
            } else {
                charpos++;
   //             buf.append(rv);
            }

            return rv;
        } catch (java.io.IOException e) {
           throw new TrOWLException(e);
        }
    }

    protected char nextChar() {
        return eof ? '\000' : thisChar[0];
    }

    protected boolean eof() {
        return eof;
    }

    protected int getLinepos() {
        return linepos;
    }

    protected int getCharpos() {
        return charpos;
    }

}