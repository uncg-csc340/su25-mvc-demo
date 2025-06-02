package com.csc340.crud_jpa_demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * StudentController is a REST controller that handles HTTP requests related to
 * students.
 * It provides endpoints for CRUD operations on student data.
 */
// @RestController
@Controller
public class StudentController {

  @Autowired
  private StudentService studentService;

  /**
   * Endpoint to get all students
   *
   * @return List of all students
   */
  @GetMapping("/students")
  public Object getAllStudents(Model model) {
    model.addAttribute("studentsList", studentService.getAllStudents());
    model.addAttribute("title", "All Students");
    return "students-list";
  }

  /**
   * Endpoint to get a student by ID
   *
   * @param id The ID of the student to retrieve
   * @return The student with the specified ID
   */
  @GetMapping("/students/{id}")
  public Object getStudentById(@PathVariable long id, Model model) {
    // return studentService.getStudentById(id);
    model.addAttribute("student", studentService.getStudentById(id));
    model.addAttribute("title", "Student #: " + id);
    return "student-details";
  }

  /**
   * Endpoint to get students by name
   *
   * @param name The name of the student to search for
   * @return List of students with the specified name
   */
  @GetMapping("/students/name")
  public Object getStudentsByName(@RequestParam String key) {
    if (key != null) {
      return studentService.getStudentsByName(key);
    } else {
      return studentService.getAllStudents();
    }

  }

  /**
   * Endpoint to get students by major
   *
   * @param major The major to search for
   * @return List of students with the specified major
   */
  @GetMapping("/students/major/{major}")
  public Object getStudentsByMajor(@PathVariable String major) {
    return studentService.getStudentsByMajor(major);
  }

  /**
   * Endpoint to get honors students with GPA above a specified threshold
   *
   * @param gpa The GPA threshold for honors students
   * @return List of honors students with GPA above the specified threshold
   */
  @GetMapping("/students/honors")
  public Object getHonorsStudents(@RequestParam(name = "gpa", defaultValue = "3.0") double gpa) {
    return new ResponseEntity<>(studentService.getHonorsStudents(gpa), HttpStatus.OK);

  }

  @GetMapping("/students/createForm")
  public Object showCreateForm(Model model) {
    Student student = new Student();
    model.addAttribute("student", student);
    model.addAttribute("title", "Create New Student");
    return "students-create";
  }

  /**
   * Endpoint to add a new student
   *
   * @param student The student to add
   * @return List of all students
   */
  @PostMapping("/students")
  public Object addStudent(Student student, @RequestParam MultipartFile picture, Model model) {
    // return studentService.addStudent(student, picture);
    Student newStudent = studentService.addStudent(student, picture);
    return "redirect:/students/" + newStudent.getStudentId();
  }

  /**
   * Endpoint to update a student
   *
   * @param id      The ID of the student to update
   * @param student The updated student information
   * @return The updated student
   */
  @PutMapping("/students/{id}")
  public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
    studentService.updateStudent(id, student);
    return studentService.getStudentById(id);
  }

  /**
   * Endpoint to delete a student
   *
   * @param id The ID of the student to delete
   * @return List of all students
   */
  @DeleteMapping("/students/{id}")
  public Object deleteStudent(@PathVariable Long id) {
    studentService.deleteStudent(id);
    return studentService.getAllStudents();
  }

  /**
   * Endpoint to write a student to a JSON file
   *
   * @param student The student to write
   * @return An empty string indicating success
   */
  @PostMapping("/students/writeFile")
  public Object writeJson(@RequestBody Student student) {
    studentService.writeJson(student);
    return studentService.writeJson(student);
  }

  /**
   * Endpoint to read a JSON file and return its contents
   *
   * @return The contents of the JSON file
   */
  @GetMapping("/students/readFile")
  public Object readJson() {
    return studentService.readJson();

  }

}
