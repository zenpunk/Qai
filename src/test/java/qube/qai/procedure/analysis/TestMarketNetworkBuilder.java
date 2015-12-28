package qube.qai.procedure.analysis;

import org.encog.ml.data.MLDataPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qube.qai.data.Selector;
import qube.qai.data.selectors.DataSelector;
import qube.qai.data.stores.StockEntityDataStore;
import qube.qai.main.QaiTestBase;
import qube.qai.network.Network;
import qube.qai.network.neural.NeuralNetwork;
import qube.qai.persistence.StockEntity;
import qube.qai.procedure.SimpleProcedure;

import java.util.*;

/**
 * Created by rainbird on 12/25/15.
 */
public class TestMarketNetworkBuilder extends QaiTestBase {

    private Logger logger = LoggerFactory.getLogger("TestMarketNetworkBuilder");
    private String SnP500Page = "List of S&P 500 companies.xml";
    /**
     * well this is actually pretty much it...
     * this is almost the moment of truth we have been waiting for...
     */
    public void testMarketBuilder() throws Exception {

        int numberOfEntities = 10;
        String[] names = new String[numberOfEntities];
        StockEntityDataStore dataStore = new StockEntityDataStore();
        injector.injectMembers(dataStore);

        Collection<StockEntity> entityList = dataStore.fetchEntitesOf(SnP500Page);

        // now we have the list of entities with which we want to build
        // the network for, we can simply pick, say 100 of them
        // and make a trial go with the thing
        Collection<StockEntity> workingSet = pickRandomFrom(numberOfEntities, entityList, names);
        logger.info("picked entities: " + array2String(names));

        Selector<Collection> selector = new DataSelector<Collection>(workingSet);
        SimpleProcedure procedure = new SimpleProcedure();
        MarketNetworkBuilder networkBuilder = new MarketNetworkBuilder(procedure);
        injector.injectMembers(networkBuilder);
        NeuralNetwork network = (NeuralNetwork) networkBuilder.buildNetwork(selector);
        assertNotNull("duh!", network);

        network.getVertices();

        // ok now we take a look at the results
//        for(MLDataPair pair: networkBuilder.getTrainer().getTrainingSet()) {
//            double[] output = network.propagate(pair.getInput().getData());
//            for (int i = 0; i < output.length; i++) {
//                logger.info("entity name: " + names[i]);
//                logger.info("input: " + pair.getInput().getData(i));
//                logger.info("output: " + output[i]);
//                logger.info("ideal: " + pair.getIdeal().getData(i));
//            }
//        }


    }

    private String array2String(String[] names) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < names.length; i++) {
            buffer.append(names[i]).append(" ");
        }
        return buffer.toString();
    }

    private Collection<StockEntity> pickRandomFrom(int number, Collection<StockEntity> original, String[] names) {
        Set<StockEntity> picked = new HashSet<StockEntity>();
        Random random = new Random();
        int addCount = 0;
        while (picked.size() < number) {
            int pick = random.nextInt(original.size());
            Iterator<StockEntity> it = original.iterator();
            for (int j = 0; it.hasNext(); j++) {
                StockEntity entity = it.next();
                if (j == pick) {
                    if (picked.add(entity)) {
                        names[addCount] = entity.getTickerSymbol();
                        addCount++;
                        break;
                    }
                }
            }
        }

        return picked;
    }
}