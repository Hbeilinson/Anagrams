/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//Just finished Milestone 2
package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import android.util.Log;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 4;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private Integer wordLength = DEFAULT_WORD_LENGTH;
    //private ArrayList<String> wordList = new ArrayList<String>();
    private HashSet wordSet = new HashSet();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<Integer, ArrayList<String>>();


    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        String sortedWord = new String();
        while((line = in.readLine()) != null) {
            String word = line.trim();
            sortedWord = sortLetters(word);
            if (lettersToWord.containsKey(sortedWord)) {
                lettersToWord.get(sortedWord).add(word);
                lettersToWord.put(sortedWord, lettersToWord.get(sortedWord));
            } else {
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(word);
                lettersToWord.put(sortedWord, temp);
            }
            if (sizeToWords.containsKey(word.length())) {
                sizeToWords.get(word.length()).add(word);
                sizeToWords.put(word.length(), sizeToWords.get(word.length()));
            } else {
                ArrayList<String> tempSize = new ArrayList<String>();
                tempSize.add(word);
                sizeToWords.put(word.length(), tempSize);
            }
            wordSet.add(word);
            //wordList.add(word);

        }
    }

    public boolean isGoodWord(String word, String base) {
        if (!wordSet.contains(word)) {
            return false;
        }
        int baseSize = base.length();
        while (word.length() >= baseSize) {
            if (word.substring(0, baseSize).equals(base)) {
                return false;
            } else {
                word = word.substring(baseSize);
            }
        }
        return true;
    }

    public List<String> getAnagrams(String targetWord) {
        String sortedTargetWord = new String();
        sortedTargetWord = sortLetters(targetWord);
        return lettersToWord.get(sortedTargetWord);


        /*
        ArrayList<String> result = new ArrayList<String>();
        String sortedTargetWord = new String();
        sortedTargetWord= sortLetters(targetWord);
        String sortedI = new String();
        for (int i = 0; i < wordList.size(); i++) {
            sortedI = sortLetters(wordList.get(i));
            if (sortedI.length() == sortedTargetWord.length()) {
                if (sortedI == sortedTargetWord) {
                    result.add(wordList.get(i));
                }
            }
        }
        return result;
        */
    }

    private String sortLetters(String word) {
        char tempArray[] = word.toCharArray();
        Arrays.sort(tempArray);
        return new String(tempArray);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        Log.d("HANNAH", word);
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> anagramsOfWord = lettersToWord.get(sortLetters(word)); //For some reason this is null. I don't know why.
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for (int i = 0; i < anagramsOfWord.size(); i++) {
            String currentAnagram = anagramsOfWord.get(i);
            for (int n = 0; n < alphabet.length; n++) {
                String currentAnagramPlusLetter = currentAnagram + alphabet[n];
                String sortedCAPL = sortLetters(currentAnagramPlusLetter);
                if (lettersToWord.containsKey(sortedCAPL)) {
                    result.addAll(lettersToWord.get(sortedCAPL));
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        Random rand = new Random();
        int  index = rand.nextInt(sizeToWords.get(wordLength).size());
        int n = 0;
        while (n < sizeToWords.get(wordLength).size()) {
            Log.d("HANNAH", sizeToWords.get(wordLength).get(index));
            if (lettersToWord.get(sortLetters(sizeToWords.get(wordLength).get(index))).size() >= MIN_NUM_ANAGRAMS) {
                Log.d("HANNAH", "point 1");
                if (!(wordLength == MAX_WORD_LENGTH)) {
                    Log.d("HANNAH", "point 2");
                    wordLength++;
                }
                return sizeToWords.get(wordLength).get(index);
            } else {
                Log.d("HANNAH", "point 3");
                if (index == sizeToWords.get(wordLength).size() - 1) {
                    Log.d("HANNAH", "point 4");
                    index = 0;
                } else {
                    Log.d("HANNAH", "point 5");
                    index++;
                }
            }
            n++;
        }
        if (!(wordLength == MAX_WORD_LENGTH)) {
            wordLength++;
        }
        return "wrong";

    }
}
