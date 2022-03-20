package com.example.demo;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class DemoApplicationTests {

  Calculator underTest = new Calculator();

  @Test
  void itShouldAddTwoNumbers() {
    // given
    int numberOne = 20;
    int numberTwo = 30;

    // when
    int result = underTest.add(numberOne, numberTwo);

    // then
    int expected = 50;
    assertThat(result).isEqualTo(expected);
  }

  class Calculator {

    int add(int a, int b) {
      return a + b;
    }
  }
}