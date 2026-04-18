package buildingblocks;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

// import util.Utilz;

public class TextAreaOutputStream extends OutputStream {

    // *************************************************************************************************
    // INSTANCE MEMBERS
    // *************************************************************************************************

    private byte[] oneByte; // array for write(int val);
    private Appender appender; // most recent action

    public TextAreaOutputStream(JTextArea txtara) {
        this(txtara, 1000);
    }

    public TextAreaOutputStream(JTextArea txtara, int maxlin) {
        if (maxlin < 1) {
            throw new IllegalArgumentException(
                    "TextAreaOutputStream maximum lines must be positive (value=" + maxlin + ")");
        }
        oneByte = new byte[1];
        appender = new Appender(txtara, maxlin);
    }

    // @edu.umd.cs.findbugs.annotations.SuppressWarnings("DM_DEFAULT_ENCODING")
    private static String bytesToString(byte[] ba, int str, int len) {
        try {
            return new String(ba, str, len, "UTF-8");
        } catch (UnsupportedEncodingException thr) {
            return new String(ba, str, len);
        } // all JVMs are required to support UTF-8
    }

    public static Frame create(String header) {

        JFrame frame = new JFrame(header);

        JTextArea ta = new JTextArea();
        TextAreaOutputStream taos = new TextAreaOutputStream(ta, 60);
        PrintStream ps = new PrintStream(taos);

        System.setOut(ps);
        System.setErr(ps);

        frame.add(new JScrollPane(ta));

        frame.pack();
        frame.setSize(800, 400);

        center(frame);

        ImageIcon img = new ImageIcon("mrv.png");
        frame.setIconImage(img.getImage());

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        return frame;
    }

    private static void center(JFrame frame) {

        // get the size of the screen, on systems with multiple displays,

        // the primary display is used

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // calculate the new location of the window

        int w = frame.getSize().width;

        int h = frame.getSize().height;

        int x = (dim.width - w) / 2;

        int y = (dim.height - h) / 2;

        // moves this component to a new location, the top-left corner of

        // the new location is specified by the x and y

        // parameters in the coordinate space of this component's parent

        frame.setLocation(x, y);
    }

    /** Clear the current console text area. */
    public synchronized void clear() {
        if (appender != null) {
            appender.clear();
        }
    }

    public synchronized void close() {
        appender = null;
    }

    public synchronized void flush() {}

    public synchronized void write(int val) {
        oneByte[0] = (byte) val;
        write(oneByte, 0, 1);
    }

    // *************************************************************************************************
    // STATIC MEMBERS
    // *************************************************************************************************

    public synchronized void write(byte[] ba) {
        write(ba, 0, ba.length);
    }

    public synchronized void write(byte[] ba, int str, int len) {
        if (appender != null) {
            appender.append(bytesToString(ba, str, len));
        }
    }

    static class Appender implements Runnable {
        private static final String EOL1 = "\n";
        private static final String EOL2 = System.getProperty("line.separator", EOL1);
        private final JTextArea textArea;
        private final int maxLines; // maximum lines allowed in text area
        private final LinkedList<Integer> lengths; // length of lines within text area
        private final List<String> values; // values waiting to be appended
        private int curLength; // length of current line
        private boolean clear;
        private boolean queue;

        Appender(JTextArea txtara, int maxlin) {
            textArea = txtara;
            maxLines = maxlin;
            lengths = new LinkedList<Integer>();
            values = new ArrayList<String>();

            curLength = 0;
            clear = false;
            queue = true;
        }

        synchronized void append(String val) {
            values.add(val);
            if (queue) {
                queue = false;
                EventQueue.invokeLater(this);
            }
        }

        synchronized void clear() {
            clear = true;
            curLength = 0;
            lengths.clear();
            values.clear();
            if (queue) {
                queue = false;
                EventQueue.invokeLater(this);
            }
        }

        // MUST BE THE ONLY METHOD THAT TOUCHES textArea!
        public synchronized void run() {
            if (clear) {
                textArea.setText("");
            }
            for (String val : values) {
                curLength += val.length();
                if (val.endsWith(EOL1) || val.endsWith(EOL2)) {
                    if (lengths.size() >= maxLines) {
                        textArea.replaceRange("", 0, lengths.removeFirst());
                    }
                    lengths.addLast(curLength);
                    curLength = 0;
                }
                textArea.append(val);
            }
            values.clear();
            clear = false;
            queue = true;
        }
    }
} /* END PUBLIC CLASS */
