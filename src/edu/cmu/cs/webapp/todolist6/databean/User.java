package edu.cmu.cs.webapp.todolist6.databean;

import org.genericdao.PrimaryKey;

@PrimaryKey("userName")
public class User {
    private String userName;
    private String password;

    public String getPassword()        { return password; }
    public String getUserName()        { return userName; }

    public void setPassword(String s)  { password = s;    }
    public void setUserName(String s)  { userName = s;    }
}
