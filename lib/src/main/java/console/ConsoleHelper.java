package console;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: implement skip_getter_setter
public class ConsoleHelper {

    private enum Action {
        DESCEND,
        NO_DESCEND,
        TO_STRING,
        ARRAY,
        SKIP,
        SKIP_RESURSIVE,
    }

    @Console.SkipDescend
    private static final Collector<CharSequence, ?, String> comma = Collectors.joining(", ");

    @Console.SkipDescend
    private static final Collector<CharSequence, ?, String> newline = Collectors.joining("\n");

    @Console.SkipDescend
    private static final Collector<CharSequence, ?, String> dot = Collectors.joining(".");

    private ConsoleOptions options;

    private static final Map<Class<?>, Class<?>> PRIMITIVES = Map.of(
        Byte.TYPE,      Byte.class,
        Short.TYPE,     Short.class,
        Integer.TYPE,   Integer.class,
        Long.TYPE,      Long.class,
        Float.TYPE,     Float.class,
        Double.TYPE,    Double.class,
        Character.TYPE, Character.class,
        Boolean.TYPE,   Boolean.class
    );

    private ArrayList<Integer> visited = new ArrayList<>();

    ConsoleHelper(ConsoleOptions options) {
        this.options = options;
    }

    private String indent(String str, int level) {
        String indent = "";
        for(int i =0; i < level; i++) { indent += " "; }
        return this.indent(str, indent);
    }

    private String indent(String str, String prefix) {
        return Arrays.stream(str.split("\n"))
            .map(s -> prefix + s)
            .collect(ConsoleHelper.newline);
    }

    private String wrap_class(Class<?> c, String inner) {

        if(this.options.json) {
            return inner.equals("")
                ? "{}"
                : "{\n" + this.indent(inner, this.options.indent) + "\n}";
        } else {
            return inner.equals("")
                ? this.type(c) + " {}"
                : this.type(c) + " {\n" + this.indent(inner, this.options.indent) + "\n}";
        }
    }

    private String type(Class<?> c) {
        if(this.options.short_names) {
            return c.getSimpleName();
        }
        if(this.options.abbreviated_names) {
            String[] split = c.getName().split("\\.");
            return Pair.fromArray(split)
                .map(p -> p.key == split.length - 1
                    ? p.value
                    : String.valueOf(p.value.charAt(0)))
                .collect(ConsoleHelper.dot);
        }
        return c.getName();
    }

    private String modifier(int m) {
        String access = "";
        if(Modifier.isPrivate(m)) {
            access = "-";
        } else if(Modifier.isPublic(m)) {
            access = "+";
        } else if(Modifier.isStatic(m)) {
            access = "#";
        }

        return access;
    }

    public String field_start(Field f) {

        if(this.options.json) {
            return "\"" + f.getName() + "\"";
        } else {
            return  this.modifier(f.getModifiers()) +
                    f.getName() +
                    ": " +
                    this.type(f.getType());
        }
    }

    public String field(Pair<String, Field> p, Object o, int depth) {
        p.value.setAccessible(true);
        Object field_value;
        try {
            field_value = p.value.get(o);
        } catch(IllegalAccessException e) {
            return p.key + " ~ (illegal access)";
        }

        Action action = Action.DESCEND;

        int hash = System.identityHashCode(field_value);

        boolean is_array = p.value.getType().isArray();

        if(this.visited.contains(hash)) {
            action = Action.SKIP_RESURSIVE;
        } else {
            this.visited.add(hash);
        }

        String serialized = this.serialize_known_types(field_value);

        if(serialized != null && action != Action.SKIP) {
            action = Action.TO_STRING;
        }

        if(p.value.getAnnotation(Console.SkipDescend.class) != null) {
            action = Action.NO_DESCEND;
        }

        if(p.value.getAnnotation(Console.SkipEntirely.class) != null) {
            action = Action.SKIP;
        }

        if(p.value.getAnnotation(Console.UseToString.class) != null) {
            action = Action.TO_STRING;
            serialized = "\"" + field_value.toString() + "\"";
        }

        if(is_array) {
            action = Action.ARRAY;
        }

        if(p.value.getName().startsWith("this$")) {
            action = this.options.skip_enclosing_scope
                ? Action.SKIP
                : Action.NO_DESCEND;
        }

        if(depth == 0) {
            action = Action.NO_DESCEND;
        }

        if(this.options.json) {
            switch(action) {
                case DESCEND:           return p.key + ": " + this.string_log_single(field_value, depth-1);
                case NO_DESCEND:        return p.key + ": " + "\"<not descending>\",";
                case TO_STRING:         return p.key + ": " + serialized + ",";
                case ARRAY:             return p.key + ": " + this.array(p, o, depth) + ",";
                case SKIP_RESURSIVE:    return p.key + ": " + "\"<recursive>\",";
                case SKIP:              return null;
                default:                return "";
            }
        } else {
            switch(action) {
                case DESCEND:           return p.key + " = " + this.string_log_single(field_value, depth-1);
                case NO_DESCEND:        return p.key + " = " + "<not descending>";
                case TO_STRING:         return p.key + " = " + serialized;
                case ARRAY:             return p.key + " = " + this.array(p, field_value, depth);
                case SKIP_RESURSIVE:    return p.key + " = " + "<recursive>";
                case SKIP:              return null;
                default:                return "";
            }
        }
    }

    public Object[] autobox(Object v) {
        Class<?> component_type = v.getClass().getComponentType();
        if(ConsoleHelper.PRIMITIVES.containsKey(component_type)) {
            Class<?> autoboxed = ConsoleHelper.PRIMITIVES.get(component_type);
            Object[] new_array;
            // sadly not possible to do this super repetetive thing in a more /generic/ way :(
            switch(autoboxed.getName()) {
                case "java.lang.Byte":        { byte[]    old_array = (byte[]) v;     new_array = (Object[]) Array.newInstance(autoboxed, old_array.length); for(int i = 0; i < old_array.length; i++) { new_array[i] = old_array[i]; } } break;
                case "java.lang.Short":       { short[]   old_array = (short[]) v;    new_array = (Object[]) Array.newInstance(autoboxed, old_array.length); for(int i = 0; i < old_array.length; i++) { new_array[i] = old_array[i]; } } break;
                case "java.lang.Integer":     { int[]     old_array = (int[]) v;      new_array = (Object[]) Array.newInstance(autoboxed, old_array.length); for(int i = 0; i < old_array.length; i++) { new_array[i] = old_array[i]; } } break;
                case "java.lang.Long":        { long[]    old_array = (long[]) v;     new_array = (Object[]) Array.newInstance(autoboxed, old_array.length); for(int i = 0; i < old_array.length; i++) { new_array[i] = old_array[i]; } } break;
                case "java.lang.Float":       { float[]   old_array = (float[]) v;    new_array = (Object[]) Array.newInstance(autoboxed, old_array.length); for(int i = 0; i < old_array.length; i++) { new_array[i] = old_array[i]; } } break;
                case "java.lang.Double":      { double[]  old_array = (double[]) v;   new_array = (Object[]) Array.newInstance(autoboxed, old_array.length); for(int i = 0; i < old_array.length; i++) { new_array[i] = old_array[i]; } } break;
                case "java.lang.Character":   { char[]    old_array = (char[]) v;     new_array = (Object[]) Array.newInstance(autoboxed, old_array.length); for(int i = 0; i < old_array.length; i++) { new_array[i] = old_array[i]; } } break;
                case "java.lang.Boolean":     { boolean[] old_array = (boolean[]) v;  new_array = (Object[]) Array.newInstance(autoboxed, old_array.length); for(int i = 0; i < old_array.length; i++) { new_array[i] = old_array[i]; } } break;
                default: throw new RuntimeException("A new primitive type must have been added which needs to be considered");
            }
            return new_array;
        } else {
            return (Object[]) v;
        }
    }

    public String array(Pair<String, Field> p, Object v, int depth) {
        Object[] a = this.autobox(v);

        Stream<String> rtn = Arrays.stream(a)
            .map((Object o) -> this.string_log_single(o, depth-1));

        return "[" + rtn.collect(ConsoleHelper.comma) + "]";
    }

    public String method(Method m, int indent) {
        String str = "";
        for(int i =0; i < indent; i++) { str += " "; }
        String m_str = this.method(m);

        return m_str == null
            ? null
            : str + m_str;
    }

    public String method(Method m) {

        // check the SkipEntirely annotation (all other annotations make no sense for methods)
        if(m.getAnnotation(Console.SkipEntirely.class) != null) {
            return null;
        }

        String[] ret_type_split = m.getReturnType().getName().split("\\.");
        String return_type = Pair.fromArray(ret_type_split)
            .map(p -> p.key == ret_type_split.length - 1
                ? p.value
                : String.valueOf(p.value.charAt(0)))
            .collect(ConsoleHelper.dot);

        String generics = Arrays.stream(m.getGenericParameterTypes())
            .map((Type generic) -> generic.getTypeName())
            .collect(ConsoleHelper.comma);

        String parameters = Arrays.stream(m.getParameterTypes())
            .map((Type param) -> param.getTypeName())
            .collect(ConsoleHelper.comma);

        return  this.modifier(m.getModifiers()) +
                m.getName() +
                (generics.equals("") ? "" : "<" + generics + ">") +
                "(" + parameters +  "): " +
                return_type;
    }

    public String serialize_known_types(Object o) {
        if(o == null) {
            return "null";
        } else if(o instanceof String) {
            return "\"" + o + "\"";
        } else if(o instanceof Character) {
            return "'" + o + "'";
        } else if(o instanceof Short) {
            return o.toString();
        } else if(o instanceof Integer) {
            return o.toString();
        } else if(o instanceof Long) {
            return o.toString();
        } else if(o instanceof Float) {
            return o.toString();
        } else if(o instanceof Double) {
            return o.toString();
        } else if(o instanceof Boolean) {
            return o.toString();
        } else {
            return null;
        }
    }

    public String string_log_single(Object value) {
        return this.string_log_single(value, this.options.depth);
    }

    public String string_log_single(Object value, int depth) {
        Class<?> c = value.getClass();

        String serialized = this.serialize_known_types(value);
        if(serialized != null) {
            return serialized;
        }

        String fields = Arrays.stream(c.getDeclaredFields())
            .map(f -> new Pair<String, Field>(this.field_start(f), f))
            .map(p -> this.field(p, value, depth))
            .filter(str -> str != null)
            .collect(ConsoleHelper.newline);

        // remove trailing comma (,) when using json output
        if(this.options.json) {
            fields = fields.substring(0, fields.length()-1);
        }

        String methods = Arrays.stream(c.getDeclaredMethods())
            .map(this::method)
            .filter(str -> str != null)
            .collect(ConsoleHelper.newline);

        if(this.options.methods && !this.options.json && !methods.equals("")) {
            return this.wrap_class(c, fields + "\n\n" + methods);
        } else {
            return this.wrap_class(c, fields);
        }
    }

}
