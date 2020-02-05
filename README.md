## NewAnce 

NewAnce (https://www.biorxiv.org/content/10.1101/758680v1) is a java software tool for proteogenomics. It performs stratified FDR calculation and combines the two MS/MS search engines Comet and MaxQuant. This allows to obtain accurate PSMs even in the case of large proteogenomics databases. Source code and an executable .jar file are provided. 

### TOC

1. Command line options 
2. Running NewAnce
3. Output format
4. Tests
5. PVD export

#### 1. Command line options 

The NewAnce command line options can be obtained by typing

```
java -jar NewAnce-1.4.0-SNAPSHOT.jar -h
```
which write the command line option infor to standard output :

```
*************************************************************************************************************************
**                                            NewAnce command line help                                                **
*************************************************************************************************************************


usage: newance.psmcombiner.CometMaxQuantCombiner
 -coD,--cometPsmDir <arg>           Comet psm root directory (required)
 -coFDR,--cometFDR <arg>            FDR for filtering Comet PSMs before combination (required)
 -coRE,--cometPsmRegex <arg>        Regular expression of Comet psm files (e.g. \.xml$) (required)
 -exclP,--excludeProts <arg>        Regular expression of proteins excluded from analysis. If not set no proteins are excluded.
 -fdrM,--fdrControlMethod <arg>     Method to control pFDR: combined or seperate (default combined).
 -h,--help                          Help option for command line help
 -maxDC,--maxDeltaCn <arg>          Maximal Comet DeltaCn in histogram (default value 2500)
 -maxL,--maxLength <arg>            Maximal length of peptide (default value: 25)
 -maxR,--maxRank <arg>              Maximal rank of peptide in list of spectrum matches (rank 1 = best) (default value: 1)
 -maxSP,--maxSpScore <arg>          Maximal Comet SpScore in histogram (default value 1)
 -maxXC,--maxXCorr <arg>            Maximal Comet XCorr in histogram (default value 5)
 -maxZ,--maxCharge <arg>            Maximal charge of PSM (default value: 5)
 -minDC,--minDeltaCn <arg>          Minimal Comet DeltaCn in histogram (default value 0)
 -minL,--minLength <arg>            Minimal length of peptide (default value: 8)
 -minPH,--minPsm4Histo <arg>        Minimal number of psms to calculate local FDR in histogram (default value: 100000).
 -minSP,--minSpScore <arg>          Minimal Comet SpScore in histogram (default value 0)
 -minXC,--minXCorr <arg>            Minimal Comet XCorr in histogram (default value 0)
 -minZ,--minCharge <arg>            Minimal charge of PSM (default value: 1)
 -mod,--modifications <arg>         Comma separated list of additional peptide modifications used in search (e.g. Cysteinyl:C3H5NO2S,Oxidation:O)
 -mqD,--maxquantPsmDir <arg>        MaxQuant psm root directory. If not provided only Comet is used.
 -noncG,--noncanonicalGroup <arg>   Name of group with non-canonical or cryptic sequences (default "nonc"). Will be used as prefix for output files.
 -noncP,--noncanonicalProts <arg>   Comma separated list of protein names to be included in noncanonical group even if they are in UniProt (e.g.
                                    PGBD5_HUMAN,POGZ_HUMAN,PGBD1_HUMAN)
 -nrDCB,--nrDeltaCnBins <arg>       Number of Comet DeltaCn bins in histogram (default value 40)
 -nrSPB,--nrSpScoreBins <arg>       Number of Comet SpScore bins in histogram (default value 40)
 -nrTh,--nrThreads <arg>            Number of threads used by NewAnce (default value: nr of available processors - 2)
 -nrXCB,--nrXCorrBins <arg>         Number of Comet XCorr bins in histogram (default value 40)
 -outD,--outputDir <arg>            Output directory for results (required)
 -outT,--outputTag <arg>            Tag inserted into output file names after prefix.
 -ppG,--peptideProteinGrouping      Perform peptide protein grouping export.
 -protG,--proteinGroup <arg>        Name of group with protein coding or canonical sequences (default "prot"). Will be used as prefix for output
                                    files.
 -protRE,--protRegExp <arg>         Regular expression to match fasta name of coding proteins (e.g. sp\||tr\| ).
 -readH,--readHistograms <arg>      Directory where histograms files are placed.
 -repH,--reportHistogram            Report histograms to text files
 -rP,--readParamFile <arg>          Name of file from which parameters should to read.
 -seFa,--searchFastaFile <arg>      Fasta file that was used for the search (required for protein grouping export)
 -smD,--smoothDegree <arg>          Degree of smoothing (0: no smoothing, n: n x smoothing) (default value 1)
 -spRE,--spectrumFilter <arg>       If this option is set, only spectrum ids that match this regexp are used.  If not set no filtering is performed.
 -upFa,--uniProtFastaFile <arg>     Fasta file with coding or canonical proteins (e.g. UniProt fasta file)
 -v,--version                       Version of NewAnce software
 -wP,--write2ParamFile <arg>        Filename where parameters should to written.
```

The comand line options in more detail:

```
-coD,--cometPsmDir <arg>           Comet psm root directory (required)

Directory where Comet pep.xml files are placed. All pep.xml files under this directory including sub-directories 
and matching the -coRE regular expression are considered for the analysis.
```

```
-coFDR,--cometFDR <arg>            FDR for filtering Comet PSMs before combination (required) (default value 0.03)

FDR for Comet search. If -fdrM is equal to "global", the FDR is calculated for all PSM's of all groups and charge states that
pass the local lFDR threshold. If -fdrM is equal to "groupwise", the FDR is calculated seperately for all charge states of the
protein coding and non-canonical group. The former method, which is the one discussed in the paper, is usually less 
conservative than the groupwise method.
```

```
-coRE,--cometPsmRegex <arg>        Regular expression of Comet psm files (e.g. \.xml$) (required)

Regular expression that defines the Comet pep.xml files used in the analysis.
 ```

```
-exclP,--excludeProts <arg>        Regular expression of proteins excluded from analysis. If not set no proteins are excluded.

This option can be used to exclude proteins (e.g. contaminant proteins) from the analysis. A UniProt protein is defined by a
string like sp|Q15800|MSMO1_HUMAN] or tr|G3V568|G3V568_HUMAN. A non-UniProt protein is defined by a string like 
ENSP00000452373.1 (string in fasta header up to the first space or end of line). If the regular expression matches a protein 
string, the protein is excluded.
```

```
-fdrM,--fdrControlMethod <arg>     Method to control pFDR: global or groupwise (default global).

The FDR can be controlled in two ways: global and groupwise. In both cases, the local lFDR is calculated in the same way. The 
lFDR threshold is then adjusted to yield the target FDR, which is estimated for both groups together (combined) or for each 
group seperately (separate). Estimating the FDR seperately for each group usually yields less PSMs.
```

```
-h,--help                          Help option for command line help
```

```
-maxDC,--maxDeltaCn <arg>          Maximal Comet DeltaCn in histogram (default value 2500)

Maximal value of Comet deltaCn score in histogram.
```

```
-maxL,--maxLength <arg>            Maximal length of peptide (default value: 25)

Maximal length for peptides considered by NewAnce.
```

```
-maxR,--maxRank <arg>              Maximal rank of peptide in list of spectrum matches (rank 1 = best) (default value: 1)

Maximal rank for PSMs considered in NewAnce.
```

```
-maxSP,--maxSpScore <arg>          Maximal Comet SpScore in histogram (default value 1)

Maximal value for Comet SPScore in histogram.
```

```
-maxXC,--maxXCorr <arg>            Maximal Comet XCorr in histogram (default value 5)

Maximal value for Comet XCorr in histogram.
```

```
-maxZ,--maxCharge <arg>            Maximal charge of PSM (default value: 5)

Maximal charge for PSMs considered by NewAnce.
```

```
-minDC,--minDeltaCn <arg>          Minimal Comet DeltaCn in histogram (default value 0)
```

```
-minL,--minLength <arg>            Minimal length of peptide (default value: 8)

Minimal length for peptides considered by NewAnce.
```

```
-minPH,--minPsm4Histo <arg>        Minimal number of psms to calculate local FDR in histogram (default value: 100000).

Minimal number of psms to calculate local lFDR in a histogram. If less data points are available, the lFDR estimate is 
considered unreliable and a precalculated default histogram is used instead. The user can also import his/her own default 
histograms (see -readH option).
```

```
-minSP,--minSpScore <arg>          Minimal Comet SpScore in histogram (default value 0)

Minimal value for Comet SpScore in histogram.
```

```
-minXC,--minXCorr <arg>            Minimal Comet XCorr in histogram (default value 0)

Minimal value for Comet XCorr in histogram.
```

```
-minZ,--minCharge <arg>            Minimal charge of PSM (default value: 1)

Minimal charge for PSMs considered by NewAnce.
```

```
-mod,--modifications <arg>         Comma separated list of peptide modifications used in search (e.g. Cysteinyl:C3H5NO2S,Oxidation:O)
 
The user can define a comma separated list of molecules (format name:formula e.g. Cysteinyl:C3H5NO2S, Oxidation:O, 
Carbamidomethyl:C2H3NO, Phospho:HO3P). If the mass of the molecule matches the mass of the modification, the name defined in 
for the modification is used in the output tables. This is useful if the user wants to change the name of a modification or if 
the modification used in the search is not in UniMod (default are UniMod names).
```

```
-mqD,--maxquantPsmDir <arg>        MaxQuant psm root directory. If not provided only Comet is used.

Directory under which MaxQuant msms.txt and peptides.txt files are placed. All msms.txt files under this directory are 
considered for the analysis, i.e. there can be several msms.txt files. NewAnce also requires the peptides.txt files, which 
have to be placed in the same directory as the corresponding msms.txt file.
```

```
-noncG,--noncanonicalGroup <arg>   Name of group with non-canonical or cryptic sequences (default "nonc"). Will be used as 
prefix for con-canonical PSM output files.

Name of group with non-canonical or cryptic sequences (default "nonc"). Will be used as prefix for con-canonical PSM output 
files.
```

```
-noncP,--noncanonicalProts <arg>   Comma separated list of protein names to be included in noncanonical group even if they are in UniProt (e.g.
                                    PGBD5_HUMAN,POGZ_HUMAN,PGBD1_HUMAN)

Some of the proteins in the -upFa fasta file may have to be moved to the noncanonical group. The user can define a list of 
these proteins here.
```

```
-nrDCB,--nrDeltaCnBins <arg>       Number of Comet DeltaCn bins in histogram (default value 40)
 
Number of Comet DeltaCn bins in histogram. The higher this number, the finer the grid cells for FDR calculation and the more 
data is needed. 
```

```
-nrSPB,--nrSpScoreBins <arg>       Number of Comet SpScore bins in histogram (default value 40)
 
Number of Comet SpScore bins in histogram. The higher this number, the finer the grid cells for FDR calculation and the more 
data is needed. 
```

```
-nrTh,--nrThreads <arg>            Number of threads used by NewAnce (default value: nr of available processors - 2)
 
Number of threads assigned for the execution of NewAnce. 
```

```
-nrXCB,--nrXCorrBins <arg>         Number of Comet XCorr bins in histogram (default value 40)

Number of Comet XCorr bins in histogram. The higher this number, the finer the grid cells for FDR calculation and the more 
data is needed. 
```

```
-outD,--outputDir <arg>            Output directory for results (required)

Directors where output files are written to. If output files with the same name already exist, they will be overwritten.
```

```
-outT,--outputTag <arg>            Tag inserted into output file names after prefix.
 
PSM ouput file have the fomat group_tag_NewAncePSMs.txt. The tag can be specified here. 
```

```
-ppG,--peptideProteinGrouping      Perform peptide protein grouping export.

This option has to be set if the protein grouping has to be performed. NewAnce implements a greedy protein grouping algorithm 
described in the publication, where it groups proteins, that share peptides together.
```

```
-protG,--proteinGroup <arg>        Name of group with protein coding or canonical sequences (default "prot"). Will be used as prefix for output
                                    files.
                                    
Name of group with protein coding or canonical sequences (default "prot"). Will be used as prefix for protein coding PSM 
output file.
```

```
-protRE,--protRegExp <arg>         Regular expression to match fasta name of coding proteins (e.g. sp\||tr\| ).

Regular expression (e.g. "sp\||tr\|" or ".*_HUMAN") defining the protein coding fasta entries.
```

```
-readH,--readHistograms <arg>      Directory where histograms files are placed.

Instead of being calculated with the available Comet PSMs, score histograms are imported. This is useful in case there is not 
enough data to build them. NewAnce provides default histograms for high resolution OrbiTrap immunopeptidomics data. Otherwise 
a user can create histograms by analysing a larger datasets 20+ runs and exporing the histograms with the -repH option. When imporing histograms the histogram settings -maxDC,-maxSP,-maxXC,-maxZ,-minDC,-minSP,-minXC,-minZ,-nrDCB,-nrSPB,-nrXCB are overwritten.
```

```
-repH,--reportHistogram            Report histograms to text files

Comet score histograms are written to text files. These histograms can be imported with the -readH option and used as score 
histograms in case there is not enough data to build them.
```

```
-rP,--readParamFile <arg>          Name of file from which parameters should to read.

NewAnce parameters are read from the specified parameter file.
```

```
-seFa,--searchFastaFile <arg>      Fasta file that was used for the search (required for protein grouping export)

Fasta file that was used for Comet search. This is required if the protein grouping option -ppG is set.
```

```
-smD,--smoothDegree <arg>          Degree of smoothing (0: no smoothing, n: n x smoothing) (default value 1)

Comet score histograms can be smoothed, where NewAnce implements a simple nearest neighbour smoothing (i.e. a cell's psm count 
is replaced by the average psm counts of the cell and its 6 nearest neighbours). The smoothing algorithm can be run n times 
(n=0: no smoothing). The higher n the higher the degree of smoothing.
```

```
-spRE,--spectrumFilter <arg>       If this option is set, only spectrum ids that match this regexp are used.  If not set no filtering is performed.

In case NewAnce should be limited to certain spectra, these spectra can be defined with this regular expression. The spectrum 
format used in NewAnce is: spectrum_raw_file_name.scan_number.scan_number.charge 
```

```
-upFa,--uniProtFastaFile <arg>     Fasta file with coding or canonical proteins (e.g. UniProt fasta file)

All peptides matching to a protein sequence in this file are considered as peptides of protein coding genes. Exceptions can be 
defined with the -noncP option.
```

```
-v,--version                       Version of NewAnce software

Show NewAnce version.
```

```
-wP,--write2ParamFile <arg>        Filename where parameters should to written.

Write NewAnce parameters to specified parameter file.
```


#### 2. Running NewAnce

The NewAnce command line options can be obtained by typing

```
java -jar -cp NewAnce-1.4.0-SNAPSHOT.jar newance.psmcombiner.CometMaxQuantCombiner -h
```

The NewAnce version can be obtained by typing

```
java -jar -cp NewAnce-1.4.0-SNAPSHOT.jar newance.psmcombiner.CometMaxQuantCombiner -v
```

Running NewAnce with 12GB memory

```
java -Xmx12G -jar -cp NewAnce-1.4.0-SNAPSHOT.jar newance.psmcombiner.CometMaxQuantCombiner -coD 0D5P/lncRNA/Comet -coRE .*pep.xml$ -mqD 0D5P/lncRNA/MaxQuant -coFDR 0.03 -outD 0D5P/lncRNA/NewAnce -outT 0D5P -protRE sp\||tr\| -protG prot -noncG lncRNA -upFa SeqDBs/human_proteome.fasta -maxR 1 -minZ 1 -maxZ 3 -minL 8 -maxL 15
```


#### 3. Output format

#### 4. Tests

#### 5. PVD export

 
