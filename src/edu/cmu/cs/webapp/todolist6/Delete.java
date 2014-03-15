package edu.cmu.cs.webapp.todolist6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import edu.cmu.cs.webapp.todolist6.dao.ItemDAO;
import edu.cmu.cs.webapp.todolist6.databean.User;
import edu.cmu.cs.webapp.todolist6.formbean.IdForm;

public class Delete extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private ItemDAO itemDAO;
	
	private FormBeanFactory<IdForm>    idFormFactory    = FormBeanFactory.getInstance(IdForm.class);
	
	public void init() throws ServletException {
        ServletContext context = getServletContext();
        String jdbcDriverName = context.getInitParameter("jdbcDriverName");
        String jdbcURL = context.getInitParameter("jdbcURL");

		try {
			ConnectionPool connectionPool = new ConnectionPool(jdbcDriverName,jdbcURL);
			itemDAO = new ItemDAO(connectionPool, "todolist");
		} catch (DAOException e) {
			throw new ServletException(e);
		}
	}

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request,response);
    }

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
        	response.sendRedirect("Login");
        	return;
        }

        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors", errors);
       	
       	try {
	        IdForm form = idFormFactory.create(request);
	        errors.addAll(form.getValidationErrors());
	        if (errors.size() > 0) {
	            RequestDispatcher d = request.getRequestDispatcher("error.jsp");
	            d.forward(request,response);
	        	return;
	        }
	        
	    	itemDAO.delete(form.getIdAsInt());
	    	
       		request.setAttribute("items", itemDAO.getItems());

            RequestDispatcher d = request.getRequestDispatcher("todolist.jsp");
            d.forward(request,response);
       	} catch (RollbackException e) {
       		errors.add(e.getMessage());
            RequestDispatcher d = request.getRequestDispatcher("error.jsp");
            d.forward(request,response);
       	} catch (FormBeanException e) {
       		errors.add(e.getMessage());
            RequestDispatcher d = request.getRequestDispatcher("error.jsp");
            d.forward(request,response);
       	}
	}
}