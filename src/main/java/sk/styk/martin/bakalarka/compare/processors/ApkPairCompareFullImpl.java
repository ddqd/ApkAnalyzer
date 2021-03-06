package sk.styk.martin.bakalarka.compare.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.styk.martin.bakalarka.analyze.data.ApkData;
import sk.styk.martin.bakalarka.compare.data.ComparisonResult;

/**
 * Full compare of two apks.
 * Works with metadata created during analyze task
 *
 * Created by Martin Styk on 12.01.2016.
 */
public class ApkPairCompareFullImpl implements ApkPairCompare {
    private static final Logger logger = LoggerFactory.getLogger(ApkPairCompare.class);
    private ApkData dataA;
    private ApkData dataB;
    private ComparisonResult comparisonResult;
    private HashPairCompare hashComparator;
    private MetadataPairCompare metadataComparator;
    private SimilarityEvaluator similarityEvaluator;

    public ApkPairCompareFullImpl(ApkData dataA, ApkData dataB) {
        if (dataA == null || dataB == null) {
            throw new IllegalArgumentException("data is null");
        }
        this.dataA = dataA;
        this.dataB = dataB;
        this.hashComparator = new HashPairCompare(dataA.getFileDigest(), dataB.getFileDigest());
        this.metadataComparator = new MetadataPairCompare(dataA, dataB);
        this.similarityEvaluator = new SimilarityEvaluator();
    }

    /**
     * Full compare of two apks.
     *
     * @return result of compare
     */
    public ComparisonResult compare() {

        logger.trace("Start compare " + dataA.getFileName() + " and " + dataB.getFileName());

        comparisonResult = new ComparisonResult(dataA.getFileName(), dataB.getFileName());

        comparisonResult.setMetadataCompareResult(metadataComparator.fullCompare());
        comparisonResult.setHashCompareResult(hashComparator.hashCompare());

        SimilarityType isDetailedSimilar = similarityEvaluator.fullEvaluate(comparisonResult);

        logger.trace("Finished compare " + dataA.getFileName() + " and " + dataB.getFileName() + " with result " + isDetailedSimilar);

        return comparisonResult;
    }

    public SimilarityEvaluator getSimilarityEvaluator() {
        return similarityEvaluator;
    }

    public void setSimilarityEvaluator(SimilarityEvaluator similarityEvaluator) {
        this.similarityEvaluator = similarityEvaluator;
    }
}
