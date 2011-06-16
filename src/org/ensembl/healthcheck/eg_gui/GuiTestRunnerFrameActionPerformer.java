package org.ensembl.healthcheck.eg_gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

import org.ensembl.healthcheck.DatabaseRegistry;
import org.ensembl.healthcheck.DatabaseRegistryEntry;
import org.ensembl.healthcheck.configuration.ConfigureHost;
import org.ensembl.healthcheck.eg_gui.TestProgressDialog;
import org.ensembl.healthcheck.testcase.EnsTestCase;
import org.ensembl.healthcheck.testcase.PerlScriptConfig;
import org.ensembl.healthcheck.util.DBUtils;

/**
 * 
 * <p>
 * 	Collection of static methods. The actionPerformed method in the 
 * GuiGroupTestRunnerFrame delegates here. It is a collection of code
 * that executes actions the user requested. Put here so it won't clutter
 * the code in GuiGroupTestRunnerFrame.
 * </p>
 * 
 * @author michael
 *
 */
public class GuiTestRunnerFrameActionPerformer {

	/**
	 * <p>
	 * 	This is called when the user has selected a different database server.
	 * </p>
	 * 
	 * <p>
	 * 	Resets the databaseTabbedPane pane with dbDetails. The DBUtils
	 * object is reset in case it was used for accessing a different database
	 * server before this one.
	 * </p>
	 * 
	 * @param databaseTabbedPane
	 * @param dbDetails
	 * 
	 */
	public static void setupDatabasePane(
			DatabaseTabbedPane databaseTabbedPane,
			ConfigureHost dbDetails
	) {
		DBUtils.initialise();
		DBUtils.setHostConfiguration(dbDetails);
		
		List<String> regexps = new ArrayList<String>();
		regexps.add(".*");

		DatabaseRegistry databaseRegistry = new DatabaseRegistry(regexps, null,
				null, false);
		// Should throw an exception which can be caught and displayed 
		// something in a dialog box.
		//
		//if (databaseRegistry.getEntryCount() == 0) {
		//	logger.warning("Warning: no databases found!");
		//}
		
		databaseTabbedPane.init(databaseRegistry);			
		databaseTabbedPane.repaint();
	}

	public static void runTests(
			final JList listOfTestsToBeRun,
			final DatabaseRegistryEntry[] databases,
			final TestProgressDialog testProgressDialog,
			final JComponent resultDisplayComponent
	) {
		runSelectedTests(
			listOfTestsToBeRun, 
			databases, 
			testProgressDialog, 
			resultDisplayComponent,
			null,
			null
		);
	}

	/**
	 * <p>
	 * 	Finds out which tests the user wants to run and runs them.
	 * </p>
	 * 
	 * @param listOfTestsToBeRun
	 * @param databases
	 * @param testProgressDialog
	 * 
	 */
	public static void runSelectedTests(
			final JList listOfTestsToBeRun,
			final DatabaseRegistryEntry[] databases,
			final TestProgressDialog testProgressDialog,
			final JComponent resultDisplayComponent,
			final String PERL5LIB,
			final PerlScriptConfig psc
	) {
		
		Object[] whatsthis = listOfTestsToBeRun.getSelectedValues();
		
		// Awkward doing this with a loop, but necessary. This won't work:
		//
		// List<TestClassListItem> TestClassListItemList = Arrays.asList( (TestClassListItem[]) whatsthis);
		//
		List<TestClassListItem> TestClassListItemList = new ArrayList<TestClassListItem>();
		
		for (Object somethingNotEasilyCasted : whatsthis) {
			TestClassListItemList.add((TestClassListItem) somethingNotEasilyCasted);
		}
		
		runListOfTestItems(
			TestClassListItemList,
			databases,
			testProgressDialog,
			resultDisplayComponent,
			PERL5LIB,
			psc
		);
	}
	
	public static void runAllTests(
			final JList listOfTestsToBeRun,
			final DatabaseRegistryEntry[] databases,
			final TestProgressDialog testProgressDialog,
			final JComponent resultDisplayComponent,
			final String PERL5LIB,
			final PerlScriptConfig psc
	) {
		
		ListModel lm = listOfTestsToBeRun.getModel();		
		List<TestClassListItem> TestClassListItemList = new ArrayList<TestClassListItem>();
		
		for(int i=0; i<lm.getSize(); i++) {
			
			TestClassListItemList.add((TestClassListItem) lm.getElementAt(i));
		}
		
		runListOfTestItems(
			TestClassListItemList,
			databases,
			testProgressDialog,
			resultDisplayComponent,
			PERL5LIB,
			psc
		);
	}
	
	public static void runListOfTestItems(
			final List<TestClassListItem> TestClassListItemList,
			final DatabaseRegistryEntry[] databases,
			final TestProgressDialog testProgressDialog,
			final JComponent resultDisplayComponent,
			final String PERL5LIB,
			final PerlScriptConfig psc
	) {
		
		List<Class<? extends EnsTestCase>> selectedTests = new ArrayList();
		
		for (TestClassListItem somethingNotEasilyCasted : TestClassListItemList) {
			
			TestClassListItem currentTestClassListItem = (TestClassListItem) somethingNotEasilyCasted;
			
			Class<? extends EnsTestCase> ensTestCaseClass = currentTestClassListItem.getTestClass();				
			selectedTests.add(ensTestCaseClass);
		}
		GuiTestRunner.runAllTests(
			selectedTests, 
			databases, 
			testProgressDialog, 
			resultDisplayComponent, 
			PERL5LIB,
			psc
		);
	}
	
	/**
	 * 
	 * <p>
	 * 	Removes the tests the user has selected from the list of tests to be run.
	 * </p>
	 * 
	 * @param listOfTestsToBeRun
	 * 
	 */
	public static void removeSelectedTests(final JList listOfTestsToBeRun) {
		
		TestClassListModel currentListModel = (TestClassListModel) listOfTestsToBeRun.getModel();
		
		for (Object selectedTestClassObject : listOfTestsToBeRun.getSelectedValues()) {
			
			TestClassListItem selectedTestClass = (TestClassListItem) selectedTestClassObject;
			currentListModel.removeTest(selectedTestClass.getTestClass());
		}
		listOfTestsToBeRun.clearSelection();
		listOfTestsToBeRun.repaint();
	}
}