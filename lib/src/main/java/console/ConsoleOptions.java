package console;

import java.text.SimpleDateFormat;

public class ConsoleOptions {
    public boolean short_names = true;
    public boolean abbreviated_names = false;

    public boolean methods = true;
    public boolean json = false;

    public boolean colors = true;
    public boolean timestamp = true;

    public int indent = 2;

    public int depth = 3;

    public SimpleDateFormat dateformat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss");

    public ConsoleOptions() {}

    public ConsoleOptions(boolean short_names, boolean abbreviated_names, boolean methods, boolean json, boolean colors, boolean timestamp, int indent, int depth, SimpleDateFormat dateformat) {
        this.short_names       = short_names;
        this.abbreviated_names = abbreviated_names;
        this.methods           = methods;
        this.json              = json;
        this.colors            = colors;
        this.timestamp         = timestamp;
        this.indent            = indent;
        this.depth             = depth;
        this.dateformat        = dateformat;
    }
}
