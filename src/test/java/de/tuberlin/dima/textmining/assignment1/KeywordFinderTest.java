package de.tuberlin.dima.textmining.assignment1;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Test;


import java.io.IOException;
import java.util.List;

/**
 * if you encounter the problem that starwars.txt cannot be found, you need to build the project
 * once on the commandline: mvn clean install
 * */
public class KeywordFinderTest {

  @Test
  public void testMyKeywordFinder() throws IOException {

    String text = Resources.toString(Resources.getResource("assignment1/starwars.txt"), Charsets.UTF_8);

    KeywordFinder finder = new KeywordFinder();
    List<String> keywords = finder.keywords(text, 10);

    for (int n = 0; n < keywords.size(); n++) {
      System.out.println(n + ": " + keywords.get(n));
    }
  }
}
