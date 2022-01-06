package com.library.controller;

import com.library.domain.Book;
import com.library.domain.Issuance;
import com.library.service.BookService;
import com.library.service.IssuanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins="http://localhost:8081")
@RestController
@RequestMapping("/library")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private IssuanceService issuanceService;

    @PostMapping("/book/upload/{author}")
    public ResponseEntity<Book> bookUpload(@RequestParam MultipartFile file,@PathVariable String author) {

        Book book = bookService.uploadBook(file,author);

        if(book==null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(book,HttpStatus.CREATED);

    }

    @GetMapping("/book/search/{searchQuery}")
    public ResponseEntity<List<Book>> bookSearch(@PathVariable String searchQuery) throws URISyntaxException {

        List<Book> book = bookService.searchBooks(searchQuery);

        if(book==null || book.isEmpty())
            return new ResponseEntity<>(Collections.emptyList(),HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(book,HttpStatus.OK);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable(name="id") Long bookId){

        Book book = bookService.getBookById(bookId);

        if(book==null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @GetMapping("/book")
    public ResponseEntity<List<Book>> getAllBooks(){

        List<Book> books = bookService.getAllBooks();

        if(books == null || books.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<String> removeBook(@PathVariable Long id) {

        Boolean deleted = bookService.removeBook(id);

        if(deleted)
            return new ResponseEntity<>("Book Deleted", HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/book/update/{id}/{author}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @PathVariable(name="author", required = false) String author,
                                           @RequestParam(required = false) MultipartFile file)
    {
        Book updatedBook = bookService.updateBook(id,file,author);

        if(updatedBook==null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    @GetMapping("/book/{id}/issuance")
    public ResponseEntity<Issuance> issueDetails(@PathVariable Long id){

        Issuance issuance = issuanceService.issueDetails(id);

        if(issuance==null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(issuance,HttpStatus.OK);
    }
}
