package com.library.service;

import com.library.domain.Book;
import com.library.domain.BookData;
import com.library.domain.SearchBookList;
import com.library.repository.BookRepository;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RestTemplate restTemplate;

    public BookService(RestTemplateBuilder restTemplateBuilder) {

        this.restTemplate = restTemplateBuilder.build();
    }

    public Book uploadBook(MultipartFile file, String author) {

        try {

            String rootPath = System.getProperty("user.home");
            String destination = rootPath +"/books/"+ file.getOriginalFilename();

            File bookFile = new File(destination);
            file.transferTo(bookFile);

            Book book = new Book();
            BookData bookData = new BookData();

            book.setAuthor(author);
            book.setAvailable(true);
            book.setName(FilenameUtils.removeExtension(file.getOriginalFilename()));
            book.setType(file.getContentType());
            book.setUri(destination);

            bookRepository.save(book);

            PDDocument document = Loader.loadPDF(bookFile);

            bookData.setContent(new PDFTextStripper().getText(document));
            bookData.setBookId(book.getId());
            URI url = new URI("http://localhost:8082/library/book/save");

            ResponseEntity<BookData> responseEntity = restTemplate.postForEntity(url, bookData, BookData.class);

            return book;
        }
        catch (Exception e){
            return null;
        }
    }

    public Book updateBook(Long bookId, MultipartFile file, String author){

        try {
            Optional<Book> book = bookRepository.findById(bookId);
            if(book.isPresent() && !book.get().getAvailable())
                return null;

            if(!author.equals("")) {
                book.get().setAuthor(author);
                bookRepository.save(book.get());
            }

            if(file!=null){

                String rootPath = System.getProperty("user.home");
                String destination = rootPath +"/books/"+ file.getOriginalFilename();

                File bookFile = new File(destination);
                file.transferTo(bookFile);
                book.get().setUri(destination);
                book.get().setName(file.getOriginalFilename());
                bookRepository.save(book.get());

                BookData bookData = new BookData();
                PDDocument doc = Loader.loadPDF(bookFile);

                bookData.setContent(new PDFTextStripper().getText(doc));
                bookData.setBookId(book.get().getId());

                URI url = new URI("http://localhost:8082/library/book/update");

                restTemplate.postForEntity(url,bookData,BookData.class);
            }
            return book.get();

        }
        catch (Exception e)
        {
            return null;
        }
    }

    public Boolean removeBook(Long bookId){

        try{
            bookRepository.deleteById(bookId);
            String uri = "http://localhost:8082/library/book/remove/"+bookId;
            restTemplate.delete(uri);
            return  true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public List<Book> getAllBooks(){
        try {
            return bookRepository.findAll();
        }
        catch (Exception e) {
            return null;
        }
    }

    public Book getBookById(Long id){

        try{
            return bookRepository.findById(id).get();
        }
        catch(Exception e){
            return null;
        }
    }

    public List<Book> searchBooks(String searchQuery) throws URISyntaxException {

        try {
            String url = "http://localhost:8082/library/book/search/" + searchQuery;

            ResponseEntity<SearchBookList> responseEntity = restTemplate.getForEntity(url, SearchBookList.class);

            List<BookData> bookData = responseEntity.getBody().getBookDataList();

            List<Book> books = new ArrayList<>();

            for (BookData book : bookData) {
                Long bookId = book.getBookId();
                Optional<Book> optionalBook = bookRepository.findById(bookId);
                if (optionalBook.isPresent())
                    books.add(optionalBook.get());
            }
            return books;
        }
        catch(Exception e){
            return null;
        }
    }
}
