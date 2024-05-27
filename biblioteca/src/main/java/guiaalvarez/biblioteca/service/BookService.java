package guiaalvarez.biblioteca.service;
import guiaalvarez.biblioteca.auth.AuthenticationFacade;
import guiaalvarez.biblioteca.entity.Book;
import guiaalvarez.biblioteca.repository.BookRepository;
import guiaalvarez.biblioteca.repository.BorrowRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Optional<Book> findBookById(long bookId) {
        return bookRepository.findById(bookId);
    }

    public Book findBookByTitle(String title) {
        if(bookRepository.findByTitle(title) == null){
            throw new RuntimeException("Book not found");
        }
        return bookRepository.findByTitle(title);
    }

    public List<Book> findBookByAuthor(String author) {
        if(bookRepository.findByAuthor(author) == null){
            throw new RuntimeException("Author not found");
        }
        return bookRepository.findByAuthor(author);
    }

    public List<Book> findBookByCategory(String category) {
        if(bookRepository.findByCategory(category) == null){
            throw new RuntimeException("Category not found");
        }
        return bookRepository.findByCategory(category);
    }

    @Transactional
    public Book updateBookQuantity(String title, int quantity) throws Exception {
        Optional<Book> optionalBook = Optional.ofNullable(bookRepository.findByTitle(title));
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setQuantity(quantity);
            return bookRepository.save(book);
        } else {
            throw new Exception("Book not found");
        }
    }


    @Transactional
    public void deleteTitle(String title) {
        Book book = findBookByTitle(title);
        bookRepository.delete(book);
    }

}
