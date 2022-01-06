package com.library.controller;

import com.library.domain.Issuance;
import com.library.domain.Student;
import com.library.service.IssuanceService;
import com.library.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins="http://localhost:8081")
@RestController
@RequestMapping("/library")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private IssuanceService issuanceService;

    @PostMapping("/student")
    public ResponseEntity<Student> addStudent(@RequestBody Student student){

        Student addedStudent = studentService.addStudent(student);

        if(addedStudent==null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(addedStudent, HttpStatus.CREATED);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable(name="id") long studentId){
        Student student = studentService.getStudentById(studentId);

        if(student==null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping("/student")
    public ResponseEntity<List<Student>> getAllStudents(){
        List<Student> students = studentService.getAllStudents();

        if (students==null || students.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @DeleteMapping("/student/{id}")
    public ResponseEntity<String> removeStudent(@PathVariable(name="id") Long studentId) {
        Boolean deleted = studentService.removeStudent(studentId);

        if(!deleted)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>("Student Deleted", HttpStatus.OK);
    }

    @PutMapping("/student/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable(name="id") Long studentId,
                                                 @RequestBody Map<String, String> update){

        Student updatedStudent = studentService.updateStudent(studentId, update);

        if(updatedStudent==null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }

    @PutMapping("/student/{id}/issuance")
    public ResponseEntity<Issuance> issueBook(@PathVariable(name="id") Long studentId,
                                              @RequestBody Map<String,Long> issue){

        Issuance issued = issuanceService.issueBook(studentId,issue);

        if(issued==null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(issued, HttpStatus.OK);
    }

    @DeleteMapping("/student/{id}/issuance")
    public ResponseEntity<String> collectBook(@PathVariable(name="id") Long studentId,
                                              @RequestBody Map<String, Long> collect){

        Boolean collected = issuanceService.collectBook(studentId,collect);

        if(!collected)
            return new ResponseEntity<>("Book could not be collected", HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>("Book collected", HttpStatus.OK);
    }

    @GetMapping("/student/{id}/issuance")
    public ResponseEntity<List<Issuance>> bookDetails(@PathVariable(name="id") Long studentId){

        List<Issuance> issuance = issuanceService.bookDetails(studentId);

        if(issuance == null || issuance.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(issuance,HttpStatus.OK);
    }
}
