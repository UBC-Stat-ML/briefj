package briefj.collections;


import java.io.Serializable;
import java.util.*;




/**
 * Represent a discrete directed tree structure, 
 * with each node consisting of a label and a list of children.
 * @author Dan Klein
 */
public class Tree<L> implements Serializable {
  private static final long serialVersionUID = 1L;
  private L label = null;
  private List<Tree<L>> children;
  



  public List<Tree<L>> getChildren() {
    return children;
  }
  public void setChildren(List<Tree<L>> children) {
    this.children = children;
  }
  public L getLabel() {
    return label;
  }
  public void setLabel(L label) {
    this.label = label;
  }

  public boolean isLeaf() {
    return getChildren().isEmpty();
  }

  /**
   * Recursively visit the nodes under to collect all the leaves under this node
   * @return 
   */
  public List<L> getYield() {
    List<L> yield = new ArrayList<L>();
    appendYield(this, yield);
    return yield;
  }

  private static <L> void appendYield(Tree<L> tree, List<L> yield) {
    if (tree.isLeaf()) {
      yield.add(tree.getLabel());
      return;
    }
    for (Tree<L> child : tree.getChildren()) {
      appendYield(child, yield);
    }
  }



  public List<Tree<L>> getPreOrderTraversal() {
    ArrayList<Tree<L>> traversal = new ArrayList<Tree<L>>();
    traversalHelper(this, traversal, true);
    return traversal;
  }

  public List<Tree<L>> getPostOrderTraversal() {
    ArrayList<Tree<L>> traversal = new ArrayList<Tree<L>>();
    traversalHelper(this, traversal, false);
    return traversal;
  }

  private static <L> void traversalHelper(Tree<L> tree, List<Tree<L>> traversal, boolean preOrder) {
    if (preOrder)
      traversal.add(tree);
    for (Tree<L> child : tree.getChildren()) {
      traversalHelper(child, traversal, preOrder);
    }
    if (! preOrder)
      traversal.add(tree);
  }

  public List<Tree<L>> toSubTreeList() {
    return getPreOrderTraversal();
  }


  public String toString() {
    StringBuilder sb = new StringBuilder();
    toStringBuilder(sb);
    return sb.toString();
  }

  public void toStringBuilder(StringBuilder sb) {
    if (! isLeaf()) sb.append('(');
    if (getLabel() != null) {
      sb.append(getLabel());
    }
    if (! isLeaf()) {
      for (Tree<L> child : getChildren()) {
        sb.append(' ');
        child.toStringBuilder(sb);
      }
      sb.append(')');
    }
  }

  public Tree<L> deepCopy() {
    return deepCopy(this);
  }

  private static <L> Tree<L> deepCopy(Tree<L> tree) {
    List<Tree<L>> childrenCopies = new ArrayList<Tree<L>>();
    for (Tree<L> child : tree.getChildren()) {
      childrenCopies.add(deepCopy(child));
    }
    return new Tree<L>(tree.getLabel(), childrenCopies);
  }

  public Tree(L label, List<Tree<L>> children) {
    this.label = label;
    this.children = children;
  }
  public Tree() {
    this.label = null;
    this.children = new ArrayList<Tree<L>>();
  }
  public Tree(L label) {
    this.label = label;
    this.children = new ArrayList<Tree<L>>();
  }

}
