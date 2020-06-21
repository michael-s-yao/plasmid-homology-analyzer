/*******************************************************************************
 * Copyright 2020 Michael S. Yao
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

import java.util.*;

public class Program {
    private String sequence;

    private int seqLength;

    private int homologyLength;

    private double match;

    public Program(String sequence, int homoLength, double match) {
        this.sequence = sequence;

        this.homologyLength = homoLength;

        this.match = match;

        this.seqLength = sequence.length();
    }

    private String subSequence(int start, int length) {
        StringBuilder subSeq = new StringBuilder();

        char[] array = this.sequence.toCharArray();

        for (int i = 0; i < length; i++) {
            subSeq.append(array[((start + i) % this.seqLength)]);
        }

        return subSeq.toString();
    }

    private ArrayList<Integer[]> checkRecomb(int start) {
        ArrayList<Integer[]> hom = new ArrayList<Integer[]>();

        int constraint = (int)(this.homologyLength * this.match);
        int counter = 0;

        if (start + (2 * this.homologyLength) >= this.seqLength) {
            return hom;
        }

        char[] argArray = this.subSequence(start, 
                                            this.homologyLength).toCharArray();
        for (int i = (start + (2 * this.homologyLength)) % this.seqLength; 
                i < this.seqLength; i++) {
            char[] compareArray = this.subSequence(i, 
                                            this.homologyLength).toCharArray();
            for (int j = 0; j < this.homologyLength; j++) {
                if (argArray[j] == compareArray[j]) {
                    counter++;
                }
            }

            if (counter >= constraint) {
                int index = 0;

                while (counter >= (int)((this.homologyLength + index) * 
                                            this.match)) {
                    if (this.subSequence(start + this.homologyLength + 
                            index + 1, 1).equalsIgnoreCase(this.subSequence(i + 
                            this.homologyLength + index + 1, 1))) {
                        counter++;
                    }

                    index++;
                }

                Integer[] entry = new Integer[2];
                entry[0] = i;
                entry[1] = this.homologyLength + index;
                hom.add(entry);
                
                i += (this.homologyLength + index - 1);
            }

            counter = 0;
        }

        return hom;
    }

    private Hashtable<Integer, ArrayList<Integer[]>> compareAll() {
        Hashtable<Integer, ArrayList<Integer[]>> comparison = new Hashtable<>();

        for (int i = 0; i < this.seqLength; i++) {
            ArrayList<Integer[]> res = this.checkRecomb(i);

            if (res.size() > 0) {
                comparison.put(i, res);

                int skip = 0;
                for (int j = 0; j < res.size(); j++) {
                    if (res.get(j)[1] > skip) { skip = res.get(j)[1]; }
                }
                i += skip;
            }

        }

        return comparison;
    }
    
    private Hashtable<ArrayList<Integer>, Integer> sort() {
        Hashtable<Integer, ArrayList<Integer[]>> comparisons = compareAll();
        
        Hashtable<ArrayList<Integer>, Integer> sorted = new Hashtable<>();
        
        for (int key : comparisons.keySet()) {
            int present = -1;

            for (ArrayList<Integer> set : sorted.keySet()) {
                if (set.contains(key)) {
                    for (Integer[] homoKey : comparisons.get(key)) {
                        if (!set.contains(homoKey[0])) { set.add(homoKey[0]); }
                    }

                    present = 1;
                }

                else {
                    for (Integer[] homoKey: comparisons.get(key)) {
                        if (set.contains(homoKey[0])) {
                            if (!set.contains(key)) { set.add(key); }
                            for (Integer[] v : comparisons.get(key)) {
                                if (!set.contains(v[0])) { set.add(v[0]); }
                            }

                            present = 1;
                        }
                    }
                }
            }

            if (present == -1) {
                ArrayList<Integer> newSort = new ArrayList<>();
                newSort.add(key);
                
                int homoLength = -1;
                for (Integer[] val: comparisons.get(key)) {
                    newSort.add(val[0]);
                    
                    homoLength = val[1];
                }
                sorted.put(newSort, homoLength);
            }
        }

        return sorted;
    }

    public String testHomology() {
        StringBuilder retString = new StringBuilder();

        for (ArrayList<Integer> set : this.sort().keySet()) {
            retString.append("GROUP: <br><br>");

            for (Integer index : set) {
                retString.append(index + ": ..." + 
                    this.subSequence(index, this.sort().get(set)) + "...<br>");
            }

            retString.append("<br><br>");
        }
        
        return retString.toString();
    }
}