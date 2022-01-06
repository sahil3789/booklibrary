package com.library.service;

import com.library.domain.Book;
import com.library.domain.Issuance;
import com.library.domain.Student;
import com.library.repository.BookRepository;
import com.library.repository.IssuanceRepository;
import com.library.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IssuanceService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private IssuanceRepository issuanceRepository;
    @Autowired
    private BookRepository bookRepository;

    public Issuance issueBook(Long studentId, Map<String,Long> issue){

        try {
            Book book = bookRepository.findByIdAndAvailable(issue.get("bookId"), true);
            Student student = studentRepository.getById(studentId);

            if (book == null)
                return null;

            else {
                Issuance issuance = new Issuance();
                issuance.setBookId(book.getId());
                issuance.setBookName(book.getName());
                issuance.setAuthorName(book.getAuthor());
                issuance.setIssueDate(new Date());
                issuance.setStudentUsn(student.getUsn());
                issuanceRepository.save(issuance);

                book.setAvailable(false);
                book.setIssuanceId(issuance.getId());

                student.setBooksCount(student.getBooksCount() + 1);

                bookRepository.save(book);
                studentRepository.save(student);
                return issuance;
            }
        }catch (Exception e){
            return null;
        }
    }


    public Boolean collectBook(Long studentId, Map<String,Long> collect){

        try{
        Book book = bookRepository.getById(collect.get("bookId"));

        if(book!=null)
        {
            Student student = studentRepository.getById(studentId);
            issuanceRepository.deleteById(book.getIssuanceId());

            book.setAvailable(true);
            book.setIssuanceId(null);

            student.setBooksCount(student.getBooksCount()-1);

            bookRepository.save(book);
            studentRepository.save(student);

            return true;
        }
            return false;
        }
        catch (Exception e){
            return null;
        }
    }

    public Issuance issueDetails(Long bookId){

        try {
            return issuanceRepository.getByBookId(bookId);

        }catch (Exception e){
            return null;}
    }

    public List<Issuance> bookDetails(Long studentId) {

        try {
            Student student = studentRepository.getById(studentId);

            if (student == null) {
                return Collections.emptyList();
            } else {
                return issuanceRepository.findByStudentUsn(student.getUsn());
            }
        }
        catch (Exception e){
            return null;
        }

    }

}
