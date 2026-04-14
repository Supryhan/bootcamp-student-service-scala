package studentservice.http

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.dsl.io.*
import org.typelevel.log4cats.Logger
import studentservice.domain.{CreateStudentRequest, StudentId, UpdateStudentRequest}
import studentservice.service.StudentService

object StudentRoutes:
  def routes(studentService: StudentService)(using logger: Logger[IO]): HttpRoutes[IO] =
    HttpRoutes.of[IO]:
      case request @ POST -> Root / "students" =>
        for
          createStudentRequest <- request.as[CreateStudentRequest]
          student <- studentService.create(createStudentRequest)
          _ <- logger.info(s"Created student ${student.id.value}")
          response <- Created(student)
        yield response

      case GET -> Root / "students" =>
        for
          students <- studentService.findAll
          response <- Ok(students)
        yield response

      case GET -> Root / "students" / UUIDVar(studentUuid) =>
        studentService.findById(StudentId(studentUuid)).flatMap:
          case Some(student) => Ok(student)
          case None          => NotFound()

      case request @ PUT -> Root / "students" / UUIDVar(studentUuid) =>
        for
          updateStudentRequest <- request.as[UpdateStudentRequest]
          response <- studentService.update(StudentId(studentUuid), updateStudentRequest).flatMap:
            case Some(student) => Ok(student)
            case None          => NotFound()
        yield response

      case DELETE -> Root / "students" / UUIDVar(studentUuid) =>
        studentService.delete(StudentId(studentUuid)).flatMap:
          case true  => NoContent()
          case false => NotFound()
