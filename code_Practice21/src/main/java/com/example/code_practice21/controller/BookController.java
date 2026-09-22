package com.example.code_practice21.controller;

import com.example.code_practice21.service.BookService;
import com.example.code_practice21.model.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // GET /api/books
    @GetMapping
    public List<Book> findAll() {
        return bookService.findAll();
    }

    // GET /api/books/{id}
    @GetMapping("/{id}")
    public Book findById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    // GET /api/books/search?genre=Fantasy
    @GetMapping("/search")
    public List<Book> searchBooks(
            @RequestParam(required = false) String genre) {

        return bookService.searchByGenre(genre);
    }

    // POST /api/books
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {

        Book savedBook = bookService.save(book);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedBook);
    }

    // PUT /api/books/{id}
    @PutMapping("/{id}")
    public Book update(
            @PathVariable Long id,
            @RequestBody Book book) {

        return bookService.update(id, book);
    }

    // PATCH /api/books/{id}
    @PatchMapping("/{id}")
    public Book patchBook(
            @PathVariable Long id,
            @RequestBody Book book) {

        return bookService.patch(id, book);
    }

    // DELETE /api/books/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {

        bookService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}