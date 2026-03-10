import kotlin.test.Test
import kotlin.test.assertEquals

class AppTest {
    @Test
    fun testMainOutput() {
        // simple check - main prints something
        assertEquals(Unit, runCatching { main() }.getOrNull())
    }
}