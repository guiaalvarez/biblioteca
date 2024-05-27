package guiaalvarez.biblioteca.config;


import guiaalvarez.biblioteca.entity.Book;
import guiaalvarez.biblioteca.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class InitialConfig {
    @Bean
    public CommandLineRunner loadInitialData(BookRepository bookRepository){
        return (args) -> {
            Book atomicHabits = new Book("Atomic habits", "James Clear",
                    "Self Help", 5);
            Book thinkFastSlow = new Book("Thinking fast and slow", "Daniel Kahneman",
                    "Self Help", 1);
            Book psyMoney = new Book("The psychology of money", "Morgan Housel",
                    "Finance", 1);
            Book fellowRing = new Book("Fellowship of the ring", "J.R.R. Tolkien",
                    "Fantasy", 2);
            Book tt = new Book("The two towers", "J.R.R. Tolkien",
                    "Fantasy", 2);
            Book rk = new Book("The return of the king", "J.R.R. Tolkien",
                    "Fantasy", 2);
            Book got = new Book("Song of fire and ice", "George R.R. Martin",
                    "Fantasy", 3);
            bookRepository.save(atomicHabits);
            bookRepository.save(thinkFastSlow);
            bookRepository.save(psyMoney);
            bookRepository.save(fellowRing);
            bookRepository.save(tt);
            bookRepository.save(rk);
            bookRepository.save(got);

        };
    }
}