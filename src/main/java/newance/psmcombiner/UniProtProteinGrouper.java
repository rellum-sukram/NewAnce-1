/**
 * Copyright (C) 2019, SIB/LICR. All rights reserved
 *
 * SIB, Swiss Institute of Bioinformatics
 * Ludwig Institute for Cancer Research (LICR)
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. Neither the name of the SIB/LICR nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL SIB/LICR BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package newance.psmcombiner;

import newance.proteinmatch.UniProtDB;
import newance.psmconverter.PeptideSpectrumMatch;
import newance.util.PsmGrouper;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Markus Müller
 */

public class UniProtProteinGrouper extends PsmGrouper {
    private final UniProtDB uniProtDB;
    private final String masterGroup;
    private final String otherGroup;

    public UniProtProteinGrouper(UniProtDB uniProtDB, String otherGroup) {
        this.uniProtDB = uniProtDB;
        this.masterGroup = "UniProt";
        this.otherGroup = otherGroup;
    }

    public UniProtProteinGrouper(UniProtDB uniProtDB, String otherGroup, String masterGroup) {
        this.uniProtDB = uniProtDB;
        this.masterGroup = masterGroup;
        this.otherGroup = otherGroup;
    }

    @Override
    public String apply(String s, PeptideSpectrumMatch psm) {
        String seq = psm.getPeptide().toSymbolString();
        if (psm.isDecoy()) seq = reverse(seq);

        if (uniProtDB.contains(seq.toCharArray()))
            return masterGroup;
        else
            return otherGroup;
    }

    @Override
    public String getMasterGroup() {
        return masterGroup;
    }

    @Override
    public Set<String> getGroups() {
        Set<String> groups = new HashSet<>();
        groups.add(masterGroup);
        groups.add(otherGroup);

        return groups;
    }

    protected static String reverse(String seq) {

        char[] array = seq.toCharArray();
        for (int i = 0; i<array.length/2; i++) {
            char tmp = array[i];
            array[i] = array[array.length-1-i];
            array[array.length-1-i] = tmp;
        }

        return new String(array);
    }

}
