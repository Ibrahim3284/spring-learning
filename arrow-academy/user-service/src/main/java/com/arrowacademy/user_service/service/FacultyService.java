package com.arrowacademy.user_service.service;

import com.arrowacademy.user_service.dao.FacultyDao;
import com.arrowacademy.user_service.dao.StudentDao;
import com.arrowacademy.user_service.feign.AuthInterface;
import com.arrowacademy.user_service.model.Faculty;
import com.arrowacademy.user_service.model.Student;
import com.arrowacademy.user_service.model.User;
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
public class FacultyService {

    @Autowired
    private FacultyDao facultyDao;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthInterface authInterface;

    public ResponseEntity<String> addFaculty(String token, Faculty faculty) {

        if(jwtService.parseTokenAsJSON(token).get("role").equals("admin")) {
            Optional<Faculty> faculties = facultyDao.findByEmail(faculty.getEmail());

            if (faculties.isPresent()) return new ResponseEntity<>("Faculty already exist", HttpStatus.BAD_REQUEST);
            else {
                try {
                    LocalDate date = LocalDate.parse(faculty.getDateOfBirth().toString());
                } catch (Exception e) {
                    return new ResponseEntity<>("Date of Birth format is incorrect. Please pass in yyyy-MM-dd format", HttpStatus.BAD_REQUEST);
                }
                try {
                    LocalDate date = LocalDate.parse(faculty.getDateOfJoining().toString());
                } catch (Exception e) {
                    return new ResponseEntity<>("Date of joining format is incorrect. Please pass in yyyy-MM-dd format", HttpStatus.BAD_REQUEST);
                }
                User user = new User();
                user.setUsername(faculty.getEmail());
                user.setRole("faculty");
                authInterface.adminRegister(user);

                facultyDao.save(faculty);
                return new ResponseEntity<>("Faculty added successfully", HttpStatus.CREATED);
            }
        } else {
            return new ResponseEntity<>("Faculties can be added by Admin", HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<String> makeFacultyInactive(String token, Integer id) {
        if(jwtService.parseTokenAsJSON(token).get("role").equals("admin")) {
            Optional<Faculty> faculty = facultyDao.findById(id);
            Faculty faculty1 = new Faculty();
            if(faculty.isPresent()) {
                faculty1 = faculty.get();
                faculty1.setActive(false);
                facultyDao.save(faculty1);
                return new ResponseEntity<>("Faculty with id: " + id + " marked inactive", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Faculty not found with id: " + id, HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Faculties can be updated by Admin", HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<?> updateFaculty(String token, Integer id, Faculty faculty) {
        if(jwtService.parseTokenAsJSON(token).get("role").equals("admin")) {
            Optional<Faculty> existingFaculty = facultyDao.findById(id);

            if (existingFaculty.isPresent()) {
                Faculty facultyToUpdate = existingFaculty.get();

                // ✅ Example: update fields from incoming student
                facultyToUpdate.setFirstName(faculty.getFirstName());
                facultyToUpdate.setLastName(faculty.getLastName());
                facultyToUpdate.setEmail(faculty.getEmail());
                facultyToUpdate.setPhoneNo(facultyToUpdate.getPhoneNo());
                facultyToUpdate.setQualification(faculty.getQualification());
                facultyToUpdate.setDepartment(faculty.getDepartment());
                facultyToUpdate.setGender(faculty.getGender());
                facultyToUpdate.setDateOfBirth(faculty.getDateOfBirth());
                facultyToUpdate.setLinkedInProfileURL(faculty.getLinkedInProfileURL());
                facultyToUpdate.setDateOfJoining(faculty.getDateOfJoining());
                facultyToUpdate.setActive(faculty.getActive());
                facultyDao.save(facultyToUpdate);
                return new ResponseEntity<>("Faculty with id " + id + " updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Faculty not found with id " + id, HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Faculties can be updated by Admin", HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<Page<Faculty>> getAllFaculties(String token, Integer pageSize, Integer pageNo) {
        if(jwtService.parseTokenAsJSON(token).get("role").equals("admin")) {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            return new ResponseEntity<>(facultyDao.findByPageParams(pageable), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<Faculty> getFacultyById(String token, Integer id) {
        if(jwtService.parseTokenAsJSON(token).get("role").equals("admin") || facultyDao.findByEmail(String.valueOf(jwtService.parseTokenAsJSON(token).get("sub"))).get().getId() == id) {
            Optional<Faculty> faculty = facultyDao.findById(id);
            Faculty faculty1 = new Faculty();
            if(faculty.isPresent()) {
                faculty1 = faculty.get();
                return new ResponseEntity<>(faculty1, HttpStatus.OK);
            }
            else return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<String> deleteFacultyById(String token, Integer id) {
        if(jwtService.parseTokenAsJSON(token).get("role").equals("admin")) {
            Optional<Faculty> faculty = facultyDao.findById(id);
            Faculty faculty1 = new Faculty();
            if (faculty.isPresent()) {
                faculty1 = faculty.get();
                facultyDao.deleteById(id);
                return new ResponseEntity<>("Faculty with id: " + id + " deleted successfully", HttpStatus.OK);
            } else return new ResponseEntity<>("Faculty not found with id: " + id, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>("Only Admins can delete faculty", HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<?> addMultipleFaculties(String token, List<Faculty> faculties) {
        return new ResponseEntity<>(facultyDao.saveAll(faculties), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getFacultyDetails(String token) {

        JSONObject parsedToken = jwtService.parseTokenAsJSON(token);
        String username = String.valueOf(parsedToken.get("sub"));

        Optional<Faculty> faculties = facultyDao.findByEmail(username);

        if(faculties.isPresent()) return new ResponseEntity<>(faculties.get(), HttpStatus.OK);
        else return new ResponseEntity<>("Faculty not present with email: " + username, HttpStatus.NOT_FOUND);
    }
}
