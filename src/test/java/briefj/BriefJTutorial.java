package briefj;

import tutorialj.Tutorial;



public class BriefJTutorial //implements Runnable
{
  /**
   * Summary [![Build Status](https://travis-ci.org/alexandrebouchard/briefj.png?branch=master)](https://travis-ci.org/alexandrebouchard/briefj)
   * -------
   * 
   * briefj contains utilities for writing succinct java.
   * 
   * 
   * Installation
   * ------------
   * 
   * Prerequisite software:
   * 
   * - Java SDK 1.6+
   * - Gradle version 1.9+ (not tested on Gradle 2.0)
   * 
   * There are several options available to install the package:
   * 
   * ### Integrate to a gradle script
   * 
   * Simply add the following lines (replacing 1.0.0 by the current version (see git tags)):
   * 
   * ```groovy
   * repositories {
   *  mavenCentral()
   *  jcenter()
   *  maven {
   *     url "http://www.stat.ubc.ca/~bouchard/maven/"
   *   }
   * }
   * 
   * dependencies {
   *   compile group: 'ca.ubc.stat', name: 'briefj', version: '1.0.0'
   * }
   * ```
   * 
   * ### Compile using the provided gradle script
   * 
   * - Check out the source ``git clone git@github.com:alexandrebouchard/briefj.git``
   * - Compile using ``gradle installApp``
   * - Add the jars in ``build/install/briefj/lib/`` into your classpath
   * 
   * ### Use in eclipse
   * 
   * - Check out the source ``git clone git@github.com:alexandrebouchard/briefj.git``
   * - Type ``gradle eclipse`` from the root of the repository
   * - From eclipse:
   *   - ``Import`` in ``File`` menu
   *   - ``Import existing projects into workspace``
   *   - Select the root
   *   - Deselect ``Copy projects into workspace`` to avoid having duplicates
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
  

}
