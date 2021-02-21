package console;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

public class Console {

    private static final String LOG_COLOR   = ConsoleColors.WHITE;
    private static final String INFO_COLOR  = ConsoleColors.BLUE;
    private static final String WARN_COLOR  = ConsoleColors.YELLOW;
    private static final String ERROR_COLOR = ConsoleColors.RED;

    @Retention(RetentionPolicy.RUNTIME)
    public @interface SkipDescend {

    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface SkipEntirely {

    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface UseToString {

    }

    private static Pair<ConsoleOptions, Integer> calculate_options(Object[] values) {

        // set options (either use first argument as settings if of type ConsoleOptions and
        // more than just the settings object exist or just fall back to default values)

        ConsoleOptions opts = new ConsoleOptions();
        int i = 0;

        if(values.length > 1 && values[0] instanceof ConsoleOptions) {
            opts = (ConsoleOptions) values[0];
            i = 1;
        }

        return new Pair<>(opts, i);
    }

    public static void log_custom(String color, String name, Object... values) {
        Pair<ConsoleOptions, Integer> p = Console.calculate_options(values);
        ConsoleOptions opts = p.key;
        int i = p.value;
        ConsoleHelper ch = new ConsoleHelper(opts);

        String prefix;
        String log_color = opts.colors ? color : "";
        String reset_color = opts.colors ? ConsoleColors.RESET : "";

        // generate prefix, colors and postfix
        if(opts.timestamp) {
            prefix = "[" + name + ";" + opts.dateformat.format(new Date()) + "] ";
        } else {
            prefix = "[" + name + "] ";
        }

        // json output omits the prefix
        if(!opts.json) System.out.print(prefix);

        for(; i < values.length; i++) {
            Object value = values[i];
            if(value instanceof String)
                System.out.print(log_color + value + reset_color);
            else
                System.out.print(ch.string_log_single(value));

            if(i < values.length-1)
                System.out.print(", ");
        }

        System.out.print("\n");
    }

    public static String s_log_custom(String color, String name, Object... values) {
        Pair<ConsoleOptions, Integer> p = Console.calculate_options(values);
        ConsoleOptions opts = p.key;
        int i = p.value;
        ConsoleHelper ch = new ConsoleHelper(opts);

        StringBuilder rtn = new StringBuilder();

        String prefix;
        String log_color = opts.colors ? color : "";
        String reset_color = opts.colors ? ConsoleColors.RESET : "";

        // generate prefix, colors and postfix
        if(opts.timestamp) {
            prefix = "[" + name + ";" + opts.dateformat.format(new Date()) + "] ";
        } else {
            prefix = "[" + name + "] ";
        }

        // json output omits the prefix
        if(!opts.json) rtn.append(prefix);

        for(; i < values.length; i++) {
            Object value = values[i];
            if(value instanceof String)
                rtn.append(log_color + value + reset_color);
            else
                rtn.append(ch.string_log_single(value));

            if(i < values.length-1)
                rtn.append(", ");
        }

        return rtn.toString();
    }

    public static void log(Object... values) {
        Console.log_custom(Console.LOG_COLOR, "LOG", values);
    }

    public static void info(Object... values) {
        Console.log_custom(Console.INFO_COLOR, "INFO", values);
    }

    public static void warn(Object... values) {
        Console.log_custom(Console.WARN_COLOR, "WARN", values);
    }

    public static void err(Object... values) {
        Console.log_custom(Console.ERROR_COLOR, "ERR", values);
    }

    public static String s_log(Object... values) { return Console.s_log_custom(Console.LOG_COLOR, "LOG", values); }

    public static String s_info(Object... values) { return Console.s_log_custom(Console.INFO_COLOR, "INFO", values); }

    public static String s_warn(Object... values) { return Console.s_log_custom(Console.WARN_COLOR, "WARN", values); }

    public static String s_err(Object... values) { return Console.s_log_custom(Console.ERROR_COLOR, "ERR", values); }

    public class TestPerson {
        private String first_name;
        private String last_name;

        @Console.SkipEntirely
        private final String foo = "bar";

        public TestPerson(String first_name, String last_name) {
            this.first_name = first_name;
            this.last_name = last_name;
        }

        public String getName() {
            return this.first_name + " " + this.last_name;
        }
    }
}
