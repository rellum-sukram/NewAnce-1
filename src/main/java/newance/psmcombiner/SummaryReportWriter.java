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

import newance.psmconverter.PeptideSpectrumMatch;
import newance.util.NewAnceParams;
import newance.util.ProcessPsmUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Markus Müller
 */

public class SummaryReportWriter {
    private final String fileName;
    private BufferedWriter reportWriter;


    public SummaryReportWriter(String fileName, boolean includeMaxQuant) {

        this.fileName = fileName;
        try {
            reportWriter = new BufferedWriter(new FileWriter(new File(fileName)));
            writeHeader(includeMaxQuant);

        } catch (IOException e) {
            System.out.println("Unable to writeHistograms to file "+ fileName);
            reportWriter = null;
        }
    }

    private void writeHeader(boolean includeMaxQuant) throws IOException {
        reportWriter.write(NewAnceParams.getInstance().toString()+"\n\n");
        if (includeMaxQuant) reportWriter.write("Group\tNrCometPSMs\tNrMaxQuantPSMs\tNrCombinedPSMs\tNrCometPepts\tNrMaxQuantPepts\tNrCombinedPepts\n");
        else reportWriter.write("Group\tNrCometPSMs\tNrCometPepts\n");
    }

    public void write(String group,
                      ConcurrentHashMap<String, List<PeptideSpectrumMatch>> combinedPsms,
                      ConcurrentHashMap<String, List<PeptideSpectrumMatch>>  cometPsms,
                      ConcurrentHashMap<String, List<PeptideSpectrumMatch>>  maxQuantPsms) {

        if (reportWriter==null) return;

        ConcurrentHashMap<String, List<PeptideSpectrumMatch>>  noDecoycombinedPsms = ProcessPsmUtils.removeDecoys(combinedPsms);
        ConcurrentHashMap<String, List<PeptideSpectrumMatch>>  noDecoyCometPsms = ProcessPsmUtils.removeDecoys(cometPsms);
        ConcurrentHashMap<String, List<PeptideSpectrumMatch>>  noDecoyMaxQuantPsms = ProcessPsmUtils.removeDecoys(maxQuantPsms);

        try {
            reportWriter.write(group+"\t"+ProcessPsmUtils.countPsms(noDecoyCometPsms)+"\t"+ ProcessPsmUtils.countPsms(noDecoyMaxQuantPsms)+"\t"+
                    ProcessPsmUtils.countPsms(noDecoycombinedPsms)+"\t"+ProcessPsmUtils.countUniquePeptides(noDecoyCometPsms)+"\t"+
                    ProcessPsmUtils.countUniquePeptides(noDecoyMaxQuantPsms)+"\t"+ ProcessPsmUtils.countUniquePeptides(noDecoycombinedPsms)+"\n");

        } catch (IOException e) {
        }
    }


    public void write(String group, ConcurrentHashMap<String, List<PeptideSpectrumMatch>> cometPsms) {

        if (reportWriter==null) return;

        ConcurrentHashMap<String, List<PeptideSpectrumMatch>>  noDecoyCometPsms = ProcessPsmUtils.removeDecoys(cometPsms);

        try {
            reportWriter.write(group+"\t"+ProcessPsmUtils.countPsms(noDecoyCometPsms)+"\t"+ProcessPsmUtils.countUniquePeptides(noDecoyCometPsms)+"\n");

        } catch (IOException e) {
        }
    }

    public void close() {
        try {
            if (reportWriter!=null) reportWriter.close();
            reportWriter = null;
        } catch (IOException e) {
            System.out.println("Unable to close file "+ fileName);
        }
    }

}
