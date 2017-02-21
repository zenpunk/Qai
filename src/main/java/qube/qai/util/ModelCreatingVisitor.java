package qube.qai.util;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import qube.qai.parsers.antimirov.nodes.*;
import qube.qai.procedure.Procedure;
import qube.qai.procedure.SelectionProcedure;
import qube.qai.procedure.SimpleProcedure;
import qube.qai.procedure.analysis.*;
import qube.qai.procedure.archive.DirectoryIndexer;
import qube.qai.procedure.archive.WikiArchiveIndexer;
import qube.qai.procedure.finance.StockEntityInitialization;
import qube.qai.procedure.finance.StockQuoteRetriever;
import qube.qai.procedure.wikiripper.WikiRipperProcedure;

/**
 * Created by rainbird on 1/28/17.
 */
public class ModelCreatingVisitor implements NodeVisitor {

    private String modelName = "procedure/";

    private String nameSpace = "file:/home/rainbird/projects/work/qai/test/"; //"http://qai.at/"; //http://www.qoan.at/" ;

    private Model model;

    public ModelCreatingVisitor(Model model) {
        this.model = model;
    }

    @Override
    public void visit(AlternationNode node) {
        allVisit(node);
    }

    @Override
    public void visit(ConcatenationNode node) {
        allVisit(node);
    }

    @Override
    public void visit(EmptyNode node) {
        allVisit(node);
    }

    @Override
    public void visit(IterationNode node) {
        allVisit(node);
    }

    @Override
    public void visit(Node node) {
        allVisit(node);
    }

    @Override
    public void visit(NameNode node) {
        allVisit(node);
    }

    @Override
    public void visit(NoneNode node) {
        allVisit(node);
    }

    @Override
    public void visit(PrimitiveNode node) {
        allVisit(node);
    }

    private void allVisit(BaseNode node) {
        Resource resource = model.createResource("http://www.qai.at/procedures:" + node.getUuid());
        resource.addLiteral(model.createProperty(nameSpace, "uuid"), node.getUuid());
        resource.addLiteral(model.createProperty(nameSpace, "name"), node.getNameString());
        resource.addLiteral(model.createProperty(nameSpace, "class"), node.getClass().getName());
        resource.addLiteral(model.createProperty(nameSpace, "hasExecuted"), ((Procedure) node).hasExecuted());
        if (node.getParent() != null) {
            resource.addLiteral(model.createProperty(nameSpace, "parentUuid"), node.getParent().getUuid());
        }
    }

    public static BaseNode createNodeFromName(String name) {

        BaseNode procedure = null;
        SelectionProcedure selection = new SelectionProcedure();

        if (ChangePointAnalysis.NAME.equals(name)) {
            procedure = new ChangePointAnalysis(selection);
        } else if (MarketNetworkBuilder.NAME.equals(name)) {
            procedure = new MarketNetworkBuilder(selection);
        } else if (MatrixStatistics.NAME.equals(name)) {
            procedure = new MatrixStatistics(selection);
        } else if (NetworkStatistics.NAME.equals(name)) {
            procedure = new NetworkStatistics(selection);
        } else if (NeuralNetworkAnalysis.NAME.equals(name)) {
            procedure = new NeuralNetworkAnalysis(selection);
        } else if (NeuralNetworkForwardPropagation.NAME.equals(name)) {
            procedure = new NeuralNetworkForwardPropagation(selection);
        } else if (SortingPercentilesProcedure.NAME.equals(name)) {
            procedure = new SortingPercentilesProcedure(selection);
        } else if (DirectoryIndexer.NAME.equals(name)) {
            procedure = new DirectoryIndexer(selection);
        } else if (WikiArchiveIndexer.NAME.equals(name)) {
            procedure = new WikiArchiveIndexer(selection);
        } else if (StockEntityInitialization.NAME.equals(name)) {
            procedure = new StockEntityInitialization();
        } else if (StockQuoteRetriever.NAME.equals(name)) {
            procedure = new StockQuoteRetriever();
        } else if (WikiRipperProcedure.NAME.equals(name)) {
            procedure = new WikiRipperProcedure();
        } else if (SelectionProcedure.NAME.equals(name)) {
            procedure = new SelectionProcedure();
        } else if (SimpleProcedure.NAME.equals(name)) {
            procedure = new SimpleProcedure();
        }

        return procedure;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}