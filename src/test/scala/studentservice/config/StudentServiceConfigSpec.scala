package studentservice.config

import munit.CatsEffectSuite

class StudentServiceConfigSpec extends CatsEffectSuite:
  test("loads service HTTP settings from application.conf"):
    StudentServiceConfig.load.map: config =>
      assertEquals(config.version, "0.3.0-SNAPSHOT")
      assertEquals(config.http.host, "0.0.0.0")
      assertEquals(config.http.port, 8080)
