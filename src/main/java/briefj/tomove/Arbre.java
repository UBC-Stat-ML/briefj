package briefj.tomove;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import briefj.BriefCollections;
import briefj.BriefMaps;
import briefj.collections.Tree;



public final class Arbre<T>  implements Serializable
{
  private static final long serialVersionUID = 7400624745947208022L;

  
  public static <T> Arbre<T> findFirstNodeWithContents(Arbre<T> a, T contents)
  {
    for (Arbre<T> node : a.nodes())
      if (node.getContents().equals(contents))
        return node;
    return null;
  }
 // 
  public static <T> Arbre<T> tree2Arbre(Tree<T> t)
  {
    List<Arbre<T>> children = new ArrayList<Arbre<T>>();
    for (Tree<T> child : t.getChildren())
      children.add(tree2Arbre(child));
    return arbre(t.getLabel(), children);
  }
  
  public static <T> Tree<T> arbre2Tree(Arbre<T> a)
  {
    List<Tree<T>> children = new ArrayList<Tree<T>>();
    for (Arbre<T> child : a.getChildren())
      children.add(arbre2Tree(child));
    return new Tree<T>(a.getContents(), children);
  }
  
  public static <T> Map<T, T> parents(Arbre<T> a)
  {
    Map<T,T> result = new HashMap<T,T>();
    for (Arbre<T> node : a.nodes())
      if (!node.isRoot())
        result.put(node.getContents(), node.getParent().getContents());
    return result;
  }
  
//  
//  /**
//   * Warning: assumes every node in the tree is distinct in the .equals,.hashcode 
//   * sense
//   * @param <T>
//   * @param childMap
//   * @param root
//   * @return
//   */
//  public static <T> Arbre<T> childMap2Arbre(Map<T,Set<T>> childMap, T root)
//  {
//    List<Arbre<T>> children = new ArrayList<Arbre<T>>();
//    if (childMap.containsKey(root))
//      for (T childContent : childMap.get(root))
//        children.add(childMap2Arbre(childMap, childContent));
//    return new Arbre<T>(root, children);
//  }
//  /**
//   * Warning: assumes every node contents in the tree is distinct in the .equals,.hashcode 
//   * sense
//   * @param <T>
//   * @param childMap
//   * @param root
//   * @return
//   */
//  public static <T> Arbre<T> parentMap2Arbre(Map<T,T> parentMap)
//  {
//    T root = findRoot(parentMap);
//    Arbre<T> result = parentMap2Arbre(parentMap, root);
//    if (result.nodes().size() != parentMap.size() + 1)
//      throw new RuntimeException("forest instead of tree? " + 
//          result.nodes().size() + "," + parentMap.size());
//    return result;
//  }
//  public static <T> Arbre<T> parentMap2Arbre(Map<T,T> parentMap, T root)
//  {
//    Map<T,Set<T>> childMap = CollUtils.invert(parentMap);
//    Arbre<T> result =  childMap2Arbre(childMap, root);
//    return result;
//  }
//  private static <T> T findRoot(Map<T,T> parentMap)
//  {
//    T result = parentMap.keySet().iterator().next(); 
//    while (parentMap.containsKey(result))
//      result = parentMap.get(result);
//    return result;
//  }
  public static <S,T> Arbre<T> map(Arbre<S> src, final Map<S,T> map)
  {
    return src.preOrderMap(new ArbreMap<S,T>() {

      @Override
      public T map(Arbre<S> currentDomainNode)
      {
        return map.get(currentDomainNode.getContents());
      }
    });
  }
//  private static <T> Arbre<List<T>> arbreSet(Arbre<T> arbre)
//  {
//    return arbre.postOrderMap(new ArbreMap<T,List<T>>(){
//      @Override public List<T> map(Arbre<T> currentDomainNode) {
//        List<T> result = new ArrayList<T>();
//        result.add(currentDomainNode.getContents());
//        for (List<T> childSet : getChildImage())
//          result.addAll(childSet);
//        return result;
//      }
//    });
//  }
  public static <T> Map<Arbre<T>, Set<T>> descMap(Arbre<T> arbre)
  {
    Map<Arbre<T>,Set<T>> result = new HashMap<Arbre<T>,Set<T>>();
    for (Arbre<T> node : arbre.nodes())
    {
      Set<T> current = new HashSet<T>();
      result.put(node, current);
      for (Arbre<T> subnode : node.nodes())
        current.add(subnode.getContents());
    }
    return result;
  }
  /**
   * @param <T>
   * @param arbre
   * @return
   */
  public static <T> Map<T, Set<T>> leavesMap(Arbre<T> arbre)
  {
    Map<T,Set<T>> result = new HashMap<T,Set<T>>();
    for (Arbre<T> leaf : arbre.leaves())
    {
      Arbre<T> current = leaf;
      do 
        BriefMaps.getOrPutSet(result, current.getContents()).add(leaf.getContents());
      while 
        ((current = current.getParentEasy()) != null);
    }
    
    return result;
  }
//  public static <T> Map<Arbre<T>, Set<T>> leavesMap(Arbre<T> arbre)
//  {
//    Map<Arbre<T>,Set<T>> result = new HashMap<Arbre<T>,Set<T>>();
//    for (Arbre<T> node : arbre.nodes())
//    {
//      Set<T> current = new HashSet<T>();
//      result.put(node, current);
//      for (Arbre<T> subnode : node.nodes())
//        if (subnode.isLeaf())
//         current.add(subnode.getContents());
//    }
//    return result;
//  }
  
//  public static <T> Map<T,Set<T>> debox(Map<Arbre<T>,Set<T>> map)
//  {
//    Map<T,Set<T>> result = CollUtils.map();
//    for (Arbre<T> subt : map.keySet())
//      if (result.containsKey(subt.getContents()))
//        throw new RuntimeException("Some internal node is not unique");
//      else
//        result.put(subt.getContents(), map.get(subt));
//    return result;
//    
//  }
  
  public static<T> Map<Arbre<T>, Set<T>> ancestorMap (Arbre<T> arbre){
	  Map<Arbre<T>,Set<T>> result = new HashMap<Arbre<T>,Set<T>>();
	    for (Arbre<T> node : arbre.nodes())
	    {
	    	Set<T> current = new HashSet<T> ();
	    	result.put(node, current);
	    	current.add(node.getContents());
	    	Arbre<T> tmp = node;
	    	while (!tmp.isRoot()) {
	    		tmp = tmp.getParent();
	    		current.add(tmp.getContents());
	    	}
	    
	    }
	    return result;
  }
  
  public static <T> Map<Arbre<T>, Set<T>> nadMap (Arbre<T> arbre) {
	  Map<Arbre<T>, Set<T>> ancestors = ancestorMap (arbre);
	  Map<Arbre<T>, Set<T>> descendants = descMap (arbre);
	  Map<Arbre<T>, Set<T>> result = new HashMap <Arbre<T>, Set<T>> ();
	  Arbre<T> r = arbre.root();
	  List<Arbre<T> > allNodes = r.nodes();
	  
	  for (Arbre<T> node: arbre.nodes()) {
		Set<T> current = new HashSet<T> ();
		result.put(node, current);
		Set<T> currentAnc = ancestors.get(node);
		Set<T> currentDesc = descendants.get(node);
		
		for (Arbre<T> tmp:allNodes) {
			T content = tmp.getContents();
			if (!(currentAnc.contains(content) || currentDesc.contains(content)))
				current.add(content);
		}
	  }
	  return result;
  }
  /**
   * creates copy
   * @param <T>
   * @param arbre
   * @param elements
   * @return
   */
  public static <T> Arbre<T> lowestCommonAncestor(Arbre<T> arbre,Set<T> elements)
  {
    return lowestCommonAncestor(arbre,descMap(arbre), elements);
  }
  private static <T> Arbre<T> lowestCommonAncestor(Arbre<T> arbre,Map<Arbre<T>, Set<T>> descMap, Set<T> elements)
  {
    // check it intersects
    if (!BriefCollections.intersects(descMap.get(arbre),elements))
      throw new RuntimeException("Should have nonnull inter:" + descMap.get(arbre) + " and " + elements);
    //
    for (Arbre<T> child : arbre.getChildren())
      if (descMap.get(child).containsAll(elements))
        return lowestCommonAncestor(child, descMap, elements);
    return arbre.copy();
  }
  /**
   * Does it in place!
   * Warning: assumes every node contents in the tree is distinct in the .equals,.hashcode 
   * sense
   * @param <T>
   * @param childMap
   * @param root
   * @return
   */
  public static <T> void removeNode(Arbre<T> node)
  {
    if (node.isRoot()) throw new RuntimeException();
    Iterator<Arbre<T>> parentsChildI = node.getParent().children.iterator();
    while (parentsChildI.hasNext())
    {
      Arbre<T> current = parentsChildI.next();
      if (current.getContents().equals(node.getContents()))
      {
        parentsChildI.remove();
        break;
      }
    }
    for (Arbre<T> nodeChild : node.getChildren())
    {
      nodeChild.parent = node.getParent();
      node.getParent().children.add(nodeChild);
    }
    node.parent = null;
    node.children = null;
  }
  /**
   * Warning: assumes every node contents in the tree is distinct in the .equals,.hashcode 
   * sense
   * @param <T>
   * @param childMap
   * @param root
   * @return
   */
  
//  public static <T> Arbre<T> removeUnaries(Arbre<T> t)
//  {
//    if (t.isRoot() && t.getChildren().size() == 1) 
//      throw new RuntimeException();
//  }
//  public static <T> Arbre<T> removeUnaries(Arbre<T> t)
//  {
//    t = t.root();
//    while (t.getChildren().size() == 1)
//      t = t.getChildren().get(0);
//    Arbre<T> result = t.copy();
////    Set<T> nodes = set(result.
//    while (true)
//    {
//      // try to find a unary
//      Arbre<T> unaryNode = null;
//      search:for (Arbre<T> node : result.nodes())
//        if (node.getChildren().size() == 1)
//        {
//          unaryNode = node;
//          break search;
//        }
//      if (unaryNode == null)
//        return result;
//      // remove 
//      Arbre<T> parent = unaryNode.getParent();
//      List<Arbre<T>> children = unaryNode.getChildren();
//      removeNode(unaryNode);
//      if (children.size() == 1)
//        parent.addLeaves(children.get(0));
//      else
//        throw new RuntimeException();
//    }
//  }
  
  private T contents;
  
  private ArrayList<Arbre<T>> children = new ArrayList<Arbre<T>>();
  private Arbre<T> parent;
  
  public T getContents()
  {
    return contents;
  }
  public void setContents(T contents) { this.contents = contents; }
  public boolean isRoot()
  {
    return (parent == null);
  }
  public boolean isLeaf()
  {
    return (children.size() == 0);
  }
  public Arbre<T> getParent()
  {
    if (parent == null) 
      throw new RuntimeException();
    return parent;
  }
  public Arbre<T> getParentEasy() 
  {
    if (parent == null)
      return null;
    return parent;
  }
  public ArrayList<Arbre<T>> forceGetChildren()
  {
    return children;
  }
  public List<Arbre<T>> getChildren()
  {
    return Collections.unmodifiableList(children);
  }
  
  /**
   * Make a copy of the tree rooted at the current node
   * @return
   */
  public Arbre<T> copy()
  {
    Arbre<T> result = arbre(contents);
    for (Arbre<T> child : children)
      result.addLeaves(child.copy());
    return result;
  }
  
  protected Arbre() { ; } // stupid serialization
  public Arbre(final T contents) 
  {
    this.contents = contents;
  }
  public static <T> Arbre<T> arbre(final T contents) { return new Arbre<T>(contents); }  
  public static <T> Arbre<T> arbre(final T contents, final List<Arbre<T>> children)
  {
    return new Arbre<T>(contents, children);
  }
  public static <T> Arbre<T> arbreWithChildren(final T contents, final Arbre<T>... childrenArray)
  {
    List<Arbre<T>> children = new ArrayList<Arbre<T>>();
    for (Arbre<T> child : childrenArray) children.add(child);
    return arbre(contents, children);
  }
  public static <T> Arbre<T> arbre(final T contents, final Arbre<T> parent)
  {
    return new Arbre<T>(contents, parent);
  }
  public Arbre(final T contents, final Arbre<T> parent)
  {
    this(contents);
    this.parent = parent;
    if (parent != null) parent.children.add(this);
  }
  public Arbre(final T contents, final List<Arbre<T>> children)
  {
    this(contents);
    this.children.addAll(children);
    for (Arbre<T> child : children)
      child.parent = this;
  }
  
  /**
   * Add leaves in place and return this for conv.
   * @param arbres
   * @return
   */
  public Arbre<T> addLeaves(Arbre<T>... arbres)
  {
    for (Arbre<T> arbre : arbres)
    {
      children.add(arbre);
      arbre.parent = this;
    }
    return this;
  }
  
  /**
   * The leaves under this node
   * @return
   */
  public List<Arbre<T>> leaves()
  {
    List<Arbre<T>> result = new ArrayList<Arbre<T>>();
    if (children.size() == 0) result.add(this);
    for (Arbre<T> child : children) result.addAll(child.leaves());
    return result;
  }
  public List<T> leaveContents()
  {
    List<T> result = new ArrayList<T>();
    if (children.size() == 0) result.add(this.getContents());
    for (Arbre<T> child : children) result.addAll(child.leaveContents());
    return result;
  }
  
  /**
   * All the nodes under this node, in preorder
   * @return
   */
  public List<Arbre<T>> nodes()
  {
    List<Arbre<T>> result = new ArrayList<Arbre<T>>();
    result.add(this);
    for (Arbre<T> child : children) result.addAll(child.nodes());
    return result;
  }
  public List<T> nodeContents()
  {
    List<T> result = new ArrayList<T>();
    result.add(getContents());
    for (Arbre<T> child : children) result.addAll(child.nodeContents());
    return result;
  }
  public List<Arbre<T>> nodesInPostOrder()
  {
    List<Arbre<T>> result = new ArrayList<Arbre<T>>();
    for (Arbre<T> child : children) result.addAll(child.nodes());
    result.add(this);
    return result;
  }
  
  /** Go up to the parent
   * 
   * @return
   */
  public Arbre<T> root()
  {
    if (parent == null) return this;
    else return parent.root();
  }
  
  @Override
  public String toString() { return deepToLispString(); } //return (contents == null ? null : contents.toString()); }
//  public String deepToString()
//  {
//    final Arbre<T> root = this;
//    return new FancyTreeRenderer(new Populator() {
//      @Override public Object populate() {
//        for (Arbre<T> node : nodes())
//          add(node, node.parent);
//        return root;
//      }
//    }).toString();
//  }
  public String deepToLispString()
  {
    StringBuilder result = new StringBuilder();
    result.append("(");
    result.append((contents == null ? "null" : contents.toString()));
    for (Arbre<T> arbre : getChildren())
      result.append(" " + arbre.deepToLispString());
    result.append(")");
    return result.toString();
  }
  
  public <T2> Arbre<T2> preOrderMap(ArbreMap<T, T2> map) 
  {
    return preOrderMap(map, null);
  }
  private <T2> Arbre<T2> preOrderMap(ArbreMap<T, T2> map, Arbre<T2> parent)
  {
    map.callerImageNode = (parent == null ? null : parent.contents);
    T2 currentImageContents = map.map(this);
    Arbre<T2> currentImageNode = null;
    if (parent == null)
      currentImageNode = new Arbre<T2>(currentImageContents);
    else
      currentImageNode = new Arbre<T2>(currentImageContents, parent);
    for (Arbre<T> child : children) child.preOrderMap(map, currentImageNode);
    return currentImageNode;
  }
//  public static <S1a, S1b, S2> Arbre<S2> preOrderBiMap(ArbreBiMap<S1a, S1b, S2> map,
//      Arbre<S1a> a1, Arbre<S1b> a2)
//  {
//    if (a1 == null && a2 == null) return null;
//    if (a1 == null || a2 == null) throw new RuntimeException();
//    if (a1.getChildren().size() != a2.getChildren().size()) throw new RuntimeException();
//    S2 rootContents = map.map(a1, a2);
//    List<Arbre<S2>> children = new ArrayList<Arbre<S2>>();
//    for (int i = 0; i < a1.getChildren().size(); i++)
//      children.add(preOrderBiMap(map, a1.getChildren().get(i), 
//          a2.getChildren().get(i)));
//    return new Arbre<S2>(rootContents, children);
//  }
  
  public <T2> Arbre<T2> postOrderMap(ArbreMap<T, T2> map)
  {
    List<Arbre<T2>> childrenImageNodes = new ArrayList<Arbre<T2>>();
    List<T2> childrenImageContents = new ArrayList<T2>();
    for (Arbre<T> child : children)
    {
      Arbre<T2> childrenImageContent = child.postOrderMap(map);
      childrenImageNodes.add(childrenImageContent);
      childrenImageContents.add(childrenImageContent.getContents());
    }
    map.childImageNode = childrenImageContents;
    T2 currentImageContents = map.map(this);
    return new Arbre<T2>(currentImageContents, childrenImageNodes);
  }
  public <T2> Arbre<T2> undirectedPreOrderMap(ArbreMap<T, T2> map)
  {
    return undirectedPreOrderMap(map, null, null, false);
  }
  private <T2> Arbre<T2> undirectedPreOrderMap(ArbreMap<T, T2> map, Arbre<T> caller, Arbre<T2> callerImage, boolean callerIsParent)
  {
    map.isCallerParent = callerIsParent;
    map.callerImageNode = (callerImage == null ? null : callerImage.contents);
    T2 currentImageContents = map.map(this);
    Arbre<T2> currentImageNode = null;
    if (caller == null)
      currentImageNode = new Arbre<T2>(currentImageContents);
    else if (callerIsParent)
      currentImageNode = new Arbre<T2>(currentImageContents, callerImage);
    else
      currentImageNode = new Arbre<T2>(currentImageContents, Collections.singletonList(callerImage));
    if (parent != null && caller != this.parent)
      parent.undirectedPreOrderMap(map, this, currentImageNode, false);
    for (Arbre<T> child : this.children)
      if (caller != child)
        child.undirectedPreOrderMap(map, this, currentImageNode, true);
    return currentImageNode;
  }
  
  public static <T> Arbre<String> arbreToArbreOfStrings(Arbre<T> a)
  {
    return a.postOrderMap(
        new ArbreMap<T,String>() {
      @Override public String map(Arbre<T> currentDomainNode)
      {
        return currentDomainNode.getContents().toString();
      }
    });
  }

  public abstract static class ArbreMap<S1, S2>
  {
    private boolean isCallerParent = true;
    private S2 callerImageNode = null;
    private List<S2> childImageNode = null;
    public final boolean isCallerParent() { return isCallerParent; }
    public final S2 getCallerImage() { return callerImageNode; }
    public final List<S2> getChildImage() 
    {
      return Collections.unmodifiableList(childImageNode);
    }
    public abstract S2 map(Arbre<S1> currentDomainNode);
  }
  
//  public abstract static class ArbreBiMap<S1a, S1b, S2>
//  {
//    public abstract S2 map(Arbre<S1a> dom1, Arbre<S1b> dom2);
//  }
  
  public static void main(String [] args)
  {
//    Arbre<String> arbre = arbre("a").addLeaves( arbre("b").addLeaves( arbre("d"), 
//                                                                      arbre("e") ),
//                                                arbre("c").addLeaves( arbre("f")) );
//    System.out.println(arbre.deepToString());
//    System.out.println(lowestCommonAncestor(arbre, new HashSet<String>(Arrays.asList("e"))).deepToString());
//    
////    Arbre<String> arbre2 = arbre.preOrderMap(new AppendPathFromRoot());
////    System.out.println(arbre2.deepToString());
////    
////    Arbre<String> arbre3 = arbre.postOrderMap(new Lispify());
////    System.out.println(arbre3.deepToString());
////    
////    Arbre<String> arbre4 = arbre.getChildren().get(0).getChildren().get(0).undirectedPreOrderMap(new AppendPathFromRoot());
////    System.out.println(arbre4.root().deepToString());
  }
  
  public static class AppendPathFromRoot extends ArbreMap<String, String>
  {
    @Override 
    public String map(Arbre<String> currentDomainNode) 
    {
      if (getCallerImage() == null) return currentDomainNode.getContents();
      return (isCallerParent() ? "" : "-") + currentDomainNode.getContents() + getCallerImage();
    }
  }
  
  public static class Lispify extends ArbreMap<String, String>
  {
    @Override 
    public String map(Arbre<String> currentDomainNode) 
    {
      StringBuilder result = new StringBuilder();
      result.append("(" + currentDomainNode.toString());
      for (String childLisp : getChildImage())
      {
        result.append(" " + childLisp);
      }
      result.append(")");
      return result.toString();
    }
  }

  public int nLeaves()
  {
    int result = 0;
    for (Arbre<T> desc : nodes())
      if (desc.isLeaf())
        result++;
    return result;
  }
  
  public List<Arbre<T>> getLeaves () {
    return leaves();
//	  ArrayList<Arbre<T>> l = new ArrayList<Arbre<T>>();
//	  for (Arbre<T> desc:nodes ()) {
//		  if (desc.isLeaf())
//			  l.add (desc);
//	  }
//	  return l;
  }
  
  public static boolean isAncestorOf (Arbre<String> a1, Arbre<String> a2) {
	  while (true) {
		  if (a1.getContents().equals(a2.getContents())) 
			  return true;
		  if (a1.isRoot())
			  break;
		  a1 = a1.getParent();
	  }
	  return false;
  }
  
  public int nInternalNodes()
  {
    if (isLeaf()) return 0;
    int n = 1;
    for (Arbre<T> child : children)
      n += child.nInternalNodes();
    return n;
  }
}





