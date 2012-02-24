/**
 * File: EgMeta.java
 * Created by: dstaines
 * Created on: Mar 2, 2010
 * CVS:  $$
 */
package org.ensembl.healthcheck.testcase.eg_core;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ensembl.healthcheck.DatabaseRegistryEntry;
import org.ensembl.healthcheck.ReportManager;
import org.ensembl.healthcheck.util.CollectionUtils;
import org.ensembl.healthcheck.util.SqlTemplate;
import org.ensembl.healthcheck.util.TestCaseUtils;

/**
 * @author dstaines
 * 
 */
public class PermittedEgMeta extends AbstractEgMeta {

	/**
	 * @param metaKeys
	 */
	public PermittedEgMeta() {
		super(
				TestCaseUtils.resourceToStringList("/org/ensembl/healthcheck/testcase/eg_core/meta_keys.txt"));
	}
	protected Map<String, Boolean> getKeys(SqlTemplate template, int speciesId) {
		Map<String, Boolean> metaKeyOut = CollectionUtils.createHashMap();
		for (Entry<String, List<String>> meta : template.queryForMap(
				META_QUERY, mapper, speciesId).entrySet()) {
			if (!metaKeys.contains(meta.getKey())) {
				metaKeyOut.put(meta.getKey(), true);
			}
		}
		return metaKeyOut;
	}

	protected boolean testKeys(DatabaseRegistryEntry dbre, int speciesId,
			Map<String, Boolean> metaKeyOut) {
		boolean passes = true;
		for (Entry<String, Boolean> e : metaKeyOut.entrySet()) {
				passes = false;
				ReportManager
						.problem(this, dbre.getConnection(), "Meta table for "
								+ speciesId + " contains the unrecognised key "
								+ e.getKey());
		}
		return passes;
	}
}