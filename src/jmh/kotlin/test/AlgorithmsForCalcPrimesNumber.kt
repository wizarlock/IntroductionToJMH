package test

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit
import kotlin.math.ceil
import kotlin.math.sqrt

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = ["-Xms2G", "-Xmx2G"])
@Warmup(iterations = 5)
@Measurement(iterations = 10)
open class AlgorithmsForCalcPrimesNumber {
    private val limit = 10000000

//Решение «в лоб»

    @Benchmark
    fun headOnSolution (bh: Blackhole) {
        if (limit <= 1) bh.consume(0)
        var primeCounter = 0
        var divisorCounter = 0
        for (i in 2..limit) {
            for (j in 1..i) {
                if (i % j == 0) divisorCounter++
            }
            if (divisorCounter == 2) primeCounter++
            divisorCounter = 0
        }
        bh.consume(primeCounter)
    }

//Решение с помощью алгоритма «Решето Эратосфена»

    @Benchmark
    fun eratosthenes(bh: Blackhole) {
        if (limit <= 1) bh.consume(0)
        val primes: Array<Boolean> = Array(limit + 1) { true }
        primes[0] = false
        primes[1] = false
        for (i in 2 until primes.size) {
            if (primes[i]) {
                var j = 2
                while (i * j < primes.size) {
                    if (primes[i * j]) primes[i * j] = false
                    j++
                }
            }
        }
        bh.consume(primes.slice(3..limit step 2).count { it }.let { it + 1 })
    }

//Улучшение для алгоритма «Решето Эратосфена»

    @Benchmark
    fun upEratosthenes(bh: Blackhole) {
        if (limit <= 1) bh.consume(0)
        val primes: Array<Boolean> = Array(limit + 1) { true }
        primes[0] = false
        primes[1] = false
        for (i in 2..ceil(sqrt(limit.toDouble())).toInt())
            if (primes[i])
                for (j in (i * i)..limit step i)
                    primes[j] = false
        bh.consume(primes.slice(3..limit step 2).count { it }.let { it + 1 })
    }

//Решение с помощью алгоритма «Решето Аткина»

    @Benchmark
    fun atkin(bh: Blackhole) {
        if (limit <= 1) bh.consume(0)
        if (limit == 2) bh.consume(1)
        var count = 2
        val prime = BooleanArray(limit + 1) { false }
        var x = 1
        while (x * x < limit) {
            var y = 1
            while (y * y < limit) {
                var n = (4 * x * x) + (y * y)
                if (n <= limit && (n % 12 == 1 || n % 12 == 5))
                    prime[n] = !prime[n]
                n = (3 * x * x) + (y * y)
                if (n <= limit && n % 12 == 7)
                    prime[n] = !prime[n]
                n = (3 * x * x) - (y * y)
                if (x > y && n <= limit && n % 12 == 11)
                    prime[n] = !prime[n]
                y++
            }
            x++
        }
        var r = 5
        while (r * r < limit) {
            if (prime[r]) {
                var i = r * r
                while (i < limit) {
                    prime[i] = false
                    i += r * r
                }
            }
            r++
        }
        for (a in 5..limit) if (prime[a]) count++
        bh.consume(count)
    }
}

