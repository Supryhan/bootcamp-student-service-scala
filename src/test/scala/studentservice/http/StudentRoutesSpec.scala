package studentservice.http

import cats.effect.IO
import munit.CatsEffectSuite
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.implicits.uri
import org.http4s.{Method, Request, Status}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import studentservice.domain.{CreateStudentRequest, Student, StudentId, UpdateStudentRequest}
import studentservice.repository.InMemoryStudentRepository
import studentservice.service.DefaultStudentService

import java.util.UUID

class StudentRoutesSpec extends CatsEffectSuite:
  private given Logger[IO] = Slf4jLogger.getLogger[IO]

  private val aliceId = StudentId(UUID.fromString("00000000-0000-0000-0000-000000000001"))
  private val missingId = StudentId(UUID.fromString("00000000-0000-0000-0000-000000000002"))

  private val alice = Student(
    id = aliceId,
    firstName = "Alice",
    lastName = "Green",
    email = "alice.green@example.com"
  )

  test("POST /students creates a student"):
    val request = Request[IO](Method.POST, uri"/students")
      .withEntity(CreateStudentRequest("Alice", "Green", "alice.green@example.com"))

    for
      repository <- InMemoryStudentRepository.create
      response <- StudentRoutes
        .routes(DefaultStudentService(repository))
        .orNotFound
        .run(request)
      student <- response.as[Student]
    yield
      assertEquals(response.status, Status.Created)
      assertEquals(student.firstName, "Alice")
      assertEquals(student.lastName, "Green")
      assertEquals(student.email, "alice.green@example.com")

  test("GET /students returns stored students"):
    val request = Request[IO](Method.GET, uri"/students")

    for
      repository <- InMemoryStudentRepository.from(List(alice))
      response <- StudentRoutes
        .routes(DefaultStudentService(repository))
        .orNotFound
        .run(request)
      students <- response.as[List[Student]]
    yield
      assertEquals(response.status, Status.Ok)
      assertEquals(students, List(alice))

  test("GET /students/{id} returns a stored student"):
    val request = Request[IO](Method.GET, uri"/students/00000000-0000-0000-0000-000000000001")

    for
      repository <- InMemoryStudentRepository.from(List(alice))
      response <- StudentRoutes
        .routes(DefaultStudentService(repository))
        .orNotFound
        .run(request)
      student <- response.as[Student]
    yield
      assertEquals(response.status, Status.Ok)
      assertEquals(student, alice)

  test("PUT /students/{id} updates a stored student"):
    val request = Request[IO](Method.PUT, uri"/students/00000000-0000-0000-0000-000000000001")
      .withEntity(UpdateStudentRequest("Alicia", "Green", "alicia.green@example.com"))

    for
      repository <- InMemoryStudentRepository.from(List(alice))
      response <- StudentRoutes
        .routes(DefaultStudentService(repository))
        .orNotFound
        .run(request)
      student <- response.as[Student]
    yield
      assertEquals(response.status, Status.Ok)
      assertEquals(student.id, aliceId)
      assertEquals(student.firstName, "Alicia")
      assertEquals(student.email, "alicia.green@example.com")

  test("DELETE /students/{id} deletes a stored student"):
    val request = Request[IO](Method.DELETE, uri"/students/00000000-0000-0000-0000-000000000001")

    for
      repository <- InMemoryStudentRepository.from(List(alice))
      response <- StudentRoutes
        .routes(DefaultStudentService(repository))
        .orNotFound
        .run(request)
      found <- repository.findById(aliceId)
    yield
      assertEquals(response.status, Status.NoContent)
      assertEquals(found, None)

  test("GET /students/{id} returns NotFound for a missing student"):
    val request = Request[IO](Method.GET, uri"/students/00000000-0000-0000-0000-000000000002")

    for
      repository <- InMemoryStudentRepository.create
      response <- StudentRoutes
        .routes(DefaultStudentService(repository))
        .orNotFound
        .run(request)
    yield assertEquals(response.status, Status.NotFound)

  test("PUT /students/{id} returns NotFound for a missing student"):
    val request = Request[IO](Method.PUT, uri"/students/00000000-0000-0000-0000-000000000002")
      .withEntity(UpdateStudentRequest("Alicia", "Green", "alicia.green@example.com"))

    for
      repository <- InMemoryStudentRepository.create
      response <- StudentRoutes
        .routes(DefaultStudentService(repository))
        .orNotFound
        .run(request)
      found <- repository.findById(missingId)
    yield
      assertEquals(response.status, Status.NotFound)
      assertEquals(found, None)

  test("DELETE /students/{id} returns NotFound for a missing student"):
    val request = Request[IO](Method.DELETE, uri"/students/00000000-0000-0000-0000-000000000002")

    for
      repository <- InMemoryStudentRepository.create
      response <- StudentRoutes
        .routes(DefaultStudentService(repository))
        .orNotFound
        .run(request)
    yield assertEquals(response.status, Status.NotFound)
