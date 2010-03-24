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

package eu.trowl.query;

import eu.trowl.query.sparql.*;
import java.io.StringReader;

/**
 *
 * @author ed
 */
public class QueryFactory {

    /**
     *
     * @param sparql
     * @return
     * @throws QueryException
     */
    public static Query create(String sparql) throws QueryException {
		// Parse query.
        StringReader s = new StringReader(sparql);
        SPARQLParser p = new SPARQLParser(s);
        
        try {
            p.CompilationUnit();
        } catch (ParseException ex) {
            throw new QuerySyntaxException(ex.getLocalizedMessage());
        }
        return p.getQuery();
	}
	/*
	private static void createSelectClause(SelectClause select, Query jenaQuery) throws QueryException {
		Iterator<String> resultVars = jenaQuery.getResultVars().iterator();
		while (resultVars.hasNext()) {
			String label = resultVars.next();
			Variable v = new Variable(label);
			select.addVariable(v);
		}
	}
	
	private static void createFromClause(FromClause from, Query jenaQuery) throws QueryException {
		if (!jenaQuery.getNamedGraphURIs().isEmpty())
			throw new QueryException("FROM NAMED not supported.");

		try {
			List<String> fromURIs = jenaQuery.getGraphURIs();
			for (String uri : fromURIs)
				from.addGraphURI(new URI(uri));
				
		} catch (URISyntaxException e) {
			throw new QueryException("Invalid URI: " + e.getInput(), e);
		}
	}
	
	private static void createWhereClause(WhereClause where, Query jenaQuery) throws QueryException {
		ElementGroup group = (ElementGroup)jenaQuery.getQueryPattern();
		List<Element> queryElements = group.getElements();
		boolean gotBGP = false;
		boolean gotFilter = false;
		
		for (Element element : queryElements) {
			if (element instanceof ElementTriplesBlock) {
				if (gotBGP)
					throw new QueryException("Multiple basic graph patterns are not supported in WHERE clause.");
				else
					gotBGP = true;
				
				GraphPattern bgp = createGraphPattern(where, (ElementTriplesBlock)element);
				where.setBasicGraphPattern(bgp);
			} else if (element instanceof ElementFilter) {
				if (gotFilter)
					throw new QueryException("Multiple filters are not supported in WHERE clause.");
				else
					gotFilter = true;
				
				Filter filter = createFilter(where, (ElementFilter)element);
				where.setFilter(filter);
			} else if (element instanceof ElementOptional) {
				GraphPattern ogp = createOptionalGraphPattern(where, (ElementOptional)element);
				where.addOptionalGraphPattern(ogp);
			} else {
				throw new QueryException("Unsupported WHERE element (" + element.getClass().getSimpleName() + ")");
			}
			
			if (!gotBGP)
				throw new QueryException("The WHERE clause must start with a Basic Graph Pattern.");
		}
	}
	
	private static GraphPattern createGraphPattern(WhereClause where, ElementTriplesBlock element) throws QueryException{
		BasicPattern bgp = element.getTriples();
		GraphPattern graphPattern = new GraphPattern();
		
		for (Triple triple : (List<Triple>)bgp.getList())
			graphPattern.addTriple(triple);

		return graphPattern;
	}
	
	private static GraphPattern createOptionalGraphPattern(WhereClause where, ElementOptional element) throws QueryException {
		List<Element> elements = ((ElementGroup)element.getOptionalElement()).getElements();
		
		ElementTriplesBlock graphPattern = null;
		for (Element el : elements) {
			if (el instanceof ElementTriplesBlock) {
				if (graphPattern != null) // not sure if this can happen...
					throw new QueryException("Only single triples block supported in OPTIONAL.");
				
				graphPattern = (ElementTriplesBlock)el;
			} else {
				throw new QueryException("Only triples supported in OPTIONAL.");
			}
		}
		
		return createGraphPattern(where, graphPattern);
	}
	
	private static Filter createFilter(WhereClause where, ElementFilter element) throws QueryException {
		return new Filter(element.getExpr());
	}*/

}
