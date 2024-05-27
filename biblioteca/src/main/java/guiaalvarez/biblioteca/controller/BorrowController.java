package guiaalvarez.biblioteca.controller;

import guiaalvarez.biblioteca.auth.AuthenticationFacade;
import guiaalvarez.biblioteca.entity.Book;
import guiaalvarez.biblioteca.entity.Borrow;
import guiaalvarez.biblioteca.entity.User;
import guiaalvarez.biblioteca.repository.BookRepository;
import guiaalvarez.biblioteca.repository.BorrowRepository;
import guiaalvarez.biblioteca.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class BorrowController {
    @Autowired
    BorrowService borrowService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BorrowRepository borrowRepository;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    
    @PostMapping(value = "/borrow/{bookId}")
    public ResponseEntity<Borrow> borrowBook(@PathVariable long bookId){
        return new ResponseEntity<>(borrowService.borrowBookById(bookId), HttpStatus.CREATED);
    }
    @PostMapping(value = "/borrow/byTitle")
    public ResponseEntity<Borrow> borrowBookByTitle(@RequestBody String title) {
        return new ResponseEntity<>(borrowService.borrowBookByTitle(title), HttpStatus.CREATED);
    }

    @GetMapping("/borrow")
    public List<Borrow> findAllBorrows(){
        return borrowRepository.findAll();
    }
    @GetMapping("/borrow/user")
    public List<Borrow> findAllBorrowsByUser(){
        User user = authenticationFacade.getUser();
        return borrowRepository.findAllBorrowsByUser(user.getUsername());
    }
    @GetMapping("/borrow/active/user")
    public List<Borrow> findAllActiveBorrowsByUser(){
        User user = authenticationFacade.getUser();
        return borrowRepository.findAllActiveBorrowsByUser(user.getUsername());
    }
    @GetMapping("/borrow/active")
    public List<Borrow> findAllActiveBorrows(){
        return borrowRepository.findAllActiveBorrows();
    }
    @GetMapping("/borrow/initialDate/{initialDate}/endDate/{endDate}")
    public List<Borrow> findAllBorrowsBetweenDates(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate initialDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return borrowRepository.findAllBorrowsBetweenDates(
                java.sql.Date.valueOf(initialDate),
                java.sql.Date.valueOf(endDate)
        );
    }


    @PostMapping(value = "/borrow/return/{borrowId}", consumes = "application/json")
    public ResponseEntity<Borrow> returnBook(@PathVariable long borrowId, @RequestBody Date returnDate) {
        return new ResponseEntity<>(borrowService.returnBookById(borrowId, returnDate), HttpStatus.CREATED);
    }

    @PostMapping(value = "/borrow/return/byTitle", consumes = "application/json")
    public ResponseEntity<Borrow> returnBookByTitle(@RequestBody Map<String, String> requestBody) {
        String title = requestBody.get("title");
        String dateString = requestBody.get("returnDate");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//setLenient(false).
        Date returnDate = null;
        try {
            returnDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid return date format. Please use yyyy-MM-dd format.");
        }

        Borrow borrow = borrowService.returnBookByTitle(title, returnDate);

        return new ResponseEntity<>(borrow, HttpStatus.CREATED);
    }

    @DeleteMapping("/borrow/delete/{id}")
    public ResponseEntity<?> deleteBorrowById(@PathVariable Long id) {
        Optional<Borrow> borrow = borrowRepository.findById(id);
        if (borrow.isPresent()) {
            borrowRepository.deleteById(id);
            return ResponseEntity.ok().body("Borrow with ID " + id + " deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
