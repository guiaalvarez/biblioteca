package guiaalvarez.biblioteca.service;
import guiaalvarez.biblioteca.auth.AuthenticationFacade;
import guiaalvarez.biblioteca.entity.Book;
import guiaalvarez.biblioteca.entity.Borrow;
import guiaalvarez.biblioteca.entity.User;
import guiaalvarez.biblioteca.repository.BookRepository;
import guiaalvarez.biblioteca.repository.BorrowRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class BorrowService {
    @Autowired
    private BorrowRepository borrowRepository;
    @Autowired
    private BookRepository bookRepository;
    ;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Transactional
    public Borrow borrowBookById(long bookId) {
        User user = authenticationFacade.getUser();
        List<Borrow> allActiveBorrows = borrowRepository.findAllActiveBorrowsByUser(user.getUsername());
        if ((long) allActiveBorrows.size()>2) {
            throw new RuntimeException("You have already reached the maximum amount of simultaneous borrows allowed.");
        }
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) {
            throw new RuntimeException("Book with ID: " + bookId + " not found");
        }
        Borrow existingBorrow = borrowRepository.findActiveBorrowByUserAndTitle(user.getUsername(), book.getTitle());
        if (existingBorrow != null && existingBorrow.getBook().getTitle().equals(book.getTitle())) {
            throw new RuntimeException("You already have this book borrowed.");
        }
        if (book.getQuantity() < 1) {
            throw new RuntimeException("Your requested book \"" + book.getTitle() + "\" is out of stock!");
        }
        Borrow borrow = new Borrow();
        borrow.setUser(user);
        borrow.setBook(book);
        borrow.setBorrowDate(new Date());
        LocalDate currentDate = LocalDate.now();
        LocalDate returnDeadline = currentDate.plusDays(60);
        borrow.setReturnDeadline(java.sql.Date.valueOf(returnDeadline));
        borrow.setReturnDate(null);
        borrow.setFine(0);
        book.borrowedOne();
        return borrowRepository.save(borrow);
    }



    @Transactional
    public Borrow borrowBookByTitle(String bookTitle) {
        User user = authenticationFacade.getUser();
        List<Borrow> allActiveBorrows = borrowRepository.findAllActiveBorrowsByUser(user.getUsername());
        if ((long) allActiveBorrows.size()>2) {
            throw new RuntimeException("You have already reached the maximum amount of simultaneous borrows allowed.");
        }
        Book book = bookRepository.findByTitle(bookTitle);
        if (book == null) {
            throw new RuntimeException("Book with title: " + bookTitle + " not found.");
        }
        Borrow existingBorrow = borrowRepository.findActiveBorrowByUserAndTitle(user.getUsername(), book.getTitle());
        if (existingBorrow != null && existingBorrow.getBook().getTitle().equals(book.getTitle())) {
            throw new RuntimeException("You already have this book borrowed.");
        }
        if (book.getQuantity() < 1) {
            throw new RuntimeException("Your requested book \"" + book.getTitle() + "\" is out of stock!");
        }
        Borrow borrow = new Borrow();
        borrow.setUser(user);
        borrow.setBook(book);
        borrow.setBorrowDate(new Date());
        LocalDate currentDate = LocalDate.now();
        LocalDate returnDeadline = currentDate.plusDays(60);
        borrow.setReturnDeadline(java.sql.Date.valueOf(returnDeadline));
        borrow.setReturnDate(null);
        borrow.setFine(0);
        book.borrowedOne();
        return borrowRepository.save(borrow);
    }

    @Transactional
    public Borrow returnBookById(long borrowId, Date returnDate) {
        Borrow borrow = borrowRepository.findById(borrowId).orElse(null);
        if (borrow != null) {
            Book book = bookRepository.findById(borrow.getBook().getId()).get();
            if (returnDate.compareTo(borrow.getReturnDeadline()) > 0) {
                long daysOverdue = ChronoUnit.DAYS.between(borrow.getReturnDeadline().toInstant(), returnDate.toInstant());
                double fine = Math.min(daysOverdue * 0.5, 100.0);
                borrow.setFine(fine);
            }
            book.returnedOne();
            borrow.setReturnDate(returnDate);
            return borrowRepository.save(borrow);
        } else {
            throw new RuntimeException("There was no borrow found for the provided borrow ID:" + borrowId);
        }
    }


    @Transactional
    public Borrow returnBookByTitle(String title, Date returnDate) {
        User user = authenticationFacade.getUser();
        Borrow borrow = borrowRepository.findActiveBorrowByUserAndTitle(user.getUsername(), title);
        if (borrow != null) {
            if (returnDate.compareTo(borrow.getReturnDeadline()) > 0) {
                long daysOverdue = ChronoUnit.DAYS.between(borrow.getReturnDeadline().toInstant(), returnDate.toInstant());
                double fine = Math.min(daysOverdue * 0.5, 100.0);
                borrow.setFine(fine);
            }
            Book book = bookRepository.findByTitle(title);
            book.returnedOne();
            borrow.setReturnDate(returnDate);
            return borrowRepository.save(borrow);
        } else {
            throw new RuntimeException("There was no active borrow found for the provided book title: " + title);
        }
    }




}

