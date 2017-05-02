package com.chinasofti.ark.bdadp.util.io;

import java.io.*;

public class SimpleFileReader {

    private BufferedReader br;

    public SimpleFileReader(File file) throws IOException {
        this(file, "UTF-8");
    }

    public SimpleFileReader(File file, String encoding) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, encoding);
        this.br = new BufferedReader(isr);
    }

    public String readLine() throws IOException {
        return this.br.readLine();
    }

    public void close() throws IOException {
        this.br.close();
    }
}
