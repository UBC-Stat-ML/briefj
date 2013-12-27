package briefj;

import tutorialj.Tutorial;


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
@Tutorial(startTutorial = "README.md", nextStep = BriefIOTutorial.class)
public class BriefJTutorial
{

}
