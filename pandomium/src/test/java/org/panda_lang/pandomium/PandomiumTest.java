package org.panda_lang.pandomium;

import org.cef.browser.CefBrowser;
import org.panda_lang.pandomium.wrapper.PandomiumClient;

public class PandomiumTest {

    public static void main(String[] args) {
        try{
            PandomiumClient client = Pandomium.get().createClient();
            CefBrowser browser = client.loadURL("http://google.com");
        } catch (Throwable exception) {
            exception.printStackTrace();
            System.out.println(System.getProperty("java.library.path"));
        }
    }

}
