package beans;

import DAO.BookDAO;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import models.Book;

@ManagedBean
@RequestScoped
public class MainBean {
    
    private BookDAO bookDAO;
    private int rankTop;
    private int rankLast;

    @ManagedProperty(value="#{loginBean}")
    LoginBean loginBean;
    
    public MainBean() {
        rankTop=0;
        rankLast=0;
        bookDAO = new BookDAO();
    }

    public String addReview(){
        if(loginBean.getUser()==null) return "login.xhtml?faces-redirect=true";
        else{
            return "bookDetails.xhtml?faces-redirect=true";
        }
    }
    
    public List<Book> fillTopFive() {
        return bookDAO.getTopFive();
    }

    public List<Book> fillLastFive() {
        return bookDAO.getLastFive();
    }
    
    public String getAverageRating(int bookID){
        Double average = bookDAO.getAverageRating(bookID);
        if(average==0) return "NaN";
        return average.toString();
    }
    
    public int getRankTop(){
        return ++rankTop;
    }
    public int getRankLast(){
        return ++rankLast;
    }
    public LoginBean getLoginBean() {
        return loginBean;
    }
    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }
  
}
