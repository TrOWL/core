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
package eu.trowl.loader;

import eu.trowl.owl.*;

/**
 *
 * @author ed
 */
public class OWLQLLoader extends Loader {

    /**
     *
     */
    public static final String ID = "TROWL_LOADER_OWLQL";
    /**
     *
     */
    public static final boolean THREAD_SAFE = false;
    private Reasoner r;
    private String data;

    /**
     *
     */
    public OWLQLLoader() {
    }

    /**
     *
     * @throws LoaderInitException
     */
    public void init() throws LoaderInitException {
        try {
            ReasonerFactory rf = new ReasonerFactory();
            rf.setType(QLReasoner.class);
            r = rf.load(in);
            
        } catch (OntologyLoadException ex) {
            LoaderInitException ex2 = new LoaderInitException();
            ex2.initCause(ex);
            throw (ex2);
        }
        out.init();
    }

    public void run() {
        /*try {
        //r.load(data);
        data = "";
        r.store(db);
        } catch (OntologyLoadException ex) {
        Logger.getLogger(OWLQLLoader.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        r.store(db);
    }

    /**
     *
     */
    public void finish() {
    }
}
