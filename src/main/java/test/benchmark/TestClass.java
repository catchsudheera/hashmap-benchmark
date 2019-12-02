package test.benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class TestClass {

    private Map<TestKey, Integer> map = new HashMap<>();
    private List<TestKey> keyList = new ArrayList<>();
    private int test1 = 0;
    private int test2 = 0;

    public TestClass() {
        Random random = new Random();
        for (int i = 0; i < 100_000; i++) {
            TestKey testKey = new TestKey(i);
            map.put(testKey, i);
            keyList.add(new TestKey(random.nextInt(100_001)));
        }
    }

    @Setup
    public void setup() {
        Random random = new Random();
        test1 = random.nextInt(100_001);
        test2 = random.nextInt(100_001);
    }

    @Benchmark
    @Fork(2)
    @BenchmarkMode(Mode.Throughput)
    @Measurement(iterations = 15, time = 10)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 6)
    public boolean benchmark_with_one_get() {

        TestKey testKey = keyList.get(test1);
        map.get(testKey);
        test1++;
        if (test1 >= 100_000) {
            test1 = 0;
        }
        return true;
    }

    @Benchmark
    @Fork(2)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Measurement(iterations = 15, time = 10)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 6)
    public boolean benchmark_with_ten_gets() {

        TestKey testKey = keyList.get(test2);
        map.get(testKey);
        map.get(testKey);
        map.get(testKey);
        map.get(testKey);
        map.get(testKey);
        map.get(testKey);
        map.get(testKey);
        map.get(testKey);
        map.get(testKey);
        map.get(testKey);

        test2++;
        if (test2 >= 100_000) {
            test2 = 0;
        }

        return true;
    }


    public class TestKey {
        private int key;

        public TestKey(int key) {
            this.key = key;
        }

        public int getKey() {
            return key;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof TestKey && ((TestKey) obj).key == this.key;
        }

        @Override
        public int hashCode() {
            return 31 * 17 + key;
        }
    }
}
