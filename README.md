# Text-File-Compression

# Text File Compression in Java

## Overview

This Java project implements a simple text file compression program that reduces the size of text files using Huffman coding. Huffman coding is an optimal prefix coding technique that assigns shorter codes to more frequent characters, resulting in efficient compression.

## How the Compression Works

1. Huffman Coding:

   - The algorithm starts by reading the input text file and calculating the frequency of each character in the file.
   - It then builds a Huffman tree based on the character frequencies, where characters with higher frequencies have shorter codes.
   - The Huffman tree is used to generate a mapping of characters to their corresponding Huffman codes.

2. Compression:

   - With the Huffman codes ready, the algorithm reads the input text file again, encoding each character with its corresponding Huffman code.
   - The compressed output is written to a new file.

3. Decompression:

   - To decompress a compressed file, the algorithm reads the compressed file and reconstructs the Huffman tree using the header information.
   - It then reads the compressed data and follows the Huffman codes to decode each character, recreating the original text.

## Usage

1. Compressing a Text File:

   To compress a text file, run the `Compressor` class with the input and output file paths as arguments.
