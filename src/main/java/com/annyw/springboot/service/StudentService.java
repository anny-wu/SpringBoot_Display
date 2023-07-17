package com.annyw.springboot.service;

import com.annyw.springboot.bean.Student;
import com.annyw.springboot.repo.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {
    @Autowired
    StudentRepository studentRepository;
    
    public boolean studentExists(String username){
        return studentRepository.findByUsername(username) != null;
    }
    //Get the total number of students
    public long getCount(){
        return studentRepository.count();
    }
    
    //Get field names from Student class as a list
    public List<String> getFieldName(String className)
        throws ClassNotFoundException {
        List<String> fieldName = new ArrayList<>();
        Class<?> cls;
        cls = Class.forName("com.annyw.springboot.bean.Student");
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            fieldName.add(field.getName().toUpperCase());
        }
        return fieldName;
    }
    
    //Get user details for display
    public List<List<Object>> getStudentList()
        throws ClassNotFoundException, IllegalAccessException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        List<Student> students = studentRepository.findAll();
        Class<?> cls;
        cls = Class.forName("com.annyw.springboot.bean.Student");
        //Get users from database
        Field[] fields = cls.getDeclaredFields();
        
        List<List<Object>> result = new ArrayList<>();
        for (Student s : students) {
            List<Object> temp = new ArrayList<>();
            for (Field field : fields) {
                field.setAccessible(true);
                Object obj = field.get(s);
                
                if (obj.getClass().getName().equals("java.sql.Timestamp")) {
                    temp.add(sdf.format(obj));
                }
                else {
                    temp.add(obj);
                }
            }
            result.add(temp);
        }
        return result;
    }
    
    public Page<Student> getStudentListByPage(int currentPage, int pageSize){
        Pageable paging = PageRequest.of(currentPage,pageSize);
        Page<Student> pageResult = studentRepository.findAll(paging);
        return pageResult;
    }
    //Get user details for display in pages
    public List<List<Object>> queryStudentsByPage(int currentPage, int pageSize)
        throws ClassNotFoundException, IllegalAccessException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        Page<Student> pageResult = getStudentListByPage(currentPage-1, pageSize);
        List<Student> students = pageResult.toList();
        Class<?> cls;
        cls = Class.forName("com.annyw.springboot.bean.Student");
        //Get users from database
        Field[] fields = cls.getDeclaredFields();
        
        List<List<Object>> result = new ArrayList<>();
        for (Student s : students) {
            List<Object> temp = new ArrayList<>();
            for (Field field : fields) {
                field.setAccessible(true);
                Object obj = field.get(s);
                
                if (obj.getClass().getName().equals("java.sql.Timestamp")) {
                    temp.add(sdf.format(obj));
                }
                else {
                    temp.add(obj);
                }
            }
            result.add(temp);
        }
        return result;
    }
    
    public void addStudent(Student student){
        studentRepository.save(student);
    }
    
    public void deleteStudent(Long id){
        studentRepository.deleteById(id);
    }
    
    public void editStudent(Student student){
        studentRepository.save(student);
    }
}

