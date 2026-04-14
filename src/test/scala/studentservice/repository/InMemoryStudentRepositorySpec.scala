package studentservice.repository

import munit.CatsEffectSuite
import studentservice.domain.{Student, StudentId}

import java.util.UUID

class InMemoryStudentRepositorySpec extends CatsEffectSuite:
  private val aliceId = StudentId(UUID.fromString("00000000-0000-0000-0000-000000000001"))
  private val bobId = StudentId(UUID.fromString("00000000-0000-0000-0000-000000000002"))
  private val missingId = StudentId(UUID.fromString("00000000-0000-0000-0000-000000000003"))

  private val alice = Student(
    id = aliceId,
    firstName = "Alice",
    lastName = "Green",
    email = "alice.green@example.com"
  )

  private val updatedAlice = alice.copy(
    firstName = "Alicia",
    email = "alicia.green@example.com"
  )

  private val bob = Student(
    id = bobId,
    firstName = "Bob",
    lastName = "Brown",
    email = "bob.brown@example.com"
  )

  test("creates and finds a student by id"):
    for
      repository <- InMemoryStudentRepository.create
      created <- repository.create(alice)
      found <- repository.findById(aliceId)
    yield
      assertEquals(created, alice)
      assertEquals(found, Some(alice))

  test("returns all stored students"):
    for
      repository <- InMemoryStudentRepository.from(List(alice, bob))
      students <- repository.findAll
    yield assertEquals(students.toSet, Set(alice, bob))

  test("updates an existing student"):
    for
      repository <- InMemoryStudentRepository.from(List(alice))
      updated <- repository.update(updatedAlice)
      found <- repository.findById(aliceId)
    yield
      assertEquals(updated, Some(updatedAlice))
      assertEquals(found, Some(updatedAlice))

  test("does not update a missing student"):
    for
      repository <- InMemoryStudentRepository.create
      updated <- repository.update(updatedAlice)
      students <- repository.findAll
    yield
      assertEquals(updated, None)
      assertEquals(students, List.empty)

  test("deletes an existing student"):
    for
      repository <- InMemoryStudentRepository.from(List(alice, bob))
      deleted <- repository.delete(aliceId)
      found <- repository.findById(aliceId)
      students <- repository.findAll
    yield
      assert(deleted)
      assertEquals(found, None)
      assertEquals(students, List(bob))

  test("does not delete a missing student"):
    for
      repository <- InMemoryStudentRepository.from(List(alice))
      deleted <- repository.delete(missingId)
      students <- repository.findAll
    yield
      assert(!deleted)
      assertEquals(students, List(alice))
