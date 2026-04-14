package studentservice.service

import cats.effect.IO
import studentservice.domain.{CreateStudentRequest, Student, StudentId, UpdateStudentRequest}
import studentservice.repository.StudentRepository

import java.util.UUID

final class DefaultStudentService private (repository: StudentRepository) extends StudentService:
  override def create(request: CreateStudentRequest): IO[Student] =
    IO.delay(StudentId(UUID.randomUUID()))
      .map: id =>
        Student(
          id = id,
          firstName = request.firstName,
          lastName = request.lastName,
          email = request.email
        )
      .flatMap(repository.create)

  override def findAll: IO[List[Student]] =
    repository.findAll

  override def findById(id: StudentId): IO[Option[Student]] =
    repository.findById(id)

  override def update(id: StudentId, request: UpdateStudentRequest): IO[Option[Student]] =
    repository.update(
      Student(
        id = id,
        firstName = request.firstName,
        lastName = request.lastName,
        email = request.email
      )
    )

  override def delete(id: StudentId): IO[Boolean] =
    repository.delete(id)

object DefaultStudentService:
  def apply(repository: StudentRepository): DefaultStudentService =
    new DefaultStudentService(repository)
