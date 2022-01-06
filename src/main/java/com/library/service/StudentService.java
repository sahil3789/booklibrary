package com.library.service;

import com.library.domain.Student;
import com.library.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student addStudent(Student student) {

        try{
            student.setRegisteredOn(new Date());
            student.setBooksCount(0);

            studentRepository.save(student);
            return student;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public List<Student> getAllStudents(){

        try{return studentRepository.findAll();}
        catch (Exception e){
            return null;
        }
    }

    public Student getStudentById(Long id){

        try{
        return studentRepository.findById(id).get();
        }catch (Exception e){
            return null;
        }
    }

    public Boolean removeStudent(Long studentId){
        try{
            studentRepository.deleteById(studentId);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public Student updateStudent(Long studentId, Map<String, String> update){

        try {
            Optional<Student> studentData = studentRepository.findById(studentId);
            Student studentUpdate = studentData.get();

            if(!update.get("name").equals(""))
                studentUpdate.setName(update.get("name"));
            if(!update.get("usn").equals(""))
                studentUpdate.setUsn(update.get("usn"));
            if(!update.get("dept").equals(""))
                studentUpdate.setDept(update.get("dept"));
            if(!update.get("email").equals(""))
                studentUpdate.setEmail(update.get("email"));

            studentRepository.save(studentUpdate);
            return studentUpdate;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
