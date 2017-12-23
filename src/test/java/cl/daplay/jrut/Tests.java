package cl.daplay.jrut;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

public class Tests {

    @Test
    public void test_to_string() {
        final JRut first = new JRut("5252856-9");
        assertEquals("5.252.856-9", first.toString());
    }

    @Test
    public void test_to_string_new_instance() {
        final JRut first = new JRut("5252856-9");
        final JRut firstAgain = new JRut(first.toString());

        assertEquals(first, firstAgain);
    }

    @Test
    public void test_order() {
        final JRut first = new JRut("52528569");
        final JRut second = new JRut("17284675-0");

        // creates list in reverse
        final List<JRut> firstAndSecond = Arrays.asList(second, first);
        // this operation should make it right
        Collections.sort(firstAndSecond);

        final JRut max = Collections.max(firstAndSecond);
        final JRut min = Collections.min(firstAndSecond);

        assertEquals(second, max);
        assertEquals(first, min);
        assertEquals(second, firstAndSecond.get(1));
        assertEquals(first, firstAndSecond.get(0));
    }

    @Test
    public void valid_examples() {
        final List<String> examples = Arrays.asList(
                "123456785-k",
                "123456785-K",
                "123456785k",
                "123456785K",
                "16173148K",
                "16173148-K",
                "52528569",
                "5252856-9",
                "167093477",
                "172846750",
                "16709347-7",
                "17284675-0");

        for (final String example : examples) {
            assertValid(example);
        }
    }

    @Test
    public void invalid_examples() {
        final List<String> examples = Arrays.asList(
                "167093875",
                "this is not a Rut",
                "esto no es un Rut",
                "",
                "19",
                "1",
                "0-0",
                "1-9");

        for (final String example : examples) {
            assertInvalid(example);
        }
    }

    private void assertValid(final String input) {
        try {
            final JRut rut = new JRut(input);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            Assert.fail(format("input: '%s' was expected to successfully create a JRut instance", input));
        }
    }

    private void assertInvalid(final String input) {
        try {
            final JRut rut = new JRut(input);
            Assert.fail(format("input: '%s' was expected to fail to create a JRut instance, but it generated: '%s'", input, rut));
        } catch (IllegalArgumentException ex) {
        }
    }

}
