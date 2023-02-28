package API;

import DAO.BookDAO;
import java.util.List;
import java.util.stream.Collectors;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import models.Book;

@WebService(serviceName = "BooksFilterAPI")
public class BooksFilterAPI {

    BookDAO bookDAO = new BookDAO();
    @WebMethod(operationName = "filterBooks")
    public List<BookData> filterBooks(@WebParam(name="title")String title,
            @WebParam(name="author") String author,
            @WebParam(name="genre")String genre,
            @WebParam(name="minAverageRating")int minAverageRating, 
            @WebParam(name="maxAverageRating")int maxAverageRating)
    {
        
        if(minAverageRating==0 && maxAverageRating==0 && !bookDAO.isValueSet(title) && !bookDAO.isValueSet(author)
                && !bookDAO.isValueSet(genre))
            return null;
        
        return bookDAO.filterBooks(title, author, maxAverageRating, 
                maxAverageRating, genre)
                .stream()
                .map(this::toBookData)
                .collect(Collectors.toList());
    }

    private BookData toBookData(Book b){
        double rating = bookDAO.getAverageRating(b.getId());
        BookData bookData = new BookData();
        bookData.setId(b.getId());
        bookData.setTitle(b.getTitle());
        bookData.setAuthor(b.getAuthor());
        bookData.setBookRating(rating);
        return bookData;
    }
}
