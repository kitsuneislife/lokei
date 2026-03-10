import database.DatabaseFactory
import database.UserRepository

fun main() {
    // initialize a minimal in-memory database
    DatabaseFactory.init()

    // demo operations
    val alice = UserRepository.add("Alice")
    println("Inserted user: $alice")

    val users = UserRepository.all()
    println("All users: $users")
}