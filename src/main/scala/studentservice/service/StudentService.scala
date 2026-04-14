package studentservice.service

import cats.effect.IO
import studentservice.domain.{CreateStudentRequest, Student, StudentId, UpdateStudentRequest}

trait StudentService:
  def create(request: CreateStudentRequest): IO[Student]

  def findAll: IO[List[Student]]

  def findById(id: StudentId): IO[Option[Student]]

  def update(id: StudentId, request: UpdateStudentRequest): IO[Option[Student]]

  def delete(id: StudentId): IO[Boolean]
