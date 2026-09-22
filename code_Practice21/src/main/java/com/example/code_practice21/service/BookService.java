package com.example.code_practice21.service;

import com.example.code_practice21.entity.BookEntity;
import com.example.code_practice21.exception.BookNotFoundException;
import com.example.code_practice21.exception.InvalidBookDataException;
import com.example.code_practice21.repository.BookRepository;
import com.example.code_practice21.model.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        List<BookEntity> bookEntities = bookRepository.findAll();

        if (bookEntities.isEmpty()) {
            throw new BookNotFoundException("There are no books in the database.");
        }

        return bookEntities.stream().map(
            bookEntity ->
                    new Book(
                        bookEntity.getId(),
                            bookEntity.getTitle(),
                            bookEntity.getAuthor(),
                            bookEntity.getGenre(),
                            bookEntity.getStock(),
                            bookEntity.getPrice()
                    )
        ).toList();
    }

    public Book findById(Long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElse(null);
        if (bookEntity == null) {
            throw new BookNotFoundException("Book with id: " + id + " was not found.");
        }
        return new Book(bookEntity.getId(), bookEntity.getTitle(),  bookEntity.getAuthor(), bookEntity.getGenre(), bookEntity.getStock(), bookEntity.getPrice());
    }

    public List<Book> searchByGenre(String genre) {

        if (genre == null || genre.isBlank()) {
            throw new InvalidBookDataException("Genre is required");
        }

        List<BookEntity> books = bookRepository.findAll()
                .stream()
                .filter(book -> book.getGenre().equalsIgnoreCase(genre))
                .toList();

        if (books.isEmpty()) {
            throw new BookNotFoundException("No books found for genre: " + genre);
        }

        return books.stream().map(
                        bookEntity ->
                        new Book(bookEntity.getId(),
                                bookEntity.getTitle(),
                                bookEntity.getAuthor(),
                                bookEntity.getGenre(),
                                bookEntity.getStock(),
                                bookEntity.getPrice())

                ).toList();
    }


    public Book save(Book book) {

        validateBook(book);

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(book.getId());
        bookEntity.setTitle(book.getTitle());
        bookEntity.setAuthor(book.getAuthor());
        bookEntity.setGenre(book.getGenre());
        bookEntity.setStock(book.getStock());
        bookEntity.setPrice(book.getPrice());
        bookRepository.save(bookEntity);

        return new Book(bookEntity.getId(), bookEntity.getTitle(), bookEntity.getAuthor(), bookEntity.getGenre(), bookEntity.getStock(), bookEntity.getPrice());
    }

    public Book update(Long id, Book updatedBook) {

        validateBook(updatedBook);

        BookEntity existingBook = bookRepository.findById(id).orElse(null);

        if (existingBook == null) {
            throw new BookNotFoundException("Book not found");
        }

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setGenre(updatedBook.getGenre());
        existingBook.setStock(updatedBook.getStock());
        existingBook.setPrice(updatedBook.getPrice());

        bookRepository.save(existingBook);

        return new Book(existingBook.getId(), existingBook.getTitle(), existingBook.getAuthor(), existingBook.getGenre(), existingBook.getStock(), existingBook.getPrice());
    }

    public Book patch(Long id, Book updatedBook) {

        validateBook(updatedBook);

        BookEntity existingBook = bookRepository.findById(id).orElse(null);

        if (existingBook == null) {
            throw new BookNotFoundException("Book not found");
        }

        if (updatedBook.getTitle() != null) {
            existingBook.setTitle(updatedBook.getTitle());
        }

        if (updatedBook.getAuthor() != null) {
            existingBook.setAuthor(updatedBook.getAuthor());
        }

        if (updatedBook.getGenre() != null) {
            existingBook.setGenre(updatedBook.getGenre());
        }

        if (updatedBook.getStock() != null) {
            existingBook.setStock(updatedBook.getStock());
        }

        if (updatedBook.getPrice() != null) {
            existingBook.setPrice(updatedBook.getPrice());
        }

        bookRepository.save(existingBook);
        return new Book(existingBook.getId(),
                existingBook.getTitle(),
                existingBook.getAuthor(),
                existingBook.getGenre(),
                existingBook.getStock(),
                existingBook.getPrice()
        );
    }

    public void deleteById(Long id) {

        Optional<BookEntity> book = bookRepository.findById(id);

        if (book.isEmpty()) {
            throw new BookNotFoundException("Book not found");
        }

        bookRepository.deleteById(id);
    }

    private void validateBook(Book book) {

        if (book.getPrice() != null && book.getPrice().signum() < 0) {
            throw new InvalidBookDataException("Price cannot be negative");
        }

        if (book.getStock() != null && book.getStock() < 0) {
            throw new InvalidBookDataException(
                    "Stock cannot be negative"
            );
        }
    }
}
