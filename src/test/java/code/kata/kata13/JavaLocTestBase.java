package code.kata.kata13;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public abstract class JavaLocTestBase {

    protected abstract JavaLoc loc();

    @Test
    void test1() {
        var java = """
                public interface Dave {
                }
                """;
        assertThat(loc().count(java), is(2));
    }

    @Test
    void test2() {
        var java = """
                \t
                public interface Dave {
                \t
                }
                \t
                """;
        assertThat(loc().count(java), is(2));
    }

    @Test
    void test3() {
        var java = """
                public interface Dave { // hello
                                        // world
                                        //
                }
                """;
        assertThat(loc().count(java), is(2));
    }

    @Test
    void test4() {
        var java = """
                /*
                  test
                */
                public interface Dave {
                /*/
                  test2
                  /*
                */
                     // /*
                }
                """;
        assertThat(loc().count(java), is(2));
    }

    @Test
    void test5() {
        var java = """
                void test() {
                   foo("/*");
                   bar(""/*);
                   ----
                   */);
                   baz("\\"/*");
                }
                """;
        assertThat(loc().count(java), is(6));
    }

    @Test
    void example1() {
        var java = """
                // This file contains 3 lines of code
                public interface Dave {
                    /**
                     * count the number of lines in a file
                     */
                    int countLines(File inFile); // not the real signature!
                }
                """;
        assertThat(loc().count(java), is(3));
    }

    @Test
    void example2() {
        var java = """
                /*****
                 * This is a test program with 5 lines of code
                 *  \\/* no nesting allowed!
                 //*****//***/// Slightly pathological comment ending...
                
                public class Hello {
                    public static final void main(String [] args) { // gotta love Java
                        // Say hello
                      System./*wait*/out./*for*/println/*it*/("Hello/*");
                    }
                
                }
                """;
        \u002f\u002f this is a comment
        assertThat(loc().count(java), is(5));
    }
}
