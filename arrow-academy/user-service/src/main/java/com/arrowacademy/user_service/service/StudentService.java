package com.arrowacademy.user_service.service;

import com.arrowacademy.user_service.dao.StudentDao;
import com.arrowacademy.user_service.model.Faculty;
import com.arrowacademy.user_service.model.Student;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private JwtService jwtService;

    public ResponseEntity<String> addStudent(String token, Student student) {

        if(jwtService.parseTokenAsJSON(token).get("role").equals("admin")) {
            Optional<Student> students = studentDao.findByEmail(student.getEmail());

            if (students.isPresent()) return new ResponseEntity<>("Student already exist", HttpStatus.BAD_REQUEST);
            else {
                try {
                    LocalDate date = LocalDate.parse(student.getDateOfBirth().toString());
                } catch (Exception e) {
                    return new ResponseEntity<>("Date of Birth format is incorrect. Please pass in yyyy-MM-dd format", HttpStatus.BAD_REQUEST);
                }
                try {
                    LocalDate date = LocalDate.parse(student.getEnrollmentDate().toString());
                } catch (Exception e) {
                    return new ResponseEntity<>("Enrollment date format is incorrect. Please pass in yyyy-MM-dd format", HttpStatus.BAD_REQUEST);
                }
                studentDao.save(student);
                return new ResponseEntity<>("Student added successfully", HttpStatus.CREATED);
            }
        } else {
            return new ResponseEntity<>("Students can be added by Admin", HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<String> makeStudentInactive(String token, Integer id) {
        if(jwtService.parseTokenAsJSON(token).get("role").equals("admin")) {
            Optional<Student> student = studentDao.findById(id);
            Student student1 = new Student();
            if(student.isPresent()) {
                student1 = student.get();
                student1.setActive(false);
                studentDao.save(student1);
                return new ResponseEntity<>("Student with id: " + id + " marked inactive", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Student not found with id: " + id, HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Students can be updated by Admin", HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<?> updateStudent(String token, Integer id, Student student) {
        if(jwtService.parseTokenAsJSON(token).get("role").equals("admin")) {
            Optional<Student> existingStudent = studentDao.findById(id);

            if (existingStudent.isPresent()) {
                Student studentToUpdate = existingStudent.get();

                // ✅ Example: update fields from incoming student
                studentToUpdate.setFirstName(student.getFirstName());
                studentToUpdate.setLastName(student.getLastName());
                studentToUpdate.setEmail(student.getEmail());
                studentToUpdate.setPhoneNo(student.getPhoneNo());
                studentToUpdate.setSection(student.getSection());
                studentToUpdate.setGender(student.getGender());
                studentToUpdate.setDateOfBirth(student.getDateOfBirth());
                studentToUpdate.setYearOfStudy(student.getYearOfStudy());
                studentToUpdate.setEnrollmentDate(student.getEnrollmentDate());
                studentToUpdate.setActive(student.isActive());
                studentDao.save(studentToUpdate);
                return new ResponseEntity<>("Student with id " + id + " updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Student not found with id " + id, HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Students can be updated by Admin", HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<Page<Student>> getAllStudents(String token, Integer pageSize, Integer pageNo) {
        if(jwtService.parseTokenAsJSON(token).get("role").equals("admin")) {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            return new ResponseEntity<>(studentDao.findByPageParams(pageable), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<Student> getStudentById(String token, Integer id) {
        if(jwtService.parseTokenAsJSON(token).get("role").equals("admin") || studentDao.findByEmail(String.valueOf(jwtService.parseTokenAsJSON(token).get("sub"))).get().getId() == id) {
            Optional<Student> student = studentDao.findById(id);
            Student student1 = new Student();
            if(student.isPresent()) {
                student1 = student.get();
                return new ResponseEntity<>(student1, HttpStatus.OK);
            }
            else return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<String> deleteStudentById(String token, Integer id) {
        if(jwtService.parseTokenAsJSON(token).get("role").equals("admin")) {
            Optional<Student> student = studentDao.findById(id);
            Student student1 = new Student();
            if (student.isPresent()) {
                student1 = student.get();
                studentDao.deleteById(id);
                return new ResponseEntity<>("Student with id: " + id + " deleted successfully", HttpStatus.OK);
            } else return new ResponseEntity<>("Student not found with id: " + id, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>("Only Admins can delete student", HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<?> addMultipleStudents(String token, List<Student> students) {
        return new ResponseEntity<>(studentDao.saveAll(students), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getStudentDetails(String token) {

        JSONObject parsedToken = jwtService.parseTokenAsJSON(token);
        String username = String.valueOf(parsedToken.get("sub"));

        Optional<Student> students = studentDao.findByEmail(username);

        if(students.isPresent()) return new ResponseEntity<>(students.get(), HttpStatus.OK);
        else return new ResponseEntity<>("Student not present with email: " + username, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Integer> getStudentId(String token) {

        String email = String.valueOf(jwtService.parseTokenAsJSON(token).get("sub"));

        Optional<Student> studentDetail = studentDao.findByEmail(email);

        if(studentDetail.isPresent()) {
            int studentId;
            studentId = studentDetail.get().getId();
            return new ResponseEntity<>(studentId, HttpStatus.OK);
        } else return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
