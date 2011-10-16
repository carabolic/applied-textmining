package de.tuberlin.dima.textmining.assignment1;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class KeywordFinderTest {

  @Test
  public void testMyKeywordFinder() throws IOException {

    String text = Resources.toString(Resources.getResource("./assignment1/starwars.txt"), Charsets.UTF_8);

    KeywordFinder finder = new KeywordFinder();
    List<String> keywords = finder.keywords(text, 10);

    assertEquals(10, keywords.size());

    for (int n = 0; n < keywords.size(); n++) {
      System.out.println(n + ": " + keywords.get(n));
    }
  }

}
