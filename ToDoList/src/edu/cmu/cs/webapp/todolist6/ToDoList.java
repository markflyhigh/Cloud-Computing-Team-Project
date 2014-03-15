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
import edu.cmu.cs.webapp.todolist6.databean.ItemBean;
import edu.cmu.cs.webapp.todolist6.databean.User;
import edu.cmu.cs.webapp.todolist6.formbean.ItemForm;

public class ToDoList extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private ItemDAO itemDAO;
	
	private FormBeanFactory<ItemForm>  itemFormFactory  = FormBeanFactory.getInstance(ItemForm.class);
	
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
       		// Fetch the items now, so that in case there is no form or there are errors
       		// We can just dispatch to the JSP to show the item list (and any errors)
       		request.setAttribute("items", itemDAO.getItems());
       		
	        ItemForm form = itemFormFactory.create(request);
        	request.setAttribute("form", form);
	        
	        if (!form.isPresent()) {
                RequestDispatcher d = request.getRequestDispatcher("todolist.jsp");
                d.forward(request,response);
        		return;
	        }
	        errors.addAll(form.getValidationErrors());
	        if (errors.size() > 0) {
                RequestDispatcher d = request.getRequestDispatcher("todolist.jsp");
                d.forward(request,response);
	        	return;
	        }
	        
	        ItemBean bean = new ItemBean();
	        bean.setItem(form.getItem());
       		bean.setIpAddress(request.getRemoteAddr());
       		bean.setUserName(((User) request.getSession().getAttribute("user")).getUserName());

        	if (form.getAction().equals("Add to Top")) {
        		itemDAO.addToTop(bean);
        	} else if (form.getAction().equals("Add to Bottom")) {
        		itemDAO.addToBottom(bean);
        	} else {
        		errors.add("Invalid action: " + form.getAction());
        	}

       		// Fetch the items again, since we modified the list
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