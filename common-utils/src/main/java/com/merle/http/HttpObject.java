package com.merle.http;

import java.io.*;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

public class HttpObject {
    private int code;
    private byte[] data;
    private String charSet = "utf-8";

    private HashMap<String, String> header = new HashMap();

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public HashMap<String, String> getHeader() {
        return this.header;
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }

    public String getCharSet() {
        return this.charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public String getHtml() {
        String html = null;
        if (this.data == null)
            return html;
        try {
            if ("gzip".equals(this.header.get("Content-Encoding"))) {
                BufferedReader br = null;
                String line = null;
                StringBuffer sb = new StringBuffer();
                br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(this.data)), this.charSet));
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\r\n");
                }

                br.close();
                html = sb.toString();
            } else if (this.charSet != null) {
                try {
                    html = new String(this.data, this.charSet);
                } catch (UnsupportedEncodingException e) {
                    html = new String(this.data);
                }
            } else {
                html = new String(this.data);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }
}