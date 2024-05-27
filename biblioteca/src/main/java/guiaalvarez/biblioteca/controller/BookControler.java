package guiaalvarez.biblioteca.controller;

import guiaalvarez.biblioteca.entity.Book;
import guiaalvarez.biblioteca.repository.BookRepository;
import guiaalvarez.biblioteca.repository.BorrowRepository;
import guiaalvarez.biblioteca.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class BookControler {
    @Autowired
    BookService bookService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BorrowRepository borrowRepository;

    @GetMapping("/book")
    public List<Book> findAllBooks(){
        return bookRepository.findAll();
    }

    @GetMapping("/book/findById/{id}")
    public ResponseEntity<Optional<Book>>findById(@PathVariable long id) {
        Optional<Book> book = bookRepository.findById(id);
        return ResponseEntity.ok().body(book);
    }

    @GetMapping("/book/findByTitle")
    public ResponseEntity<Book> findByTitle(@RequestParam String title) {
        if(bookRepository.findByTitle(title) == null){
            throw new RuntimeException("Book not found");
        }
        Book book = bookRepository.findByTitle(title);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/book/findByAuthor")
    public ResponseEntity<List<Book>> findBookByAuthor(@RequestParam String author) {
        return ResponseEntity.ok(bookService.findBookByAuthor(author));
    }

    @GetMapping("/book/findByCategory")
    public ResponseEntity<List<Book>> findByCategory(@RequestParam String category) {
        if(bookRepository.findByCategory(category) == null){
            throw new RuntimeException("Category not found");
        }
        List<Book> books = bookRepository.findByCategory(category);
            return ResponseEntity.ok(books);
    }

    @PostMapping("/book")
    public Book addBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @PostMapping("/book/updateQuantity")
    public Book updateQuantity(@RequestParam String title, @RequestParam int quantity) {
        try {
            return bookService.updateBookQuantity(title, quantity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update book quantity: " + e.getMessage());
        }
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<?> deleteBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            bookRepository.deleteById(id);
            return ResponseEntity.ok().body("Book with ID " + id + " deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/book/deleteTitle")
    public ResponseEntity<?> deleteBookByTitle(@RequestParam String title) {
        Optional<Book> book = Optional.ofNullable(bookRepository.findByTitle(title));
        if (book.isPresent()) {
            bookService.deleteTitle(title);
            return ResponseEntity.ok().body("Book " + title + " deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
