/*
  Copyright (C) 2003 EBI, GRL
 
  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.
 
  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.
 
  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.ensembl.healthcheck.testcase;

/**
 * EnsEMBL Healthcheck test case that ensures that the results of the SQL
 * query <code>DESCRIBE external_db</code> are the same for a set of databases.
 */

public class ExternalDBDescribeTestCase extends EnsTestCase {
  
  /** Creates a new instance of ExternalDBDescriptionTestCase */
  public ExternalDBDescribeTestCase() {
  }
  
  TestResult run() {
    
    boolean result = super.checkSameSQLResult("DESCRIBE external_db");
    
    return new TestResult(getShortTestName(), result, "");
  }
  
}
