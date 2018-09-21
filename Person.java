/**
 * Class representing a person in a medical data set.
 *
 * @author Michael S. Kirkpatrick
 * @version V1, 8/2018
 */
public class Person {
  private GenomeOracle oracle;
  private String name;

  /**
   * Create a person with a given name.
   *
   * @param name The name of the person to create.
   */
  public Person(String name) {
    this.name = name;
    oracle = new GenomeOracle(hash(name));
  }

  /**
   * Get a gene snippet represented as a string.
   *
   * @param index The starting location of the snippet.
   * @param length The number of nucleotides in the snippet.
   */
  public String getSnippet(long index, int length) {
    return new String(oracle.getStrandSegment(index, length));
  }

  /*
   * Helper method for converting the name to a number. Allows users to
   * create Person instances based on names, rather than generating random
   * numbers.
   */
  private int hash(String string) {
    int hash = 0;
    int padding = 17;
    byte[] bytes = string.getBytes();
    for (int i = 0; i < bytes.length; i++) {
      hash += bytes[i] * padding;
      hash %= 256;
      padding *= 17;
    }

    return hash;
  }

}
