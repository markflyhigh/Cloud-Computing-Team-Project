package edu.cmu.cs.webapp.todolist6.dao;

import java.util.Comparator;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import edu.cmu.cs.webapp.todolist6.databean.ItemBean;

public class ItemDAO extends GenericDAO<ItemBean> {
	public ItemDAO(ConnectionPool cp, String tableName) throws DAOException {
		super(ItemBean.class, tableName, cp);
	}
	
	public void addToTop(ItemBean item) throws RollbackException {
		try {
			Transaction.begin();
			
			// Get item at top of list
			ItemBean[] a = match(MatchArg.min("position"));
			
			ItemBean topBean;
			if (a.length == 0) {
				topBean = null;
			} else {
				topBean = a[0];
			}
			
			int newPos;
			if (topBean == null) {
				// List is empty...just add it with position = 1
				newPos = 1;
			} else {
				// Create the new item with position one less than the top bean's position
				newPos = topBean.getPosition() - 1;
			}

			item.setPosition(newPos);

			// Create a new ItemBean in the database with the next id number
			createAutoIncrement(item);

			Transaction.commit();
		} finally {
			if (Transaction.isActive()) Transaction.rollback();
		}
	}
	
	public void addToBottom(ItemBean item) throws RollbackException {
		try {
			Transaction.begin();
			
			// Get item at bottom of list
			ItemBean[] a = match(MatchArg.max("position"));
			
			ItemBean bottomBean;
			if (a.length == 0) {
				bottomBean = null;
			} else {
				bottomBean = a[0];
			}
			
			int newPos;
			if (bottomBean == null) {
				// List is empty...just add it with position = 1
				newPos = 1;
			} else {
				// Create the new item with position one more than the bottom bean's position
				newPos = bottomBean.getPosition() + 1;
			}

			item.setPosition(newPos);

			// Create a new ItemBean in the database with the next id number
			createAutoIncrement(item);

			Transaction.commit();
		} finally {
			if (Transaction.isActive()) Transaction.rollback();
		}
	}
	
	public ItemBean[] getItems() throws RollbackException {
		
		// Calls GenericDAO's match() method.
		// This no match constraint arguments, match returns all the Item beans 
		ItemBean[] items = match();
        
        // Sort the list in position order
        java.util.Arrays.sort(items, new Comparator<ItemBean>() {
        	public int compare(ItemBean item1, ItemBean item2) {
        		return item1.getPosition() - item2.getPosition();
        	}
		});
        
        return items;
	}

}
