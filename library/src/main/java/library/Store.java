package library;

import java.util.List;
import java.util.Optional;

/**
 * Store is the interface that describes book store operations.
 * <p>
 * Defines a couple find methods which might return zero or more results.
 * <p>
 * Defines a paged-browsing operation, to allow book browsing.
 * <p>
 * Defines an operation to return books bought to date by a given client.
 * @author André Diogo
 * @author Diogo Pimenta
 * @version 1.0, 20-12-2017
 */
public interface Store {
    /**
     * Search the store by title.
     * @param title Title to search.
     * @return List of none or more books matching the title.
     */
    List<Book> findByTitle(String title);

    /**
     * Search the store by author.
     * @param author Name of the author to search.
     * @return List of none or more books matching the author.
     */
    List<Book> findByAuthor(String author);

    /**
     * Search the store by authors.
     * @param authors List of author names to search.
     * @return List of none or more books matching the author.
     */
    List<Book> findByAuthor(List<String> authors);

    /**
     * Search the store by isbn.
     * @param isbn ISBN to search.
     * @return The book or null.
     */
    Optional<Book> findByISBN(long isbn);

    /**
     * Search the store by author and title.
     * @param title Title of the book to search.
     * @param author Name of the author to search.
     * @return List of none or more books matching the author.
     */
    List<Book> findByTitleAndAuthor(String title, String author);

    /**
     * Search the store by authors and title.
     * @param title Title of the book to search.
     * @param author List of author names to search.
     * @return List of none or more books matching the author.
     */
    List<Book> findByTitleAndAuthor(String title, List<String> author);

    /**
     * Lists a page of existing books in the store.
     * @return List containing a page of books in the store.
     */
    List<Book> browse(int page);

    /**
     * Lists all existing books in the store.
     * @return List containing all the books in the store.
     */
    List<Book> listBooks();

    /**
     * Lists all books purchased by a client in the store.
     * @return List containing all the books purchased by the client in the store.
     */
    List<Book> getPurchased(int cid);
}