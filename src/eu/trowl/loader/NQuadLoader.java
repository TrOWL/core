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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.trowl.loader;

import eu.trowl.rdf.*;
import eu.trowl.vocab.*;

/**
 *
 * @author ed
 */
public class NQuadLoader extends NTripleLoader {
    /**
     *
     */
    public final static boolean IS_THREAD_SAFE = true;

    /**
     *
     */
    public NQuadLoader() {
        super();
    }

    /**
     *
     */
    @Override
    public void init() {
        out.init();
    }

    @Override
    public void run() {
        NQuadReader reader = new NQuadReader();
        reader.read(this, in);
        out.close();
    }

    

    /**
     *
     */
    @Override
    public void finish() {
        System.out.println("All threads finished, storing paths...");
        out.storePaths();
        out.rebuildIndices();
        out.close();
        System.out.println("All done!");
    }
}
