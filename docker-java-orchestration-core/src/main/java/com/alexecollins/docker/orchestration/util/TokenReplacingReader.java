package com.alexecollins.docker.orchestration.util;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.nio.CharBuffer;

/**
 * <p>
 * A token replacing reader which replaces tokens of the form
 * <code>${tokenName}</code>. The replacement value is provided by an
 * implementation of the interface {@link TokenResolver}.
 * </p>
 * <p/>
 * <p>
 * Origin: <a href="http://tutorials.jenkov.com/java-howto/replace-strings-in-streams-arrays-files.html">
 * http://tutorials.jenkov.com/java-howto/replace-strings-in-streams-arrays-files.html</a>
 * </p>
 *
 * @author Jakob Jenkov
 */
public class TokenReplacingReader extends Reader {
    private final StringBuilder tokenNameBuffer = new StringBuilder();
    private PushbackReader pushbackReader = null;
    private TokenResolver tokenResolver = null;
    private String tokenValue = null;
    private int tokenValueIndex = 0;

    public TokenReplacingReader(Reader source, TokenResolver resolver) {
        this.pushbackReader = new PushbackReader(source, 2);
        this.tokenResolver = resolver;
    }

    public int read(@SuppressWarnings("NullableProblems") CharBuffer target) throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

    public int read() throws IOException {
        if (this.tokenValue != null) {
            if (this.tokenValueIndex < this.tokenValue.length()) {
                return this.tokenValue.charAt(this.tokenValueIndex++);
            }
            if (this.tokenValueIndex == this.tokenValue.length()) {
                this.tokenValue = null;
                this.tokenValueIndex = 0;
            }
        }

        int data = this.pushbackReader.read();
        if (data != '$')
            return data;

        data = this.pushbackReader.read();
        if (data != '{') {
            this.pushbackReader.unread(data);
            return '$';
        }
        this.tokenNameBuffer.delete(0, this.tokenNameBuffer.length());

        data = this.pushbackReader.read();
        while (data != '}') {
            this.tokenNameBuffer.append((char) data);
            data = this.pushbackReader.read();
        }

        this.tokenValue = this.tokenResolver.resolveToken(this.tokenNameBuffer
                .toString());

        if (this.tokenValue == null) {
            this.tokenValue = "${" + this.tokenNameBuffer.toString() + "}";
        }
        if (this.tokenValue.length() == 0) {
            return read();
        }
        return this.tokenValue.charAt(this.tokenValueIndex++);

    }

    public int read(@SuppressWarnings("NullableProblems") char cbuf[]) throws IOException {
        return read(cbuf, 0, cbuf.length);
    }

    public int read(@SuppressWarnings("NullableProblems") char cbuf[], int off, int len) throws IOException {
        int charsRead = 0;
        for (int i = 0; i < len; i++) {
            int nextChar = read();
            if (nextChar == -1) {
                if (charsRead == 0) {
                    charsRead = -1;
                }
                break;
            }
            charsRead = i + 1;
            cbuf[off + i] = (char) nextChar;
        }
        return charsRead;
    }

    public void close() throws IOException {
        this.pushbackReader.close();
    }

    public long skip(long n) throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

    public boolean ready() throws IOException {
        return this.pushbackReader.ready();
    }

    public boolean markSupported() {
        return false;
    }

    public void mark(int readAheadLimit) throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

    public void reset() throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }
}
