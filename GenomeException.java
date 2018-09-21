/**
 * Exception class for invalid parameters for either the GenomeOracle
 * or the SequenceAnalyzer.
 *
 * @author Michael S. Kirkpatrick
 * @version V1, 8/2018
 */
public class GenomeException extends RuntimeException {

  /**
   * Create a run-time exception with a custom string.
   */
  public GenomeException() {
    super("Invalid base pair index");
  }
}
