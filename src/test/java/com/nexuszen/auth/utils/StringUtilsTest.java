package com.nexuszen.auth.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class StringUtilsTest {

  @Test
  void testNormalizeEmail() {
    assertEquals("test@example.com", StringUtils.normalizeEmail("  TEST@example.com  "));
    assertEquals("user.name@example.com", StringUtils.normalizeEmail("User.Name@Example.Com"));
    assertNull(StringUtils.normalizeEmail(null));
  }

  @Test
  void testNormalizeUsername() {
    assertEquals("cristulo", StringUtils.normalizeUsername("  Cristulo  "));
    assertEquals("admin", StringUtils.normalizeUsername("ADMIN"));
    assertNull(StringUtils.normalizeUsername(null));
  }
}
