package beans;

import DAO.UserDAO;
import java.util.Base64;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import models.User;

@ManagedBean
@RequestScoped
public class UpdateProfileBean {

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
    private String messagePassword;

    private String messageDetails;
    private String email;
    private String surname;
    private String name;

    public UpdateProfileBean() {
    }

    public User getCurrentUser() {
        return loginBean.getUser();
    }

    public String getName() {
        return getCurrentUser().getName();
    }

    public String getEmail() {
        return getCurrentUser().getEmail();
    }

    public String getSurname() {
        return getCurrentUser().getSurname();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setCurrentPassowrd(String currentPassowrd) {
        this.currentPassword = currentPassowrd;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getCurrentPassowrd() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public String getMessagePassword() {
        return messagePassword;
    }

    public String getMessageDetails() {
        return messageDetails;
    }

    public void updateProfile() {
        UserDAO userDAO = new UserDAO();
        try {
            getCurrentUser().setName(name);
            getCurrentUser().setSurname(surname);
            getCurrentUser().setEmail(email);
            userDAO.update(getCurrentUser());
            messageDetails = "User Data updated successfully!";
        } catch (Exception e) {
            messageDetails = "This email is already taken! "
                    + "Try another one or simply go back without making any changes.";
        }
    }

    public void updatePassword() {
        if (!doPasswordsMatch(encryptPassword(currentPassword),
                getCurrentUser().getPassword())) {
            messagePassword = " Incorrect current password ";
        } else if (doPasswordsMatch(currentPassword, newPassword)) {
            messagePassword = "New password cannot be the same as old password!";
        } else if (!doPasswordsMatch(newPassword, confirmNewPassword)) {
            messagePassword = "New and Confirmed passwords do not match";
        } else {
            UserDAO userDAO = new UserDAO();
            getCurrentUser().setPassword(encryptPassword(newPassword));
            userDAO.update(getCurrentUser());
            messagePassword = "Password changed successfully";
        }
    }

    private String encryptPassword(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }
    private boolean doPasswordsMatch(String pass1, String pass2) {
        return pass1.equals(pass2);
    }

}
