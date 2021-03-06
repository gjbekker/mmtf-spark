package edu.sdsc.mmtf.spark.filters.demos;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import edu.sdsc.mmtf.spark.filters.ExperimentalMethods;
import edu.sdsc.mmtf.spark.io.MmtfReader;

/**
 * Example how to filter PDB entries by experimental methods.
 * To learn more about <a href="http://pdb101.rcsb.org/learn/guide-to-understanding-pdb-data/methods-for-determining-structure">experimental methods</a> 
 * 
 * @author Peter Rose
 *
 */
public class FilterByExperimentalMethods {

	public static void main(String[] args) {

		String path = System.getProperty("MMTF_REDUCED");
	    if (path == null) {
	    	    System.err.println("Environment variable for Hadoop sequence file has not been set");
	        System.exit(-1);
	    }
	    
	    SparkConf conf = new SparkConf().setMaster("local[*]").setAppName(FilterByExperimentalMethods.class.getSimpleName());
	    JavaSparkContext sc = new JavaSparkContext(conf);
		 
	    MmtfReader
	    		.readSequenceFile(path, sc) // read MMTF hadoop sequence file
	    		 // filter by experimental methods using joint Neutron/X-RAY diffraction
	    		.filter(new ExperimentalMethods(ExperimentalMethods.NEUTRON_DIFFRACTION, ExperimentalMethods.X_RAY_DIFFRACTION)) 
	    		.keys() // extract the keys (PDB IDs)
	    		.foreach(key -> System.out.println(key)); // print the keys (using a lambda expression)
	    
	    sc.close();
	}
}
