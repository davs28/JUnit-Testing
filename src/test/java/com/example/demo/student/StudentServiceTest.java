package com.example.demo.student;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository studentRepository;
  private StudentService underTest;

  @BeforeEach
  void setUp() {
    underTest = new StudentService(studentRepository);
  }


  @Test
  void canGetAllStudents() {
    // when
    underTest.getAllStudents();
    // then
    verify(studentRepository).findAll();
  }

  @Test
  void canAddStudent() {
    // given
    Student student = new Student(
        "Jamila",
        "jamila@gmail.com",
        Gender.FEMALE
    );
    // when
    underTest.addStudent(student);

    // then
    ArgumentCaptor<Student> studentArgumentCaptor =
        ArgumentCaptor.forClass(Student.class);

    verify(studentRepository)
        .save(studentArgumentCaptor.capture());

    Student capturedStudent = studentArgumentCaptor.getValue();

    assertThat(capturedStudent).isEqualTo(student);
  }

  @Test
  void willThrowWhenEmailIsTaken() {
    // given
    Student student = new Student(
        "Jamila",
        "jamila@gmail.com",
        Gender.FEMALE
    );

    given(studentRepository.selectExistsEmail(anyString()))
        .willReturn(true);
    // when
    // then
    assertThatThrownBy(() -> underTest.addStudent(student))
        .isInstanceOf(BadRequestException.class)
        .hasMessageContaining("Email " + student.getEmail() + " taken");

    verify(studentRepository, never()).save(any());
  }

  @Test
  void canDeleteStudent() {
    // given
    long studentId = 4096;
    given(studentRepository.existsById(studentId))
        .willReturn(true);

    // when
    underTest.deleteStudent(studentId);

    // then
    verify(studentRepository).deleteById(studentId);
  }

  @Test
  void willThrowWhenStudentIsNotFound() {
    // given
    long studentId = 4096;
    given(studentRepository.existsById(studentId))
        .willReturn(false);

    // when
    // then
    assertThatThrownBy(() -> underTest.deleteStudent(studentId))
        .isInstanceOf(StudentNotFoundException.class)
        .hasMessageContaining("Student with id " + studentId + " does not exists");

    verify(studentRepository, never()).deleteById(any());
  }
}