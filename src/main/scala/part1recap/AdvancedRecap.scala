package part1recap

import scala.concurrent.Future

object AdvancedRecap extends App {

  // partials functions
  val partialsFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 65
    case 5 => 999
  }

  // the same
  val pf = (x: Int) => x match{
    case 1 => 42
    case 2 => 65
    case 5 => 999
  }

  val function: (Int => Int) = partialsFunction

  val modifiedList = List(1,2,3).map({
    case 1 => 42
    case _ => 0
  })

  //lifting
  val lifted = partialsFunction.lift //total function Int => Option[Int]
  lifted(2) //Some(65)
  lifted(2000) //None

  val pfChain = partialsFunction.orElse[Int, Int]{
    case 60 => 9000
  }

  pfChain(5) // 999 per partialsFunction
  pfChain(60) // 9000
//  pfChain(457) // throw a MatchError

  //type aliases
  type ReceiveFunction = PartialFunction[Any, Unit]
  def receive: ReceiveFunction = {
    case 1 => println("hello")
    case _ => println ("Confused")
  }

  //implicits
  implicit val timeout = 3000
  def setTimeout(f: () => Unit)(implicit timeout: Int) = f()
  setTimeout(() => println("timeout"))

  // implicit conversations
  // 1) implicit defs
  case class Person(name: String){
    def greet = s"Hi, my name is $name"
  }

  implicit def fromStringToPerson(string: String): Person = Person(string)
  "Peter".greet // compile to fromStringToPerson("Peter").greet

  // 2) implicit classes
  implicit class Dog(name: String){
    def bark = println("bark!")
  }
  "Lassie".bark // compile to new Dog("Lessie").bark

  // organize
  //local scope
  implicit val inverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  List(1,2,3).sorted // List(3,2,1)

  //imported scope
//  val future =  Future {
//    println("hello, future")
//  } (...) // here is required implicit argument
  import scala.concurrent.ExecutionContext.Implicits.global
  val future =  Future {
    println("hello, future")
  }

  // companion objects of the types included in the call
  object Person {
    implicit val personOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  List(Person("Bob"), Person("Alice")).sorted
  // List(Person("Alice"), Person("Bob"))



}
