package studentservice.repository

import cats.effect.IO
import studentservice.domain.{Student, StudentId}

trait StudentRepository:
  def create(student: Student): IO[Student]

  def findAll: IO[List[Student]]

  def findById(id: StudentId): IO[Option[Student]]

  def update(student: Student): IO[Option[Student]]

  def delete(id: StudentId): IO[Boolean]
