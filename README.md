# Text File Compression and Decompression in Java

## Overview

This Java project implements a text file compression and decompression program using a combination of Run-Length Encoding (RLE) and Huffman coding techniques. The algorithm aims to reduce the size of text files by efficiently representing repetitive sequences and frequently occurring characters while allowing decompression to reconstruct the original file.

## How the Algorithm Works

1. Compression:

   - Run-Length Encoding (RLE):
     The algorithm starts with a pre-processing step using RLE. It scans the input text file and replaces consecutive repeated characters with a marker that denotes the character and the number of occurrences.
     For example, if the algorithm finds "AAAAA" in the text, it will be compressed to "A5".

   - Huffman Coding:
     After RLE, the algorithm uses Huffman coding on the pre-processed data. It calculates the frequency of each character in the text and builds a Huffman tree based on these frequencies.
     Characters that appear more frequently will have shorter codes in the Huffman tree, optimizing the compression.

   - Output:
     The compressed output is written to a new file, containing both the Huffman tree (for decompression) and the compressed data.

2. Decompression:

   - Decompression is performed by reading the compressed file and reconstructing the Huffman tree from the header information.
   - The compressed data is then decoded using the Huffman tree and expanded back to its original form using the RLE markers.

## Usage

1. Compressing a Text File:

   To compress a text file, run the `Compressor` class with the input and output file paths as arguments.
