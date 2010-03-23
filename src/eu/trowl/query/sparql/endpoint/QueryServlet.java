package eu.trowl.query.sparql.endpoint;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.trowl.query.*;
import eu.trowl.query.sparql.*;

/**
 * 
 * @author sttaylor
 * 
 */
public class QueryServlet extends HttpServlet {

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		try {
			String queryString = request.getParameter("query");
			if (queryString == null || queryString.length() == 0)
				throw new InvalidParameterException("Empty query string.");
			
			Query sparql = QueryFactory.create(queryString);
			
			eu.trowl.query.ResultSet rs =  sparql.execute("default");
			XMLResultSet xmlrs = new XMLResultSet(rs);
			xmlrs.write(out);
		} catch (InvalidParameterException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} catch (QueryException e) {
			// TODO output the proper XML error here (or should it be a HTTP error?).
			PrintWriter writer = new PrintWriter(out);
			writer.println(e.getMessage());
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
         *            servlet response
         * @throws ServletException
         * @throws IOException
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
         *
         * @return
         */
    @Override
	public String getServletInfo() {
		return "Short description";
	}
	// </editor-fold>
}
