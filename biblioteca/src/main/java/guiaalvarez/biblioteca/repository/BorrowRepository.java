package guiaalvarez.biblioteca.repository;

import guiaalvarez.biblioteca.entity.Book;
import guiaalvarez.biblioteca.entity.Borrow;
import guiaalvarez.biblioteca.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    @Query("select b from Borrow b where b.user.username = :username")
    List<Borrow> findAllBorrowsByUser(@Param("username") String username);

    @Query("select b from Borrow b where b.user.username = :username and b.returnDate is null")
    List<Borrow> findAllActiveBorrowsByUser(@Param("username") String username);

    @Query("select b from Borrow b " +
            "where b.user.username = :username " +
            "and b.book.title = :title " +
            "and b.returnDate is null")
    Borrow findActiveBorrowByUserAndTitle(@Param("username") String username,
                                             @Param("title") String title);

    @Query("select b from Borrow b where b.returnDate is null")
    List<Borrow> findAllActiveBorrows();

    @Query("select b from Borrow b where b.borrowDate between :initialDate and " +
            "coalesce(:endDate, current_date())")
    List<Borrow> findAllBorrowsBetweenDates(@Param("initialDate") Date initialDate,
                                            @Param("endDate") Date endDate
                                            );
}
