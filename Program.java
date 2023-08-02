import java.io.*;
import java.util.*;

class HuffmanNode implements Comparable<HuffmanNode> {
    char character;
    int frequency;
    HuffmanNode left, right;

    public HuffmanNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }

    public int compareTo(HuffmanNode node) {
        return this.frequency - node.frequency;
    }
}

public class Program {
    private static final int BYTE_SIZE = 8;

    private String inputFile;
    private String compressedFile;
    private String decompressedFile;
    private Map<Character, String> huffmanCodes;

    public Program(String inputFile, String compressedFile, String decompressedFile) {
        this.inputFile = inputFile;
        this.compressedFile = compressedFile;
        this.decompressedFile = decompressedFile;
        this.huffmanCodes = new HashMap<>();
    }

    public void compress() {
        String text = readTextFromFile(inputFile);
        Map<Character, Integer> frequencyTable = buildFrequencyTable(text);
        HuffmanNode root = buildHuffmanTree(frequencyTable);
        buildHuffmanCodes(root, "");
        String compressedText = encodeText(text);
        writeCompressedTextToFile(compressedText);
    }

    public void decompress() {
        String compressedText = readCompressedTextFromFile();
        String decodedText = decodeText(compressedText);
        writeDecompressedTextToFile(decodedText);
    }

    private String readTextFromFile(String filePath) {
        StringBuilder text = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    private Map<Character, Integer> buildFrequencyTable(String text) {
        Map<Character, Integer> frequencyTable = new HashMap<>();
        for (char character : text.toCharArray()) {
            frequencyTable.put(character, frequencyTable.getOrDefault(character, 0) + 1);
        }
        return frequencyTable;
    }

    private HuffmanNode buildHuffmanTree(Map<Character, Integer> frequencyTable) {
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencyTable.entrySet()) {
            pq.offer(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();

            HuffmanNode parent = new HuffmanNode('\0', left.frequency + right.frequency);
            parent.left = left;
            parent.right = right;

            pq.offer(parent);
        }

        return pq.poll();
    }

    private void buildHuffmanCodes(HuffmanNode node, String code) {
        if (node == null) {
            return;
        }

        if (node.left == null && node.right == null) {
            huffmanCodes.put(node.character, code);
        }

        buildHuffmanCodes(node.left, code + "0");
        buildHuffmanCodes(node.right, code + "1");
    }

    private String encodeText(String text) {
        StringBuilder encodedText = new StringBuilder();
        for (char character : text.toCharArray()) {
            encodedText.append(huffmanCodes.get(character));
        }
        return encodedText.toString();
    }

    private void writeCompressedTextToFile(String compressedText) {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(compressedFile))) {
            int numPaddingBits = BYTE_SIZE - (compressedText.length() % BYTE_SIZE);
            outputStream.write(numPaddingBits);

            for (int i = 0; i < compressedText.length(); i += BYTE_SIZE) {
                String byteStr = compressedText.substring(i, Math.min(i + BYTE_SIZE, compressedText.length()));
                int byteValue = Integer.parseInt(byteStr, 2);
                outputStream.write(byteValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readCompressedTextFromFile() {
        StringBuilder compressedText = new StringBuilder();
        try (DataInputStream inputStream = new DataInputStream(new FileInputStream(compressedFile))) {
            int numPaddingBits = inputStream.readByte();
            while (inputStream.available() > 0) {
                int byteValue = inputStream.readByte() & 0xFF;
                String byteStr = Integer.toBinaryString(byteValue);
                byteStr = String.format("%8s", byteStr).replace(' ', '0');
                compressedText.append(byteStr);
            }
            compressedText.setLength(compressedText.length() - numPaddingBits);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compressedText.toString();
    }

    private String decodeText(String compressedText) {
        StringBuilder decodedText = new StringBuilder();
        Map<String, Character> reverseHuffmanCodes = buildReverseHuffmanCodes();

        StringBuilder currentCode = new StringBuilder();
        for (char bit : compressedText.toCharArray()) {
            currentCode.append(bit);
            if (reverseHuffmanCodes.containsKey(currentCode.toString())) {
                char character = reverseHuffmanCodes.get(currentCode.toString());
                decodedText.append(character);
                currentCode.setLength(0);
            }
        }

        return decodedText.toString();
    }

    private Map<String, Character> buildReverseHuffmanCodes() {
        Map<String, Character> reverseHuffmanCodes = new HashMap<>();
        for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            reverseHuffmanCodes.put(entry.getValue(), entry.getKey());
        }
        return reverseHuffmanCodes;
    }

    private void writeDecompressedTextToFile(String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(decompressedFile))) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String inputFile = "input.txt";
        String compressedFile = "compressed.bin";
        String decompressedFile = "decompressed.txt";
        
        Program huffmanCompression = new Program(inputFile, compressedFile, decompressedFile);
        huffmanCompression.compress();
        huffmanCompression.decompress();

        System.out.println("Text file compressed and decompressed successfully.");

    }
}
