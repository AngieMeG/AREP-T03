package edu.escuelaing.arep.networking;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Unit test for simple App.
 * @author Angie Medina
 */
public class AppTest{
    private static final String URL_STRING = "https://http-server-arep.herokuapp.com";

    /**
     * Test if the url is been deployed by heroku
     */
    @Test
    public void shouldFindPage(){
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(URL_STRING).openStream()));
        } catch(IOException e) {
            fail("Resource: " + URL_STRING + " was not found.");
        }
    }

    /**
     * Test the static resources that are html
     */
    @Test
    public void shouldReturnHTMLContent(){
        try{
            URL urlWelcomePage = new URL(URL_STRING);
            URLConnection u  = urlWelcomePage.openConnection();
            String type = u.getHeaderField("Content-Type");
            Assert.assertEquals("text/html", type);

            URL urlIndex = new URL(URL_STRING+"/index.html");
            u = urlIndex.openConnection();
            type = u.getHeaderField("Content-Type");
            Assert.assertEquals("text/html", type);

            URL urlPage = new URL(URL_STRING+"/page.html");
            u = urlPage.openConnection();
            type = u.getHeaderField("Content-Type");
            Assert.assertEquals("text/html", type);


        } catch(IOException e) {
            fail("Resource: " + URL_STRING + " was not found.");
        }
    }

    /**
     * Test the static resources that are js
     */
    @Test
    public void shouldReturnJavaScriptContent(){
        try{
            URL urlScript = new URL(URL_STRING+"/script.js");
            URLConnection u  = urlScript.openConnection();
            String type = u.getHeaderField("Content-Type");
            Assert.assertEquals("text/javascript", type);

            URL urlPageScript = new URL(URL_STRING+"/scriptPage.js");
            u = urlPageScript.openConnection();
            type = u.getHeaderField("Content-Type");
            Assert.assertEquals("text/javascript", type);

        } catch(IOException e) {
            fail("Resource: " + URL_STRING + " was not found.");
        }
    }

    /**
     * Test the static resources that are css
     */
    @Test
    public void shouldReturnCSSContent(){
        try{
            URL urlCSS = new URL(URL_STRING+"/styles.css");
            URLConnection u  = urlCSS.openConnection();
            String type = u.getHeaderField("Content-Type");
            Assert.assertEquals("text/css", type);

            URL urlPageScript = new URL(URL_STRING+"/stylesPage.css");
            u = urlPageScript.openConnection();
            type = u.getHeaderField("Content-Type");
            Assert.assertEquals("text/css", type);

        } catch(IOException e) {
            fail("Resource: " + URL_STRING + " was not found.");
        }
    }

    /**
     * Test the static resources that are an image
     */
    @Test
    public void shouldReturnImageContent(){
        try{
            URL urlCSS = new URL(URL_STRING+"/Eevee.png");
            URLConnection u  = urlCSS.openConnection();
            String type = u.getHeaderField("Content-Type");
            Assert.assertEquals("image/PNG", type);

        } catch(IOException e) {
            fail("Resource: " + URL_STRING + " was not found.");
        }
    }
}
