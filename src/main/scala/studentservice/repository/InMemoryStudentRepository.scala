package studentservice.repository

import cats.effect.{IO, Ref}
import studentservice.domain.{Student, StudentId}

final class InMemoryStudentRepository private (
    studentsRef: Ref[IO, Map[StudentId, Student]]
) extends StudentRepository:
  override def create(student: Student): IO[Student] =
    studentsRef.update(_.updated(student.id, student)).as(student)

  override def findAll: IO[List[Student]] =
    studentsRef.get.map(_.values.toList)

  override def findById(id: StudentId): IO[Option[Student]] =
    studentsRef.get.map(_.get(id))

  override def update(student: Student): IO[Option[Student]] =
    studentsRef.modify: students =>
      students.get(student.id) match
        case Some(_) => (students.updated(student.id, student), Some(student))
        case None    => (students, None)

  override def delete(id: StudentId): IO[Boolean] =
    studentsRef.modify: students =>
      val studentExists = students.contains(id)
      (students.removed(id), studentExists)

object InMemoryStudentRepository:
  def create: IO[InMemoryStudentRepository] =
    Ref.of[IO, Map[StudentId, Student]](Map.empty).map(new InMemoryStudentRepository(_))

  def from(students: List[Student]): IO[InMemoryStudentRepository] =
    Ref
      .of[IO, Map[StudentId, Student]](students.map(student => student.id -> student).toMap)
      .map(new InMemoryStudentRepository(_))
