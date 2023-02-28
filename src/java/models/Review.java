package models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "reviews")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Review.findAll", query = "SELECT r FROM Review r")
    , @NamedQuery(name = "Review.findByUserID", query = "SELECT r FROM Review r WHERE r.reviewPK.userID = :userID")
    , @NamedQuery(name = "Review.findByBookID", query = "SELECT r FROM Review r WHERE r.reviewPK.bookID = :bookID")
    , @NamedQuery(name = "Review.findByRating", query = "SELECT r FROM Review r WHERE r.rating = :rating")
    , @NamedQuery(name = "Review.findByText", query = "SELECT r FROM Review r WHERE r.text = :text")})
public class Review implements Serializable {

    @JoinColumn(name = "BookID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Book book;
    @JoinColumn(name = "UserID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ReviewPK reviewPK;
    @Basic(optional = false)
    @Column(name = "Rating")
    private int rating;
    @Basic(optional = false)
    @Column(name = "Text")
    private String text;

    public Review() {
    }

    public Review(ReviewPK reviewPK) {
        this.reviewPK = reviewPK;
    }

    public Review(ReviewPK reviewPK, int rating, String text) {
        this.reviewPK = reviewPK;
        this.rating = rating;
        this.text = text;
    }

    public Review(int userID, int bookID) {
        this.reviewPK = new ReviewPK(userID, bookID);
    }

    public ReviewPK getReviewPK() {
        return reviewPK;
    }

    public void setReviewPK(ReviewPK reviewPK) {
        this.reviewPK = reviewPK;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reviewPK != null ? reviewPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Review)) {
            return false;
        }
        Review other = (Review) object;
        if ((this.reviewPK == null && other.reviewPK != null) || (this.reviewPK != null && !this.reviewPK.equals(other.reviewPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Review[ reviewPK=" + reviewPK + " ]";
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}
