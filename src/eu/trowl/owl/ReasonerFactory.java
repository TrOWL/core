/*
 * This file is part of TrOWL.
 *
 * Foobar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with TrOWL.  If not, see <http://www.gnu.org/licenses/>. 
 *
 * Copyright 2010 University of Aberdeen
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.trowl.owl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerFactory;
import org.semanticweb.owl.io.OWLOntologyInputSource;
import org.semanticweb.owl.io.ReaderInputSource;
import org.semanticweb.owl.io.StringInputSource;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;

/**
 *
 * @author ed
 */
public class ReasonerFactory implements OWLReasonerFactory {
    private static final String reasonerName = "TrOWL";
    private static final Class defaultType = QLReasoner.class;
    Class type = defaultType;


    /**
     *
     * @param type
     */
    public void setType(Class type) {
        if (Reasoner.class.isAssignableFrom(type)) {
            this.type = type;
        } else {
            throw new InvalidReasonerException();
        }
    }

    /**
     *
     * @param uri
     * @return
     * @throws OntologyLoadException
     */
    public Reasoner load(URI uri) throws OntologyLoadException {
        try {
            Reasoner r = (Reasoner) type.newInstance();
            ReasonerProxy p = new ReasonerProxy(r);
            OWLOntologyManager man = OWLManager.createOWLOntologyManager();
            man.loadOntologyFromPhysicalURI(uri);
            r.load(man);
            
            return p;
        } catch (OWLOntologyCreationException ex) {
            OntologyLoadException e2 = new OntologyLoadException();
            e2.initCause(ex);
            throw (e2);
        } catch (InstantiationException ex) {
            OntologyLoadException e2 = new OntologyLoadException();
            e2.initCause(ex);
            throw (e2);
        } catch (IllegalAccessException ex) {
            OntologyLoadException e2 = new OntologyLoadException();
            e2.initCause(ex);
            throw (e2);
        }
    }

    /**
     *
     * @param man
     * @return
     * @throws OntologyLoadException
     */
    public Reasoner load(OWLOntologyManager man) throws OntologyLoadException {
        try {
            Reasoner r = (Reasoner) type.newInstance();
            r.load(man);
            return r;
        } catch (InstantiationException ex) {
            OntologyLoadException e2 = new OntologyLoadException();
            e2.initCause(ex);
            throw (e2);
        } catch (IllegalAccessException ex) {
            OntologyLoadException e2 = new OntologyLoadException();
            e2.initCause(ex);
            throw (e2);
        }
    }

    public OWLReasoner createReasoner (OWLOntologyManager man) {
        try {
            return (OWLReasoner) load(man);
        } catch (OntologyLoadException ex) {
            Logger.getLogger(ReasonerFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     *
     * @param input
     * @return
     * @throws OntologyLoadException
     */
    public Reasoner load(Reader input) throws OntologyLoadException {
        BufferedReader bufInput;

        if (input.getClass().isAssignableFrom(BufferedReader.class)) {
            bufInput = (BufferedReader) input;
        } else {
            bufInput = new BufferedReader(input);
        }

        try {
            Reasoner r = (Reasoner) type.newInstance();
            OWLOntologyManager man = OWLManager.createOWLOntologyManager();
            OWLOntologyInputSource in = new ReaderInputSource(bufInput);
            man.loadOntology(in);
            r.load(man);
            return r;
        } catch (OWLOntologyCreationException ex) {
            OntologyLoadException e2 = new OntologyLoadException();
            e2.initCause(ex);
            throw (e2);
        } catch (InstantiationException ex) {
            OntologyLoadException e2 = new OntologyLoadException();
            e2.initCause(ex);
            throw (e2);
        } catch (IllegalAccessException ex) {
            OntologyLoadException e2 = new OntologyLoadException();
            e2.initCause(ex);
            throw (e2);
        }
    }

    /**
     *
     * @param content
     * @return
     * @throws OntologyLoadException
     */
    public Reasoner load(String content) throws OntologyLoadException {
        try {
            Reasoner r = (Reasoner) type.newInstance();
            OWLOntologyManager man = OWLManager.createOWLOntologyManager();
            OWLOntologyInputSource in = new StringInputSource(content);
            man.loadOntology(in);
            r.load(man);
            return r;
        } catch (OWLOntologyCreationException ex) {
            OntologyLoadException e2 = new OntologyLoadException();
            e2.initCause(ex);
            throw (e2);
        } catch (InstantiationException ex) {
            OntologyLoadException e2 = new OntologyLoadException();
            e2.initCause(ex);
            throw (e2);
        } catch (IllegalAccessException ex) {
            OntologyLoadException e2 = new OntologyLoadException();
            e2.initCause(ex);
            throw (e2);
        }
    }

    private String readToString(URI what) throws IOException {
        URL in = what.toURL();
        InputStream is = in.openStream();
        Reader input = new BufferedReader(new InputStreamReader(is));
        StringBuilder data = new StringBuilder(1000);
        char[] buf = new char[1024];
        int readCount;
        while ((readCount = input.read(buf)) != -1) {
            data.append(buf, 0, readCount);
        }
        return data.toString();
    }

    public String getReasonerName() {
        return reasonerName;
    }
}
