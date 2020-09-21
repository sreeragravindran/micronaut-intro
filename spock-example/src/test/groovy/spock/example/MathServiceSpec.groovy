package spock.example

import io.micronaut.test.annotation.MicronautTest
import spock.example.MathService
import spock.lang.Specification
import spock.lang.Unroll

import javax.inject.Inject

@MicronautTest
class MathServiceSpec extends Specification {

    @Inject
    MathService mathService

    @Unroll
    void "should compute #num times 4"() {
        when:
        def result = mathService.compute(num)

        then:
        result == expected

        where:
        num | expected
        2   | 8
        3   | 12
    }
}
