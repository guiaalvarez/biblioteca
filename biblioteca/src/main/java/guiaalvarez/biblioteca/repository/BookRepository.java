package guiaalvarez.biblioteca.repository;

import guiaalvarez.biblioteca.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("select b from Book b where b.title = :title")
    Book findByTitle(@Param("title") String title);
    @Query("select b from Book b where b.author = :author")
    List<Book> findByAuthor(@Param("author") String author);
    @Query("select b from Book b where b.category = :category")
    List<Book> findByCategory(@Param("category") String category);

    void deleteByTitle(String title);

}
