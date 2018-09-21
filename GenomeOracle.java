import java.util.Random;

/**
 * Oracle for producing gene sequences. Do NOT modify this file.
 *
 * @author Michael S. Kirkpatrick
 * @version V1, 8/2018
 */
public class GenomeOracle {
  // humanRand makes sure all gene regions are in the same locations
  private static final int humanRandomSeed = 13789;
  private Random humanRand;

  // create a set of codons to choose from (makes for longer matching strings)
  public static final long MAX_BASE_INDEX = 3088286400L;
  public static final String[] codons = {
    "ATA", "AGA", "ACA", "ATT", "AGT", "ACT", "AGG", "ACG", "ATC", "AGC", "ACC",
    "TAA", "TGA", "TCA", "TAT", "TGT", "TCT", "TGG", "TCG", "TAC", "TGC", "TCC",
    "GAA", "GTA", "GCA", "GAT", "GTT", "GCT", "GAG", "GTG", "GCG", "GAC", "GTC", "GCC",
    "CAA", "CTA", "CGA", "CAT", "CTT", "CGT", "CAG", "CTG", "CGG", "CAC", "CTC", "CGC" };

  // rand is used to create consistent pseudorandom gene sequences per person
  private final int seed;
  private Random rand;

  // generate the sequence 60 nucleotide characters at a time
  private int bufferChunkIndex;
  private String bufferChunk;
  private final int codonLength = 3;
  private final int codonsPerChunk = 20;
  private final int chunkLength = codonsPerChunk * codonLength;


  /**
   * Initialize a GenomeOracle for a person based on a seed value.
   *
   * @param seed The random seed value.
   */
  public GenomeOracle(int seed) {
    this.seed = seed;
    rand = new Random(seed);
    humanRand = new Random(humanRandomSeed);
    bufferChunkIndex = -1;
    bufferChunk = "";
  }

  /**
   * Generate an array of ATGC nucleotides of a given length. Base pairs
   * are numbered from 0 to 3,088,286,400. Maximum index exceeds the
   * capacity of an int, so the index has to be a long.
   *
   * @param index Starting index for the random oracle.
   * @param length The number of base pairs to produce.
   *
   * @return An array of 2-character strings representing base pairs.
   */
  public char[] getStrandSegment(long index, int length) {
    // Proactive error checking for bad parameters
    if (index >= MAX_BASE_INDEX || (index + length) >= MAX_BASE_INDEX ||
        length < 0 || index < 0) {
      throw new GenomeException();
    }

    // indexChunk - which 60-char chunk are we working with
    // indexOffset - where are we starting in this chunk
    int indexChunk = (int)(index / (long)chunkLength);
    int indexOffset = (int)(index % (long)chunkLength);

    if (indexChunk < bufferChunkIndex) {
      // reset needed
      bufferChunkIndex = -1;
      rand = new Random(seed);
      humanRand = new Random(humanRandomSeed);
    }

    // advance to the specific chunk that we want to start with
    while (bufferChunkIndex < indexChunk) {
      generateChunk(rand); // increments bufferChunkIndex
    }

    // At this point, bufferChunk contains the chunk where index starts

    char[] ntides = new char[length];
    int idx = 0; // the index in the current buffer of nucleotides
    int remaining = length; // number of chars left to read

    // Initially, read all the way to the end of the chunk
    int bytesToRead = chunkLength - indexOffset;
    // But if request is all in this chunk, stop early
    if (bytesToRead > length) {
      bytesToRead = length;
    }

    // Iteratively read a chunk at a time until all bytes are read
    while (true) { // more bytes needed
      // Copy the bytes from the current chunk
      for (int i = 0; i < bytesToRead; i++) {
        ntides[idx++] = bufferChunk.charAt(indexOffset + i);
      }
      remaining -= bytesToRead;

      // If more bytes are needed, generate the next chunk
      bytesToRead = (remaining < chunkLength ? remaining : chunkLength);
      indexOffset = 0;
      if (remaining > 0) {
        generateChunk(rand);
      } else {
        break;
      }
    }

    return ntides;

  }

  private void generateChunk(Random rand) {
    // Algorithm:
    //   Random number [0-16] of junk - might be none, but gene cannot be none
    //   Start codon
    //   Remainder (18 minus first length) of junk
    //   Stop codon
    String sequence = "";

    // Use humanRand to ensure all genes occur in the same place
    int skip = humanRand.nextInt(codonsPerChunk-3); // random between 0 and 16
    for (int j = 0; j < skip; j++) {
      sequence += codons[rand.nextInt(codons.length)];
    }

    // Get the start codon
    sequence += "AUG";
    skip = (codonsPerChunk - skip - 2);
    for (int j = 0; j < skip - 1; j += 2) {
      int index = rand.nextInt(codons.length);
      // Genes grab 2 codons at a time to increase the length of matches
      sequence += codons[index];
      sequence += codons[(index+1) % codons.length];
    }

    // Since junk codons are grabbed one at a time and regions are grabbed two
    // at a time, we might need one extra codon to get the 60-char chunk
    if (sequence.length() != (chunkLength - codonLength)) {
      sequence += codons[rand.nextInt(codons.length)];
    }

    // Get the stop codon
    sequence += "UAG";

    // Advance the buffer chunk and set the chunk sequence. Note that we set an
    // instance variable instead of returning a value, because the same chunk
    // may be used repeatedly.
    ++bufferChunkIndex;
    bufferChunk = sequence;
  }

}
