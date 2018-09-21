/**
 * Runner class for experimenting and for encoding selection criteria.
 *
 * @author Michael S. Kirkpatrick
 * @version V1, 8/2018
 */
public class Runner {

  public static void main(String[] args) {

    Person alice = new Person("Alice");
    Person bob = new Person("Bob");
    Person carol = new Person("Carol");
    Person dave = new Person("Dave");
    Person eve = new Person("Eve");

    SequenceAnalyzer analyzer = new SequenceAnalyzer(alice, bob);

  }

  // Suggestsions for test cases to consider writing:
  //   Find the longest common subsequences between Alice and Bob
  //   Find the longest common between Alice and Bob from index 120,000 to 120,000,000
  //   Find Carol's candidate genes that are between 25 and 45 in length
  //   Find Carol's candidates less than 30 in length starting at 1,000,000,000
  //   Find matches between Dave and Eve with between 6 and 12 common nucleotides
  //   Find Dave's and Eve's longest match starting before 900,000

  // In all cases except the last, return a WeightedList. The last should just be
  // a single Region.

}
