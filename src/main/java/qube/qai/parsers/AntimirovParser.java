package qube.qai.parsers;

import info.bliki.wiki.template.expr.Scanner;
import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.functors.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qube.qai.parsers.antimirov.IncompleteTypeException;
import qube.qai.parsers.antimirov.nodes.*;

import java.util.Iterator;
import java.util.List;

/**
 * Created by rainbird on 9/27/16.
 */
public class AntimirovParser {

    private static Logger logger = LoggerFactory.getLogger("AntimirovParser");

    protected BaseNode previousNode;

    protected BaseNode currentNode;

    public BaseNode parseExpression(String expressionString) {
        return null;
    }

    public Parser<BaseNode> name() {
        return Scanners.IDENTIFIER
                .map(new org.codehaus.jparsec.functors.Map<String, BaseNode>() {
            @Override
            public BaseNode map(String s) {
                Name name = new Name(s);
                BaseNode node = null;
                if (Name.BOOLEAN.equals(s)
                        || "int".equals(s)
                        || Name.INTEGER.equals(s)
                        || Name.DECIMAL.equals(s)
                        || Name.DOUBLE.equals(s)
                        || Name.STRING.equals(s)) {
                    node = new PrimitiveNode(name);
                    if (currentNode != null)  {
                        currentNode.setFirstChild(node);
                    }
                } else if ("e".equals(s)) {
                    node = new EmptyNode();
                } else {
                    try {
                        node = new Node(name);
                    } catch (IncompleteTypeException e) {
                        logger.error("AntimirovParser.name() threw IncompleteTypeException:", e);
                    }
                }
                //
                popNodes(node);

                return node;
            }
        });
    }

    public Parser<BaseNode> typedName() {
        return name().followedBy(type());
    }

    public Parser<BaseNode> type() {
        return Parsers.between(Scanners.string("["),
                name(),
                Scanners.string("]"));
    }

    public Parser<BaseNode> element() {
        return Parsers.or(typedName(), name());
    }

    public Parser<BaseNode> spaceElement() {
        return Scanners.WHITESPACES.followedBy(element()).map(new Map<Void, BaseNode>() {
            @Override
            public BaseNode map(Void aVoid) {
                return previousNode;
            }
        });
    }

    public Parser<BaseNode> concatenation() {
        return element().next(spaceElement().many()).map(new Map() {
            @Override
            public Object map(Object o) {
                ConcatenationNode node = null;
                List<BaseNode> children = null;
                if (o instanceof List) {
                    children = (List<BaseNode>) o;
                } else {
                    throw new IllegalArgumentException("parse doesn't follow");
                }
                for (Iterator<BaseNode> it = children.iterator(); it.hasNext(); ) {
                    BaseNode child = it.next();
                    try {
                        if (child != null) {
                            node = new ConcatenationNode(child, previousNode);
                        } else {
                            node = new ConcatenationNode(previousNode, currentNode);
                        }
                    } catch (IncompleteTypeException e) {
                        logger.error("AntimirovParser.concat() threw IncompleteTypeException:", e);
                    }
                    popNodes(node);
                }
                return node;
            }
        });
    }

    public Parser<BaseNode> iteration() {
        return element().followedBy(Scanners.string("*")).map(new Map<BaseNode, BaseNode>() {
            @Override
            public BaseNode map(BaseNode baseNode) {
                IterationNode node = null;
                try {
                    node = new IterationNode(baseNode);
                } catch (IncompleteTypeException e) {
                    logger.error("AntimirovParser.alternation() threw IncompleteTypeException", e);
                }
                popNodes(node);
                return node;
            }
        });
    }

    private void popNodes(BaseNode node) {
        previousNode = currentNode;
        currentNode = node;
    }
}
