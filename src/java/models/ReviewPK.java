package models;

import DAO.ReviewDAO;
import DAO.UserDAO;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ReviewPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "UserID")
    private int userID;
    @Basic(optional = false)
    @Column(name = "BookID")
    private int bookID;

    public ReviewPK() {
    }

    public ReviewPK(int userID, int bookID) {
        this.userID = userID;
        this.bookID = bookID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userID;
        hash += (int) bookID;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ReviewPK)) {
            return false;
        }
        ReviewPK other = (ReviewPK) object;
        if (this.userID != other.userID) {
            return false;
        }
        if (this.bookID != other.bookID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.ReviewPK[ userID=" + userID + ", bookID=" + bookID + " ]";
    }
    
}
