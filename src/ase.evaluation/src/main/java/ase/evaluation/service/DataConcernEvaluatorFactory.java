package ase.evaluation.service;

import ase.evaluation.service.concerns.DataRelevanceConcernEvaluator;
import ase.evaluation.service.concerns.EvaluationPerformanceConcernEvaluator;
import ase.evaluation.service.concerns.SamplingRateConcernEvaluator;
import ase.shared.enums.DataConcernType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Michael on 20.06.2015.
 */
@Component
public class DataConcernEvaluatorFactory {

    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    public DataConcernEvaluatorBase getEvaluator(DataConcernType dataConcernType){

        DataConcernEvaluatorBase dataConcernEvaluatorBase = null;

        switch (dataConcernType) {
            case SamplingRate:
                dataConcernEvaluatorBase = new SamplingRateConcernEvaluator();
                break;
            case DataRelevance:
                dataConcernEvaluatorBase = new DataRelevanceConcernEvaluator();
                break;
            case EvaluationPerformance:
                dataConcernEvaluatorBase = new EvaluationPerformanceConcernEvaluator();
                break;
        }

        autowireCapableBeanFactory.autowireBean(dataConcernEvaluatorBase);

        return dataConcernEvaluatorBase;
    }
}
