/**
 * Insert data
 * <p>
 * 
 * @author Matej Crepinsek
 * @version 1
 * 
 *          <h3>License</h3>
 * 
 *          Copyright (c) 2011 by Matej Crepinsek. <br>
 *          All rights reserved. <br>
 * 
 *          <p>
 *          Redistribution and use in source and binary forms, with or without
 *          modification, are permitted provided that the following conditions
 *          are met:
 *          <ul>
 *          <li>Redistributions of source code must retain the above copyright
 *          notice, this list of conditions and the following disclaimer.
 *          <li>Redistributions in binary form must reproduce the above
 *          copyright notice, this list of conditions and the following
 *          disclaimer in the documentation and/or other materials provided with
 *          the distribution.
 *          <li>Neither the name of the copyright owners, their employers, nor
 *          the names of its contributors may be used to endorse or promote
 *          products derived from this software without specific prior written
 *          permission.
 *          </ul>
 *          <p>
 *          THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *          "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *          LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 *          FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 *          COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 *          INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *          BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *          LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *          CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *          LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *          ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *          POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package org.um.feri.ears.tuning;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.tuning.GSA_Tuning;

/**
 * @author Administrator
 * 
 */
public class RunMainTuningMethods {
	
    public static void main(String[] args) {
    	
    	int dim = 5;
        int eval = 1000*dim;
    	
    	double draw = 0.0000000001;
    	int decimals = 10;
    	int runs = 5;  

    	TuningBenchmark b2 = new TuningBenchmark(draw,dim,eval);
    	b2.setDisplayRatingCharts(false);
        
    	// Define: parameters
    	////public GSA(double RPower, double alfa, double G0)

    	ArrayList<ControlParameter> control_parameters = new ArrayList<ControlParameter>();
        control_parameters.add(new ControlParameter("RPower", "double", 1, 3));
        control_parameters.add(new ControlParameter("alfa", "double", 5, 50));
        control_parameters.add(new ControlParameter("G0", "double", 50, 200));
    	
        
        try {
        	CRSTuning m = new CRSTuning(false, false, b2, 500);
        	m.tune(runs,control_parameters,GSA_Tuning.class,"GSA",decimals);
        	//m.tune(runs,control_parameters,"org.um.feri.ears.algorithms.de3.DEAlgorithm","DE",decimals);
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        /*BankOfResults br = m.getBankOfResults();
        FriedmanTransport fr = br.calc4Friedman();
        ArrayList<PlayerAlgorithmExport> vsi = m.getListAll();   
        Friedman.setStatistics(fr.getDatasets(), fr.getAlgoritms(), fr.getMean(), decimals);
        Results[] statistics_results = Friedman.getResults();        
		Vector<String> al = fr.getAlgoritms();
		Vector<String> pr = fr.getDatasets();*/
    }
    
    public static double roundToDecimals(double d, int c)  
    {   
       int temp = (int)(d * Math.pow(10 , c));  
       return ((double)temp)/Math.pow(10 , c);  
    }
}
