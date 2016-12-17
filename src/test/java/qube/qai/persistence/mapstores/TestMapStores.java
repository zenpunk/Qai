package qube.qai.persistence.mapstores;

import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import qube.qai.main.QaiTestBase;
import qube.qai.persistence.StockEntity;
import qube.qai.persistence.StockEntityId;
import qube.qai.persistence.WikiArticle;
import qube.qai.procedure.Procedure;
import qube.qai.services.ProcedureSourceInterface;
import qube.qai.services.SearchServiceInterface;
import qube.qai.services.implementation.SearchResult;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * Created by rainbird on 11/19/15.
 */
public class TestMapStores extends QaiTestBase {

    @Inject
    private ProcedureSourceInterface procedureSource;

    @Inject @Named("Wikipedia_en")
    private SearchServiceInterface searchService;

    private String testDirectory = "./test/procedures/";

    private String testWikiArchive = "/media/rainbird/ALEPH/wiki-archives/wikipedia_en.zip";

    /**
     * test wiki-article map-store
     * @throws Exception
     */
    public void testWikiArticleMapStore() throws Exception {

        // well, this is all there is to it really...
        String[] someWikiArticles = {"mickey mouse", "mouse", "crow", "stock market"};

        WikiArticleMapStore mapStore = new WikiArticleMapStore(testWikiArchive);
        for (String name : someWikiArticles) {
            Collection<SearchResult> results = searchService.searchInputString(name, "title", 100);
            SearchResult result = results.iterator().next();
            logger.info("searching result: " + result.getTitle() + " with " + result.getRelevance() + " %relevance");
            WikiArticle article = mapStore.load(result.getFilename());
            assertNotNull("there has to be something", article);
        }

    }

    /**
     * in this case we will be storing away the procedures on
     * file-system... in xml-format, with x-stream, so that there are
     * no hassles because of serializable
     * @throws Exception
     */
    public void testDirectoryMapStore() throws Exception {

        // begin with creating the thing
        DirectoryMapStore mapStore = new DirectoryMapStore(testDirectory);

        String[] names = procedureSource.getProcedureNames();
        List<String> uuidList = new ArrayList<String>();
        Map<String, Procedure> procedures = new HashMap<String, Procedure>();
        for (String name : names) {
            Procedure procedure = procedureSource.getProcedureWithName(name);
            String uuid = procedure.getUuid();
            procedures.put(uuid, procedure);
            uuidList.add(uuid);
            logger.info("storing procedure with uuid: " + uuid);
            mapStore.store(uuid, procedure);
        }

        Iterable<String> keys = mapStore.loadAllKeys();
        for (String uuid : uuidList) {
            if (procedures.containsKey(uuid)) {
                logger.info("uuid " + uuid + " was not found on the list of keys which are available on map-store");
                continue;
            }
            Procedure procedure = procedures.get(uuid);
            logger.info("procedure: " + procedure.getName() +" with uuid: " + uuid + " has not been written on file-system");
        }

        logger.info("writing was easy, and is done. now comes reading that was written:");
        // now read the things back
        for (String uuid : uuidList) {
            logger.info("loading procedure with uuid: " + uuid);
            Procedure procedure = mapStore.load(uuid);
            assertNotNull("procedure cannot be null", procedure);
            if (procedure == null) {
                logger.info("loading procedure with uuid: " + uuid + " has failed");
            } else {
                logger.info("procedure: " + procedure.getName() + " was loaded alright");
            }
        }

        // we are almost there, we now delete the things
        for (String uuid : uuidList) {
            logger.info("deleting procedure with uuid: " + uuid);
            mapStore.delete(uuid);
        }
    }


}
