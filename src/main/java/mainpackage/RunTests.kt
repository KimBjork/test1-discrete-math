package mainpackage

import com.marcinmoskala.math.tests.*
import com.marcinmoskala.math.tests.CombinationTest
import com.marcinmoskala.math.tests.CombinationWithRepetitionTest
import com.marcinmoskala.math.tests.FactorialTest
import com.marcinmoskala.math.tests.PermutationTest
import com.marcinmoskala.math.tests.PowerTest
import org.junit.runner.JUnitCore
import org.junit.runner.Result
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunListener
import java.io.File

class TestListener : RunListener() {
    lateinit var result : Result

    override fun testRunFinished(result: Result){
        this.result = result
    }
}

data class HandlerOutput(val startTime: Long,
                         val runtime: Long,
                         val coldStart: Boolean,
                         val wasSuccess: Boolean,
                         val failures: MutableList<Failure>)

class RunTests {
    fun getStringResult(startTime: Long) : String {
        val listener = run()
        return createString(startTime, listener.result)
    }
    fun getJsonResult(startTime: Long) : HandlerOutput {
        val listener = run()
        return createJson(startTime, listener.result)
    }

    private fun run() : TestListener{
        val junit = JUnitCore()
        val listener = TestListener()
        junit.addListener(listener)
        junit.run(CombinationTest::class.java,
                CombinationWithRepetitionTest::class.java,
                FactorialTest::class.java,
                PermutationTest::class.java,
                PowerTest::class.java,
                SublistsBySplittersTest::class.java,
                SetSplitTest::class.java,
                ProductTest::class.java,
                IterableMultipliationTest::class.java,
                NumbersDivisibleTest::class.java,
                PowersetTest::class.java,
                ProductTest::class.java)
        return listener
    }

    private fun createString(startTime : Long, result: Result):String {
        return "{\"startTime\": ${startTime}, \"runtime\": ${result.runTime}, \"coldStart\": ${isColdStart()}, \"wasSuccess\": ${result.wasSuccessful()}," +
                " \"failures\": ${result.failures}}"
    }

    private fun createJson(startTime: Long, result: Result) : HandlerOutput{
        return HandlerOutput(startTime, result.runTime, isColdStart(), result.wasSuccessful(), result.failures)
    }

    private fun isColdStart(): Boolean{
        val fileName = "/tmp/out.txt"
        val file = File(fileName)

        if(file.exists()){
            return false;
        }
        File(fileName).createNewFile()
        return true;
    }
}