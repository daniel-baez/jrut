package cl.daplay.jrut;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Objects;
import java.util.regex.Pattern;

import static java.lang.String.format;

/**
 * Immutable, Comparable and Serializable
 */
public final class JRut implements Comparable<JRut>, Serializable {

    private static final String DVS = "_123456789K0";

    /**
     * formatter used for toString()
     */
    private static final DecimalFormat FMT;

    static {
        final NumberFormat _fmt = NumberFormat.getInstance();
        if (!(_fmt instanceof NumberFormat)) {
            throw new IllegalStateException("JRut, Can't create NumberFormat");
        }

        final DecimalFormat fmt = (DecimalFormat) _fmt;

        final DecimalFormatSymbols symbols = fmt.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');

        fmt.setDecimalFormatSymbols(symbols);

        fmt.setMaximumFractionDigits(0);
        fmt.setMinimumFractionDigits(0);

        fmt.setGroupingSize(3);
        fmt.setGroupingUsed(true);

        FMT = fmt;
    }

    /**
     * sequence of 2, 3, 4, 5, 6, 7
     */
    private static final int[] MULTIPLIERS = new int[] {2, 3, 4, 5, 6, 7};

    /**
     * regex used to remove invalid rut characters
     */
    private static final Pattern FILTER = Pattern.compile("(?i)[^\\dk]");

    /**
     * "template" for a rut
     */
    private static final Pattern EXPECTED = Pattern.compile("^\\d+[K\\d]?$");

    /**
     * @param _rut
     * @return
     */
    private static String filter(final String _rut) {
        if (_rut == null || _rut.trim().length() == 0) {
            throw new IllegalArgumentException("JRut. Can't create instance on empty argument");
        }

        if (_rut.length() <= 1) {
            throw new IllegalArgumentException(format("JRut. Can't create instance on '%s'", _rut));
        }

        final String rut = FILTER.matcher(_rut).replaceAll("").trim().toUpperCase();

        if (rut.length() <= 1 || !EXPECTED.matcher(rut).matches()) {
            throw new IllegalArgumentException(format("JRut. Invalid input '%s'", _rut));
        }

        return rut;
    }

    /**
     * @param _number
     * @return
     */
    public static String dv(final String _number) {
        final String number = filter(_number);

        int multiplier = 0;
        int sum = 0;

        // for each character in number, going backwards
        for (int i = number.length() - 1; i >= 0; i--) {
            // current element
            final int it = Integer.parseInt(number.substring(i, i + 1));
            sum = sum + it * MULTIPLIERS[multiplier];

            // move multiplier forward
            multiplier = multiplier + 1 == MULTIPLIERS.length ? 0 : multiplier + 1;
        }

        int mod11 = sum % 11;
        int dv = 11 - mod11;

        return DVS.substring(dv, dv + 1);
    }

    private final int num;
    private final String number;
    private final String dv;
    private final String toString;

    /**
     * a Rut:
     *
     * - 16.709.383-2
     * - 16,709,383-2
     * - 16709383-2
     * - 16709383-2
     * - 167093832
     * - 16709383k
     * - 16709383K
     *
     * @param _rut
     */
    public JRut(final String _rut) throws IllegalArgumentException {
        final String rut = filter(_rut);
        final int len = rut.length();

        this.number = rut.substring(0, len - 1);
        this.dv = rut.substring(len - 1);

        final String expectedDV = dv(this.number);
        if (!this.dv.equals(expectedDV)) {
            final String t = "Illegal DV for input: '%s', dv should be: '%s' instead of '%s'";
            final String m = format(t, _rut, expectedDV, this.dv);
            throw new IllegalArgumentException(m);
        }

        this.toString = FMT.format(Integer.parseInt(this.number)) + "-" + this.dv;
        this.num = Integer.parseInt(this.number, 10);
    }

    /**
     * @inheritDoc
     */
    public int compareTo(final JRut _other) {
        if (null == _other) {
            throw new IllegalArgumentException("JRut. comparing JRut instance to null");
        }

        // we assume a well constructed JRut instance will behave properly
        final JRut other = _other;

        if (num < other.num) {
            return -1;
        } else if (num > other.num) {
            return 1;
        }

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JRut jRut = (JRut) o;
        return num == jRut.num;
    }

    @Override
    public int hashCode() {
        return num;
    }

    /**
     * @return the numeric "portion" of a Rut, eg: 16709383 for 16709383-2
     */
    public String getNumber() {
        return number;
    }

    /**
     * @return the DV "portion" of this Rut,
     * DV is short for: "Dígito Verificador" (Verifying Digit), eg: 2 for 16709383-2
     */
    public String getDv() {
        return dv;
    }

    /**
     * @return Well formatted Rut: As they use it in Chile. 16.709.383-2
     */
    @Override
    public String toString() {
        return toString;
    }

}
