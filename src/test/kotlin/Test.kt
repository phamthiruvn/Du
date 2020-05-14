import main.Du
import main.getSize
import main.unit
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DuTest {
    @Test
    fun unitTest() {
        assertEquals(unit(2577, 1024), "2 KB")
        assertEquals(unit(3335, 1024), "3 KB")
        assertEquals(unit(100, 1000), "100 B")
        assertEquals(unit(10_000_000_000, 1000), "10 GB")
        assertEquals(unit(1_111_000_000_000, 1000), "1111 GB")
    }

    @Test
    fun sizeTest() {
        assertEquals(getSize("0"), null)
        assertEquals(getSize("src/test/resources/Files/align_in1.txt"), 2577)
        assertEquals(getSize("src/test/resources/Files"), 3335)
    }

    @Test
    fun lauchTest() {
        fun command(command: String, h: Boolean, c: Boolean, si: Boolean) {
            val cmd = Du()
            cmd.sizeDu(command.split(" ").toTypedArray())
            assert(cmd.c == c && cmd.h == h && cmd.si == si)
        }
        command("src/test/resources/Files", false, false, false)
        command("-h src/test/resources/Files", true, false, false)
        command("-h src/test/file", true, false, false)
        command("-h", true, false, false)
        command(
            "-h -c --si src/test/resources/Files/align_in1.txt src/test/resources/Files/center_in1.txt",
            true,
            true,
            true
        )
        command(
            "-h --si src/test/resources/Files/align_in1.txt src/test/resources/Files/center_in1.txt",
            true,
            false,
            true
        )
        command("-h -c ab bc", true, true, false)
        command("-a", false, false, false)
    }

}
