package beans;

import DAO.UserDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import models.User;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private String email;
    private String password;
    private User user;
    private String outputMessage = "";

    public LoginBean() {
    }


    public void login() throws IOException {
        outputMessage = "";
        boolean isUserAuthenticated = authenticateUser();

        if (!isUserAuthenticated) {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("login.xhtml");
        } else {
            outputMessage = "";
            if ("standard".equals(user.getUserType())) {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("indexStandard.xhtml");
            } else {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("indexAdmin.xhtml");
            }
        }
    }

    public void logOut() throws IOException {

        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("", new FacesMessage("You have been successfully logged out!"));
        context.getExternalContext().invalidateSession();
        context.getExternalContext().redirect("login.xhtml");
    }

    private boolean authenticateUser() {

        UserDAO userDAO = new UserDAO();

        try {
            User potentialUser = userDAO.getUserByEmail(email);
            if (potentialUser == null) {
                email = "";
                outputMessage = "Incorrect email.";
                return false;
            }

            if (decryptPassword(potentialUser.getPassword()).equals(password)) {
                user = potentialUser;
                return true;
            } else {
                outputMessage = "Incorrect password.";
                return false;
            }
        } catch (Exception e) {
            email = "";
            outputMessage = "Incorrect email. Exception occurred";
            return false;
        }
    }

    public String isUserLoggedIn(String type) {
        email = "";
        try {
            if (user == null || !type.equals(user.getUserType())) {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("login.xhtml");
            }
        } catch (IOException e) {
            return "login.xhtml";
        }
        return "";
    }

    public String navigate(String userType) {

        if (userType.equals("standard")) {
            return "indexStandard.xhtml";
        } else {
            return "indexAdmin.xhtml";
        }
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOutputMessage() {
        return outputMessage;
    }

    public User getUser() {
        return user;
    }

    public String decryptPassword(String password) {
        return new String(Base64.getDecoder().decode(password.getBytes()));
    }
}
