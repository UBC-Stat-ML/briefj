package briefj;

import tutorialj.Tutorial;



public class BriefJTutorial //implements Runnable
{
  /**
   * Summary
   * -------
   * 
   * briefj contains utilities for writing succinct java.
   * 
   * Installation
   * ------------
   * 
   * - Compile using ``gradle installApp``
   * - Add the jars in  ``build/install/briefj/lib/`` to your classpath, OR, add
   * the following to your project gradle script 
   * 
   * ```groovy
   * dependencies {
   *   compile group: 'com.3rdf', name: 'briefj', version: '1.1'
   * }
   * repositories {
   *   mavenCentral()
   *   jcenter()
   *   maven {
   *     url "http://www.stat.ubc.ca/~bouchard/maven/"
   *   }
   * }
   * ```
   * 
   */
  @Tutorial(startTutorial = "README.md", nextStep = BriefIOTutorial.class, showSource = false)
  public static void firstStep()
  {
    
  }
  
  /**
   * 
   */
  @Tutorial(showSource = false, nextStep = BriefCollectionsTutorial.class)
  public static void secondStep() {}
  
  /**
   * 
   */
  @Tutorial(showSource = false, nextStep = BriefStringsTutorial.class)
  public static void thirdStep() {}
  
//  /**
//   * Command line utils
//   * ------------------
//   * 
//   * Currently limited to a thin wrapper around JCommander, for creating command line programs:
//   */
//  @Tutorial(showSource = true)
//  public static void mainExample(String [] args)
//  {
////    start(new BriefJTutorial(), args);
//  }
//
//  @Override
//  public void run()
//  {
//    System.out.println("Execution of the program.");
//  }
}
