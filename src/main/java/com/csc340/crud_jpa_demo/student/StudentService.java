package com.csc340.crud_jpa_demo.student;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * StudentService is a service class that handles the business logic for
 * managing students.
 * It provides methods to perform CRUD operations on student data.
 */
@Service
public class StudentService {

  @Autowired
  private StudentRepository studentRepository;

  private static final String UPLOAD_DIR = "src/main/resources/static/profile-pictures/";

  /**
   * Method to get all students
   *
   * @return List of all students
   */
  public Object getAllStudents() {
    return studentRepository.findAll();
  }

  /**
   * Method to get a student by ID
   *
   * @param studentId The ID of the student to retrieve
   * @return The student with the specified ID
   */
  public Student getStudentById(@PathVariable long studentId) {
    return studentRepository.findById(studentId).orElse(null);
  }

  /**
   * Method to get students by name
   *
   * @param name The name of the student to search for
   * @return List of students with the specified name
   */
  public Object getStudentsByName(String name) {
    return studentRepository.getStudentsByName(name);
  }

  /**
   * Method to get students by major
   *
   * @param major The major to search for
   * @return List of students with the specified major
   */
  public Object getStudentsByMajor(String major) {
    return studentRepository.getStudentsByMajor(major);
  }

  /**
   * Fetch all students with a GPA above a threshold.
   *
   * @param gpa the threshold
   * @return the list of matching Students
   */
  public Object getHonorsStudents(double gpa) {
    return studentRepository.getHonorsStudents(gpa);
  }

  /**
   * Method to add a new student
   *
   * @param student The student to add
   */
  public Student addStudent(Student student, MultipartFile profilePicture) {
    Student newStudent = studentRepository.save(student);
    String originalFileName = profilePicture.getOriginalFilename();

    try {
      if (originalFileName != null && originalFileName.contains(".")) {
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String fileName = String.valueOf(newStudent.getStudentId()) + "." + fileExtension;
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        InputStream inputStream = profilePicture.getInputStream();

        Files.createDirectories(Paths.get(UPLOAD_DIR));// Ensure directory exists
        Files.copy(inputStream, filePath,
            StandardCopyOption.REPLACE_EXISTING);

        // profilePicture.transferTo(filePath.toFile()); // Save file
        newStudent.setProfilePicturePath(fileName);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return studentRepository.save(newStudent);
  }

  /**
   * Method to update a student
   *
   * @param studentId The ID of the student to update
   * @param student   The updated student information
   */
  public Student updateStudent(Long studentId, Student student) {
    return studentRepository.save(student);
  }

  /**
   * Method to delete a student
   *
   * @param studentId The ID of the student to delete
   */
  public void deleteStudent(Long studentId) {
    studentRepository.deleteById(studentId);
  }

  /**
   * Method to write a student object to a JSON file
   *
   * @param student The student object to write
   */
  public String writeJson(Student student) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      objectMapper.writeValue(new File("students.json"), student);
      return "Student written to JSON file successfully";
    } catch (IOException e) {
      e.printStackTrace();
      return "Error writing student to JSON file";
    }

  }

  /**
   * Method to read a student object from a JSON file
   *
   * @return The student object read from the JSON file
   */
  public Object readJson() {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(new File("students.json"), Student.class);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

  }

}
