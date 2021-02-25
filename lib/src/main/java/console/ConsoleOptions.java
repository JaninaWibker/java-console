package console;

import java.text.SimpleDateFormat;

public class ConsoleOptions {
    public boolean short_names = true;
    public boolean abbreviated_names = false;

    public boolean methods = true;
    public boolean json = false;
    public boolean skip_getter_setter = false;
    public boolean skip_enclosing_scope = true;

    public boolean colors = true;
    public boolean timestamp = true;

    public int indent = 2;

    public int depth = 3;

    public SimpleDateFormat dateformat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss");

    public ConsoleOptions() {}

    public ConsoleOptions(boolean short_names, boolean abbreviated_names, boolean methods, boolean json, boolean skip_getter_setter, boolean skip_enclosing_scope, boolean colors, boolean timestamp, int indent, int depth, SimpleDateFormat dateformat) {
        this.short_names          = short_names;
        this.abbreviated_names    = abbreviated_names;
        this.methods              = methods;
        this.json                 = json;
        this.skip_getter_setter   = skip_getter_setter;
        this.skip_enclosing_scope = skip_enclosing_scope;
        this.colors               = colors;
        this.timestamp            = timestamp;
        this.indent               = indent;
        this.depth                = depth;
        this.dateformat           = dateformat;
    }

    /**
     * Create a new ConsoleOptions object from another one
     */
    public static ConsoleOptions from(ConsoleOptions from_opts) {
        ConsoleOptions opts = new ConsoleOptions();

        opts.short_names          = from_opts.short_names;
        opts.abbreviated_names    = from_opts.abbreviated_names;
        opts.methods              = from_opts.methods;
        opts.json                 = from_opts.json;
        opts.skip_getter_setter   = from_opts.skip_getter_setter;
        opts.skip_enclosing_scope = from_opts.skip_enclosing_scope;
        opts.colors               = from_opts.colors;
        opts.timestamp            = from_opts.timestamp;
        opts.indent               = from_opts.indent;
        opts.depth                = from_opts.depth;
        opts.dateformat           = from_opts.dateformat;

        return opts;
    }

    /**
     * Create a ConsoleOptions object with all boolean values set to false
     */
    public static ConsoleOptions none() {
        ConsoleOptions opts = new ConsoleOptions();
        opts.short_names = false;
        opts.abbreviated_names = false;
        opts.methods = false;
        opts.json = false;
        opts.skip_getter_setter = false;
        opts.colors = false;
        opts.timestamp = false;

        return opts;
    }

    /**
     * Create a ConsoleOptions object with all (even uncompatible) boolean values set to true
     */
    public static ConsoleOptions all() {
        ConsoleOptions opts = new ConsoleOptions();
        opts.short_names = true;
        opts.abbreviated_names = true;
        opts.methods = true;
        opts.json = true;
        opts.skip_getter_setter = true;
        opts.colors = true;
        opts.timestamp = true;

        return opts;
    }

    /**
     * Set short_names to true in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions short_names() {
        this.short_names = true;
        return this;
    }

    /**
     * Set short_names to the passed value in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions short_names(boolean short_names) {
        this.short_names = short_names;
        return this;
    }

    /**
     * Set short_names to false in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions no_short_names() {
        this.short_names = false;
        return this;
    }

    /**
     * Set abbreviated_names to true in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions abbreviated_names() {
        this.abbreviated_names = true;
        return this;
    }

    /**
     * Set abbreviated_names to the passed value in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions abbreviated_names(boolean abbreviated_names) {
        this.abbreviated_names = abbreviated_names;
        return this;
    }

    /**
     * Set abbreviated_names to false in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions no_abbreviated_names() {
        this.abbreviated_names = false;
        return this;
    }

    /**
     * Set methods to true in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions methods() {
        this.methods = true;
        return this;
    }

    /**
     * Set methods to the passed value in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions methods(boolean methods) {
        this.methods = methods;
        return this;
    }

    /**
     * Set methods to false in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions no_methods() {
        this.methods = false;
        return this;
    }

    /**
     * Set json to true in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions json() {
        this.json = true;
        return this;
    }

    /**
     * Set json to the passed value in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions json(boolean json) {
        this.json = json;
        return this;
    }

    /**
     * Set json to false in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions no_json() {
        this.json = false;
        return this;
    }

    /**
     * Set skip_getter_setter to true in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions skip_getter_setter() {
        this.skip_getter_setter = true;
        return this;
    }

    /**
     * Set skip_getter_setter to the passed value in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions skip_getter_setter(boolean skip_getter_setter) {
        this.skip_getter_setter = skip_getter_setter;
        return this;
    }

    /**
     * Set skip_getter_setter to false in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions no_skip_getter_setter() {
        this.skip_getter_setter = false;
        return this;
    }

    /**
     * Set skip_enclosing_scope to true in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions skip_enclosing_scope() {
        this.skip_enclosing_scope = true;
        return this;
    }

    /**
     * Set skip_enclosing_scope to the passed value in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions skip_enclosing_scope(boolean skip_enclosing_scope) {
        this.skip_enclosing_scope = skip_enclosing_scope;
        return this;
    }

    /**
     * Set skip_enclosing_scope to false in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions no_skip_enclosing_scope() {
        this.skip_enclosing_scope = false;
        return this;
    }

    /**
     * Set colors to true in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions colors() {
        this.colors = true;
        return this;
    }

    /**
     * Set colors to the passed value in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions colors(boolean colors) {
        this.colors = colors;
        return this;
    }

    /**
     * Set colors to false in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions no_colors() {
        this.colors = false;
        return this;
    }

    /**
     * Set timestamp to true in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions timestamp() {
        this.timestamp = true;
        return this;
    }

    /**
     * Set timestamp to the passed value in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions timestamp(boolean timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * Set timestamp to false in the **current** ConsoleOptions object.
     * WARNING: this is NOT immutable
     */
    public ConsoleOptions no_timestamp() {
        this.timestamp = false;
        return this;
    }

    /** Copy the current ConsoleOptions object and create a new one */
    public ConsoleOptions copy() {
        return ConsoleOptions.from(this);
    }
}
