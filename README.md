## NewAnce 

NewAnce (https://www.biorxiv.org/content/10.1101/758680v1) is a java software for proteogenomics. First it preformes 
stratified FDR calculation for Comet and then combines the two MS/MS Comet and MaxQuant PSMs by taking their intersection.
This allows to obtain more accurate PSMs even in the case of large proteogenomics databases. Source code and an executable 
.jar file are provided. 

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
 -fdrM,--fdrControlMethod <arg>     Method to control pFDR: global or groupwise (default global).
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

