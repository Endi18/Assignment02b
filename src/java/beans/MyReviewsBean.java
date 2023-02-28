package beans;

import DAO.BookDAO;
import DAO.ReviewDAO;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import models.Book;
import models.Review;

@ManagedBean(name="myReviewsBean")
@RequestScoped
public class MyReviewsBean {
    
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;
    private ReviewDAO reviewDao = new ReviewDAO();
    private BookDAO bookDao = new BookDAO();
    private String bookTitle;
    
    public MyReviewsBean(){
        
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }



    public String removeReview(int userID, int bookID) throws Exception{
        Review review = reviewDao.getByIds(userID, bookID);
        reviewDao.delete(review);
        return "";
    }

    public List<Review> getReviews(){
        return reviewDao.getUserReviews(loginBean.getUser().getId());
    }

    public String getBookTitle(int bookId){
        Book book = bookDao.getById(bookId);
        return book.getTitle();
    }
    
}
